package t145.metalchests.lib;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import t145.metalchests.config.ServerConfig;

public enum MetalChestType implements IStringSerializable {

	COPPER("ingotCopper", "Copper Chest"),
	IRON("ingotIron", "Iron Chest"),
	SILVER("ingotSilver", "Silver Chest"),
	GOLD("ingotGold", "Gold Chest"),
	DIAMOND("gemDiamond", "Diamond Chest"),
	OBSIDIAN("obsidian", "Obsidian Chest"),
	EMERALD("gemEmerald", "Emerald Chest");

	private final String ore;
	private final String title;

	MetalChestType(String ore, String title) {
		this.ore = ore;
		this.title = title;
	}

	@Override
	public String getName() {
		return name().toLowerCase();
	}

	public String getOre() {
		return ore;
	}

	public ITextComponent getTitle() {
		return new StringTextComponent(title);
	}

	public int getRows() {
		switch (this) {
		case COPPER:
			return ServerConfig.INSTANCE.numOfCopperRows.get();
		case IRON:
			return ServerConfig.INSTANCE.numOfIronRows.get();
		case SILVER:
			return ServerConfig.INSTANCE.numOfSilverRows.get();
		case GOLD:
			return ServerConfig.INSTANCE.numOfGoldRows.get();
		case DIAMOND:
			return ServerConfig.INSTANCE.numOfDiamondRows.get();
		case OBSIDIAN:
			return ServerConfig.INSTANCE.numOfObsidianRows.get();
		case EMERALD:
			return ServerConfig.INSTANCE.numOfEmeraldRows.get();
		default:
			return 1;
		}
	}

	public int getColumns() {
		switch (this) {
		case COPPER:
			return ServerConfig.INSTANCE.numOfCopperColumns.get();
		case IRON:
			return ServerConfig.INSTANCE.numOfIronColumns.get();
		case SILVER:
			return ServerConfig.INSTANCE.numOfSilverColumns.get();
		case GOLD:
			return ServerConfig.INSTANCE.numOfGoldColumns.get();
		case DIAMOND:
			return ServerConfig.INSTANCE.numOfDiamondColumns.get();
		case OBSIDIAN:
			return ServerConfig.INSTANCE.numOfObsidianColumns.get();
		case EMERALD:
			return ServerConfig.INSTANCE.numOfEmeraldColumns.get();
		default:
			return 1;
		}
	}

	public int getInventorySize() {
		return this.getRows() * this.getColumns();
	}
}
