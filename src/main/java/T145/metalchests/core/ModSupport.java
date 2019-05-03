/*******************************************************************************
 * Copyright 2019 T145
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package T145.metalchests.core;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.text.WordUtils;

import T145.metalchests.api.BlocksMC;
import T145.metalchests.api.chests.IInventoryHandler;
import T145.metalchests.api.chests.IMetalChest;
import T145.metalchests.api.chests.UpgradeRegistry;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.RegistryMC;
import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.client.render.blocks.RenderMetalChest;
import T145.metalchests.client.render.blocks.RenderMetalSortingChest;
import T145.metalchests.config.ModConfig;
import T145.metalchests.entities.EntityMinecartMetalChest;
import T145.metalchests.tiles.TileMetalChest;
import T145.metalchests.tiles.TileMetalHungryChest;
import T145.metalchests.tiles.TileMetalHungrySortingChest;
import T145.metalchests.tiles.TileMetalSortingChest;
import cubex2.mods.chesttransporter.api.TransportableChest;
import cubex2.mods.chesttransporter.chests.TransportableChestOld;
import net.blay09.mods.refinedrelocation.item.ItemSortingUpgrade;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import thaumcraft.api.blocks.BlocksTC;

public class ModSupport {

	public static final String INVTWEAKS_MOD_ID = "invtweaks";
	public static final String IFACE_CHEST_CONTAINER = "invtweaks.api.container.ChestContainer";

	public static final String HOLOINVENTORY_MOD_ID = "holoinventory";
	public static final String IFACE_NAMED_ITEM_HANDLER = "net.dries007.holoInventory.api.INamedItemHandler";

	public static final String QUARK_MOD_ID = "quark";
	public static final String IFACE_CHEST_BUTTON_CALLBACK = "vazkii.quark.api.IChestButtonCallback";
	public static final String IFACE_DROPOFF_MANAGER = "vazkii.quark.api.IDropoffManager";
	public static final String IFACE_SEARCH_BAR = "vazkii.quark.api.IItemSearchBar";

	public static final String RAILCRAFT_MOD_ID = "railcraft";
	public static final String IFACE_FLUID_CART = "mods.railcraft.api.carts.IFluidCart";
	public static final String IFACE_ITEM_CART = "mods.railcraft.api.carts.IItemCart";

	public static final String THERMALEXPANSION_MOD_ID = "thermalexpansion";
	public static final String IFACE_ENCHANTABLE_ITEM = "cofh.core.item.IEnchantableItem";

	public static final String CHESTTRANSPORTER_MOD_ID = "chesttransporter";
	public static final String THAUMCRAFT_MOD_ID = "thaumcraft";

	public static final String REFINEDRELOCATION_MOD_ID = "refinedrelocation";
	public static final String IFACE_NAME_TAGGABLE = "net.blay09.mods.refinedrelocation.api.INameTaggable";

	private ModSupport() {}

	public static boolean hasThaumcraft() {
		return ModConfig.GENERAL.enableMetalHungryChests && Loader.isModLoaded(THAUMCRAFT_MOD_ID);
	}

	public static boolean hasRefinedRelocation() {
		return ModConfig.GENERAL.enableMetalHungrySortingChests && Loader.isModLoaded(REFINEDRELOCATION_MOD_ID);
	}

	public static boolean hasThermalExpansion() {
		return Loader.isModLoaded(THERMALEXPANSION_MOD_ID);
	}

	@EventBusSubscriber(modid = RegistryMC.MOD_ID)
	static class ServerLoader {

		@Optional.Method(modid = CHESTTRANSPORTER_MOD_ID)
		@SubscribeEvent
		public static void registerChestTransporter(final RegistryEvent.Register<TransportableChest> event) {
			final IForgeRegistry<TransportableChest> registry = event.getRegistry();

			registry.register(new TransportableMetalChestImpl(BlocksMC.METAL_CHEST, RegistryMC.KEY_METAL_CHEST));

			if (ModSupport.hasThaumcraft()) {
				registry.register(new TransportableChestOld(BlocksTC.hungryChest, -1, 1, "vanilla"));
				registry.register(new TransportableMetalChestImpl(BlocksMC.METAL_HUNGRY_CHEST, "hungry/", RegistryMC.KEY_METAL_HUNGRY_CHEST));
			}

			if (ModSupport.hasRefinedRelocation()) {
				registry.register(new TransportableMetalChestImpl(BlocksMC.METAL_SORTING_CHEST, RegistryMC.KEY_METAL_SORTING_CHEST));

				if (ModSupport.hasThaumcraft()) {
					registry.register(new TransportableMetalChestImpl(BlocksMC.METAL_HUNGRY_SORTING_CHEST, "hungry/", RegistryMC.KEY_METAL_HUNGRY_SORTING_CHEST));
				}
			}
		}

		@Optional.Method(modid = QUARK_MOD_ID)
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

		@Optional.Method(modid = REFINEDRELOCATION_MOD_ID)
		@SubscribeEvent
		public static void registerRefinedRelocationBlocks(final RegistryEvent.Register<Block> event) {
			if (ModSupport.hasRefinedRelocation()) {
				final IForgeRegistry<Block> registry = event.getRegistry();

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
				ModLoader.registerTileEntity(TileMetalSortingChest.class);
			}
		}

		@Optional.Method(modid = REFINEDRELOCATION_MOD_ID)
		@SubscribeEvent
		public static void registerRefinedRelocationItems(final RegistryEvent.Register<Item> event) {
			if (ModSupport.hasRefinedRelocation()) {
				final IForgeRegistry<Item> registry = event.getRegistry();

				ModLoader.registerItemBlock(registry, BlocksMC.METAL_SORTING_CHEST, ChestType.class);
			}
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

		@Optional.Method(modid = REFINEDRELOCATION_MOD_ID)
		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public static void registerRefinedRelocationRecipes(RegistryEvent.Register<IRecipe> event) {
			if (ModSupport.hasRefinedRelocation()) {
				for (ChestType type : ChestType.values()) {
					if (type.isRegistered()) {
						ItemStack chestStack = new ItemStack(BlocksMC.METAL_SORTING_CHEST, 1, type.ordinal());
						OreDictionary.registerOre("chest", chestStack);
						OreDictionary.registerOre(String.format("chestSorting%s", WordUtils.capitalize(type.getName())), chestStack);
						registerSortingChestRecipe(BlocksMC.METAL_CHEST, BlocksMC.METAL_SORTING_CHEST, type, "chest");

						if (ModSupport.hasThaumcraft()) {
							registerSortingChestRecipe(BlocksMC.METAL_HUNGRY_CHEST, BlocksMC.METAL_HUNGRY_SORTING_CHEST, type, "hungry_chest");
						}
					}
				}
			}
		}

		@Optional.Method(modid = REFINEDRELOCATION_MOD_ID)
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

		@Optional.Method(modid = ModSupport.THAUMCRAFT_MOD_ID)
		@SubscribeEvent
		public static void registerThaumcraftBlocks(final RegistryEvent.Register<Block> event) {
			if (ModSupport.hasThaumcraft()) {
				final IForgeRegistry<Block> registry = event.getRegistry();

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

				ModLoader.registerTileEntity(TileMetalHungryChest.class);

				if (ModSupport.hasRefinedRelocation()) {
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

					ModLoader.registerTileEntity(TileMetalHungrySortingChest.class);
				}
			}
		}

		@Optional.Method(modid = ModSupport.THAUMCRAFT_MOD_ID)
		@SubscribeEvent
		public static void registerThaumcraftItems(final RegistryEvent.Register<Item> event) {
			if (ModSupport.hasThaumcraft()) {
				final IForgeRegistry<Item> registry = event.getRegistry();

				ModLoader.registerItemBlock(registry, BlocksMC.METAL_HUNGRY_CHEST, ChestType.class);
				UpgradeRegistry.registerChest(BlocksTC.hungryChest, BlocksMC.METAL_HUNGRY_CHEST);

				if (ModSupport.hasRefinedRelocation()) {
					ModLoader.registerItemBlock(registry, BlocksMC.METAL_HUNGRY_SORTING_CHEST, ChestType.class);
				}
			}
		}

		@Optional.Method(modid = ModSupport.THAUMCRAFT_MOD_ID)
		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public static void registerThaumcraftRecipes(RegistryEvent.Register<IRecipe> event) {
			if (ModSupport.hasThaumcraft()) {
				for (ChestType type : ChestType.values()) {
					if (type.isRegistered()) {
						String capitalizedName = WordUtils.capitalize(type.getName());
						ItemStack chestStack = new ItemStack(BlocksMC.METAL_HUNGRY_CHEST, 1, type.ordinal());

						OreDictionary.registerOre("chest", chestStack);
						OreDictionary.registerOre(String.format("chestHungry%s", capitalizedName), chestStack);

						if (ModSupport.hasRefinedRelocation()) {
							chestStack = new ItemStack(BlocksMC.METAL_HUNGRY_SORTING_CHEST, 1, type.ordinal());

							OreDictionary.registerOre("chest", chestStack);
							OreDictionary.registerOre(String.format("chestSortingHungry%s", capitalizedName), chestStack);
						}
					}
				}

				ModLoader.registerMetalChestRecipes(BlocksTC.hungryChest, BlocksMC.METAL_HUNGRY_CHEST, "hungry_chest");
			}
		}
	}

	@EventBusSubscriber(modid = RegistryMC.MOD_ID, value = Side.CLIENT)
	static class ClientLoader {

		@Optional.Method(modid = REFINEDRELOCATION_MOD_ID)
		@SubscribeEvent
		public static void registerRefinedRelocationModels(ModelRegistryEvent event) {
			if (ModSupport.hasRefinedRelocation()) {
				for (ChestType type : ChestType.values()) {
					ModLoader.registerModel(BlocksMC.METAL_SORTING_CHEST, type.ordinal(), ModLoader.getVariantName(type));
				}

				ModLoader.registerTileRenderer(TileMetalSortingChest.class, new RenderMetalSortingChest());

				if (ModSupport.hasThaumcraft()) {
					for (ChestType type : ChestType.values()) {
						ModLoader.registerModel(BlocksMC.METAL_HUNGRY_SORTING_CHEST, type.ordinal(), ModLoader.getVariantName(type));
					}

					ModLoader.registerTileRenderer(TileMetalHungrySortingChest.class, new RenderMetalSortingChest(BlocksMC.METAL_HUNGRY_SORTING_CHEST) {

						@Override
						protected ResourceLocation getActiveResource(ChestType type) {
							return new ResourceLocation(RegistryMC.MOD_ID, String.format("textures/entity/chest/hungry/%s.png", type.getName()));
						}

						@Override
						protected ResourceLocation getActiveOverlay(ChestType type) {
							return new ResourceLocation(RegistryMC.MOD_ID, String.format("textures/entity/chest/hungry/overlay/%s.png", type.getName()));
						}
					});
				}
			}
		}

		@Optional.Method(modid = ModSupport.THAUMCRAFT_MOD_ID)
		@SubscribeEvent
		public static void registerThaumcraftModels(ModelRegistryEvent event) {
			if (ModSupport.hasThaumcraft()) {
				for (ChestType type : ChestType.values()) {
					ModLoader.registerModel(BlocksMC.METAL_HUNGRY_CHEST, type.ordinal(), ModLoader.getVariantName(type));
				}

				ModLoader.registerTileRenderer(TileMetalHungryChest.class, new RenderMetalChest() {

					@Override
					protected ResourceLocation getActiveResource(ChestType type) {
						return new ResourceLocation(RegistryMC.MOD_ID, String.format("textures/entity/chest/hungry/%s.png", type.getName()));
					}
				});
			}
		}
	}
}
