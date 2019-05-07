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

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import T145.metalchests.api.chests.IMetalChest;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.RegistryMC;
import T145.metalchests.api.immutable.SupportedMods;
import net.dries007.holoInventory.api.INamedItemHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vazkii.quark.api.IDropoffManager;

@Optional.InterfaceList({
	@Optional.Interface(modid = SupportedMods.HOLOINVENTORY_MOD_ID, iface = SupportedMods.IFACE_NAMED_ITEM_HANDLER, striprefs = true),
	@Optional.Interface(modid = SupportedMods.QUARK_MOD_ID, iface = SupportedMods.IFACE_DROPOFF_MANAGER, striprefs = true)
})
public class TileMetalChest extends TileEntity implements IMetalChest, ITickable, INamedItemHandler, IDropoffManager {

	public float lidAngle;
	public float prevLidAngle;
	public int numPlayersUsing;

	protected ChestType chestType;
	protected EnumFacing front;
	protected ItemStackHandler inventory;
	protected byte enchantLevel;
	protected boolean trapped;
	protected boolean luminous;

	public TileMetalChest(ChestType chestType) {
		this.setChestType(chestType);
		this.setFront(EnumFacing.EAST);
		this.inventory = this.initInventory();
	}

	public TileMetalChest() {
		this(ChestType.IRON);
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
	public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(super.getUpdateTag());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
		handleUpdateTag(packet.getNbtCompound());
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
	public EnumFacing getFront() {
		return front;
	}

	@Override
	public void setFront(EnumFacing front) {
		this.front = front;
	}

	@Override
	public byte getEnchantLevel() {
		return enchantLevel;
	}

	@Override
	public void setEnchantLevel(byte enchantLevel) {
		this.enchantLevel = enchantLevel;
	}

	public boolean isTrapped() {
		return trapped;
	}

	public void setTrapped(boolean trapped) {
		this.trapped = trapped;
	}

	public boolean isLuminous() {
		return luminous;
	}

	public void setLuminous(boolean luminous) {
		this.luminous = luminous;

		if (this.getBlockType() != null) {
			this.getBlockType().setLightLevel(luminous ? 1F : 0F);
		}
	}

	@Override
	public ItemStackHandler getInventory() {
		return inventory;
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
		this.setChestType(ChestType.valueOf(tag.getString(TAG_CHEST_TYPE)));
		inventory.deserializeNBT(tag.getCompoundTag(TAG_INVENTORY));
		this.setFront(EnumFacing.byName(tag.getString(TAG_FRONT)));
		this.setEnchantLevel(tag.getByte(TAG_ENCHANT_LEVEL));
		this.setTrapped(tag.getBoolean(TAG_TRAPPED));
		this.setLuminous(tag.getBoolean(TAG_LUMINOUS));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag = super.writeToNBT(tag);
		tag.setString(TAG_CHEST_TYPE, chestType.toString());
		tag.setTag(TAG_INVENTORY, inventory.serializeNBT());
		tag.setString(TAG_FRONT, front.toString());
		tag.setByte(TAG_ENCHANT_LEVEL, enchantLevel);
		tag.setBoolean(TAG_TRAPPED, trapped);
		tag.setBoolean(TAG_LUMINOUS, luminous);
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
		return String.format("tile.%s:%s.%s.name", RegistryMC.MOD_ID, RegistryMC.KEY_METAL_CHEST, chestType.getName());
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentTranslation(getTranslationKey());
	}

	@Optional.Method(modid = SupportedMods.HOLOINVENTORY_MOD_ID)
	@Override
	public String getItemHandlerName() {
		return getTranslationKey();
	}

	@Optional.Method(modid = SupportedMods.QUARK_MOD_ID)
	@Override
	public boolean acceptsDropoff(EntityPlayer player) {
		return true;
	}

	@Optional.Method(modid = SupportedMods.QUARK_MOD_ID)
	@Override
	public IItemHandler getDropoffItemHandler(Supplier<IItemHandler> defaultSupplier) {
		return inventory;
	}

	@Override
	public String getName() {
		return getTranslationKey();
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}
}
