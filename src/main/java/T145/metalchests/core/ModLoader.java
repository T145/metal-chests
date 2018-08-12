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

import T145.metalchests.api.ModSupport;
import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.blocks.BlockMetalChest.ChestType;
import T145.metalchests.blocks.BlockModItem;
import T145.metalchests.client.render.RenderMetalChest;
import T145.metalchests.client.render.RenderMinecartMetalChest;
import T145.metalchests.compat.chesttransporter.TransportableMetalChest;
import T145.metalchests.compat.thaumcraft.BlockHungryMetalChest;
import T145.metalchests.compat.thaumcraft.RenderHungryMetalChest;
import T145.metalchests.compat.thaumcraft.TileHungryMetalChest;
import T145.metalchests.config.ModConfig;
import T145.metalchests.entities.EntityMinecartMetalChest;
import T145.metalchests.entities.ai.EntityAIOcelotSitOnChest;
import T145.metalchests.items.ItemChestUpgrade;
import T145.metalchests.items.ItemChestUpgrade.ChestUpgrade;
import T145.metalchests.items.ItemMetalMinecart;
import T145.metalchests.tiles.TileMetalChest;
import cubex2.mods.chesttransporter.api.TransportableChest;
import cubex2.mods.chesttransporter.chests.TransportableChestOld;
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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;
import thaumcraft.api.blocks.BlocksTC;

@ObjectHolder(MetalChests.MOD_ID)
public class ModLoader {

	@ObjectHolder(BlockMetalChest.NAME)
	public static final Block METAL_CHEST = new BlockMetalChest();

	@ObjectHolder(BlockHungryMetalChest.NAME)
	public static final Block HUNGRY_METAL_CHEST = new BlockHungryMetalChest();

	@ObjectHolder(ItemChestUpgrade.NAME)
	public static final Item METAL_UPGRADE = new ItemChestUpgrade();

	@ObjectHolder(ItemMetalMinecart.NAME)
	public static final Item MINECART_METAL_CHEST = new ItemMetalMinecart();

	@EventBusSubscriber(modid = MetalChests.MOD_ID)
	static class ServerLoader {

		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			final IForgeRegistry<Block> registry = event.getRegistry();

			registry.register(METAL_CHEST);
			registerTileEntity(TileMetalChest.class);

			if (ModSupport.hasThaumcraft()) {
				registry.register(HUNGRY_METAL_CHEST);
				registerTileEntity(TileHungryMetalChest.class);
			}
		}

		private static void registerTileEntity(Class tileClass) {
			GameRegistry.registerTileEntity(tileClass, new ResourceLocation(MetalChests.MOD_ID, tileClass.getSimpleName()));
		}

		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			final IForgeRegistry<Item> registry = event.getRegistry();

			registerItemBlock(registry, METAL_CHEST, ChestType.class);

			if (ModSupport.hasThaumcraft()) {
				registerItemBlock(registry, HUNGRY_METAL_CHEST, ChestType.class);
			}

			registry.register(METAL_UPGRADE);
			registry.register(MINECART_METAL_CHEST);
		}

		private static void registerItemBlock(IForgeRegistry<Item> registry, Block block) {
			registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		}

		private static void registerItemBlock(IForgeRegistry<Item> registry, Block block, Class types) {
			registry.register(new BlockModItem(block, types).setRegistryName(block.getRegistryName()));
		}

		@SubscribeEvent
		public static void registerEntities(final RegistryEvent.Register<EntityEntry> event) {
			final IForgeRegistry<EntityEntry> registry = event.getRegistry();

			registry.register(createBuilder("MinecartMetalChest").entity(EntityMinecartMetalChest.class).tracker(80, 3, true).build());
		}

		private static int entityID = 0;

		private static <E extends Entity> EntityEntryBuilder<E> createBuilder(final String name) {
			final EntityEntryBuilder<E> builder = EntityEntryBuilder.create();
			final ResourceLocation registryName = new ResourceLocation(MetalChests.MOD_ID, name);
			return builder.id(registryName, entityID++).name(MetalChests.MOD_ID + ":" + name);
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

		@SubscribeEvent
		public static void registerChestTransporter(final RegistryEvent.Register<TransportableChest> event) {
			final IForgeRegistry<TransportableChest> registry = event.getRegistry();

			for (ChestType type : ChestType.values()) {
				registry.register(new TransportableMetalChest(METAL_CHEST, type, new ResourceLocation(MetalChests.MOD_ID, "item/chesttransporter/" + type.getName())));
			}

			if (ModSupport.hasThaumcraft()) {
				registry.register(new TransportableChestOld(BlocksTC.hungryChest, -1, 1, "vanilla"));

				for (ChestType type : ChestType.values()) {
					registry.register(new TransportableMetalChest(HUNGRY_METAL_CHEST, type, new ResourceLocation(MetalChests.MOD_ID, "item/chesttransporter/hungry/" + type.getName())));
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
				registerModel(MINECART_METAL_CHEST, "item_minecart", type.ordinal(), "item=" + type.getName() + "_chest");
			}

			registerTileRenderer(TileMetalChest.class, RenderMetalChest.INSTANCE);

			if (ModSupport.hasThaumcraft()) {
				for (ChestType type : ChestType.values()) {
					registerModel(HUNGRY_METAL_CHEST, type.ordinal(), getVariantName(type));
				}

				registerTileRenderer(TileHungryMetalChest.class, new RenderHungryMetalChest());
			}

			for (ChestUpgrade type : ChestUpgrade.values()) {
				registerModel(METAL_UPGRADE, "item_upgrade", type.ordinal(), "item=" + type.getName());
			}

			RenderingRegistry.registerEntityRenderingHandler(EntityMinecartMetalChest.class, manager -> new RenderMinecartMetalChest(manager));
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
