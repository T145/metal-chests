package T145.metalchests.items;

import T145.metalchests.items.base.ItemBase;
import T145.metalchests.lib.MetalChestType;

public class ItemChestStructureUpgrade extends ItemBase {

	public ItemChestStructureUpgrade() {
		super("structure_upgrade", MetalChestType.StructureUpgrade.values());
	}
}