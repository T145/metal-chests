package T145.metalchests.core;

import java.util.HashSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.text.WordUtils;

import T145.metalchests.api.BlocksMC;
import T145.metalchests.api.EntitiesMC;
import T145.metalchests.api.ItemsMC;
import T145.metalchests.api.chests.IInventoryHandler;
import T145.metalchests.api.chests.IMetalChest;
import T145.metalchests.api.chests.UpgradeRegistry;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.ChestUpgrade;
import T145.metalchests.api.immutable.RegistryMC;
import T145.metalchests.api.immutable.SupportedMods;
import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.blocks.BlockModItem;
import T145.metalchests.config.ModConfig;
import T145.metalchests.entities.EntityBoatMetalChest;
import T145.metalchests.entities.EntityMinecartMetalChest;
import T145.metalchests.entities.ai.EntityAIOcelotSitOnChest;
import T145.metalchests.items.ItemChestUpgrade;
import T145.metalchests.items.ItemMetalMinecart;
import T145.metalchests.tiles.TileMetalChest;
import T145.metalchests.tiles.TileMetalHungryChest;
import T145.metalchests.tiles.TileMetalHungrySortingChest;
import T145.metalchests.tiles.TileMetalSortingChest;
import cofh.core.init.CoreEnchantments;
import cofh.core.util.helpers.MathHelper;
import cubex2.mods.chesttransporter.api.TransportableChest;
import cubex2.mods.chesttransporter.chests.TransportableChestOld;
import net.blay09.mods.refinedrelocation.ModBlocks;
import net.blay09.mods.refinedrelocation.item.ItemSortingUpgrade;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import thaumcraft.api.blocks.BlocksTC;

@EventBusSubscriber(modid = RegistryMC.MOD_ID)
class LoaderCommon {

	private LoaderCommon() {}

	@SubscribeEvent
	public static void onConfigChanged(OnConfigChangedEvent event) {
		if (event.getModID().equals(RegistryMC.MOD_ID)) {
			ConfigManager.sync(RegistryMC.MOD_ID, Config.Type.INSTANCE);
		}
	}

	static void registerTileEntity(Class tileClass) {
		GameRegistry.registerTileEntity(tileClass, new ResourceLocation(RegistryMC.MOD_ID, tileClass.getSimpleName()));
	}

	static void registerItemBlock(IForgeRegistry<Item> registry, Block block) {
		registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}

	static void registerItemBlock(IForgeRegistry<Item> registry, Block block, Class types) {
		registry.register(new BlockModItem(block, types).setRegistryName(block.getRegistryName()));
	}

	static Object getUpgradeBase(Object base, Item upgrade, ChestUpgrade type) {
		switch (type) {
		case WOOD_COPPER:
			return base;
		case WOOD_IRON:
			if (ChestType.COPPER.isRegistered()) {
				break;
			}
			return base;
		case WOOD_GOLD:
			if (ChestType.SILVER.isRegistered()) {
				break;
			}
			return new ItemStack(upgrade, 1, ChestUpgrade.WOOD_IRON.ordinal());
		case COPPER_IRON: case IRON_SILVER: case SILVER_GOLD: case GOLD_DIAMOND: case DIAMOND_OBSIDIAN:
			return type.getBase().getOreName();
		case COPPER_GOLD:
			if (ChestType.SILVER.isRegistered()) {
				break;
			}
			return new ItemStack(upgrade, 1, ChestUpgrade.COPPER_IRON.ordinal());
		case IRON_GOLD:
			if (ChestType.SILVER.isRegistered()) {
				break;
			}
			return type.getBase().getOreName();
		default:
			break;
		}
		return new ItemStack(upgrade, 1, type.ordinal() - 1);
	}

