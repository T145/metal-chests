package T145.metalchests.core;

import T145.metalchests.MetalChests;
import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.blocks.base.BlockItemBase;
import T145.metalchests.client.render.blocks.RenderMetalChest;
import T145.metalchests.lib.MetalChestType;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(MetalChests.MODID)
public class ModLoader {

	public static final BlockMetalChest METAL_CHEST = new BlockMetalChest();

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
		}

		private static void registerItemBlock(IForgeRegistry<Item> registry, Block block) {
			registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		}

		private static void registerItemBlock(IForgeRegistry<Item> registry, Block block, Class types) {
			registry.register(new BlockItemBase(block, types).setRegistryName(block.getRegistryName()));
		}
	}

	@EventBusSubscriber(modid = MetalChests.MODID, value = Side.CLIENT)
	public static class ClientLoader {

		@SubscribeEvent
		public static void onModelRegistration(ModelRegistryEvent event) {
			for (MetalChestType type : MetalChestType.values()) {
				registerBlockModel(METAL_CHEST, type.ordinal(), type);
			}
			ClientRegistry.bindTileEntitySpecialRenderer(TileMetalChest.class, new RenderMetalChest());
		}

		public static void registerBlockModel(Block block, int meta, String variant) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(block.getRegistryName(), variant));
		}

		public static void registerBlockModel(Block block, int meta, IStringSerializable variant) {
			registerBlockModel(block, meta, "variant=" + variant.getName());
		}
	}
}