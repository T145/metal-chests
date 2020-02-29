package t145.metalchests.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import t145.metalchests.blocks.MetalChestBlock;
import t145.metalchests.tiles.MetalChestTile;

@Mod(MetalChests.MOD_ID)
@EventBusSubscriber()
public class MetalChests {

	public static final String MOD_ID = "metalchests";
	public static final String MOD_NAME = "MetalChests";
	public static final String MOD_VERSION = "@VERSION@";
	public static final String UPDATE_JSON = "https://raw.githubusercontent.com/T145/metalchests/master/update.json";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static final Block METAL_CHEST_BLOCK = new MetalChestBlock();
	public static final TileEntityType<MetalChestTile> METAL_CHEST_TYPE = TileEntityType.Builder.create(MetalChestTile::new, METAL_CHEST_BLOCK).build(null);

	static {
		METAL_CHEST_TYPE.setRegistryName(MOD_ID, "metal_chest");
	}

	public MetalChests() {
		FMLJavaModLoadingContext ctx = FMLJavaModLoadingContext.get();
		IEventBus bus = ctx.getModEventBus();
		bus.addListener(this::metalchests$setup);
		bus.addListener(this::metalchests$setupClient);
	}

	private void metalchests$setup(final FMLCommonSetupEvent event) {}

	private void metalchests$setupClient(final FMLClientSetupEvent event) {}

	@SubscribeEvent
	public static void registerBlocks(final Register<Block> event) {
		event.getRegistry().register(METAL_CHEST_BLOCK);
	}

	@SubscribeEvent
	public static void registerTiles(final Register<TileEntityType<?>> event) {
		event.getRegistry().register(METAL_CHEST_TYPE);
	}
}
