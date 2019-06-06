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
package T145.metalchests.network.client;

import java.io.IOException;

import T145.metalchests.api.chests.IMetalChest;
import T145.metalchests.tiles.TileMetalChest;
import T145.tbone.network.TMessage;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSyncMetalChest extends TMessage {

	protected BlockPos pos;
	protected boolean trapped;
	protected boolean luminous;
	protected byte enchantLevel;

	public MessageSyncMetalChest() {
		// DEFAULT CONSTRUCTOR REQUIRED
	}

	protected void setChestData(IMetalChest chest) {
		this.trapped = chest.isTrapped();
		this.luminous = chest.isLuminous();
		this.enchantLevel = chest.getEnchantLevel();
	}

	public MessageSyncMetalChest(BlockPos pos, IMetalChest chest) {
		this.pos = pos;
		this.setChestData(chest);
	}

	@Override
	public void serialize(PacketBuffer buf) {
		buf.writeBlockPos(pos);
		buf.writeBoolean(trapped);
		buf.writeBoolean(luminous);
		buf.writeByte(enchantLevel);
	}

	@Override
	public void deserialize(PacketBuffer buf) throws IOException {
		pos = buf.readBlockPos();
		trapped = buf.readBoolean();
		luminous = buf.readBoolean();
		enchantLevel = buf.readByte();
	}

	protected void syncChestData(IMetalChest chest) {
		chest.setTrapped(trapped);
		chest.setLuminous(luminous);
		chest.setEnchantLevel(enchantLevel);
	}

	@Override
	public void process(MessageContext ctx) {
		World world = getClientWorld();

		if (world != null) {
			TileEntity te = world.getTileEntity(pos);

			if (te instanceof TileMetalChest) {
				this.syncChestData((TileMetalChest) te);
			}
		}
	}
}
