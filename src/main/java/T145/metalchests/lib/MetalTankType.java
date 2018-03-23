package T145.metalchests.lib;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.oredict.OreDictionary;

public enum MetalTankType implements IStringSerializable {

	BASE("blockGlass"),
	COPPER(MetalChestType.COPPER),
	IRON(MetalChestType.IRON),
	SILVER(MetalChestType.SILVER),
	GOLD(MetalChestType.GOLD),
	DIAMOND(MetalChestType.DIAMOND),
	OBSIDIAN(MetalChestType.OBSIDIAN);

	private final String oreDictEntry;

	public Material getMaterial() {
		return Material.GLASS;
	}

	public MapColor getMapColor() {
		return MapColor.QUARTZ;
	}

	public SoundType getSoundType() {
		return SoundType.GLASS;
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

	MetalTankType(String oreDictEntry) {
		this.oreDictEntry = oreDictEntry;
	}

	MetalTankType(MetalChestType type) {
		this(type.getOreDictEntry());
	}
}