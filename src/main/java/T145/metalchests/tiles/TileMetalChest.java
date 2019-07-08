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
package t145.metalchests.tiles;

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import org.apache.commons.lang3.StringUtils;

import T145.tbone.lib.ChestAnimator;
import T145.tbone.lib.ChestHandler;
import net.dries007.holoInventory.api.INamedItemHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import t145.metalchests.api.chests.IMetalChest;
import t145.metalchests.api.consts.ChestType;
import t145.metalchests.api.consts.RegistryMC;
import t145.metalchests.api.objs.ItemsMC;
import vazkii.quark.api.IDropoffManager;

@Optional.InterfaceList({
	@Optional.Interface(modid = RegistryMC.ID_HOLOINVENTORY, iface = RegistryMC.IFACE_NAMED_ITEM_HANDLER, striprefs = true),
	@Optional.Interface(modid = RegistryMC.ID_QUARK, iface = RegistryMC.IFACE_DROPOFF_MANAGER, striprefs = true)
})
public class TileMetalChest extends TileEntity implements IMetalChest, ITickable, INamedItemHandler, IDropoffManager {

	protected final ChestAnimator animator = new ChestAnimator(this);
	protected ChestType chestType;
	protected EnumFacing front;
	protected ChestHandler inventory;
	protected String customName = StringUtils.EMPTY;
	protected byte enchantLevel;
	protected boolean trapped;
	protected boolean luminous;

	public TileMetalChest(ChestType chestType) {
		super();
		this.setChestType(chestType);
		this.setFront(EnumFacing.EAST);
		this.inventory = this.initInventory();
	}

	public TileMetalChest() {
		this(ChestType.IRON);
	}

	protected ChestHandler initInventory() {
		return new ChestHandler(chestType.getInventorySize());
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
	public ChestAnimator getChestAnimator() {
		return animator;
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

	public String getCustomName() {
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	@Override
	public byte getEnchantLevel() {
		return enchantLevel;
	}

	@Override
	public void setEnchantLevel(byte enchantLevel) {
		this.enchantLevel = enchantLevel;
	}

	@Override
	public boolean isTrapped() {
		return trapped;
	}

	@Override
	public void setTrapped(boolean trapped) {
		this.trapped = trapped;
	}

	@Override
	public boolean isLuminous() {
		return luminous;
	}

	@Override
	public void setLuminous(boolean luminous) {
		this.luminous = luminous;
	}

	@Override
	public boolean canUpgradeUsing(Item upgrade) {
		return upgrade == ItemsMC.CHEST_UPGRADE;
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
		animator.openInventory(player);
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		animator.closeInventory(player);
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return world.getTileEntity(pos) == this && player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void update() {
		inventory.tick(world, pos, getBlockType());
		animator.tickTileEntity(pos);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setString(TAG_CHEST_TYPE, chestType.toString());
		tag.setTag(TAG_INVENTORY, inventory.serializeNBT());
		tag.setString(TAG_FRONT, front.toString());
		tag.setString("CustomName", customName);
		tag.setByte(TAG_ENCHANT_LEVEL, enchantLevel);
		tag.setBoolean(TAG_TRAPPED, trapped);
		tag.setBoolean(TAG_LUMINOUS, luminous);
		return tag;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.setChestType(ChestType.valueOf(tag.getString(TAG_CHEST_TYPE)));
		inventory.deserializeNBT(tag.getCompoundTag(TAG_INVENTORY));
		this.setFront(EnumFacing.byName(tag.getString(TAG_FRONT)));
		this.setCustomName(tag.getString("CustomName"));
		this.setEnchantLevel(tag.getByte(TAG_ENCHANT_LEVEL));
		this.setTrapped(tag.getBoolean(TAG_TRAPPED));
		this.setLuminous(tag.getBoolean(TAG_LUMINOUS));
	}

	@Override
	public boolean receiveClientEvent(int event, int data) {
		return animator.receiveClientEvent(event, data);
	}

	@Override
	public void invalidate() {
		updateContainingBlockInfo();
		super.invalidate();
	}

	public String getTranslationKey() {
		return "container.chest";
	}

	@Override
	public boolean hasCustomName() {
		return !StringUtils.isEmpty(customName);
	}

	@Override
	public String getName() {
		return getTranslationKey();
	}

	@Override
	public ITextComponent getDisplayName() {
		return hasCustomName() ? new TextComponentString(customName) : new TextComponentTranslation(getTranslationKey());
	}

	@Optional.Method(modid = RegistryMC.ID_HOLOINVENTORY)
	@Override
	public String getItemHandlerName() {
		return getTranslationKey();
	}

	@Optional.Method(modid = RegistryMC.ID_QUARK)
	@Override
	public boolean acceptsDropoff(EntityPlayer player) {
		return true;
	}

	@Optional.Method(modid = RegistryMC.ID_QUARK)
	@Override
	public IItemHandler getDropoffItemHandler(Supplier<IItemHandler> defaultSupplier) {
		return inventory;
	}
}
