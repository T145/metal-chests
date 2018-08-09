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

import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.blocks.BlockMetalChest.ChestType;
import T145.metalchests.blocks.BlockModItem;
import T145.metalchests.client.render.RenderMetalChest;
import T145.metalchests.config.ModConfig;
import T145.metalchests.entities.ai.EntityAIOcelotSitOnChest;
import T145.metalchests.items.ItemStructureUpgrade;
import T145.metalchests.items.ItemStructureUpgrade.ChestUpgrade;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(MetalChests.MOD_ID)
public class ModLoader {

	@ObjectHolder(BlockMetalChest.NAME)
	public static final Block METAL_CHEST = new BlockMetalChest();

	@ObjectHolder(ItemStructureUpgrade.NAME)
	public static final Item METAL_UPGRADE = new ItemStructureUpgrade();

	@EventBusSubscriber(modid = MetalChests.MOD_ID)
	static class ServerLoader {

		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			final IForgeRegistry<Block> registry = event.getRegistry();
			registry.register(METAL_CHEST);
			registerTileEntity(TileMetalChest.class);
		}

		private static void registerTileEntity(Class tileClass) {
			GameRegistry.registerTileEntity(tileClass, new ResourceLocation(MetalChests.MOD_ID, tileClass.getSimpleName()));
		}

		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			final IForgeRegistry<Item> registry = event.getRegistry();
			registerItemBlock(registry, METAL_CHEST, ChestType.class);
			registry.register(METAL_UPGRADE);
		}

		private static void registerItemBlock(IForgeRegistry<Item> registry, Block block) {
			registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		}

		private static void registerItemBlock(IForgeRegistry<Item> registry, Block block, Class types) {
			registry.register(new BlockModItem(block, types).setRegistryName(block.getRegistryName()));
		}

		@SubscribeEvent
		public static void onPlayerLogIn(PlayerLoggedInEvent event) {
			if (ModConfig.GENERAL.checkForUpdates && UpdateChecker.hasUpdate()) {
				event.player.sendMessage(UpdateChecker.getUpdateNotification());
			}
		}

		@SubscribeEvent
		public void changeSittingTaskForOcelots(LivingUpdateEvent event) {
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
				registerModel(METAL_CHEST, type.ordinal(), getVariantName(type));
			}

			for (ChestUpgrade type : ChestUpgrade.values()) {
				registerModel(METAL_UPGRADE, "item_upgrade", type.ordinal(), "item=" + type.getName());
			}

			registerTileRenderer(TileMetalChest.class, new RenderMetalChest());
		}

		private static String getVariantName(IStringSerializable variant) {
			return "variant=" + variant.getName();
		}

		private static ModelResourceLocation getCustomModel(Item item, String customDomain, StringBuilder variantPath) {
			if (StringUtils.isNullOrEmpty(customDomain)) {
				return new ModelResourceLocation(item.getRegistryName(), variantPath.toString());
			} else {
				return new ModelResourceLocation(MetalChests.MOD_ID + ":" + customDomain, variantPath.toString());
			}
		}

		private static void registerModel(Item item, String customDomain, int meta, String... variants) {
			StringBuilder variantPath = new StringBuilder(variants[0]);

			for (int i = 1; i < variants.length; ++i) {
				variantPath.append(',').append(variants[i]);
			}

			ModelLoader.setCustomModelResourceLocation(item, meta, getCustomModel(item, customDomain, variantPath));
		}

		private static void registerModel(Block block, String customDomain, int meta, String... variants) {
			registerModel(Item.getItemFromBlock(block), customDomain, meta, variants);
		}

		private static void registerModel(Item item, int meta, String... variants) {
			registerModel(item, null, meta, variants);
		}

		private static void registerModel(Block block, int meta, String... variants) {
			registerModel(block, null, meta, variants);
		}

		private static void registerTileRenderer(Class tileClass, TileEntitySpecialRenderer tileRenderer) {
			ClientRegistry.bindTileEntitySpecialRenderer(tileClass, tileRenderer);
		}
	}
}
