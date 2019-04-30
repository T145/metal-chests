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

import java.util.HashSet;

import org.apache.commons.lang3.text.WordUtils;

import T145.metalchests.api.BlocksMC;
import T145.metalchests.api.EntitiesMC;
import T145.metalchests.api.ItemsMC;
import T145.metalchests.api.chests.UpgradeRegistry;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.ChestUpgrade;
import T145.metalchests.api.immutable.ModSupport;
import T145.metalchests.api.immutable.RegistryMC;
import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.blocks.BlockModItem;
import T145.metalchests.client.render.blocks.RenderMetalChest;
import T145.metalchests.client.render.entities.RenderBoatMetalChest;
import T145.metalchests.client.render.entities.RenderMinecartMetalChest;
import T145.metalchests.config.ModConfig;
import T145.metalchests.entities.EntityBoatMetalChest;
import T145.metalchests.entities.EntityMinecartMetalChest;
import T145.metalchests.entities.ai.EntityAIOcelotSitOnChest;
import T145.metalchests.items.ItemChestUpgrade;
import T145.metalchests.items.ItemMetalMinecart;
import T145.metalchests.tiles.TileMetalChest;
import net.blay09.mods.refinedrelocation.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

class ModLoader {

	public static void registerTileEntity(Class tileClass) {
		GameRegistry.registerTileEntity(tileClass, new ResourceLocation(RegistryMC.MOD_ID, tileClass.getSimpleName()));
	}

	public static void registerItemBlock(IForgeRegistry<Item> registry, Block block) {
		registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}

	public static void registerItemBlock(IForgeRegistry<Item> registry, Block block, Class types) {
		registry.register(new BlockModItem(block, types).setRegistryName(block.getRegistryName()));
	}

	public static String getVariantName(IStringSerializable variant) {
		return "variant=" + variant.getName();
	}

	public static Object getUpgradeBase(Object base, Item upgrade, ChestUpgrade type) {
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

	public static void registerMetalChestRecipes(Object baseChest, Block metalChest, String name) {
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

	@EventBusSubscriber(modid = RegistryMC.MOD_ID)
	static class ServerLoader {

		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			final IForgeRegistry<Block> registry = event.getRegistry();

			registry.register(BlocksMC.METAL_CHEST = new BlockMetalChest());
			registerTileEntity(TileMetalChest.class);
		}

		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			final IForgeRegistry<Item> registry = event.getRegistry();

			registerItemBlock(registry, BlocksMC.METAL_CHEST, ChestType.class);
			registry.register(ItemsMC.CHEST_UPGRADE = new ItemChestUpgrade(RegistryMC.RESOURCE_CHEST_UPGRADE));

			if (ModSupport.hasRefinedRelocation()) {
				UpgradeRegistry.registerChest(ModBlocks.sortingChest, BlocksMC.METAL_SORTING_CHEST);
			}

			if (ModConfig.GENERAL.enableMinecarts) {
				registry.register(ItemsMC.MINECART_METAL_CHEST = new ItemMetalMinecart());
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

		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
			for (ChestType type : ChestType.values()) {
				if (type.isRegistered()) {
					ItemStack chestStack = new ItemStack(BlocksMC.METAL_CHEST, 1, type.ordinal());
					OreDictionary.registerOre("chest", chestStack);
					OreDictionary.registerOre("chest" + WordUtils.capitalize(type.getName()), chestStack);
					GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, "recipe_minecart_chest_" + type.getName()), null, new ItemStack(ItemsMC.MINECART_METAL_CHEST, 1, type.ordinal()), "a", "b", 'a', new ItemStack(BlocksMC.METAL_CHEST, 1, type.ordinal()), 'b', Items.MINECART);
				}
			}

			registerMetalChestRecipes("chestWood", BlocksMC.METAL_CHEST, "chest");

			for (ChestUpgrade upgrade : ChestUpgrade.values()) {
				GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, "recipe_chest_upgrade_" + upgrade.getName()), null, new ItemStack(ItemsMC.CHEST_UPGRADE, 1, upgrade.ordinal()), "aaa", "aaa", "baa", 'a', upgrade.getUpgrade().getOreName(), 'b', getUpgradeBase("plankWood", ItemsMC.CHEST_UPGRADE, upgrade));
			}
		}

		@SubscribeEvent
		public static void onConfigChanged(OnConfigChangedEvent event) {
			if (event.getModID().equals(RegistryMC.MOD_ID)) {
				ConfigManager.sync(RegistryMC.MOD_ID, Config.Type.INSTANCE);
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
	}

	@SideOnly(Side.CLIENT)
	public static ModelResourceLocation getCustomModel(Item item, String customDomain, StringBuilder variantPath) {
		if (StringUtils.isNullOrEmpty(customDomain)) {
			return new ModelResourceLocation(item.getRegistryName(), variantPath.toString());
		} else {
			return new ModelResourceLocation(RegistryMC.MOD_ID + ":" + customDomain, variantPath.toString());
		}
	}

	@SideOnly(Side.CLIENT)
	public static void registerModel(Item item, String customDomain, int meta, String... variants) {
		StringBuilder variantPath = new StringBuilder(variants[0]);

		for (int i = 1; i < variants.length; ++i) {
			variantPath.append(',').append(variants[i]);
		}

		ModelLoader.setCustomModelResourceLocation(item, meta, getCustomModel(item, customDomain, variantPath));
	}

	@SideOnly(Side.CLIENT)
	public static void registerModel(Block block, String customDomain, int meta, String... variants) {
		registerModel(Item.getItemFromBlock(block), customDomain, meta, variants);
	}

	@SideOnly(Side.CLIENT)
	public static void registerModel(Item item, int meta, String... variants) {
		registerModel(item, null, meta, variants);
	}

	@SideOnly(Side.CLIENT)
	public static void registerModel(Block block, int meta, String... variants) {
		registerModel(block, null, meta, variants);
	}

	@SideOnly(Side.CLIENT)
	public static void registerTileRenderer(Class tileClass, TileEntitySpecialRenderer tileRenderer) {
		ClientRegistry.bindTileEntitySpecialRenderer(tileClass, tileRenderer);
	}

	@EventBusSubscriber(modid = RegistryMC.MOD_ID, value = Side.CLIENT)
	static class ClientLoader {

		@SubscribeEvent
		public static void onModelRegistration(ModelRegistryEvent event) {
			for (ChestType type : ChestType.values()) {
				registerModel(BlocksMC.METAL_CHEST, type.ordinal(), getVariantName(type));

				if (ModConfig.GENERAL.enableMinecarts) {
					registerModel(ItemsMC.MINECART_METAL_CHEST, "item_minecart", type.ordinal(), "item=" + type.getName() + "_chest");
				}
			}

			registerTileRenderer(TileMetalChest.class, RenderMetalChest.INSTANCE);

			for (ChestUpgrade type : ChestUpgrade.values()) {
				registerModel(ItemsMC.CHEST_UPGRADE, "item_chest_upgrade", type.ordinal(), "item=" + type.getName());
			}

			RenderingRegistry.registerEntityRenderingHandler(EntityMinecartMetalChest.class, manager -> new RenderMinecartMetalChest(manager));
			RenderingRegistry.registerEntityRenderingHandler(EntityBoatMetalChest.class, manager -> new RenderBoatMetalChest(manager));
		}
	}
}