	static void registerMetalChestRecipes(Object baseChest, Block metalChest, String name) {
		if (ChestType.COPPER.isRegistered()) {
			GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, String.format("recipe_copper_%s", name)), null,
					new ItemStack(metalChest, 1, 0),
					"aaa", "aba", "aaa",
					'a', "ingotCopper",
					'b', baseChest
					);
			GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, String.format("recipe_iron_%s", name)), null,
					new ItemStack(metalChest, 1, 1),
					"aaa", "aba", "aaa",
					'a', "ingotIron",
					'b', new ItemStack(metalChest, 1, 0)
					);
		} else {
			GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, String.format("recipe_iron_%s", name)), null,
					new ItemStack(metalChest, 1, 1),
					"aaa", "aba", "aaa",
					'a', "ingotIron",
					'b', baseChest
					);
		}

		if (ChestType.SILVER.isRegistered()) {
			GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, String.format("recipe_silver_%s", name)), null,
					new ItemStack(metalChest, 1, 2),
					"aaa", "aba", "aaa",
					'a', "ingotSilver",
					'b', new ItemStack(metalChest, 1, 1)
					);
			GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, String.format("recipe_gold_%s", name)), null,
					new ItemStack(metalChest, 1, 3),
					"aaa", "aba", "aaa",
					'a', "ingotGold",
					'b', new ItemStack(metalChest, 1, 2)
					);
		} else {
			GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, String.format("recipe_gold_%s", name)), null,
					new ItemStack(metalChest, 1, 3),
					"aaa", "aba", "aaa",
					'a', "ingotGold",
					'b', new ItemStack(metalChest, 1, 1)
					);
		}

		GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, String.format("recipe_diamond_%s", name)), null,
				new ItemStack(metalChest, 1, 4),
				"aaa", "aba", "aaa",
				'a', "gemDiamond",
				'b', new ItemStack(metalChest, 1, 3)
				);
		GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, String.format("recipe_obsidian_%s", name)), null,
				new ItemStack(metalChest, 1, 5),
				"aaa", "aba", "aaa",
				'a', "obsidian",
				'b', new ItemStack(metalChest, 1, 4)
				);
	}

	private static ItemStack tryToInsertStack(IItemHandler inv, @Nonnull ItemStack stack) {
		for (int slot = 0; slot < inv.getSlots(); ++slot) {
			if (!inv.getStackInSlot(slot).isEmpty()) {
				stack = inv.insertItem(slot, stack, false);

				if (stack.isEmpty()) {
					return ItemStack.EMPTY;
				}
			}
		}

		for (int slot = 0; slot < inv.getSlots(); ++slot) {
			stack = inv.insertItem(slot, stack, false);

			if (stack.isEmpty()) {
				return ItemStack.EMPTY;
			}
		}

		return stack;
	}

	private static void tryToEatItem(World world, BlockPos pos, IBlockState state, Entity entity, Block receiver) {
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof IInventoryHandler && entity instanceof EntityItem && !entity.isDead) {
			IInventoryHandler chest = (IInventoryHandler) te;
			EntityItem item = (EntityItem) entity;
			ItemStack stack = item.getItem();
			ItemStack leftovers = tryToInsertStack(chest.getInventory(), stack);

			if (leftovers == null || leftovers.getCount() != stack.getCount()) {
				entity.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.25F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
				world.addBlockEvent(pos, receiver, 2, 2);
			}

			if (leftovers != null) {
				item.setItem(leftovers);
			} else {
				entity.setDead();
			}
		}
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		final IForgeRegistry<Block> registry = event.getRegistry();

		registry.register(BlocksMC.METAL_CHEST = new BlockMetalChest());
		registerTileEntity(TileMetalChest.class);

		if (ModConfig.hasRefinedRelocation()) {
			registry.register(BlocksMC.METAL_SORTING_CHEST = new BlockMetalChest() {

				@Override
				protected void registerResource() {
					this.registerResource(RegistryMC.RESOURCE_METAL_SORTING_CHEST);
				}

				@Nullable
				@Override
				public TileEntity createTileEntity(World world, IBlockState state) {
					return new TileMetalSortingChest(state.getValue(IMetalChest.VARIANT));
				}
			});
			registerTileEntity(TileMetalSortingChest.class);
		}

		if (ModConfig.hasThaumcraft()) {
			registry.register(BlocksMC.METAL_HUNGRY_CHEST = new BlockMetalChest() {

				@Override
				protected void registerResource() {
					this.registerResource(RegistryMC.RESOURCE_METAL_HUNGRY_CHEST);
				}

				@Nullable
				@Override
				public TileEntity createTileEntity(World world, IBlockState state) {
					return new TileMetalHungryChest(state.getValue(IMetalChest.VARIANT));
				}

				@Override
				public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
					tryToEatItem(world, pos, state, entity, this);
				}
			});

			registerTileEntity(TileMetalHungryChest.class);

			if (ModConfig.hasRefinedRelocation()) {
				registry.register(BlocksMC.METAL_HUNGRY_SORTING_CHEST = new BlockMetalChest() {

					protected void registerResource() {
						this.registerResource(RegistryMC.RESOURCE_METAL_HUNGRY_SORTING_CHEST);
					}

					@Nullable
					@Override
					public TileEntity createTileEntity(World world, IBlockState state) {
						return new TileMetalHungrySortingChest(state.getValue(IMetalChest.VARIANT));
					}

					@Override
					public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
						tryToEatItem(world, pos, state, entity, this);
					}
				});

				registerTileEntity(TileMetalHungrySortingChest.class);
			}
		}
	}

	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();

		registerItemBlock(registry, BlocksMC.METAL_CHEST, ChestType.class);
		registry.register(ItemsMC.CHEST_UPGRADE = new ItemChestUpgrade(RegistryMC.RESOURCE_CHEST_UPGRADE));

		if (ModConfig.hasRefinedRelocation()) {
			UpgradeRegistry.registerChest(ModBlocks.sortingChest, BlocksMC.METAL_SORTING_CHEST);
			registerItemBlock(registry, BlocksMC.METAL_SORTING_CHEST, ChestType.class);
		}

		if (ModConfig.GENERAL.enableMinecarts) {
			registry.register(ItemsMC.MINECART_METAL_CHEST = new ItemMetalMinecart());
		}

		if (ModConfig.hasThaumcraft()) {
			registerItemBlock(registry, BlocksMC.METAL_HUNGRY_CHEST, ChestType.class);
			UpgradeRegistry.registerChest(BlocksTC.hungryChest, BlocksMC.METAL_HUNGRY_CHEST);

			if (ModConfig.hasRefinedRelocation()) {
				registerItemBlock(registry, BlocksMC.METAL_HUNGRY_SORTING_CHEST, ChestType.class);
			}
		}
	}

	@SubscribeEvent
	public static void registerEntities(final RegistryEvent.Register<EntityEntry> event) {
		final IForgeRegistry<EntityEntry> registry = event.getRegistry();

		if (ModConfig.GENERAL.enableMinecarts) {
			registry.register(EntitiesMC.MINECART_METAL_CHEST = EntityEntryBuilder.create().id(RegistryMC.KEY_MINECART_METAL_CHEST, 0).name(RegistryMC.KEY_MINECART_METAL_CHEST).entity(EntityMinecartMetalChest.class).tracker(80, 3, true).build());
		}

		registry.register(EntitiesMC.BOAT_METAL_CHEST = EntityEntryBuilder.create().id(RegistryMC.KEY_BOAT_METAL_CHEST, 1).name(RegistryMC.KEY_BOAT_METAL_CHEST).entity(EntityBoatMetalChest.class).tracker(80, 3, true).build());
	}

	private static void registerSortingChestRecipe(Block baseChest, Block metalChest, ChestType type, String name) {
		GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, String.format("recipe_%s_sorting_%s", type.getName(), name)), null,
				new ItemStack(metalChest, 1, type.ordinal()),
				" a ", "bcb", " d ",
				'a', Items.WRITABLE_BOOK,
				'b', Items.REDSTONE,
				'c', new ItemStack(baseChest, 1, type.ordinal()),
				'd', Blocks.HOPPER
				);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		registerMetalChestRecipes("chestWood", BlocksMC.METAL_CHEST, "chest");

		if (ModConfig.hasThaumcraft()) {
			registerMetalChestRecipes(BlocksTC.hungryChest, BlocksMC.METAL_HUNGRY_CHEST, "hungry_chest");
		}

		for (ChestType type : ChestType.values()) {
			if (type.isRegistered()) {
				ItemStack result = new ItemStack(BlocksMC.METAL_CHEST, 1, type.ordinal());
				OreDictionary.registerOre("chest", result);
				OreDictionary.registerOre("chest" + WordUtils.capitalize(type.getName()), result);
				GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, String.format("recipe_minecart_chest_%s", type.getName())), null,
						new ItemStack(ItemsMC.MINECART_METAL_CHEST, 1, type.ordinal()),
						"a", "b",
						'a', new ItemStack(BlocksMC.METAL_CHEST, 1, type.ordinal()),
						'b', Items.MINECART
						);

				if (ModConfig.hasThaumcraft()) {
					String capitalizedName = WordUtils.capitalize(type.getName());
					ItemStack chestStack = new ItemStack(BlocksMC.METAL_HUNGRY_CHEST, 1, type.ordinal());

					OreDictionary.registerOre("chest", chestStack);
					OreDictionary.registerOre(String.format("chestHungry%s", capitalizedName), chestStack);

					if (ModConfig.hasRefinedRelocation()) {
						chestStack = new ItemStack(BlocksMC.METAL_HUNGRY_SORTING_CHEST, 1, type.ordinal());

						OreDictionary.registerOre("chest", chestStack);
						OreDictionary.registerOre(String.format("chestSortingHungry%s", capitalizedName), chestStack);
					}
				}

				if (ModConfig.hasRefinedRelocation()) {
					result = new ItemStack(BlocksMC.METAL_SORTING_CHEST, 1, type.ordinal());
					OreDictionary.registerOre("chest", result);
					OreDictionary.registerOre(String.format("chestSorting%s", WordUtils.capitalize(type.getName())), result);
					registerSortingChestRecipe(BlocksMC.METAL_CHEST, BlocksMC.METAL_SORTING_CHEST, type, "chest");

					if (ModConfig.hasThaumcraft()) {
						registerSortingChestRecipe(BlocksMC.METAL_HUNGRY_CHEST, BlocksMC.METAL_HUNGRY_SORTING_CHEST, type, "hungry_chest");
					}
				}
			}
		}

		for (ChestUpgrade upgrade : ChestUpgrade.values()) {
			GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, String.format("recipe_chest_upgrade_%s", upgrade.getName())), null,
					new ItemStack(ItemsMC.CHEST_UPGRADE, 1, upgrade.ordinal()),
					"aaa", "aaa", "baa",
					'a', upgrade.getUpgrade().getOreName(),
					'b', getUpgradeBase("plankWood", ItemsMC.CHEST_UPGRADE, upgrade)
					);
		}
	}

	@SubscribeEvent
	public static void changeSittingTaskForOcelots(LivingUpdateEvent event) {
		EntityLivingBase creature = event.getEntityLiving();

		if (creature instanceof EntityOcelot && creature.ticksExisted < 5) {
			EntityOcelot ocelot = (EntityOcelot) creature;
			HashSet<EntityAITaskEntry> tasks = new HashSet<EntityAITaskEntry>();

			for (EntityAITaskEntry task : ocelot.tasks.taskEntries) {
				if (task.action.getClass() == EntityAIOcelotSit.class) {
					tasks.add(task);
				}
			}

			for (EntityAITaskEntry task : tasks) {
				ocelot.tasks.removeTask(task.action);
				ocelot.tasks.addTask(task.priority, new EntityAIOcelotSitOnChest(ocelot, 0.4F));
			}
		}
	}

	@SubscribeEvent
	public static void onEntityInteract(EntityInteract event) {
		Entity target = event.getTarget();

		if (target instanceof EntityBoat && target.getPassengers().isEmpty()) {
			EntityPlayer player = event.getEntityPlayer();
			EnumHand hand = EnumHand.MAIN_HAND;
			ItemStack stack = player.getHeldItemMainhand();
			Item chestItem = Item.getItemFromBlock(BlocksMC.METAL_CHEST);
			boolean hasChest = stack.getItem().equals(chestItem);

			if (stack.isEmpty() || !hasChest) {
				stack = player.getHeldItemOffhand();
				hand = EnumHand.OFF_HAND;
			}

			if (!stack.isEmpty() && hasChest) {
				player.swingArm(hand);

				World world = event.getWorld();

				if (!world.isRemote) {
					EntityBoatMetalChest boat = new EntityBoatMetalChest((EntityBoat) target);

					boat.setChestType(ChestType.byMetadata(stack.getItemDamage()));

					if (ModConfig.hasThermalExpansion() && stack.getTagCompound() != null) {
						boat.setEnchantLevel((byte) MathHelper.clamp(EnchantmentHelper.getEnchantmentLevel(CoreEnchantments.holding, stack), 0, CoreEnchantments.holding.getMaxLevel()));

						if (stack.getTagCompound().hasKey("Inventory")) {
							boat.setInventoryTag(stack.getTagCompound().getCompoundTag("Inventory"));
						}
					}

					if (boat != null) {
						target.setDead();
						world.spawnEntity(boat);
						event.setCanceled(true);

						if (!player.capabilities.isCreativeMode) {
							stack.shrink(1);

							if (stack.getCount() <= 0) {
								player.setHeldItem(hand, ItemStack.EMPTY);
							}
						}
					}
				}
			}
		}
	}

	@Optional.Method(modid = SupportedMods.CHESTTRANSPORTER_MOD_ID)
	@SubscribeEvent
	public static void registerChestTransporter(final RegistryEvent.Register<TransportableChest> event) {
		final IForgeRegistry<TransportableChest> registry = event.getRegistry();

		registry.register(new TransportableMetalChestImpl(BlocksMC.METAL_CHEST, RegistryMC.KEY_METAL_CHEST));

		if (ModConfig.hasThaumcraft()) {
			registry.register(new TransportableChestOld(BlocksTC.hungryChest, -1, 1, "vanilla"));
			registry.register(new TransportableMetalChestImpl(BlocksMC.METAL_HUNGRY_CHEST, "hungry/", RegistryMC.KEY_METAL_HUNGRY_CHEST));
		}

		if (ModConfig.hasRefinedRelocation()) {
			registry.register(new TransportableMetalChestImpl(BlocksMC.METAL_SORTING_CHEST, RegistryMC.KEY_METAL_SORTING_CHEST));

			if (ModConfig.hasThaumcraft()) {
				registry.register(new TransportableMetalChestImpl(BlocksMC.METAL_HUNGRY_SORTING_CHEST, "hungry/", RegistryMC.KEY_METAL_HUNGRY_SORTING_CHEST));
			}
		}
	}

	@Optional.Method(modid = SupportedMods.QUARK_MOD_ID)
	@SubscribeEvent
	public static void processQuarkEntityInteract(EntityInteract event) {
		Entity target = event.getTarget();

		if (target instanceof EntityMinecartEmpty && target.getPassengers().isEmpty()) {
			EntityPlayer player = event.getEntityPlayer();
			EnumHand hand = EnumHand.MAIN_HAND;
			ItemStack stack = player.getHeldItemMainhand();
			Item chestItem = Item.getItemFromBlock(BlocksMC.METAL_CHEST);
			boolean hasChest = stack.getItem().equals(chestItem);

			if (stack.isEmpty() || !hasChest) {
				stack = player.getHeldItemOffhand();
				hand = EnumHand.OFF_HAND;
			}

			if (!stack.isEmpty() && hasChest) {
				player.swingArm(hand);

				World world = event.getWorld();

				if (!world.isRemote) {
					EntityMinecartMetalChest cart = new EntityMinecartMetalChest(world, target.posX, target.posY, target.posZ);

					cart.setChestType(ChestType.byMetadata(stack.getItemDamage()));

					if (ModConfig.hasThermalExpansion() && stack.getTagCompound() != null) {
						cart.setEnchantLevel((byte) MathHelper.clamp(EnchantmentHelper.getEnchantmentLevel(CoreEnchantments.holding, stack), 0, CoreEnchantments.holding.getMaxLevel()));

						if (stack.getTagCompound().hasKey("Inventory")) {
							cart.setInventoryTag(stack.getTagCompound().getCompoundTag("Inventory"));
						}
					}

					if (cart != null) {
						target.setDead();
						world.spawnEntity(cart);
						event.setCanceled(true);

						if (!player.capabilities.isCreativeMode) {
							stack.shrink(1);

							if (stack.getCount() <= 0) {
								player.setHeldItem(hand, ItemStack.EMPTY);
							}
						}
					}
				}
			}
		}
	}

	@Optional.Method(modid = SupportedMods.REFINEDRELOCATION_MOD_ID)
	@SubscribeEvent
	public static void onRR2RightClick(RightClickBlock event) {
		World world = event.getWorld();

		if (world.isRemote) {
			return;
		}

		BlockPos pos = event.getPos();
		TileEntity te = world.getTileEntity(pos);

		if (te == null) {
			return;
		}

		EntityPlayer player = event.getEntityPlayer();
		ItemStack stack = event.getItemStack();

		if (player.isSneaking() && te instanceof TileMetalChest && stack.getItem() instanceof ItemSortingUpgrade) {
			te.updateContainingBlockInfo();

			TileMetalChest oldChest = (TileMetalChest) te;
			TileMetalChest newChest = te instanceof TileMetalHungryChest ? new TileMetalHungrySortingChest(oldChest.getChestType()) : new TileMetalSortingChest(oldChest.getChestType());
			Block chestBlock = newChest instanceof TileMetalHungrySortingChest ? BlocksMC.METAL_HUNGRY_SORTING_CHEST : BlocksMC.METAL_SORTING_CHEST;

			world.removeTileEntity(pos);
			world.setBlockToAir(pos);
			world.setTileEntity(pos, newChest);

			IBlockState state = chestBlock.getDefaultState().withProperty(IMetalChest.VARIANT, newChest.getChestType());
			world.setBlockState(pos, state, 3);
			world.notifyBlockUpdate(pos, state, state, 3);

			TileEntity tile = world.getTileEntity(pos);

			if (tile instanceof TileMetalChest) {
				TileMetalChest chest = (TileMetalChest) tile;
				chest.setInventory(oldChest.getInventory());
				chest.setFront(oldChest.getFront());
			}
		}
	}
}
