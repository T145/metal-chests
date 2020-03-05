package t145.metalchests.tiles;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;
import t145.metalchests.blocks.MetalChestBlock;
import t145.metalchests.containers.MetalChestContainer;
import t145.metalchests.lib.ChestHandler;
import t145.metalchests.lib.MetalChestType;

@OnlyIn(value = Dist.CLIENT, _interface = IChestLid.class)
public class MetalChestTile extends TileEntity implements IChestLid, ITickableTileEntity, INamedContainerProvider {

	public static final int EVENT_PLAYER_USED = -1;
	public static final int EVENT_CHEST_NOM = -2;

	public ItemStackHandler handler;
	public float lidAngle;
	public float prevLidAngle;
	public int numPlayersUsing;

	public MetalChestTile(TileEntityType<?> tileType) {
		super(tileType);
		this.handler = new ChestHandler(this.getMetalType().getInventorySize());
	}

	public MetalChestType getMetalType() {
		return this.hasWorld() ? this.getBlockState().get(MetalChestBlock.METAL_TYPE) : MetalChestType.IRON;
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
			world.addBlockEvent(pos, getBlockState().getBlock(), EVENT_PLAYER_USED, numPlayersUsing);
		}

		prevLidAngle = lidAngle;
		lidAngle = lerp(lidAngle, numPlayersUsing > 0 ? 1.0F : 0.0F, 0.1F);
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
		return new MetalChestContainer(windowId, inv, this);
	}

	@Override
	public ITextComponent getDisplayName() {
		return this.getMetalType().getTitle();
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
		return MathHelper.lerp(partialTicks, prevLidAngle, lidAngle);
	}

	public void openOrCloseChest(PlayerEntity player, boolean opening) {
		if (!player.isSpectator()) {
			numPlayersUsing = MathHelper.clamp(numPlayersUsing, 0, numPlayersUsing) + (opening ? 1 : -1);
			world.playSound(player, pos, opening ? SoundEvents.BLOCK_CHEST_OPEN : SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
			world.addBlockEvent(pos, getBlockState().getBlock(), 1, numPlayersUsing);
			world.notifyNeighborsOfStateChange(pos, getBlockState().getBlock());
		}
	}

	public boolean isUsableByPlayer(PlayerEntity player) {
		return world.getTileEntity(pos) == this && player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
	}
}