/*******************************************************************************
 * Copyright 2018 T145
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

import T145.metalchests.api.immutable.BlocksMC;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.ChestUpgrade;
import T145.metalchests.api.immutable.ItemsMC;
import T145.metalchests.api.immutable.ModSupport;
import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.client.render.blocks.RenderMetalChest;
import T145.metalchests.client.render.entities.RenderMinecartMetalChest;
import T145.metalchests.config.ModConfig;
import T145.metalchests.entities.EntityMinecartMetalChest;
import T145.metalchests.entities.ai.EntityAIOcelotSitOnChest;
import T145.metalchests.items.ItemChestUpgrade;
import T145.metalchests.items.ItemMetalMinecart;
import T145.metalchests.lib.items.BlockModItem;
import T145.metalchests.tiles.TileMetalChest;
import T145.metalchests.tiles.TileSortingMetalChest;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

public class ModLoader {

	public static void registerTileEntity(Class tileClass) {
		GameRegistry.registerTileEntity(tileClass, new ResourceLocation(MetalChests.MOD_ID, tileClass.getSimpleName()));
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

	public static ModelResourceLocation getCustomModel(Item item, String customDomain, StringBuilder variantPath) {
		if (StringUtils.isNullOrEmpty(customDomain)) {
			return new ModelResourceLocation(item.getRegistryName(), variantPath.toString());
		} else {
			return new ModelResourceLocation(MetalChests.MOD_ID + ":" + customDomain, variantPath.toString());
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

	@EventBusSubscriber(modid = MetalChests.MOD_ID)
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

			ItemChestUpgrade upgrade = new ItemChestUpgrade(ItemsMC.KEY_CHEST_UPGRADE);
			upgrade.addDefaultChest(TileEntityChest.class, new TileMetalChest());

			if (ModSupport.hasRefinedRelocation()) {
				upgrade.addDefaultChest(TileSortingChest.class, new TileSortingMetalChest());
			}

			ItemsMC.CHEST_UPGRADE = upgrade;
			registry.register(ItemsMC.CHEST_UPGRADE);

			if (ModConfig.GENERAL.enableMinecarts) {
				registry.register(ItemsMC.MINECART_METAL_CHEST = new ItemMetalMinecart());
			}
		}

		@SubscribeEvent
		public static void registerEntities(final RegistryEvent.Register<EntityEntry> event) {
			final IForgeRegistry<EntityEntry> registry = event.getRegistry();

			if (ModConfig.GENERAL.enableMinecarts) {
				registry.register(createBuilder("MinecartMetalChest").entity(EntityMinecartMetalChest.class).tracker(80, 3, true).build());
			}
		}

		private static int entityID = 0;

		private static <E extends Entity> EntityEntryBuilder<E> createBuilder(final String name) {
			final EntityEntryBuilder<E> builder = EntityEntryBuilder.create();
			final ResourceLocation registryName = new ResourceLocation(MetalChests.MOD_ID, name);
			return builder.id(registryName, entityID++).name(MetalChests.MOD_ID + ":" + name);
		}

		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
			OreDictionary.registerOre("chestCopper", new ItemStack(BlocksMC.METAL_CHEST, 1, 0));
			OreDictionary.registerOre("chestIron", new ItemStack(BlocksMC.METAL_CHEST, 1, 1));
			OreDictionary.registerOre("chestSilver", new ItemStack(BlocksMC.METAL_CHEST, 1, 2));
			OreDictionary.registerOre("chestGold", new ItemStack(BlocksMC.METAL_CHEST, 1, 3));
			OreDictionary.registerOre("chestDiamond", new ItemStack(BlocksMC.METAL_CHEST, 1, 4));
			OreDictionary.registerOre("chestObsidian", new ItemStack(BlocksMC.METAL_CHEST, 1, 5));
		}

		@SubscribeEvent
		public static void onPlayerLogIn(PlayerLoggedInEvent event) {
			if (ModConfig.GENERAL.checkForUpdates && UpdateChecker.hasUpdate()) {
				event.player.sendMessage(UpdateChecker.getUpdateNotification());
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
	}

	@EventBusSubscriber(modid = MetalChests.MOD_ID, value = Side.CLIENT)
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
		}
	}
}
