package t145.metalchests.core;

import static t145.metalchests.api.Reference.COPPER_CHEST_ID;
import static t145.metalchests.api.Reference.DIAMOND_CHEST_ID;
import static t145.metalchests.api.Reference.GOLD_CHEST_ID;
import static t145.metalchests.api.Reference.IRON_CHEST_ID;
import static t145.metalchests.api.Reference.MOD_ID;
import static t145.metalchests.api.Reference.OBSIDIAN_CHEST_ID;
import static t145.metalchests.api.Reference.SILVER_CHEST_ID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import t145.metalchests.api.constants.MetalChestType;
import t145.metalchests.api.registries.BlockRegistry;
import t145.metalchests.api.registries.TileTypeRegistry;
import t145.metalchests.blocks.MetalChestBlock;
import t145.metalchests.client.MetalChestRenderer;
import t145.metalchests.config.ServerConfig;
import t145.metalchests.items.MetalChestItem;
import t145.metalchests.tiles.MetalChestTile;

@Mod(MOD_ID)
@EventBusSubscriber
public class MetalChests {

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public MetalChests() {
		FMLJavaModLoadingContext ctx = FMLJavaModLoadingContext.get();
		IEventBus bus = ctx.getModEventBus();
		ModLoadingContext config = ModLoadingContext.get();

		bus.addListener(this::metalchests$setup);
		bus.addListener(this::metalchests$setupClient);
		bus.addGenericListener(Block.class, this::metalchests$registerBlocks);
		bus.addGenericListener(Item.class, this::metalchests$registerItems);
		bus.addGenericListener(TileEntityType.class, this::metalchests$registerTiles);
		bus.addGenericListener(ContainerType.class, this::metalchests$registerContainers);

		config.registerConfig(ModConfig.Type.COMMON, ServerConfig.CONFIG);
		//config.registerConfig(ModConfig.Type.CLIENT, ClientConfig.CONFIG);
	}

	private void metalchests$setup(final FMLCommonSetupEvent event) {
	}

	private void metalchests$setupClient(final FMLClientSetupEvent event) {
		ClientRegistry.bindTileEntityRenderer(TileTypeRegistry.COPPER_CHEST_TILE_TYPE, (dispatcher) -> new MetalChestRenderer<MetalChestTile>(dispatcher));
		ClientRegistry.bindTileEntityRenderer(TileTypeRegistry.IRON_CHEST_TILE_TYPE, (dispatcher) -> new MetalChestRenderer<MetalChestTile>(dispatcher));
		ClientRegistry.bindTileEntityRenderer(TileTypeRegistry.SILVER_CHEST_TILE_TYPE, (dispatcher) -> new MetalChestRenderer<MetalChestTile>(dispatcher));
		ClientRegistry.bindTileEntityRenderer(TileTypeRegistry.GOLD_CHEST_TILE_TYPE, (dispatcher) -> new MetalChestRenderer<MetalChestTile>(dispatcher));
		ClientRegistry.bindTileEntityRenderer(TileTypeRegistry.DIAMOND_CHEST_TILE_TYPE, (dispatcher) -> new MetalChestRenderer<MetalChestTile>(dispatcher));
		ClientRegistry.bindTileEntityRenderer(TileTypeRegistry.OBSIDIAN_CHEST_TILE_TYPE, (dispatcher) -> new MetalChestRenderer<MetalChestTile>(dispatcher));
	}

