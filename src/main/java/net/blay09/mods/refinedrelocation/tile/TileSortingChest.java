package net.blay09.mods.refinedrelocation.tile;

import com.google.common.base.Strings;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.capability.CapabilityRootFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilitySimpleFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilitySortingGridMember;
import net.blay09.mods.refinedrelocation.capability.CapabilitySortingInventory;
import net.blay09.mods.refinedrelocation.util.DoorAnimator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileSortingChest extends TileMod implements ITickable, INameable {

	private final ItemStackHandler itemHandler = new ItemStackHandler(27) {
		@Override
		protected void onContentsChanged(int slot) {
			markDirty();
			sortingInventory.onSlotChanged(slot);
		}
	};
	private final DoorAnimator doorAnimator = new DoorAnimator(this, 0, 1);

	private final ISortingInventory sortingInventory = Capabilities.getDefaultInstance(Capabilities.SORTING_INVENTORY);
	private final IRootFilter rootFilter = Capabilities.getDefaultInstance(Capabilities.ROOT_FILTER);

	private String customName = "";

	public TileSortingChest() {
		doorAnimator.setSoundEventOpen(SoundEvents.BLOCK_CHEST_OPEN);
		doorAnimator.setSoundEventClose(SoundEvents.BLOCK_CHEST_CLOSE);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		sortingInventory.onLoad(this);
	}

	@Override
	public void update() {
		sortingInventory.onUpdate(this);
		doorAnimator.update();
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

	public DoorAnimator getDoorAnimator() {
		return doorAnimator;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		itemHandler.deserializeNBT(compound.getCompoundTag("ItemHandler"));
		sortingInventory.deserializeNBT(compound.getCompoundTag("SortingInventory"));
		// vvv Backwards Compatibility
		if(compound.getTagId("RootFilter") == Constants.NBT.TAG_LIST) {
			NBTTagList tagList = compound.getTagList("RootFilter", Constants.NBT.TAG_COMPOUND);
			compound.removeTag("RootFilter");
			NBTTagCompound rootFilter = new NBTTagCompound();
			rootFilter.setTag("FilterList", tagList);
			compound.setTag("RootFilter", rootFilter);
		}
		// ^^^ Backwards Compatibility
		rootFilter.deserializeNBT(compound.getCompoundTag("RootFilter"));
	}

	@Override
	public void readFromNBTSynced(NBTTagCompound compound) {
		customName = compound.getString("CustomName");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("ItemHandler", itemHandler.serializeNBT());
		compound.setTag("SortingInventory", sortingInventory.serializeNBT());
		compound.setTag("RootFilter", rootFilter.serializeNBT());
		return compound;
	}

	@Override
	public NBTTagCompound writeToNBTSynced(NBTTagCompound compound) {
		compound.setString("CustomName", customName);
		return compound;
	}


	@Override
	public boolean receiveClientEvent(int id, int type) {
		return doorAnimator.receiveClientEvent(id, type) || super.receiveClientEvent(id, type);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				|| capability == CapabilitySortingInventory.CAPABILITY || capability == CapabilitySortingGridMember.CAPABILITY
				|| capability == CapabilityRootFilter.CAPABILITY || capability == CapabilitySimpleFilter.CAPABILITY
				|| super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) itemHandler;
		} else if(capability == CapabilitySortingInventory.CAPABILITY || capability == CapabilitySortingGridMember.CAPABILITY) {
			return (T) sortingInventory;
		} else if(capability == CapabilityRootFilter.CAPABILITY || capability == CapabilitySimpleFilter.CAPABILITY) {
			return (T) rootFilter;
		}
		return super.getCapability(capability, facing);
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
		return "container.refinedrelocation:sorting_chest";
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}

	public ItemStackHandler getItemHandler() {
		return itemHandler;
	}

}
