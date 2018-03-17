package T145.metalchests.containers.base;

import T145.metalchests.lib.MetalChestType;
import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

@ChestContainer(isLargeChest = true)
public abstract class ContainerMetalChestBase extends Container {

	protected final MetalChestType type;
	protected final MetalChestType.GUI gui;

	public ContainerMetalChestBase(MetalChestType type) {
		this.type = type;
		this.gui = MetalChestType.GUI.byType(type);
	}

	public MetalChestType.GUI getGuiType() {
		return gui;
	}

	protected void layoutInventory(ContainerMetalChestBase container, IItemHandler handler, IInventory mcInventory) {
		for (int chestRow = 0; chestRow < type.getRowCount(); chestRow++) {
			for (int chestCol = 0; chestCol < type.getRowLength(); chestCol++) {
				if (handler == null) {
					if (mcInventory != null) {
						addSlotToContainer(new Slot(mcInventory, chestCol + chestRow * type.getRowLength(), 12 + chestCol * 18, 8 + chestRow * 18) {

							@Override
							public void onSlotChanged() {
								super.onSlotChanged();
								container.onSlotChanged();
							}
						});
					}
				} else {
					addSlotToContainer(new SlotItemHandler(handler, chestCol + chestRow * type.getRowLength(), 12 + chestCol * 18, 8 + chestRow * 18) {

						@Override
						public void onSlotChanged() {
							container.onSlotChanged();
						}
					});
				}
			}
		}
	}

	protected abstract void onSlotChanged();

	protected void layoutPlayerInventory(EntityPlayer player) {
		int leftCol = (gui.getSizeX() - 162) / 2 + 1;
		int ySize = gui.getSizeY();

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
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack copyStack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack slotStack = slot.getStack();
			copyStack = slotStack.copy();
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

		return copyStack;
	}

	@ChestContainer.RowSizeCallback
	public int getNumColumns() {
		return type.getRowLength();
	}
}