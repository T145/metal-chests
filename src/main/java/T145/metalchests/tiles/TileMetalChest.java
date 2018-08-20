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

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import T145.metalchests.api.ModSupport;
import T145.metalchests.api.chests.IFacing;
import T145.metalchests.api.chests.IInventoryHandler;
import T145.metalchests.api.chests.IUpgradeableChest;
import T145.metalchests.api.immutable.BlocksMC;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.ChestUpgrade;
import T145.metalchests.api.immutable.ItemsMC;
import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.lib.tiles.TileMod;
import net.dries007.holoInventory.api.INamedItemHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vazkii.quark.api.IDropoffManager;

@Optional.InterfaceList({
	@Optional.Interface(modid = ModSupport.HoloInventory.MOD_ID, iface = ModSupport.HoloInventory.NAMED_ITEM_HANDLER, striprefs = true),
	@Optional.Interface(modid = ModSupport.Quark.MOD_ID, iface = ModSupport.Quark.DROPOFF_MANAGER, striprefs = true)
})
public class TileMetalChest extends TileMod implements IUpgradeableChest, IFacing, IInventoryHandler, ITickable, INamedItemHandler, IDropoffManager {

	public float lidAngle;
	public float prevLidAngle;
	public int numPlayersUsing;

	protected ChestType chestType;
	protected EnumFacing front;
	protected ItemStackHandler inventory;

	public TileMetalChest(ChestType chestType) {
		this.setChestType(chestType);
		this.setFront(EnumFacing.EAST);
		this.inventory = this.initInventory();
	}

	public TileMetalChest() {
		this(ChestType.IRON);
	}

	public static void registerFixes(DataFixer fixer) {
		fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileMetalChest.class, new String[] { "Items" }));
	}

	protected ItemStackHandler initInventory() {
		return new ItemStackHandler(chestType.getInventorySize()) {

			@Override
			protected void onContentsChanged(int slot) {
				TileMetalChest.this.markDirty();
				world.updateComparatorOutputLevel(pos, getBlockType());
			}
		};
	}

	@Override
	public ChestType getChestType() {
		return chestType;
	}

	@Override
	public void setChestType(ChestType chestType) {
		this.chestType = chestType;
	}

	@Override
	public boolean canApplyUpgrade(ChestUpgrade upgrade, TileEntity chest, ItemStack upgradeStack) {
		return upgrade.getBase() == chestType && chest instanceof TileMetalChest && upgradeStack.getItem().getRegistryName().equals(ItemsMC.CHEST_UPGRADE.getRegistryName());
	}

	@Override
	public IBlockState createBlockState(ChestType chestType) {
		return BlocksMC.METAL_CHEST.getDefaultState().withProperty(BlockMetalChest.VARIANT, chestType);
	}

	@Override
	public TileEntity createTileEntity(ChestType chestType) {
		return new TileMetalChest(chestType);
	}

	@Override
	public EnumFacing getFront() {
		return front;
	}

	@Override
	public void setFront(EnumFacing front) {
		this.front = front;
	}

	@Override
	public IItemHandler getInventory() {
		return inventory;
	}

	@Override
	public void setInventory(IItemHandler inventory) {
		this.inventory = this.initInventory();

		assert(this.inventory.getSlots() >= inventory.getSlots());

		for (int slot = 0; slot < inventory.getSlots(); ++slot) {
			this.inventory.setStackInSlot(slot, inventory.getStackInSlot(slot));
		}
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void openInventory(EntityPlayer player) {
		if (!player.isSpectator()) {
			if (numPlayersUsing < 0) {
				numPlayersUsing = 0;
			}

			world.addBlockEvent(pos, getBlockType(), 1, ++numPlayersUsing);
			world.notifyNeighborsOfStateChange(pos, blockType, false);
			world.playSound(player, pos, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
		}
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		if (!player.isSpectator()) {
			world.addBlockEvent(pos, getBlockType(), 1, --numPlayersUsing);
			world.notifyNeighborsOfStateChange(pos, blockType, false);
			world.playSound(player, pos, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
		}
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return world.getTileEntity(pos) == this && player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
	}

	/**
	 * @param a   The value
	 * @param b   The value to approach
	 * @param max The maximum step
	 * @return the closed value to b no less than max from a
	 */
	private static float approachLinear(float a, float b, float max) {
		return (a > b) ? (a - b < max ? b : a - max) : (b - a < max ? b : a + max);
	}

	@Override
	public void update() {
		if (!world.isRemote && world.getTotalWorldTime() % 20 == 0) {
			world.addBlockEvent(pos, getBlockType(), 1, numPlayersUsing);
			world.notifyNeighborsOfStateChange(pos, blockType, true);
		}

		prevLidAngle = lidAngle;
		lidAngle = approachLinear(lidAngle, numPlayersUsing > 0 ? 1.0F : 0.0F, 0.1F);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.setChestType(ChestType.valueOf(tag.getString("ChestType")));
		this.setFront(EnumFacing.byName(tag.getString("Front")));
		inventory.deserializeNBT(tag.getCompoundTag("Inventory"));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag = super.writeToNBT(tag);
		tag.setString("ChestType", chestType.toString());
		tag.setString("Front", front.toString());
		tag.setTag("Inventory", inventory.serializeNBT());
		return tag;
	}

	@Override
	public boolean receiveClientEvent(int id, int data) {
		switch (id) {
		case 1:
			numPlayersUsing = data;
			return true;
		default:
			return false;
		}
	}

	@Override
	public void invalidate() {
		updateContainingBlockInfo();
		super.invalidate();
	}

	public String getTranslationKey() {
		return "tile.metalchests:metal_chest." + chestType.getName() + ".name";
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentTranslation(getTranslationKey());
	}

	@Optional.Method(modid = ModSupport.HoloInventory.MOD_ID)
	@Override
	public String getItemHandlerName() {
		return getTranslationKey();
	}

	@Optional.Method(modid = ModSupport.Quark.MOD_ID)
	@Override
	public boolean acceptsDropoff(EntityPlayer player) {
		return true;
	}

	@Optional.Method(modid = ModSupport.Quark.MOD_ID)
	@Override
	public IItemHandler getDropoffItemHandler(Supplier<IItemHandler> defaultSupplier) {
		return inventory;
	}
}
