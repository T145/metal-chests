package T145.metalchests.containers.base;

import T145.metalchests.lib.MetalChestType;
import T145.metalchests.lib.SimpleItemStackHandler;
import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

@ChestContainer(isLargeChest = true)
public class ContainerMetalChestBase extends Container {

	protected final MetalChestType type;
	protected final SimpleItemStackHandler upperInventory;

	public ContainerMetalChestBase(MetalChestType type, SimpleItemStackHandler upperInventory, EntityPlayer player, int xSize, int ySize) {
		this.type = type;
		this.upperInventory = upperInventory;

		for (int chestRow = 0; chestRow < type.getRowCount(); chestRow++) {
			for (int chestCol = 0; chestCol < type.getRowLength(); chestCol++) {
				int slotIndex = chestCol + chestRow * type.getRowLength();

				addSlotToContainer(new SlotItemHandler(upperInventory, slotIndex, 12 + chestCol * 18, 8 + chestRow * 18) {

					@Override
					public void onSlotChanged() {
						upperInventory.onContentsChanged(slotIndex);
					}
				});
			}
		}

		int leftCol = (xSize - 162) / 2 + 1;

		for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++) {
			for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++) {
				addSlotToContainer(new Slot(player.inventory, playerInvCol + playerInvRow * 9 + 9, leftCol + playerInvCol * 18, ySize - (4 - playerInvRow) * 18 - 10));
			}
		}

		for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
			addSlotToContainer(new Slot(player.inventory, hotbarSlot, leftCol + hotbarSlot * 18, ySize - 24));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack slotStack = slot.getStack();
			stack = slotStack.copy();
			int inventorySize = type.getInventorySize();

			if (index < inventorySize) {
				if (!mergeItemStack(slotStack, inventorySize, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(slotStack, 0, inventorySize, false)) {
				return ItemStack.EMPTY;
			}

			if (slotStack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}

		return stack;
	}

	@ChestContainer.RowSizeCallback
	public int getNumColumns() {
		return type.getRowLength();
	}
}