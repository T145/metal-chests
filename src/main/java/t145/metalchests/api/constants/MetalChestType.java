package t145.metalchests.api.constants;

import net.minecraft.util.IStringSerializable;

public enum MetalChestType implements IStringSerializable {

	COPPER("ingotCopper"),
	IRON("ingotIron"),
	SILVER("ingotSilver"),
	GOLD("ingotGold"),
	DIAMOND("gemDiamond"),
	OBSIDIAN("obsidian");

	private final String ore;

	MetalChestType(String ore) {
		this.ore = ore;
	}

	@Override
	public String getName() {
		return name().toLowerCase();
	}

	public String getOre() {
		return ore;
	}
}
