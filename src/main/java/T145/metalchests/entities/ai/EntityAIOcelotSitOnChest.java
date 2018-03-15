package T145.metalchests.entities.ai;

import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIOcelotSitOnChest extends EntityAIOcelotSit {

	public EntityAIOcelotSitOnChest(EntityOcelot ocelot, double speed) {
		super(ocelot, speed);
	}

	@Override
	protected boolean shouldMoveTo(World world, BlockPos pos) {
		if (!world.isAirBlock(pos.up())) {
			return false;
		} else {
			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();

			if (block instanceof BlockMetalChest) {
				TileEntity te = world.getTileEntity(pos);

				if (te instanceof TileMetalChest && ((TileMetalChest) te).numPlayersUsing < 1) {
					return true;
				}
			}

			return super.shouldMoveTo(world, pos);
		}
	}
}