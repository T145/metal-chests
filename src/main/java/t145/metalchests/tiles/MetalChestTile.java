package t145.metalchests.tiles;

import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;

@OnlyIn(value = Dist.CLIENT, _interface = IChestLid.class)
public class MetalChestTile extends TileEntity implements IChestLid, ITickableTileEntity {

	public static final int EVENT_PLAYER_USED = -1;
	public static final int EVENT_CHEST_NOM = -2;

	public ItemStackHandler handler;
	public float lidAngle;
	public float prevLidAngle;
	public int numPlayersUsing;

	public MetalChestTile(TileEntityType<?> tileType) {
		super(tileType);
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

	@Override
	public void tick() {
		if (!world.isRemote && ((world.getGameTime() + pos.getX() + pos.getY() + pos.getZ()) & 0x1F) == 0) {
			world.addBlockEvent(pos, world.getBlockState(pos).getBlock(), EVENT_PLAYER_USED, this.numPlayersUsing);
		}

		prevLidAngle = lidAngle;
		lidAngle = lerp(lidAngle, numPlayersUsing > 0 ? 1.0F : 0.0F, 0.1F);
	}

	@Override
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

	@OnlyIn(Dist.CLIENT)
	@Override
	public float getLidAngle(float partialTicks) {
		return MathHelper.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
	}
}