	private void metalchests$registerBlocks(final Register<Block> event) {
		event.getRegistry().registerAll(
				new MetalChestBlock(getPropertiesFromBlock(MaterialColor.SAND, Blocks.IRON_BLOCK), () -> TileTypeRegistry.COPPER_CHEST_TILE_TYPE, MetalChestType.COPPER).setRegistryName(MOD_ID, COPPER_CHEST_ID),
				new MetalChestBlock(getPropertiesFromBlock(MaterialColor.IRON, Blocks.IRON_BLOCK), () -> TileTypeRegistry.IRON_CHEST_TILE_TYPE, MetalChestType.IRON).setRegistryName(MOD_ID, IRON_CHEST_ID),
				new MetalChestBlock(getPropertiesFromBlock(MaterialColor.LIGHT_GRAY, Blocks.IRON_BLOCK), () -> TileTypeRegistry.SILVER_CHEST_TILE_TYPE, MetalChestType.SILVER).setRegistryName(MOD_ID, SILVER_CHEST_ID),
				new MetalChestBlock(getPropertiesFromBlock(MaterialColor.GOLD, Blocks.GOLD_BLOCK), () -> TileTypeRegistry.GOLD_CHEST_TILE_TYPE, MetalChestType.GOLD).setRegistryName(MOD_ID, GOLD_CHEST_ID),
				new MetalChestBlock(getPropertiesFromBlock(MaterialColor.DIAMOND, Blocks.DIAMOND_BLOCK), () -> TileTypeRegistry.DIAMOND_CHEST_TILE_TYPE, MetalChestType.DIAMOND).setRegistryName(MOD_ID, DIAMOND_CHEST_ID),
				new MetalChestBlock(getPropertiesFromBlock(MaterialColor.SAND, Blocks.OBSIDIAN), () -> TileTypeRegistry.OBSIDIAN_CHEST_TILE_TYPE, MetalChestType.OBSIDIAN).setRegistryName(MOD_ID, OBSIDIAN_CHEST_ID)
			);
	}

	@SuppressWarnings("deprecation")
	private Properties getPropertiesFromBlock(MaterialColor color, Block block) {
		BlockState state = block.getDefaultState();
		return Properties.create(state.getMaterial(), color).sound(state.getSoundType()).harvestLevel(state.getHarvestLevel()).harvestTool(state.getHarvestTool()).hardnessAndResistance(state.getBlockHardness(null, null), block.getExplosionResistance());
	}

	private void metalchests$registerItems(final Register<Item> event) {
		event.getRegistry().registerAll(
				new MetalChestItem(BlockRegistry.COPPER_CHEST).setRegistryName(MOD_ID, COPPER_CHEST_ID),
				new MetalChestItem(BlockRegistry.IRON_CHEST).setRegistryName(MOD_ID, IRON_CHEST_ID),
				new MetalChestItem(BlockRegistry.SILVER_CHEST).setRegistryName(MOD_ID, SILVER_CHEST_ID),
				new MetalChestItem(BlockRegistry.GOLD_CHEST).setRegistryName(MOD_ID, GOLD_CHEST_ID),
				new MetalChestItem(BlockRegistry.DIAMOND_CHEST).setRegistryName(MOD_ID, DIAMOND_CHEST_ID),
				new MetalChestItem(BlockRegistry.OBSIDIAN_CHEST).setRegistryName(MOD_ID, OBSIDIAN_CHEST_ID)
			);
	}

	private void metalchests$registerTiles(final Register<TileEntityType<?>> event) {
		event.getRegistry().registerAll(
				TileEntityType.Builder.create(() -> new MetalChestTile(TileTypeRegistry.COPPER_CHEST_TILE_TYPE), BlockRegistry.COPPER_CHEST).build(null).setRegistryName(MOD_ID, COPPER_CHEST_ID),
				TileEntityType.Builder.create(() -> new MetalChestTile(TileTypeRegistry.IRON_CHEST_TILE_TYPE), BlockRegistry.COPPER_CHEST).build(null).setRegistryName(MOD_ID, IRON_CHEST_ID),
				TileEntityType.Builder.create(() -> new MetalChestTile(TileTypeRegistry.SILVER_CHEST_TILE_TYPE), BlockRegistry.COPPER_CHEST).build(null).setRegistryName(MOD_ID, SILVER_CHEST_ID),
				TileEntityType.Builder.create(() -> new MetalChestTile(TileTypeRegistry.GOLD_CHEST_TILE_TYPE), BlockRegistry.COPPER_CHEST).build(null).setRegistryName(MOD_ID, GOLD_CHEST_ID),
				TileEntityType.Builder.create(() -> new MetalChestTile(TileTypeRegistry.DIAMOND_CHEST_TILE_TYPE), BlockRegistry.COPPER_CHEST).build(null).setRegistryName(MOD_ID, DIAMOND_CHEST_ID),
				TileEntityType.Builder.create(() -> new MetalChestTile(TileTypeRegistry.OBSIDIAN_CHEST_TILE_TYPE), BlockRegistry.COPPER_CHEST).build(null).setRegistryName(MOD_ID, OBSIDIAN_CHEST_ID)
			);
	}

	private void metalchests$registerContainers(final Register<ContainerType<?>> event) {
	}
}
