package T145.metalchests.api.chests;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChestAnimator {

	private class WorldContextProvider {

		private TileEntity tileEntity;
		private Entity entity;

		public WorldContextProvider(TileEntity tileEntity) {
			this.tileEntity = tileEntity;
		}

		public WorldContextProvider(Entity entity) {
			this.entity = entity;
		}

		public boolean hasTileEntity() {
			return tileEntity != null;
		}

		public boolean hasEntity() {
			return entity != null;
		}

		public boolean hasContext() {
			return hasTileEntity() || hasEntity();
		}

		public World getWorld() {
			if (hasEntity()) {
				return entity.getEntityWorld();
			}
			return tileEntity.getWorld();
		}

		public BlockPos getPos() {
			if (hasEntity()) {
				return entity.getPosition();
			}
			return tileEntity.getPos();
		}

		public void addBlockEvent(int event, int data) {
			if (hasTileEntity()) {
				World world = tileEntity.getWorld();
				Block block = tileEntity.getBlockType();
				BlockPos pos = tileEntity.getPos();

				world.addBlockEvent(pos, block, 1, data);
				world.notifyNeighborsOfStateChange(pos, block, false);
			}
		}
	}

	public static final int EVENT_PLAYER_USED = -1;
	public static final int EVENT_CHEST_NOM = -2;

	public float lidAngle;
	public float prevLidAngle;
	public int numPlayersUsing;

	private final WorldContextProvider provider;

	public ChestAnimator(TileEntity tileEntity) {
		this.provider = new WorldContextProvider(tileEntity);
	}

	public ChestAnimator(Entity entity) {
		this.provider = new WorldContextProvider(entity);
	}

	public boolean isOpen() {
		return lidAngle > 0.0F;
	}

	public void openInventory(SoundEvent openSound, EntityPlayer player) {
		if (!player.isSpectator()) {
			if (numPlayersUsing < 0) {
				numPlayersUsing = 0;
			}

			World world = provider.getWorld();
			BlockPos pos = provider.getPos();

			provider.addBlockEvent(EVENT_PLAYER_USED, ++numPlayersUsing);
			world.playSound(player, pos, openSound, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
		}
	}

	public void openInventory(EntityPlayer player) {
		this.openInventory(SoundEvents.BLOCK_CHEST_OPEN, player);
	}

	public void closeInventory(SoundEvent closeSound, EntityPlayer player) {
		if (!player.isSpectator()) {
			World world = provider.getWorld();
			BlockPos pos = provider.getPos();

			provider.addBlockEvent(EVENT_PLAYER_USED, --numPlayersUsing);
			world.playSound(player, pos, closeSound, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
		}
	}

	public void closeInventory(EntityPlayer player) {
		this.closeInventory(SoundEvents.BLOCK_CHEST_CLOSE, player);
	}

	/**
	 * @param a   The value
	 * @param b   The value to approach
	 * @param max The maximum step
	 * @return the closed value to b no less than max from a
	 */
	private float lerp(float a, float b, float max) {
		return (a > b) ? (a - b < max ? b : a - max) : (b - a < max ? b : a + max);
	}

	public void tick() {
		if (provider.hasTileEntity()) {
			World world = provider.getWorld();

			if (!world.isRemote && world.getTotalWorldTime() % 20 == 0) {
				provider.addBlockEvent(1, numPlayersUsing);
			}
		}

		prevLidAngle = lidAngle;
		lidAngle = lerp(lidAngle, numPlayersUsing > 0 ? 1.0F : 0.0F, 0.1F);
	}

	public boolean receiveClientEvent(int event, int data) {
		switch (event) {
		case EVENT_PLAYER_USED:
			numPlayersUsing = data;
			return true;
		case EVENT_CHEST_NOM:
			if (lidAngle < data / 10F) {
				lidAngle = data / 10F;
			}
			return true;
		default:
			return false;
		}
	}

	public float getRenderAngle(float partialTicks) {
		float f = prevLidAngle + (lidAngle - prevLidAngle) * partialTicks;
		f = 1.0F - f;
		f = 1.0F - f * f * f;
		return (float) -(f * (Math.PI / 2F));
	}
}
