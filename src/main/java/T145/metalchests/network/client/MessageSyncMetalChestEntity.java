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

import T145.metalchests.api.chests.IMetalChest;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSyncMetalChestEntity extends MessageSyncMetalChest {

	private int entityId;

	public MessageSyncMetalChestEntity() {
		// DEFAULT CONSTRUCTOR REQUIRED
	}

	private MessageSyncMetalChestEntity(BlockPos pos, IMetalChest chest) {
		super(pos, chest);
	}

	public MessageSyncMetalChestEntity(int entityId, IMetalChest chest) {
		this.entityId = entityId;
		this.setChestData(chest);
	}

	@Override
	public void process(MessageContext ctx) {
		World world = getClientWorld();

		if (world != null) {
			Entity entity = world.getEntityByID(entityId);

			if (entity instanceof IMetalChest) {
				this.syncChestData((IMetalChest) entity);
			}
		}
	}
}
