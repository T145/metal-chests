package T145.metalchests.tiles.base;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileInventory extends TileBase {

	protected SimpleItemStackHandler handler = createInventory();

	@Override
	public void readPacketNBT(NBTTagCompound tag) {
		handler = createInventory();
		handler.deserializeNBT(tag);
	}

	@Override
	public void writePacketNBT(NBTTagCompound tag) {
		tag.merge(handler.serializeNBT());
	}

	public abstract int getInventorySize();

	protected SimpleItemStackHandler createInventory() {
		return new SimpleItemStackHandler(this, true);
	}

	public IItemHandlerModifiable getInventory() {
		return handler;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler);
		}
		return super.getCapability(cap, side);
	}

	protected static class SimpleItemStackHandler extends ItemStackHandler {

		private final boolean allowWrite;
		private final TileInventory inv;

		public SimpleItemStackHandler(TileInventory inv, boolean allowWrite) {
			super(inv.getInventorySize());
			this.inv = inv;
			this.allowWrite = allowWrite;
		}

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			if (allowWrite) {
				return super.insertItem(slot, stack, simulate);
			} else {
				return stack;
			}
		}

		@Nonnull
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if (allowWrite) {
				return super.extractItem(slot, amount, simulate);
			} else {
				return ItemStack.EMPTY;
			}
		}

		@Override
		public void onContentsChanged(int slot) {
			inv.markDirty();
		}
	}
}