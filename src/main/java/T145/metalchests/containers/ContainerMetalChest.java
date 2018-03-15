package T145.metalchests.containers;

import T145.metalchests.lib.MetalChestType;
import T145.metalchests.tiles.TileMetalChest;
import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

@ChestContainer(isLargeChest = true)
public class ContainerMetalChest extends Container {

	private final TileMetalChest chest;

	public ContainerMetalChest(TileMetalChest chest, EntityPlayer player, int xSize, int ySize) {
		this.chest = chest;
		chest.openInventory(player);

		MetalChestType type = chest.getType();

		for (int chestRow = 0; chestRow < type.getRowCount(); chestRow++) {
			for (int chestCol = 0; chestCol < type.getRowLength(); chestCol++) {
				this.addSlotToContainer(new SlotItemHandler(chest.getInventory(), chestCol + chestRow * type.getRowLength(), 12 + chestCol * 18, 8 + chestRow * 18));
			}
		}

		int leftCol = (xSize - 162) / 2 + 1;

		for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++) {
			for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++) {
				this.addSlotToContainer(new Slot(player.inventory, playerInvCol + playerInvRow * 9 + 9, leftCol + playerInvCol * 18, ySize - (4 - playerInvRow) * 18 - 10));
			}

		}

		for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
			this.addSlotToContainer(new Slot(player.inventory, hotbarSlot, leftCol + hotbarSlot * 18, ySize - 24));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return chest.isUsableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			int inventorySize = chest.getType().getInventorySize();

			if (index < inventorySize) {
				if (!mergeItemStack(itemstack1, inventorySize, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(itemstack1, 0, inventorySize, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		chest.closeInventory(player);
	}

	@ChestContainer.RowSizeCallback
	public int getNumColumns() {
		return chest.getType().getRowLength();
	}
}