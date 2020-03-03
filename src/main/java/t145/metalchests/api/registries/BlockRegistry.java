package t145.metalchests.api.registries;

import static t145.metalchests.api.Reference.*;

import net.minecraft.block.Block;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(MOD_ID)
public class BlockRegistry {

	@ObjectHolder(COPPER_CHEST_ID)
	public static final Block COPPER_CHEST = null;

	@ObjectHolder(IRON_CHEST_ID)
	public static final Block IRON_CHEST = null;

	@ObjectHolder(SILVER_CHEST_ID)
	public static final Block SILVER_CHEST = null;

	@ObjectHolder(GOLD_CHEST_ID)
	public static final Block GOLD_CHEST = null;

	@ObjectHolder(OBSIDIAN_CHEST_ID)
	public static final Block OBSIDIAN_CHEST = null;

	@ObjectHolder(DIAMOND_CHEST_ID)
	public static final Block DIAMOND_CHEST = null;

	@ObjectHolder(EMERALD_CHEST_ID)
	public static final Block EMERALD_CHEST = null;

	private BlockRegistry() {
		throw new UnsupportedOperationException();
	}
}
