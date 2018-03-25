package T145.metalchests.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class InventoryProjectTableCrafting extends InventoryCrafting {

	private IItemHandler handler;

	public InventoryProjectTableCrafting(IItemHandler handler) {
		super(new Container() {

			@Override
			public boolean canInteractWith(EntityPlayer player) {
				return false;
			}
		}, 3, 3);
		this.handler = handler;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if (handler != null) {
			return handler.getStackInSlot(index);
		}
		return super.getStackInSlot(index);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		if (handler != null) {
			return handler.extractItem(index, getStackInSlot(index).getCount(), false);
		}
		return super.removeStackFromSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (handler != null) {
			return handler.extractItem(index, count, false);
		}
		return super.decrStackSize(index, count);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (handler != null) {
			handler.insertItem(index, stack, false);
		}
		super.setInventorySlotContents(index, stack);
	}
}