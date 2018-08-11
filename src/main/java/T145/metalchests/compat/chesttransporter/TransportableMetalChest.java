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
package T145.metalchests.compat.chesttransporter;

import cubex2.mods.chesttransporter.chests.TransportableChestImpl;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TransportableMetalChest extends TransportableChestImpl {

	public TransportableMetalChest(Block chestBlock, int chestMeta, String name) {
		super(chestBlock, chestMeta, name);
	}

	@Override
	public boolean copyTileEntity() {
		return true;
	}

	@Override
	public NBTTagCompound modifyTileCompound(NBTTagCompound nbt, World world, BlockPos pos, EntityPlayer player, ItemStack transporter) {
		nbt.setString("Front", player.getHorizontalFacing().getOpposite().toString());
		return super.modifyTileCompound(nbt, world, pos, player, transporter);
	}
}
