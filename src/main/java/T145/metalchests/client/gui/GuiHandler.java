/*******************************************************************************
 * Copyright 2019 T145
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
package T145.metalchests.client.gui;

import T145.metalchests.api.chests.IMetalChest;
import T145.metalchests.containers.ContainerMetalChest;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiHandler implements IGuiHandler {

	@Override
	public ContainerMetalChest getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case 0:
			TileMetalChest chest = (TileMetalChest) world.getTileEntity(new BlockPos(x, y, z));
			return new ContainerMetalChest(chest, player);
		default:
			Entity entity = world.getEntityByID(ID);

			if (entity instanceof IMetalChest) {
				return new ContainerMetalChest((IMetalChest) entity, player);
			}

			return null;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiMetalChest getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new GuiMetalChest(getServerGuiElement(ID, player, world, x, y, z));
	}
}
