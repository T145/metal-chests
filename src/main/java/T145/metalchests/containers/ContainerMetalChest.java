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
import T145.tbone.api.IInventoryHandler;
import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.SlotItemHandler;

@ChestContainer(isLargeChest = true)
public class ContainerMetalChest extends Container {

	private final IInventoryHandler handler;
	private final ChestType type;
	private int rowSize;

	public ContainerMetalChest(IInventoryHandler handler, EntityPlayer player, ChestType type) {
		this.handler = handler;
		this.type = type;
		this.rowSize = MathHelper.clamp(type.getInventorySize(), 9, 14);

		handler.openInventory(player);

		int rows = MathHelper.clamp(type.getInventorySize(), 2, 9);
		int slots = rowSize * rows;
		int yOffset = 17;

		if (type.getInventorySize() == 1) {
			this.rowSize = 1;
			this.addSlotToContainer(new SlotItemHandler(handler.getInventory(), 0, 80, 26));
		} else {
			for (int i = 0; i < slots; ++i) {
				addSlotToContainer(new SlotItemHandler(handler.getInventory(), i, 8 + i % rowSize * 18, yOffset + i / rowSize * 18));
			}
		}

		layoutPlayerInventory(player.inventory);
	}

	public ContainerMetalChest(IMetalChest chest, EntityPlayer player) {
		this(chest, player, chest.getChestType());
	}

	protected void layoutPlayerInventory(InventoryPlayer inv) {
		int xOffset = 8 + 9 * (rowSize - 9);
		int yOffset = 30 + 18 * MathHelper.clamp(type.getInventorySize(), 2, 9);

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(inv, j + i * 9 + 9, xOffset + j * 18, yOffset + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(inv, i, xOffset + i * 18, yOffset + 58));
		}
	}

	public ChestType getType() {
		return type;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
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
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack slotStack = slot.getStack();
			stack = slotStack.copy();

			int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

			if (index < containerSlots) {
				if (!this.mergeItemStack(slotStack, containerSlots, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(slotStack, 0, containerSlots, false)) {
				return ItemStack.EMPTY;
			}

			if (slotStack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (slotStack.getCount() == stack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, slotStack);
		}

		return stack;
	}

	@ChestContainer.RowSizeCallback
	public int getNumColumns() {
		return rowSize;
	}
}
