package T145.metalchests.tiles;

import T145.metalchests.lib.MetalChestType;
import T145.metalchests.tiles.base.TileInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

public class TileMetalChest extends TileInventory implements ITickable {

	public float lidAngle;
	public float prevLidAngle;
	public int numPlayersUsing;
	private int ticksSinceSync;

	private MetalChestType type;
	private EnumFacing front;

	public TileMetalChest(MetalChestType type) {
		this.type = type;
		this.front = EnumFacing.NORTH;
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

	@Override
	public void readPacketNBT(NBTTagCompound tag) {
		super.readPacketNBT(tag);
		type = MetalChestType.valueOf(tag.getString("Type"));
		front = EnumFacing.valueOf(tag.getString("Front"));
	}

	@Override
	public void writePacketNBT(NBTTagCompound tag) {
		super.writePacketNBT(tag);
		tag.setString("Type", type.toString());
		tag.setString("Front", front.toString());
	}

	@Override
	public int getInventorySize() {
		return type.getInventorySize();
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
		case 2:
			++numPlayersUsing;
			world.addBlockEvent(pos, getBlockType(), 1, numPlayersUsing);
			return true;
		case 3:
			--numPlayersUsing;
			world.addBlockEvent(pos, getBlockType(), 1, numPlayersUsing);
			return true;
		default:
			return super.receiveClientEvent(id, data);
		}
	}

	@Override
	public void invalidate() {
		updateContainingBlockInfo();
		super.invalidate();
	}

	public boolean isUsableByPlayer(EntityPlayer player) {
		if (world.getTileEntity(pos) != this) {
			return false;
		} else {
			return player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
		}
	}
}