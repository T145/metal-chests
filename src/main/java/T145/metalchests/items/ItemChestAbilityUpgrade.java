package T145.metalchests.items;

import T145.metalchests.items.base.ItemBase;
import T145.metalchests.lib.MetalChestType;

public class ItemChestAbilityUpgrade extends ItemBase {

	public ItemChestAbilityUpgrade() {
		super("ability_upgrade", MetalChestType.AbilityUpgrade.values());
	}
}