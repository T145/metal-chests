package T145.metalchests.tiles;

import java.util.Collections;
import java.util.Comparator;

import javax.annotation.Nonnull;

import T145.metalchests.lib.MetalChestType;
import T145.metalchests.lib.SimpleItemStackHandler;
import T145.metalchests.tiles.base.TileBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileMetalChest extends TileBase implements ITickable {

	public float lidAngle;
	public float prevLidAngle;
	public int numPlayersUsing;
	private int ticksSinceSync;
	private boolean touched;

	private MetalChestType type;
	private EnumFacing front;
	private SimpleItemStackHandler inventory;
	private SimpleItemStackHandler topStacks;

	public TileMetalChest(MetalChestType type) {
		this.type = type;
		this.front = EnumFacing.EAST;
		this.inventory = createInventory(type.getInventorySize());
		this.topStacks = createInventory(8);
	}

	public TileMetalChest() {
		this(MetalChestType.IRON);
	}

	public MetalChestType getType() {
		return type;
	}

	public void setFront(EnumFacing front) {
		this.front = front;
	}

	public EnumFacing getFront() {
		return front;
	}

	public SimpleItemStackHandler getInventory() {
		return inventory;
	}

	public SimpleItemStackHandler getTopStacks() {
		return topStacks;
	}

	public void setInventory(NonNullList<ItemStack> items) {
		for (int slot = 0; slot < items.size(); ++slot) {
			if (slot < type.getInventorySize()) {
				inventory.setStackInSlot(slot, items.get(slot));
			}
		}
	}

	private SimpleItemStackHandler createInventory(int inventorySize) {
		return new SimpleItemStackHandler(inventorySize, this, true);
	}

	public static void registerFixesChest(DataFixer fixer) {
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
	public void readPacketNBT(NBTTagCompound tag) {
		type = MetalChestType.valueOf(tag.getString("Type"));
		front = EnumFacing.byName(tag.getString("Front"));
		inventory.deserializeNBT(tag.getCompoundTag("Inventory"));
		topStacks.deserializeNBT(tag.getCompoundTag("TopStacks"));
		sortTopStacks();
	}

	@Override
	public void writePacketNBT(NBTTagCompound tag) {
		tag.setString("Type", type.toString());
		tag.setString("Front", front.toString());
		tag.setTag("Inventory", inventory.serializeNBT());
		tag.setTag("TopStacks", topStacks.serializeNBT());
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
		switch (id) {
		case 1:
			numPlayersUsing = data;
			return true;
		default:
			return super.receiveClientEvent(id, data);
		}
	}

	@Override
	public void markDirty() {
		super.markDirty();
		sortTopStacks();
	}

	public void sortTopStacks() {
		if (type != MetalChestType.CRYSTAL || (world != null && world.isRemote)) {
			return;
		}

		NonNullList<ItemStack> temp = NonNullList.<ItemStack>withSize(type.getInventorySize(), ItemStack.EMPTY);
		boolean hasStuff = false;
		int maxSlot = 0;

		copyInventory: for (int i = 0; i < type.getInventorySize(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);

			if (!stack.isEmpty()) {
				for (int j = 0; j < maxSlot; j++) {
					ItemStack tempStack = temp.get(j);

					if (ItemStack.areItemsEqualIgnoreDurability(tempStack, stack)) {
						if (stack.getCount() != tempStack.getCount()) {
							tempStack.grow(stack.getCount());
						}

						continue copyInventory;
					}
				}

				temp.set(maxSlot++, stack.copy());
				hasStuff = true;
			}
		}

		if (!hasStuff && touched) {
			touched = false;

			for (int i = 0; i < topStacks.size(); ++i) {
				topStacks.setStackInSlot(i, ItemStack.EMPTY);
			}

			if (world != null) {
				IBlockState state = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state, state, 3);
			}

			return;
		}

		touched = true;

		Collections.sort(temp, new Comparator<ItemStack>() {

			@Override
			public int compare(ItemStack stack1, ItemStack stack2) {
				if (stack1.isEmpty()) {
					return 1;
				} else if (stack2.isEmpty()) {
					return -1;
				} else {
					return stack2.getCount() - stack1.getCount();
				}
			}
		});

		maxSlot = 0;

		for (ItemStack element : temp) {
			if (!element.isEmpty() && element.getCount() > 0) {
				if (maxSlot == topStacks.size()) {
					break;
				}

				topStacks.setStackInSlot(maxSlot++, element);
			}
		}

		for (int i = maxSlot; i < topStacks.size(); ++i) {
			topStacks.setStackInSlot(i, ItemStack.EMPTY);
		}

		if (world != null) {
			IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	@Override
	public void invalidate() {
		updateContainingBlockInfo();
		super.invalidate();
	}

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

	public void closeInventory(EntityPlayer player) {
		if (!player.isSpectator()) {
			--numPlayersUsing;
			world.addBlockEvent(pos, getBlockType(), 1, numPlayersUsing);
			world.notifyNeighborsOfStateChange(pos, getBlockType(), false);
		}
	}

	public boolean isUsableByPlayer(EntityPlayer player) {
		if (world.getTileEntity(pos) != this) {
			return false;
		} else {
			return player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
		}
	}
}