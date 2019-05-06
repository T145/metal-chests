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

import T145.metalchests.api.immutable.ChestType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;

public interface IMetalChest extends IInventoryHandler {

	public static final PropertyEnum<ChestType> VARIANT = PropertyEnum.<ChestType>create("variant", ChestType.class);
	public static final String TAG_CHEST_TYPE = "ChestType";
	public static final String TAG_FRONT = "Front";
	public static final String TAG_ENCHANT_LEVEL = "EnchantLevel";

	ChestType getChestType();

	void setChestType(ChestType chestType);

	EnumFacing getFront();

	void setFront(EnumFacing front);

	byte getEnchantLevel();

	void setEnchantLevel(byte enchantLevel);

	@Override
	default void setInventory(IItemHandler inv) {
		for (int slot = 0; slot < inv.getSlots(); ++slot) {
			if (slot < getChestType().getInventorySize()) {
				setInventorySlotContents(slot, inv.getStackInSlot(slot));
			}
		}
	}
}
