package T145.metalchests.core;

import java.util.HashSet;

import T145.metalchests.MetalChests;
import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.blocks.base.BlockItemBase;
import T145.metalchests.client.render.blocks.RenderMetalChest;
import T145.metalchests.entities.EntityMinecartMetalChest;
import T145.metalchests.entities.ai.EntityAIOcelotSitOnChest;
import T145.metalchests.items.ItemChestStructureUpgrade;
import T145.metalchests.items.ItemMinecartMetalChest;
import T145.metalchests.items.base.ItemBase;
import T145.metalchests.lib.MetalChestType;
import T145.metalchests.tiles.TileMetalChest;
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
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(MetalChests.MODID)
public class ModLoader {

	public static final BlockMetalChest METAL_CHEST = new BlockMetalChest();

	public static final ItemBase MINECART_METAL_CHEST = new ItemMinecartMetalChest();
	public static final ItemBase CHEST_UPGRADE_STRUCTURE = new ItemChestStructureUpgrade();

	@EventBusSubscriber(modid = MetalChests.MODID)
	public static class ServerLoader {

		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			final IForgeRegistry<Block> registry = event.getRegistry();
			registry.register(METAL_CHEST);
			registerTileEntity(TileMetalChest.class);
		}

		private static void registerTileEntity(Class tileClass) {
			GameRegistry.registerTileEntity(tileClass, tileClass.getSimpleName());
		}

		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			final IForgeRegistry<Item> registry = event.getRegistry();
			registerItemBlock(registry, METAL_CHEST, MetalChestType.class);
			registry.register(MINECART_METAL_CHEST);
			registry.register(CHEST_UPGRADE_STRUCTURE);
		}

		private static void registerItemBlock(IForgeRegistry<Item> registry, Block block) {
			registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		}

		private static void registerItemBlock(IForgeRegistry<Item> registry, Block block, Class types) {
			registry.register(new BlockItemBase(block, types).setRegistryName(block.getRegistryName()));
		}

		@SubscribeEvent
		public static void registerEntities(final RegistryEvent.Register<EntityEntry> event) {
			final IForgeRegistry<EntityEntry> registry = event.getRegistry();
			EntityEntry minecartEntry = createBuilder("minecart_metal_chest")
					.entity(EntityMinecartMetalChest.class)
					.tracker(80, 3, true)
					.build();
			registry.register(minecartEntry);
		}

		private static int entityID = 0;

		private static <E extends Entity> EntityEntryBuilder<E> createBuilder(final String name) {
			final EntityEntryBuilder<E> builder = EntityEntryBuilder.create();
			final ResourceLocation registryName = new ResourceLocation(MetalChests.MODID, name);
			return builder.id(registryName, entityID++).name(MetalChests.MODID + ":" + name);
		}

		@SubscribeEvent
		public void changeSittingTaskForOcelots(LivingUpdateEvent event) {
			EntityLivingBase creature = event.getEntityLiving();

			if (creature instanceof EntityOcelot && creature.ticksExisted < 5) {
				EntityOcelot ocelot = (EntityOcelot) creature;
				HashSet<EntityAITaskEntry> hashset = new HashSet<EntityAITaskEntry>();

				for (EntityAITaskEntry task : ocelot.tasks.taskEntries) {
					if (task.action.getClass() == EntityAIOcelotSit.class) {
						hashset.add(task);
					}
				}

				for (EntityAITaskEntry task : hashset) {
					ocelot.tasks.removeTask(task.action);
					ocelot.tasks.addTask(task.priority, new EntityAIOcelotSitOnChest(ocelot, 0.4F));
				}
			}
		}
	}

	@EventBusSubscriber(modid = MetalChests.MODID, value = Side.CLIENT)
	public static class ClientLoader {

		@SubscribeEvent
		public static void onModelRegistration(ModelRegistryEvent event) {
			for (MetalChestType type : MetalChestType.values()) {
				registerBlockModel(METAL_CHEST, type.ordinal(), type);
			}

			registerTileRenderer(TileMetalChest.class, new RenderMetalChest());

			for (MetalChestType.StructureUpgrade type : MetalChestType.StructureUpgrade.values()) {
				registerItemModel(CHEST_UPGRADE_STRUCTURE, type.ordinal(), type.getName());
			}
		}

		public static String getVariantName(IStringSerializable variant) {
			return "variant=" + variant.getName();
		}

		public static void registerBlockModel(Block block, int meta, String path, String variant) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(MetalChests.MODID + ":" + path, variant));
		}

		public static void registerBlockModel(Block block, int meta, String variant) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(block.getRegistryName(), variant));
		}

		public static void registerBlockModel(Block block, int meta, IStringSerializable variant) {
			registerBlockModel(block, meta, getVariantName(variant));
		}

		public static void registerItemModel(Item item, int meta, String path) {
			ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(MetalChests.MODID + ":" + path, "inventory"));
		}

		public static void registerItemModel(Item item, int meta) {
			ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}

		public static void registerTileRenderer(Class tileClass, TileEntitySpecialRenderer tileRenderer) {
			ClientRegistry.bindTileEntitySpecialRenderer(tileClass, tileRenderer);
		}
	}
}