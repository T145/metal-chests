package T145.metalchests.core.compat;

import T145.metalchests.api.BlocksMC;
import T145.metalchests.api.chests.UpgradeRegistry;
import T145.metalchests.api.constants.RegistryMC;
import T145.metalchests.core.MetalChests;
import T145.metalchests.entities.EntityMinecartMetalChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.quark.decoration.feature.VariedChests;

@EventBusSubscriber(modid = RegistryMC.ID)
class CompatQuark {

	private CompatQuark() {}

	@Optional.Method(modid = RegistryMC.ID_QUARK)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		UpgradeRegistry.registerChest(VariedChests.custom_chest_trap, BlocksMC.METAL_CHEST);
	}

	@Optional.Method(modid = RegistryMC.ID_QUARK)
	@SubscribeEvent
	public static void onEntityInteractQuark(EntityInteract event) {
		Entity target = event.getTarget();

		if (target instanceof EntityMinecartEmpty) {
			MetalChests.createChestEntityFromInteraction(target, new EntityMinecartMetalChest((EntityMinecartEmpty) target), event);
		}
	}
}
