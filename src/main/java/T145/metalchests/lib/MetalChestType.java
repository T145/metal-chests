package T145.metalchests.lib;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.oredict.OreDictionary;

public enum MetalChestType implements IStringSerializable {

	COPPER(Material.IRON, Material.IRON.getMaterialMapColor(), SoundType.METAL, "ingotCopper"),
	IRON(Material.IRON, MapColor.IRON, SoundType.METAL, "ingotIron"),
	SILVER(Material.IRON, Material.IRON.getMaterialMapColor(), SoundType.METAL, "ingotSilver"),
	GOLD(Material.IRON, MapColor.GOLD, SoundType.METAL, "ingotGold"),
	DIAMOND(Material.IRON, MapColor.DIAMOND, SoundType.METAL, "gemDiamond"),
	OBSIDIAN(Material.ROCK, MapColor.OBSIDIAN, SoundType.STONE, "blockObsidian"),
	CRYSTAL(Material.GLASS, MapColor.QUARTZ, SoundType.GLASS, "blockGlass");

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
		return 0;
	}

	@Override
	public String getName() {
		return name().toLowerCase();
	}

	MetalChestType(Material material, MapColor color, SoundType sound, String oreDictEntry) {
		this.material = material;
		this.color = color;
		this.sound = sound;
		this.oreDictEntry = oreDictEntry;
	}
}