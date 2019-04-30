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
package T145.metalchests.tiles;

import javax.annotation.Nullable;

import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.RegistryMC;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.INameTaggable;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileMetalSortingChest extends TileMetalChest {

	protected final ISortingInventory sortingInventory = Capabilities.getDefaultInstance(Capabilities.SORTING_INVENTORY);
	protected final IRootFilter rootFilter = Capabilities.getDefaultInstance(Capabilities.ROOT_FILTER);
	protected final INameTaggable nameTaggable = Capabilities.getDefaultInstance(Capabilities.NAME_TAGGABLE);

	public TileMetalSortingChest(ChestType chestType) {
		super(chestType);
	}

	public TileMetalSortingChest() {
		super();
	}

	@Override
	protected ItemStackHandler initInventory() {
		return new ItemStackHandler(chestType.getInventorySize()) {

			@Override
			protected void onContentsChanged(int slot) {
				TileMetalSortingChest.this.markDirty();
				sortingInventory.onSlotChanged(slot);
				world.updateComparatorOutputLevel(pos, getBlockType());
			}
		};
	}

	@Override
	public void onLoad() {
		super.onLoad();
		sortingInventory.onLoad(this);
	}

	@Override
	public void update() {
		sortingInventory.onUpdate(this);
		super.update();
	}

	@Override
	public void invalidate() {
		super.invalidate();
		sortingInventory.onInvalidate(this);
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		sortingInventory.onInvalidate(this);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == Capabilities.NAME_TAGGABLE || Capabilities.isSortingGridCapability(capability) || Capabilities.isFilterCapability(capability) || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		} else if (Capabilities.isSortingGridCapability(capability)) {
			return Capabilities.SORTING_INVENTORY.cast(sortingInventory);
		} else if (Capabilities.isFilterCapability(capability)) {
			return Capabilities.ROOT_FILTER.cast(rootFilter);
		} else if (capability == Capabilities.NAME_TAGGABLE) {
			return Capabilities.NAME_TAGGABLE.cast(nameTaggable);
		}

		return super.getCapability(capability, facing);
	}

	/**
	 * TODO remove in 1.13
	 */
	public static void fixRootFilterTag(NBTTagCompound compound) {
		if (compound.getTagId("RootFilter") == Constants.NBT.TAG_LIST) {
			NBTTagList tagList = compound.getTagList("RootFilter", Constants.NBT.TAG_COMPOUND);
			compound.removeTag("RootFilter");
			NBTTagCompound rootFilter = new NBTTagCompound();
			rootFilter.setTag("FilterList", tagList);
			compound.setTag("RootFilter", rootFilter);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		inventory.deserializeNBT(compound.getCompoundTag("ItemHandler"));
		sortingInventory.deserializeNBT(compound.getCompoundTag("SortingInventory"));

		fixRootFilterTag(compound);

		rootFilter.deserializeNBT(compound.getCompoundTag("RootFilter"));
		nameTaggable.deserializeNBT(compound.getCompoundTag("NameTaggable"));
		nameTaggable.setCustomName(compound.getString("CustomName"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("ItemHandler", inventory.serializeNBT());
		compound.setTag("SortingInventory", sortingInventory.serializeNBT());
		compound.setTag("RootFilter", rootFilter.serializeNBT());
		compound.setTag("NameTaggable", nameTaggable.serializeNBT());
		compound.setString("CustomName", nameTaggable.getCustomName());
		return compound;
	}

	@Nullable
	@Override
	public ITextComponent getDisplayName() {
		return nameTaggable.getDisplayName();
	}

	@Override
	public String getTranslationKey() {
		return String.format("tile.%s:%s.%s.name", RegistryMC.MOD_ID, RegistryMC.KEY_METAL_SORTING_CHEST, chestType.getName());
	}
}
