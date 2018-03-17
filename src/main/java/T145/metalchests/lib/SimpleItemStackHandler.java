package T145.metalchests.lib;

import javax.annotation.Nullable;

import T145.metalchests.tiles.base.TileBase;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class SimpleItemStackHandler extends ItemStackHandler {

	private final TileBase te;

	public SimpleItemStackHandler(int invSize, @Nullable TileBase te) {
		super(invSize);
		this.te = te;
	}

	public SimpleItemStackHandler(NonNullList<ItemStack> topStacks, @Nullable TileBase te) {
		super(topStacks);
		this.te = te;
	}

	@Override
	public void onContentsChanged(int slot) {
		if (te != null) {
			te.markDirty();
		}
	}

	public NonNullList<ItemStack> getStacks() {
		return stacks;
	}

	public int size() {
		return stacks.size();
	}

	public void dropItems(World world, BlockPos pos) {
		for (int i = 0; i < size(); ++i) {
			ItemStack stack = getStackInSlot(i);

			if (!stack.isEmpty()) {
				InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
			}
		}
	}

	public int calcRedstone() {
		int i = 0;
		float f = 0.0F;

		for (int j = 0; j < size(); ++j) {
			ItemStack itemstack = getStackInSlot(j);

			if (!itemstack.isEmpty()) {
				f += itemstack.getCount() / Math.min(getSlotLimit(j), itemstack.getMaxStackSize());
				++i;
			}
		}

		f = f / size();
		return MathHelper.floor(f * 14.0F) + (i > 0 ? 1 : 0);
	}
}