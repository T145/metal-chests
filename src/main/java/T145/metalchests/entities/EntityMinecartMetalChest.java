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
package T145.metalchests.entities;

import javax.annotation.Nullable;

<<<<<<< HEAD
=======
import T145.metalchests.api.BlocksMetalChests;
import T145.metalchests.api.ItemsMetalChests;
import T145.metalchests.api.ModSupport;
>>>>>>> parent of e893474... Updated the API
import T145.metalchests.api.chests.IInventoryHandler;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.ChestUpgrade;
<<<<<<< HEAD
import T145.metalchests.api.immutable.ItemsMC;
import T145.metalchests.api.immutable.ModSupport;
=======
>>>>>>> parent of e893474... Updated the API
import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.core.MetalChests;
import T145.metalchests.items.ItemChestUpgrade;
import T145.metalchests.lib.DataSerializers;
import T145.metalchests.tiles.TileMetalChest;
import mods.railcraft.api.carts.IItemCart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

@Optional.Interface(modid = ModSupport.Railcraft.MOD_ID, iface = ModSupport.Railcraft.ITEM_CART, striprefs = true)
public class EntityMinecartMetalChest extends EntityMinecart implements IInventory, IInventoryHandler, IItemCart {

	private static final DataParameter<ChestType> CHEST_TYPE = EntityDataManager.<ChestType>createKey(EntityMinecart.class, DataSerializers.CHEST_TYPE);
	private final ItemStackHandler inventory = new ItemStackHandler(getChestType().getInventorySize());

	public EntityMinecartMetalChest(World world) {
		super(world);
	}

	public EntityMinecartMetalChest(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public ChestType getChestType() {
		return dataManager.get(CHEST_TYPE);
	}

	public void setChestType(ChestType type) {
		dataManager.set(CHEST_TYPE, type);
	}

	public TileMetalChest getMetalChest() {
		return new TileMetalChest(getChestType());
	}

	public void setInventory(IItemHandler stacks) {
		for (int slot = 0; slot < stacks.getSlots(); ++slot) {
			if (slot < getChestType().getInventorySize()) {
				inventory.setStackInSlot(slot, stacks.getStackInSlot(slot));
			}
		}
	}

	public static void addDataFixers(DataFixer fixer) {
		EntityMinecart.registerFixesMinecart(fixer, EntityMinecartMetalChest.class);
		fixer.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(EntityMinecartMetalChest.class, new String[] { "Items" }));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(CHEST_TYPE, ChestType.OBSIDIAN);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setString("Type", getChestType().toString());
		tag.setTag("Inventory", inventory.serializeNBT());
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		setChestType(ChestType.valueOf(tag.getString("Type")));
		inventory.deserializeNBT(tag.getCompoundTag("Inventory"));
	}

	@Override
	public void killMinecart(DamageSource source) {
		super.killMinecart(source);

		if (world.getGameRules().getBoolean("doEntityDrops")) {
			for (int i = 0; i < inventory.getSlots(); ++i) {
				ItemStack stack = inventory.getStackInSlot(i);

				if (!stack.isEmpty()) {
					InventoryHelper.spawnItemStack(world, posX, posY, posZ, stack);
				}
			}

			entityDropItem(new ItemStack(BlocksMetalChests.METAL_CHEST, 1, getChestType().ordinal()), 0.0F);
		}
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (super.processInitialInteract(player, hand) || world.isRemote) {
			return true;
		}

		if (player.isSneaking()) {
			ItemStack stack = player.getHeldItem(hand);

			if (stack.getItem() instanceof ItemChestUpgrade) {
				ChestUpgrade upgrade = ChestUpgrade.byMetadata(stack.getItemDamage());

				if (getChestType() == upgrade.getBase()) {
					setChestType(upgrade.getUpgrade());
					setInventory(inventory);

					if (!player.capabilities.isCreativeMode) {
						stack.shrink(1);
					}

					player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.4F, 0.8F);
				}
			}
		} else {
			player.openGui(MetalChests.MOD_ID, hashCode(), world, 0, 0, 0);
		}

		return true;
	}

	@Override
	protected void applyDrag() {
		float f = 0.98F;

		int i = 15 - ItemHandlerHelper.calcRedstoneFromInventory(inventory);
		f += (float) i * 0.001F;

		this.motionX *= (double) f;
		this.motionY *= 0.0D;
		this.motionZ *= (double) f;
	}

	@Override
	public ItemStack getCartItem() {
		return new ItemStack(ItemsMetalChests.MINECART_METAL_CHEST, 1, getChestType().ordinal());
	}

	@Override
	public int getSizeInventory() {
		return inventory.getSlots();
	}

	@Override
	public boolean isEmpty() {
		for (int slot = 0; slot < this.getSizeInventory(); ++slot) {
			ItemStack stack = this.getStackInSlot(slot);

			if (!stack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return inventory.getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return inventory.extractItem(index, count, false);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return this.decrStackSize(index, this.getStackInSlot(index).getCount());
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		inventory.setStackInSlot(index, stack);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return !this.isDead && player.getDistanceSq(this) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return inventory.isItemValid(index, stack);
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for (int slot = 0; slot < this.getSizeInventory(); ++slot) {
			this.setInventorySlotContents(slot, ItemStack.EMPTY);
		}
	}

	@Override
	public EntityMinecart.Type getType() {
		return EntityMinecart.Type.CHEST;
	}

	@Override
	public IBlockState getDefaultDisplayTile() {
		return BlocksMetalChests.METAL_CHEST.getDefaultState().withProperty(BlockMetalChest.VARIANT, getChestType());
	}

	@Override
	public int getDefaultDisplayTileOffset() {
		return 8;
	}

	@Override
	public String getName() {
		if (hasCustomName()) {
			return getCustomNameTag();
		} else {
			return I18n.translateToLocal("item.metalchests:minecart_metal_chest." + getChestType().getName() + ".name");
		}
	}

	@Override
	public IItemHandler getInventory() {
		return this.inventory;
	}

	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) inventory;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Optional.Method(modid = ModSupport.Railcraft.MOD_ID)
	@Override
	public boolean canPassItemRequests() {
		return true;
	}

	@Optional.Method(modid = ModSupport.Railcraft.MOD_ID)
	@Override
	public boolean canAcceptPushedItem(EntityMinecart requester, ItemStack stack) {
		return !ItemHandlerHelper.insertItemStacked(inventory, stack, true).isEmpty();
	}

	@Optional.Method(modid = ModSupport.Railcraft.MOD_ID)
	@Override
	public boolean canProvidePulledItem(EntityMinecart requester, ItemStack stack) {
		ItemStack result = ItemStack.EMPTY;

		for (int i = 0; i < inventory.getSlots(); ++i) {
			ItemStack extractedStack = inventory.extractItem(i, stack.getCount(), true);

			if (ItemStack.areItemStacksEqual(extractedStack, stack)) {
				result = extractedStack;
			}
		}

		return !result.isEmpty();
	}
}
