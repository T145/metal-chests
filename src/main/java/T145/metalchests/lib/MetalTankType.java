package T145.metalchests.lib;

import net.minecraft.util.IStringSerializable;
import net.minecraftforge.oredict.OreDictionary;

public enum MetalTankType implements IStringSerializable {

	BASE(4, "blockGlass"),
	COPPER(6, MetalChestType.COPPER),
	IRON(8, MetalChestType.IRON),
	SILVER(10, MetalChestType.SILVER),
	GOLD(12, MetalChestType.GOLD),
	DIAMOND(14, MetalChestType.DIAMOND),
	OBSIDIAN(14, MetalChestType.OBSIDIAN);

	private final int capacity;
	private final String oreDictEntry;

	public int getCapacity() {
		return capacity;
	}

	public String getOreDictEntry() {
		return oreDictEntry;
	}

	public boolean isRegistered() {
		return !OreDictionary.getOres(oreDictEntry).isEmpty();
	}

	@Override
	public String getName() {
		return name().toLowerCase();
	}

	public static MetalTankType byMetadata(int meta) {
		return values()[meta]; 
	}

	MetalTankType(int capacity, String oreDictEntry) {
		this.capacity = capacity;
		this.oreDictEntry = oreDictEntry;
	}

	MetalTankType(int capacity, MetalChestType type) {
		this(capacity, type.getOreDictEntry());
	}
}