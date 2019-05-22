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
package T145.metalchests.tiles;

import T145.metalchests.api.constants.ChestType;
import T145.metalchests.api.constants.RegistryMC;

public class TileMetalHungryChest extends TileMetalChest {

	public TileMetalHungryChest(ChestType chestType) {
		super(chestType);
	}

	public TileMetalHungryChest() {
		super();
	}

	@Override
	public boolean receiveClientEvent(int id, int data) {
		switch (id) {
		case 2:
			if (lidAngle < data / 10F) {
				lidAngle = data / 10F;
			}
			return true;
		default:
			return super.receiveClientEvent(id, data);
		}
	}

	@Override
	public String getTranslationKey() {
		return String.format("tile.%s:%s.%s.name", RegistryMC.ID, RegistryMC.KEY_METAL_HUNGRY_CHEST, chestType.getName());
	}
}
