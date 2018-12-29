/*******************************************************************************
 * Copyright 2018 T145
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

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class InventoryManager {

	private InventoryManager() {}

	public static ItemStack tryInsertItemStackToInventory(IItemHandler inv, @Nonnull ItemStack stack) {
		return tryInsertItemStackToInventoryWithinSlotRange(inv, stack, new SlotRange(inv));
	}

	public static ItemStack tryInsertItemStackToInventoryWithinSlotRange(IItemHandler inv, @Nonnull ItemStack stack, SlotRange slotRange) {
		final int lastSlot = Math.min(slotRange.lastInc, inv.getSlots() - 1);

		for (int slot = slotRange.first; slot <= lastSlot; slot++) {
			if (inv.getStackInSlot(slot).isEmpty() == false) {
				stack = inv.insertItem(slot, stack, false);

				if (stack.isEmpty()) {
					return ItemStack.EMPTY;
				}
			}
		}

		for (int slot = slotRange.first; slot <= lastSlot; slot++) {
			stack = inv.insertItem(slot, stack, false);

			if (stack.isEmpty()) {
				return ItemStack.EMPTY;
			}
		}

		return stack;
	}
}
