package T145.metalchests.api;

import T145.metalchests.blocks.BlockHungryMetalChest;
import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.blocks.BlockSortingHungryMetalChest;
import T145.metalchests.blocks.BlockSortingMetalChest;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder("metalchests")
public class BlocksMetalChests {

	private BlocksMetalChests() {}

	@ObjectHolder(BlockMetalChest.NAME)
	public static Block METAL_CHEST;

	@ObjectHolder(BlockHungryMetalChest.NAME)
	public static Block HUNGRY_METAL_CHEST;

	@ObjectHolder(BlockSortingMetalChest.NAME)
	public static Block SORTING_METAL_CHEST;

	@ObjectHolder(BlockSortingHungryMetalChest.NAME)
	public static Block SORTING_HUNGRY_METAL_CHEST;

}
