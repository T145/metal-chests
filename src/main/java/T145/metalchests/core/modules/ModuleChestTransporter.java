package T145.metalchests.core.modules;

import T145.metalchests.api.BlocksMetalChests;
import T145.metalchests.api.ModSupport;
import T145.metalchests.blocks.BlockMetalChest.ChestType;
import T145.metalchests.compat.chesttransporter.TransportableMetalChest;
import T145.metalchests.core.MetalChests;
import cubex2.mods.chesttransporter.api.TransportableChest;
import cubex2.mods.chesttransporter.chests.TransportableChestOld;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import thaumcraft.api.blocks.BlocksTC;

@EventBusSubscriber(modid = MetalChests.MOD_ID)
class ModuleChestTransporter {

	@Optional.Method(modid = ModSupport.ChestTransporter.MOD_ID)
	@SubscribeEvent
	public static void registerChestTransporter(final RegistryEvent.Register<TransportableChest> event) {
		final IForgeRegistry<TransportableChest> registry = event.getRegistry();

		for (ChestType type : ChestType.values()) {
			TransportableMetalChest chest = new TransportableMetalChest(BlocksMetalChests.METAL_CHEST, type, new ResourceLocation(MetalChests.MOD_ID, "item/chesttransporter/" + type.getName()));
			registry.register(chest);
			//ChestRegistry.registerMinecart(EntityMinecartMetalChest.class, chest);
		}

		if (ModSupport.hasThaumcraft()) {
			registry.register(new TransportableChestOld(BlocksTC.hungryChest, -1, 1, "vanilla"));

			for (ChestType type : ChestType.values()) {
				registry.register(new TransportableMetalChest(BlocksMetalChests.HUNGRY_METAL_CHEST, type, new ResourceLocation(MetalChests.MOD_ID, "item/chesttransporter/hungry/" + type.getName())));
			}
		}
	}
}
