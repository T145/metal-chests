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
package T145.metalchests.network;

import T145.metalchests.api.constants.RegistryMC;
import T145.metalchests.network.client.MessageSyncMetalChest;
import T145.tbone.network.TPacketHandler;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandlerMC extends TPacketHandler {

	public PacketHandlerMC() {
		super(RegistryMC.ID);
	}

	@Override
	public void registerMessages() {
		registerMessage(MessageSyncMetalChest.class, Side.CLIENT);
	}
}
