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

import T145.metalchests.api.IInventoryHandler;
import T145.metalchests.api.SupportedInterfaces;
import T145.metalchests.api.SupportedMods;
import T145.metalchests.blocks.BlockMetalChest.ChestType;
import net.dries007.holoInventory.api.INamedItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vazkii.quark.api.IDropoffManager;

@Optional.InterfaceList({
	@Optional.Interface(modid = SupportedMods.HOLOINVENTORY, iface = SupportedInterfaces.NAMED_ITEM_HANDLER, striprefs = true),
	@Optional.Interface(modid = SupportedMods.QUARK, iface = SupportedInterfaces.DROPOFF_MANAGER, striprefs = true)
})
public class TileMetalChest extends TileMod implements ITickable, IInventoryHandler, IDropoffManager, INamedItemHandler {

	private ChestType type;
	private ItemStackHandler inventory;
	private EnumFacing front;

	public float lidAngle;
	public float prevLidAngle;
	public int numPlayersUsing;
	private int ticksSinceSync;

	public TileMetalChest(ChestType type) {
		this.type = type;
		this.inventory = new ItemStackHandler(type.getInventorySize());
		this.setFront(EnumFacing.EAST);
	}

	public TileMetalChest() {
		this(ChestType.IRON);
	}

	public ChestType getType() {
		return type;
	}

	public ItemStackHandler getInventory() {
		return inventory;
	}

	public void setFront(EnumFacing front) {
		this.front = front;
	}

	public EnumFacing getFront() {
		return front;
	}

	public static void registerFixes(DataFixer fixer) {
		fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileMetalChest.class, new String[] { "Items" }));
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
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		front = EnumFacing.byName(tag.getString("Front"));
		type = ChestType.byMetadata(tag.getInteger("Type"));
		inventory.deserializeNBT(tag.getCompoundTag("Inventory"));
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();
		tag.setString("Front", front.toString());
		tag.setInteger("Type", type.ordinal());
		tag.setTag("Inventory", inventory.serializeNBT());
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		front = EnumFacing.byName(compound.getString("Front"));
		type = ChestType.byMetadata(compound.getInteger("Type"));
		inventory.deserializeNBT(compound.getCompoundTag("Inventory"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setString("Front", front.toString());
		compound.setInteger("Type", type.ordinal());
		compound.setTag("Inventory", inventory.serializeNBT());
		return compound;
	}

	@Optional.Method(modid = SupportedMods.HOLOINVENTORY)
	@Override
	public String getItemHandlerName() {
		return "tile.metalchests:metal_chest." + type.getName() + ".name";
	}

	@Optional.Method(modid = SupportedMods.QUARK)
	@Override
	public boolean acceptsDropoff(EntityPlayer player) {
		return true;
	}

	@Optional.Method(modid = SupportedMods.QUARK)
	@Override
	public IItemHandler getDropoffItemHandler(Supplier<IItemHandler> defaultSupplier) {
		return inventory;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		if (!player.isSpectator()) {
			if (numPlayersUsing < 0) {
				numPlayersUsing = 0;
			}

			++numPlayersUsing;
			world.addBlockEvent(pos, getBlockType(), 1, numPlayersUsing);
			world.notifyNeighborsOfStateChange(pos, getBlockType(), false);
		}
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		if (!player.isSpectator()) {
			--numPlayersUsing;
			world.addBlockEvent(pos, getBlockType(), 1, numPlayersUsing);
			world.notifyNeighborsOfStateChange(pos, getBlockType(), false);
		}
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (world.getTileEntity(pos) != this) {
			return false;
		} else {
			return player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public void update() {
		if (++ticksSinceSync % 20 * 4 == 0) {
			world.addBlockEvent(pos, getBlockType(), 1, numPlayersUsing);
		}

		prevLidAngle = lidAngle;
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();
		float f = 0.1F;

		if (numPlayersUsing > 0 && lidAngle == 0.0F) {
			double d0 = i + 0.5D;
			double d1 = k + 0.5D;
			world.playSound(null, d0, j + 0.5D, d1, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (numPlayersUsing == 0 && lidAngle > 0.0F || numPlayersUsing > 0 && lidAngle < 1.0F) {
			float f2 = lidAngle;

			if (numPlayersUsing > 0) {
				lidAngle += 0.1F;
			} else {
				lidAngle -= 0.1F;
			}

			if (lidAngle > 1.0F) {
				lidAngle = 1.0F;
			}

			float f1 = 0.5F;

			if (lidAngle < 0.5F && f2 >= 0.5F) {
				double d3 = i + 0.5D;
				double d2 = k + 0.5D;
				world.playSound(null, d3, j + 0.5D, d2, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
			}

			if (lidAngle < 0.0F) {
				lidAngle = 0.0F;
			}
		}
	}

	@Override
	public boolean receiveClientEvent(int id, int data) {
		if (id == 1) {
			numPlayersUsing = data;
			return true;
		} else {
			return super.receiveClientEvent(id, data);
		}
	}

	@Override
	public void invalidate() {
		updateContainingBlockInfo();
		super.invalidate();
	}
}
