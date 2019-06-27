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

import T145.metalchests.api.consts.RegistryMC;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class UpgradeRegistry {

	private static final Map<ResourceLocation, HashMap<ResourceLocation, ResourceLocation>> UPGRADES = new HashMap<>();

	private UpgradeRegistry() {}

	public static void register(Item upgrade, Block chest, Block metalChest) {
		ResourceLocation upgradeKey = upgrade.getRegistryName();

		if (!UPGRADES.containsKey(upgradeKey)) {
			UPGRADES.put(upgradeKey, new HashMap<ResourceLocation, ResourceLocation>());
		}

		HashMap<ResourceLocation, ResourceLocation> blocks = UPGRADES.get(upgradeKey);
		ResourceLocation chestKey = chest.getRegistryName();
		ResourceLocation metalChestKey = metalChest.getRegistryName();

		if (blocks.containsKey(chestKey)) {
			RegistryMC.LOG.warn(String.format("{%s} The chest [%s] is already registered to [%s]!", upgradeKey, chestKey, blocks.get(chestKey)));
		} else {
			blocks.put(chestKey, metalChestKey);
		}
	}

	public static boolean hasMetalChest(Item upgrade, Block chest) {
		return UPGRADES.get(upgrade.getRegistryName()).containsKey(chest.getRegistryName());
	}

	public static Block getMetalChest(Item upgrade, Block chest) {
		return Block.getBlockFromName(UPGRADES.get(upgrade.getRegistryName()).get(chest.getRegistryName()).toString());
	}

	public static boolean hasMultipleUpgrades() {
		return UPGRADES.size() > 1;
	}
}
