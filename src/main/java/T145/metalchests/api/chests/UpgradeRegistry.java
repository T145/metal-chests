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
package T145.metalchests.api.chests;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class UpgradeRegistry {

	private static final Map<String, HashMap<Block, Block>> chests = new HashMap<>();

	public static void registerChest(ResourceLocation resource, Block chestTile, Block destTile) {
		String upgradeName = resource.toString();

		if (chests.containsKey(upgradeName)) {
			HashMap<Block, Block> map = chests.get(upgradeName);
			map.put(chestTile, destTile);
		} else {
			HashMap<Block, Block> map = new HashMap<>();
			map.put(chestTile, destTile);
			chests.put(upgradeName, map);
		}
	}

	public static boolean hasChest(ResourceLocation resource, Block chestTile) {
		HashMap<Block, Block> map = chests.get(resource.toString());
		return map.containsKey(chestTile) && map.get(chestTile) != null;
	}

	public static Block getChest(ResourceLocation resource, Block chestTile) {
		return chests.get(resource.toString()).get(chestTile);
	}
}
