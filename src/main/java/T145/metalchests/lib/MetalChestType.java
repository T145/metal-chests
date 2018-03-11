package T145.metalchests.lib;

import T145.metalchests.MetalChests;
import T145.metalchests.containers.ContainerMetalChest;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public enum MetalChestType implements IStringSerializable {

	COPPER(45, Material.IRON, Material.IRON.getMaterialMapColor(), SoundType.METAL, "ingotCopper"),
	IRON(54, Material.IRON, MapColor.IRON, SoundType.METAL, "ingotIron"),
	SILVER(72, Material.IRON, Material.IRON.getMaterialMapColor(), SoundType.METAL, "ingotSilver"),
	GOLD(81, Material.IRON, MapColor.GOLD, SoundType.METAL, "ingotGold"),
	DIAMOND(108, Material.IRON, MapColor.DIAMOND, SoundType.METAL, "gemDiamond"),
	OBSIDIAN(108, Material.ROCK, MapColor.OBSIDIAN, SoundType.STONE, "blockObsidian"),
	CRYSTAL(108, Material.GLASS, MapColor.QUARTZ, SoundType.GLASS, "blockGlass");

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

	public static MetalChestType byMetadata(int meta) {
		return MetalChestType.values()[meta]; 
	}

	MetalChestType(int inventorySize, Material material, MapColor color, SoundType sound, String oreDictEntry) {
		this.inventorySize = inventorySize;
		this.material = material;
		this.color = color;
		this.sound = sound;
		this.oreDictEntry = oreDictEntry;
	}

	public enum GUI {

		COPPER(184, 184),
		IRON(184, 202),
		SILVER(184, 238),
		GOLD(184, 256),
		DIAMOND(238, 256),
		OBSIDIAN(238, 256),
		CRYSTAL(238, 256);

		private final int xSize;
		private final int ySize;

		public int getSizeX() {
			return xSize;
		}

		public int getSizeY() {
			return ySize;
		}

		public static GUI byMetadata(int meta) {
			return GUI.values()[meta]; 
		}

		public static GUI byType(MetalChestType type) {
			return byMetadata(type.ordinal());
		}

		public ResourceLocation getGuiTexture() {
			MetalChestType type = MetalChestType.byMetadata(ordinal());
			return new ResourceLocation(MetalChests.MODID, "textures/gui/" + (type.isLarge() ? "diamond" : type.getName()) + "_container.png");
		}

		public Container getContainer(TileMetalChest chest, EntityPlayer player) {
			return new ContainerMetalChest(chest, player, xSize, ySize);
		}

		GUI(int xSize, int ySize) {
			this.xSize = xSize;
			this.ySize = ySize;
		}
	}
}