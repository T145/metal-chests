package T145.metalchests.containers;

import T145.metalchests.api.IInventoryHandler;
import T145.metalchests.lib.MetalChestType;
import invtweaks.api.container.ChestContainer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.SlotItemHandler;
import vazkii.quark.api.IChestButtonCallback;

@ChestContainer(isLargeChest = true)
@Optional.Interface(modid = "quark", iface = "vazkii.quark.api.IChestButtonCallback", striprefs = true)
public class ContainerMetalChest extends Container implements IChestButtonCallback {

	private final IInventoryHandler handler;
	private final MetalChestType type;
	private final MetalChestType.GUI gui;

	public ContainerMetalChest(IInventoryHandler handler, EntityPlayer player, MetalChestType type) {
		this.handler = handler;
		this.type = type;
		this.gui = MetalChestType.GUI.byType(type);
		handler.openInventory(player);
		layoutInventory();
		layoutPlayerInventory(player);
	}

	public MetalChestType.GUI getGuiType() {
		return gui;
	}

	protected void layoutInventory() {
		for (int chestRow = 0; chestRow < type.getRowCount(); chestRow++) {
			for (int chestCol = 0; chestCol < type.getRowLength(); chestCol++) {
				addSlotToContainer(new SlotItemHandler(handler.getInventory(), chestCol + chestRow * type.getRowLength(), 12 + chestCol * 18, 8 + chestRow * 18));
			}
		}
	}

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
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		handler.closeInventory(player);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return handler.isUsableByPlayer(player);
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

	@Optional.Method(modid = "quark")
	@Override
	public boolean onAddChestButton(GuiButton button, int buttonType) {
		return true;
	}
}