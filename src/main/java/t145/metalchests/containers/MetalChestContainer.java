package t145.metalchests.containers;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import t145.metalchests.api.ContainerTypeRegistry;
import t145.metalchests.lib.MetalChestType;
import t145.metalchests.tiles.MetalChestTile;

public class MetalChestContainer extends Container {

	public static final int MAX_COLUMNS = 23;
	public static final int MAX_ROWS = 8;

	protected final MetalChestTile chest;
	protected final MetalChestType type;

	public MetalChestContainer(int id, PlayerInventory inv, MetalChestTile chest) {
		super(ContainerTypeRegistry.METAL_CHEST_CONTAINER_TYPE, id);
		this.chest = chest;
		this.type = chest.hasWorld() ? chest.getMetalType() : MetalChestType.IRON;
		chest.openOrCloseChest(inv.player, true);
		this.setupChestSlots();
		this.setupPlayerSlots(inv);
	}

	public MetalChestType getMetalType() {
		return type;
	}

	public int getBorderTop() {
		return 17;
	}

	public int getBorderSide() {
		return 7;
	}

	public int getBorderBottom() {
		return 7;
	}

	/** Returns the space between container and player inventory in pixels. */
	public int getBufferInventory() {
		return 13;
	}

	/** Returns the space between player inventory and hotbar in pixels. */
	public int getBufferHotbar() {
		return 4;
	}

	public int getWidth() {
		return Math.max(type.getColumns(), 9) * 18 + getBorderSide() * 2;
	}

	public int getHeight() {
		return getBorderTop() + (type.getRows() * 18) + getBufferInventory() + (4 * 18) + getBufferHotbar() + getBorderBottom();
	}

	public int getContainerInvWidth() {
		return type.getColumns() * 18;
	}

	public int getContainerInvHeight() {
		return type.getRows() * 18;
	}

	public int getContainerInvXOffset() {
		return getBorderSide() + Math.max(0, (getPlayerInvWidth() - getContainerInvWidth()) / 2);
	}

	public int getPlayerInvWidth() {
		return 9 * 18;
	}

	public int getPlayerInvHeight() {
		return 4 * 18 + getBufferHotbar();
	}

	public int getPlayerInvXOffset() {
		return getBorderSide() + Math.max(0, (getContainerInvWidth() - getPlayerInvWidth()) / 2);
	}

	protected void setupChestSlots() {
		int xOffset = 1 + getContainerInvXOffset();
		int yOffset = 1 + getBorderTop();

		for (int y = 0; y < type.getRows(); ++y, yOffset += 18) {
			for (int x = 0; x < type.getColumns(); ++x) {
				addSlot(new SlotItemHandler(chest.handler, x + y * type.getColumns(), xOffset + x * 18, yOffset));
			}
		}
	}

	protected void setupPlayerSlots(PlayerInventory inv) {
		int xOffset = 1 + getPlayerInvXOffset();
		int yOffset = 1 + getBorderTop() + getContainerInvHeight() + getBufferInventory();

		// Inventory
		for (int y = 0; y < 3; ++y, yOffset += 18) {
			for (int x = 0; x < 9; ++x) {
				addSlot(new Slot(inv, x + y * 9 + 9, xOffset + x * 18, yOffset));
			}
		}

		// HotBar
		yOffset += getBufferHotbar();

		for (int x = 0; x < 9; ++x) {
			addSlot(new Slot(inv, x, xOffset + x * 18, yOffset));
		}
	}

	@Override
	public boolean canInteractWith(PlayerEntity player) {
		return chest.isUsableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int index) {
		Slot slot = inventorySlots.get(index);
		ItemStack result = ItemStack.EMPTY;

		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			result = stack.copy();
			int slotCount = inventorySlots.size() - player.inventory.mainInventory.size();

			if (index < slotCount) {
				if (!mergeItemStack(stack, slotCount, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(stack, 0, slotCount, false)) {
				return ItemStack.EMPTY;
			}

			slot.onSlotChanged();
		}

		return result;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onContainerClosed(PlayerEntity player) {
		super.onContainerClosed(player);
		chest.openOrCloseChest(player, false);
	}
}
