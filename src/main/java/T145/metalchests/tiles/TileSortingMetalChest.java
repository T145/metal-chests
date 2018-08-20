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
package T145.metalchests.tiles;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Strings;

import T145.metalchests.api.BlocksMetalChests;
import T145.metalchests.api.ItemsMetalChests;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.items.ItemChestUpgrade.ChestUpgrade;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.capability.CapabilityRootFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilitySimpleFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilitySortingGridMember;
import net.blay09.mods.refinedrelocation.capability.CapabilitySortingInventory;
import net.blay09.mods.refinedrelocation.tile.INameable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

public class TileSortingMetalChest extends TileMetalChest implements INameable {

	protected ISortingInventory sortingInventory = Capabilities.getDefaultInstance(Capabilities.SORTING_INVENTORY);
	protected IRootFilter rootFilter = Capabilities.getDefaultInstance(Capabilities.ROOT_FILTER);
	protected String customName = StringUtils.EMPTY;

	public TileSortingMetalChest(ChestType chestType) {
		super(chestType);
	}

	public TileSortingMetalChest() {
		super();
	}

	public static void registerFixes(DataFixer fixer) {
		fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileSortingMetalChest.class, new String[] { "Items" }));
	}

	@Override
	protected ItemStackHandler initInventory() {
		return new ItemStackHandler(chestType.getInventorySize()) {

			@Override
			protected void onContentsChanged(int slot) {
				TileSortingMetalChest.this.markDirty();
				sortingInventory.onSlotChanged(slot);
				world.updateComparatorOutputLevel(pos, getBlockType());
			}
		};
	}

	@Override
	public boolean canApplyUpgrade(ChestUpgrade upgrade, TileEntity chest, ItemStack upgradeStack) {
		return upgrade.getBase() == chestType && chest instanceof TileSortingMetalChest && upgradeStack.getItem().getRegistryName().equals(ItemsMetalChests.CHEST_UPGRADE.getRegistryName());
	}

	@Override
	public IBlockState createBlockState() {
		return BlocksMetalChests.SORTING_METAL_CHEST.getDefaultState().withProperty(BlockMetalChest.VARIANT, getChestType());
	}

	@Override
	public TileEntity createTileEntity(ChestType chestType) {
		return new TileSortingMetalChest(chestType);
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
		return capability == CapabilitySortingInventory.CAPABILITY || capability == CapabilitySortingGridMember.CAPABILITY || capability == CapabilityRootFilter.CAPABILITY || capability == CapabilitySimpleFilter.CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilitySortingInventory.CAPABILITY || capability == CapabilitySortingGridMember.CAPABILITY) {
			return (T) sortingInventory;
		} else if (capability == CapabilityRootFilter.CAPABILITY || capability == CapabilitySimpleFilter.CAPABILITY) {
			return (T) rootFilter;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("SortingInventory", sortingInventory.serializeNBT());
		compound.setTag("RootFilter", rootFilter.serializeNBT());
		setCustomName(compound.getString("CustomName"));
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		sortingInventory.deserializeNBT(compound.getCompoundTag("SortingInventory"));

		if (compound.getTagId("RootFilter") == Constants.NBT.TAG_LIST) {
			NBTTagList tagList = compound.getTagList("RootFilter", Constants.NBT.TAG_COMPOUND);
			compound.removeTag("RootFilter");
			NBTTagCompound rootFilter = new NBTTagCompound();
			rootFilter.setTag("FilterList", tagList);
			compound.setTag("RootFilter", rootFilter);
		}

		rootFilter.deserializeNBT(compound.getCompoundTag("RootFilter"));
		customName = compound.getString("CustomName");
	}

	@Override
	public String getTranslationKey() {
		return "tile.metalchests:sorting_metal_chest." + chestType.getName() + ".name";
	}

	@Override
	public void setCustomName(String customName) {
		this.customName = customName;
	}

	@Override
	public String getCustomName() {
		return customName;
	}

	@Override
	public boolean hasCustomName() {
		return !Strings.isNullOrEmpty(customName);
	}

	@Override
	public String getUnlocalizedName() {
		return getTranslationKey();
	}
}
