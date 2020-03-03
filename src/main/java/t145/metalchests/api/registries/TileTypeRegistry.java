package t145.metalchests.api.registries;

import static t145.metalchests.api.Reference.*;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import t145.metalchests.tiles.MetalChestTile;

@ObjectHolder(MOD_ID)
public class TileTypeRegistry {

	@ObjectHolder(COPPER_CHEST_ID)
	public static final TileEntityType<MetalChestTile> COPPER_CHEST_TILE_TYPE = null;

	@ObjectHolder(IRON_CHEST_ID)
	public static final TileEntityType<MetalChestTile> IRON_CHEST_TILE_TYPE = null;

	@ObjectHolder(SILVER_CHEST_ID)
	public static final TileEntityType<MetalChestTile> SILVER_CHEST_TILE_TYPE = null;

	@ObjectHolder(GOLD_CHEST_ID)
	public static final TileEntityType<MetalChestTile> GOLD_CHEST_TILE_TYPE = null;

	@ObjectHolder(DIAMOND_CHEST_ID)
	public static final TileEntityType<MetalChestTile> DIAMOND_CHEST_TILE_TYPE = null;

	@ObjectHolder(OBSIDIAN_CHEST_ID)
	public static final TileEntityType<MetalChestTile> OBSIDIAN_CHEST_TILE_TYPE = null;

	@ObjectHolder(EMERALD_CHEST_ID)
	public static final TileEntityType<MetalChestTile> EMERALD_CHEST_TILE_TYPE = null;

	private TileTypeRegistry() {
		throw new UnsupportedOperationException();
	}
}
