package T145.metalchests.compat;

import T145.metalchests.api.config.ConfigMC;
import T145.metalchests.api.consts.ChestType;
import T145.metalchests.api.consts.ChestUpgrade;
import T145.metalchests.api.obj.BlocksMC;
import T145.metalchests.api.obj.ItemsMC;
import mezz.jei.api.IItemBlacklist;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class CompatJEI implements IModPlugin {

	private IItemBlacklist blacklist;

	@Override
	public void register(final IModRegistry registry) {
		this.blacklist = registry.getJeiHelpers().getItemBlacklist();

		if (ConfigMC.hasThaumcraft()) {
			ChestType.TIERS.forEach(type -> registry.addDescription(new ItemStack(BlocksMC.METAL_HUNGRY_CHEST, 1, type.ordinal()), "info.metalchests.jei_hungry_reminder", "info.metalchests.jei_hungry_cost"));
			ChestUpgrade.TIERS.forEach(type -> registry.addDescription(new ItemStack(ItemsMC.HUNGRY_CHEST_UPGRADE, 1, type.ordinal()), "info.metalchests.jei_hungry_reminder", "info.metalchests.jei_hungry_cost"));
		}
	}

	/*
	 * For some reason, JEI works w/ chest upgrades but not the chests.
	 */
	@Override
	public void onRuntimeAvailable(final IJeiRuntime runtime) {
		for (ChestType type : ChestType.values()) {
			if (!type.isRegistered()) {
				blacklist.addItemToBlacklist(new ItemStack(BlocksMC.METAL_CHEST, 1, type.ordinal()));

				if (ConfigMC.hasThaumcraft()) {
					blacklist.addItemToBlacklist(new ItemStack(BlocksMC.METAL_HUNGRY_CHEST, 1, type.ordinal()));
				}

				if (ConfigMC.hasRefinedRelocation()) {
					blacklist.addItemToBlacklist(new ItemStack(BlocksMC.METAL_SORTING_CHEST, 1, type.ordinal()));

					if (ConfigMC.hasThaumcraft()) {
						blacklist.addItemToBlacklist(new ItemStack(BlocksMC.METAL_SORTING_HUNGRY_CHEST, 1, type.ordinal()));
					}
				}
			}
		}
	}
}
