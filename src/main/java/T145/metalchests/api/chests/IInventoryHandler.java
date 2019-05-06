/*******************************************************************************
 * Copyright 2019 T145
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
package T145.metalchests.api.chests;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public interface IInventoryHandler extends IInventory {

	ItemStackHandler getInventory();

	void setInventory(IItemHandler inv);

	default void writeInventoryTag(NBTTagCompound tag) {
		tag.setTag("Inventory", getInventory().serializeNBT());
	}

	default void readInventoryTag(NBTTagCompound tag) {
		getInventory().deserializeNBT(tag.getCompoundTag("Inventory"));
	}

	@Override
	default int getSizeInventory() {
		return getInventory().getSlots();
	}

	/**
	 * Returns the stack in the given slot.
	 */
	@Override
	default ItemStack getStackInSlot(int index) {
		return getInventory().getStackInSlot(index);
	}

	@Override
	default boolean isEmpty() {
		for (int slot = 0; slot < getSizeInventory(); ++slot) {
			if (!getStackInSlot(slot).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	 */
	@Override
	default ItemStack decrStackSize(int index, int count) {
		return getInventory().extractItem(index, count, false);
	}

	/**
	 * Removes a stack from the given slot and returns it.
	 */
	@Override
	default ItemStack removeStackFromSlot(int index) {
		return getInventory().extractItem(index, getStackInSlot(index).getCount(), false);
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	@Override
	default void setInventorySlotContents(int index, ItemStack stack) {
		getInventory().insertItem(index, stack, false);
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
	 */
	@Override
	default int getInventoryStackLimit() {
		return 64;
	}

	/**
	 * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
	 * hasn't changed and skip it.
	 */
	@Override
	default void markDirty() {}

	/**
	 * Don't rename this method to canInteractWith due to conflicts with Container
	 */
	@Override
	boolean isUsableByPlayer(EntityPlayer player);

	@Override
	void openInventory(EntityPlayer player);

	@Override
	void closeInventory(EntityPlayer player);

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
	 * guis use Slot.isItemValid
	 */
	@Override
	default boolean isItemValidForSlot(int index, ItemStack stack) {
		return getInventory().isItemValid(index, stack);
	}

	@Override
	default int getField(int id) {
		return 0;
	}

	@Override
	default void setField(int id, int value) {}

	@Override
	default int getFieldCount() {
		return 0;
	}

	@Override
	default void clear() {
		for (int slot = 0; slot < getSizeInventory(); ++slot) {
			setInventorySlotContents(slot, ItemStack.EMPTY);
		}
	}
}
