package t145.metalchests.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MetalChests.MOD_ID)
@EventBusSubscriber()
public class MetalChests {

	public static final String MOD_ID = "metalchests";
	public static final String MOD_NAME = "MetalChests";
	public static final String MOD_VERSION = "@VERSION@";
	public static final String UPDATE_JSON = "https://raw.githubusercontent.com/T145/metalchests/master/update.json";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public MetalChests() {
		FMLJavaModLoadingContext ctx = FMLJavaModLoadingContext.get();
		IEventBus bus = ctx.getModEventBus();
		bus.addListener(this::metalchests$setup);
		bus.addListener(this::metalchests$setupClient);
	}

	private void metalchests$setup(final FMLCommonSetupEvent event) {
	}

	private void metalchests$setupClient(final FMLClientSetupEvent event) {
	}

	@SubscribeEvent
	public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
		// register a new block here
		LOGGER.info("HELLO from Register Block");
	}
}
