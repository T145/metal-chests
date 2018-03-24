package T145.metalchests.lib;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.oredict.OreDictionary;

public enum ProjectTableType implements IStringSerializable {

	WOOD(27, Material.WOOD, MapColor.WOOD, SoundType.WOOD, "plankWood"),
	COPPER(MetalChestType.COPPER),
	IRON(MetalChestType.IRON),
	SILVER(MetalChestType.SILVER),
	GOLD(MetalChestType.GOLD),
	DIAMOND(MetalChestType.DIAMOND),
	OBSIDIAN(MetalChestType.OBSIDIAN);

	private final int inventorySize;
	private final Material material;
	private final MapColor color;
	private final SoundType sound;
	private final String oreDictEntry;

	public Material getMaterial() {
		return material;
	}

	public MapColor getMapColor() {
		return color;
	}

	public SoundType getSoundType() {
		return sound;
	}

	public boolean isRegistered() {
		return !OreDictionary.getOres(oreDictEntry).isEmpty();
	}

	public int getInventorySize() {
		return inventorySize;
	}

	public boolean isLarge() {
		return inventorySize > 100;
	}

	public int getRowLength() {
		return isLarge() ? 12 : 9;
	}

	public int getRowCount() {
		return inventorySize / getRowLength();
	}

	@Override
	public String getName() {
		return name().toLowerCase();
	}

	public static ProjectTableType byMetadata(int meta) {
		return values()[meta]; 
	}

	ProjectTableType(int inventorySize, Material material, MapColor color, SoundType sound, String oreDictEntry) {
		this.inventorySize = inventorySize;
		this.material = material;
		this.color = color;
		this.sound = sound;
		this.oreDictEntry = oreDictEntry;
	}

	ProjectTableType(MetalChestType type) {
		this(type.getInventorySize(), type.getMaterial(), type.getMapColor(), type.getSoundType(), type.getOreDictEntry());
	}
}