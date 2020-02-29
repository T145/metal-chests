package t145.metalchests.api;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.IStringSerializable;

public enum MetalChestType implements IStringSerializable {

	COPPER("ingotCopper", MaterialColor.IRON),
	IRON("ingotIron", MaterialColor.IRON),
	SILVER("ingotSilver", MaterialColor.LIGHT_GRAY),
	GOLD("ingotGold", MaterialColor.GOLD),
	DIAMOND("gemDiamond", MaterialColor.DIAMOND),
	OBSIDIAN("obsidian", Material.ROCK, MaterialColor.OBSIDIAN, SoundType.STONE);

	private final String ore;
	private final Material material;
	private final MaterialColor color;
	private final SoundType sound;

	MetalChestType(String ore, Material material, MaterialColor color, SoundType sound) {
		this.ore = ore;
		this.material = material;
		this.color = color;
		this.sound = sound;
	}

	MetalChestType(String ore, MaterialColor color, SoundType sound) {
		this(ore, Material.IRON, color, sound);
	}

	MetalChestType(String ore, MaterialColor color) {
		this(ore, color, SoundType.METAL);
	}

	@Override
	public String getName() {
		return name().toLowerCase();
	}

	public String getOre() {
		return ore;
	}

	public Material getMaterial() {
		return material;
	}

	public MaterialColor getColor() {
		return color;
	}

	public SoundType getSound() {
		return sound;
	}
}
