package T145.metalchests.containers;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class InventoryProjectTableCrafting extends InventoryCrafting {

	private final ContainerProjectTable eventHandler;
	private final IItemHandler handler;

	public InventoryProjectTableCrafting(ContainerProjectTable eventHandler, int width, int height) {
		super(eventHandler, width, height);
		this.eventHandler = eventHandler;
		this.handler = eventHandler.getHandler().getInventory();
	}

	@Override
	public int getSizeInventory() {
		return this.handler.getSlots();
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < this.getSizeInventory(); ++i) {
			ItemStack itemstack = this.handler.getStackInSlot(i);

			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return index >= this.getSizeInventory() ? ItemStack.EMPTY : (ItemStack) this.handler.getStackInSlot(index);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return handler.extractItem(index, handler.getStackInSlot(index).getCount(), false);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack itemstack = handler.extractItem(index, count, false);

		if (!itemstack.isEmpty()) {
			this.eventHandler.onCraftMatrixChanged(this);
		}

		return itemstack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.handler.insertItem(index, stack, false);
		this.eventHandler.onCraftMatrixChanged(this);
	}
}