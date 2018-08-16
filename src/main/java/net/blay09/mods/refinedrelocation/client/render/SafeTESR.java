package net.blay09.mods.refinedrelocation.client.render;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

/**
 * Something is stupid in Vanilla or Forge and causes TESRs to render with minecraft:air block states, apparently during respawn from death.
 * This class makes sure not to render in that case.
  */
public abstract class SafeTESR<T extends TileEntity> extends TileEntitySpecialRenderer<T> {

	private final Block block;

	public SafeTESR(Block block) {
		this.block = block;
	}

	@Override
	public final void render(T tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(shouldRenderNameTag(tileEntity)) {
			super.render(tileEntity, x, y, z, partialTicks, destroyStage, alpha);
		}
		IBlockState state = tileEntity.hasWorld() ? tileEntity.getWorld().getBlockState(tileEntity.getPos()) : null;
		if(state != null && state.getBlock() != block) {
			return;
		}
		renderTileEntityAt(tileEntity, x, y, z, partialTicks, destroyStage, state);
	}

	protected abstract void renderTileEntityAt(T tileEntity, double x, double y, double z, float partialTicks, int destroyStage, @Nullable IBlockState state);

	protected boolean shouldRenderNameTag(T tileEntity) {
		return false;
	}
}
