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
package t145.metalchests.entities.ai;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import t145.metalchests.blocks.BlockMetalChest;
import t145.metalchests.tiles.TileMetalChest;

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

				if (te instanceof TileMetalChest && !((TileMetalChest) te).getChestAnimator().isOpen()) {
					return true;
				}
			}

			return super.shouldMoveTo(world, pos);
		}
	}
}
