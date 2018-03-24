package T145.metalchests.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;

public class InventoryProjectTableCrafting extends InventoryCrafting {

	private IItemHandler handler;

	public InventoryProjectTableCrafting() {
		super(new Container() {

			@Override
			public boolean canInteractWith(EntityPlayer player) {
				return false;
			}
		}, 3, 3);
	}

	public InventoryProjectTableCrafting(IItemHandler handler) {
		this();
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
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (handler != null) {
			handler.insertItem(index, stack, false);
		} else {
			super.setInventorySlotContents(index, stack);
		}
	}

	public void setInventoryContents(NonNullList<ItemStack> stacks) {
		if (stacks == null || stacks.size() != getSizeInventory()) {
			return;
		}

		for (int i = 0; i < stacks.size(); ++i) {
			setInventorySlotContents(i, stacks.get(i));
		}
	}
}