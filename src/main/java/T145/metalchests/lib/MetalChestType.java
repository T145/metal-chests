package T145.metalchests.lib;

import javax.annotation.Nullable;

import T145.metalchests.MetalChests;
import T145.metalchests.containers.ContainerMetalChest;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.block.BlockChest;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public enum MetalChestType implements IStringSerializable {

	COPPER(45, 1, Material.IRON, Material.IRON.getMaterialMapColor(), SoundType.METAL, "ingotCopper"),
	IRON(54, 1, Material.IRON, MapColor.IRON, SoundType.METAL, "ingotIron"),
	SILVER(72, 2, Material.IRON, Material.IRON.getMaterialMapColor(), SoundType.METAL, "ingotSilver"),
	GOLD(81, 2, Material.IRON, MapColor.GOLD, SoundType.METAL, "ingotGold"),
	DIAMOND(108, 3, Material.IRON, MapColor.DIAMOND, SoundType.METAL, "gemDiamond"),
	OBSIDIAN(108, 3, Material.ROCK, MapColor.OBSIDIAN, SoundType.STONE, "blockObsidian"),
	CRYSTAL(108, 3, Material.GLASS, MapColor.QUARTZ, SoundType.GLASS, "blockGlass");

	private final int inventorySize;
	private final int upgradeInventorySize;
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

	public int getUpgradeInventorySize() {
		return upgradeInventorySize;
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
		return values()[meta]; 
	}

	MetalChestType(int inventorySize, int upgradeInventorySize, Material material, MapColor color, SoundType sound, String oreDictEntry) {
		this.inventorySize = inventorySize;
		this.upgradeInventorySize = upgradeInventorySize;
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

	public enum StructureUpgrade implements IStringSerializable {

		WOOD_COPPER(null, COPPER),
		WOOD_IRON(null, IRON),
		COPPER_IRON(COPPER, IRON),
		COPPER_SILVER(COPPER, SILVER),
		IRON_GOLD(IRON, GOLD),
		SILVER_GOLD(SILVER, GOLD),
		GOLD_DIAMOND(GOLD, DIAMOND),
		DIAMOND_OBSIDIAN(DIAMOND, OBSIDIAN),
		DIAMOND_CRYSTAL(DIAMOND, CRYSTAL);

		@Nullable
		private final MetalChestType base;
		private final MetalChestType upgrade;

		@Nullable
		public MetalChestType getBase() {
			return base;
		}

		public boolean hasBase() {
			return base != null;
		}

		public MetalChestType getUpgrade() {
			return upgrade;
		}

		public boolean canUpgradeWood(IBlockState target) {
			return base == null && target.getBlock() instanceof BlockChest;
		}

		@Override
		public String getName() {
			return name().toLowerCase();
		}

		public static StructureUpgrade byMetadata(int meta) {
			return values()[meta]; 
		}

		StructureUpgrade(MetalChestType base, MetalChestType upgrade) {
			this.base = base;
			this.upgrade = upgrade;
		}
	}

	public enum AbilityUpgrade implements IStringSerializable {

		STORAGE,
		ENDER,
		REDSTONE,
		HOPPING,
		VOID,
		CREATIVE;

		@Override
		public String getName() {
			return name().toLowerCase();
		}
	}
}