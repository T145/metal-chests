package t145.metalchests.api.registries;

import static t145.metalchests.api.registries.ModReference.*;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(MOD_ID)
public enum ModTileTypes {
	;

	@ObjectHolder(METAL_CHEST_TILE_TYPE_ID)
	public static final TileEntityType<?> METAL_CHEST_TILE_TYPE = null;
}
