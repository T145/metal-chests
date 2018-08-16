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
package T145.metalchests.lib;

import java.io.IOException;

import T145.metalchests.blocks.BlockMetalChest.ChestType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;

public class DataSerializers {

	private DataSerializers() {}

	public static void registerSerializers() {
		net.minecraft.network.datasync.DataSerializers.registerSerializer(CHEST_TYPE);
	}

	public static final DataSerializer<ChestType> CHEST_TYPE = new DataSerializer<ChestType>() {

		@Override
		public void write(PacketBuffer buf, ChestType value) {
			buf.writeEnumValue(value);
		}

		@Override
		public ChestType read(PacketBuffer buf) throws IOException {
			return buf.readEnumValue(ChestType.class);
		}

		@Override
		public DataParameter<ChestType> createKey(int id) {
			return new DataParameter<ChestType>(id, this);
		}

		@Override
		public ChestType copyValue(ChestType value) {
			return value;
		}
	};
}
