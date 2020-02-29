package t145.metalchests.core;

import static t145.metalchests.api.registries.ModReference.*;

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
import t145.metalchests.api.registries.ModBlocks;
import t145.metalchests.blocks.MetalChestBlock;
import t145.metalchests.tiles.MetalChestTile;

@Mod(MOD_ID)
@EventBusSubscriber
public class MetalChests {

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
		event.getRegistry().register(new MetalChestBlock().setRegistryName(MOD_ID, METAL_CHEST_ID));
	}

	@SubscribeEvent
	public static void registerTiles(final Register<TileEntityType<?>> event) {
		event.getRegistry().register(TileEntityType.Builder.create(MetalChestTile::new, ModBlocks.METAL_CHEST).build(null).setRegistryName(MOD_ID, METAL_CHEST_TILE_TYPE_ID));
	}
}
