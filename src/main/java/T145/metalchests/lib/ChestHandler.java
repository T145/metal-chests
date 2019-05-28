package T145.metalchests.lib;

import T145.metalchests.api.constants.ChestType;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class ChestHandler extends ItemStackHandler {

	private boolean updateComparator;

	public ChestHandler(ChestType type) {
		super(type.getInventorySize());
	}

	@Override
	protected void onLoad() {
		// Perhaps usable for something?
	}

	@Override
	protected void onContentsChanged(int slot) {
		// TileMetalChest.this.markDirty();
		// TODO: Used when the chest is transparent, to automatically
		// update items and display them properly.
		// Implement it when crystal upgrades are being added.

		//world.updateComparatorOutputLevel(pos, getBlockType());
		// update on the next tick, reducing comparator updates to the bare minimum
		if (!updateComparator) {
			updateComparator = true;
		}
	}

	public void tick(World world, BlockPos pos, Block block) {
		if (updateComparator) {
			updateComparator = false;
			world.updateComparatorOutputLevel(pos, block);
		}
	}
}
