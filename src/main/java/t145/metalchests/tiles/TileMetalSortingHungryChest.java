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
package t145.metalchests.tiles;

import net.minecraft.item.Item;
import t145.metalchests.api.config.ConfigMC;
import t145.metalchests.api.consts.ChestType;
import t145.metalchests.api.consts.RegistryMC;
import t145.metalchests.api.objs.ItemsMC;

public class TileMetalSortingHungryChest extends TileMetalSortingChest {

	public TileMetalSortingHungryChest(ChestType chestType) {
		super(chestType);
	}

	public TileMetalSortingHungryChest() {
		super();
	}

	@Override
	public boolean canUpgradeUsing(Item upgrade) {
		return upgrade == ItemsMC.HUNGRY_CHEST_UPGRADE;
	}

	@Override
	public String getTranslationKey() {
		return ConfigMC.archaicNaming ? super.getTranslationKey() : String.format("tile.%s:%s.%s.name", RegistryMC.ID, RegistryMC.KEY_METAL_SORTING_HUNGRY_CHEST, chestType.getName());
	}
}
