/*******************************************************************************
 * Copyright 2018-2019 T145
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package T145.metalchests.containers;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import T145.metalchests.api.chests.IMetalChest;
import T145.metalchests.api.consts.ChestType;
import T145.metalchests.api.consts.RegistryMC;
import T145.tbone.api.IInventoryHandler;
import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.SlotItemHandler;

@ChestContainer
public class ContainerMetalChest extends Container {

	public static final int MAX_COLUMNS = 17;
	public static final int MAX_ROWS = 6;

	public final IInventoryHandler mainInv;
	public final ChestType type;

	public ContainerMetalChest(IInventoryHandler mainInv, InventoryPlayer playerInv, ChestType type) {
		this.mainInv = mainInv;
		this.type = type;
		mainInv.openInventory(playerInv.player);
		this.setupChestSlots();
		this.setupPlayerSlots(playerInv);
	}

	public ContainerMetalChest(IMetalChest chest, InventoryPlayer playerInv) {
		this(chest, playerInv, chest.getChestType());
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
				addSlotToContainer(new SlotItemHandler(mainInv.getInventory(), x + y * type.getColumns(), xOffset + x * 18, yOffset));
			}
		}
	}

	protected void setupPlayerSlots(InventoryPlayer playerInv) {
		int xOffset = 1 + getPlayerInvXOffset();
		int yOffset = 1 + getBorderTop() + getContainerInvHeight() + getBufferInventory();

		// Inventory
		for (int y = 0; y < 3; ++y, yOffset += 18) {
			for (int x = 0; x < 9; ++x) {
				addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, xOffset + x * 18, yOffset));
			}
		}

		// HotBar
		yOffset += getBufferHotbar();

		for (int x = 0; x < 9; ++x) {
			addSlotToContainer(new Slot(playerInv, x, xOffset + x * 18, yOffset));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return mainInv.isUsableByPlayer(player);
	}

	@OverridingMethodsMustInvokeSuper
	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		mainInv.closeInventory(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
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

	//	@Optional.Method(modid = RegistryMC.ID_INVTWEAKS)
	//	@ContainerSectionCallback
	//	public Map<ContainerSection, List<Slot>> getContainerSections() {
	//		return new Object2ObjectOpenHashMap() {
	//			{
	//				put(ContainerSection.INVENTORY, inventorySlots.subList(0, 36));
	//				put(ContainerSection.INVENTORY_NOT_HOTBAR, inventorySlots.subList(0, 27));
	//				put(ContainerSection.INVENTORY_HOTBAR, inventorySlots.subList(27, 36));
	//				put(ContainerSection.CHEST, inventorySlots.subList(36, inventorySlots.size()));
	//			}
	//		};
	//	}

	@Optional.Method(modid = RegistryMC.ID_INVTWEAKS)
	@ChestContainer.RowSizeCallback
	public int getRowSize() {
		return type.getColumns();
	}
}
