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
package T145.metalchests.api.chests;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;

public class UpgradeRegistry {

	private static final Map<Block, Block> chests = new HashMap<>();

	public static void registerChest(Block chestTile, Block destTile) {
		if (!chests.containsKey(chestTile)) {
			chests.put(chestTile, destTile);
		}
	}

	public static boolean hasChest(Block chestTile) {
		return chests.containsKey(chestTile) && chests.get(chestTile) != null;
	}

	public static Block getDestTile(Block chestTile) {
		return chests.get(chestTile);
	}
}
