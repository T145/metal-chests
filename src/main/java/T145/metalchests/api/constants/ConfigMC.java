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
package T145.metalchests.api.constants;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Loader;

@Config(modid = RegistryMC.ID, name = "T145/" + RegistryMC.NAME)
@Config.LangKey(RegistryMC.ID)
public class ConfigMC {

	private ConfigMC() {}

	@Config.Comment("The upgrade path for metal chests. Recipes will update to reflect this.\n Current valid options are: { \"copper\", \"iron\", \"silver\", \"gold\", \"diamond\", \"obsidian\" }")
	@Config.RequiresMcRestart
	public static String[] upgradePath = new String[] { "copper", "iron", "silver", "gold", "diamond", "obsidian" };

	@Config.Comment("Whether or not all metal chest model textures are like the vanilla chest; black in the middle.\n* Hollow textures contributed by phyne")
	public static boolean hollowModelTextures = false;

	@Config.Comment("Whether or not you want to enable the Minecarts with Metal Chests.")
	@Config.RequiresMcRestart
	public static boolean enableMinecarts = true;

	@Config.Comment("Whether or not you want to enable placing Metal Chests in boats.")
	@Config.RequiresMcRestart
	public static boolean enableBoats = true;

	@Config.Comment("If Thaumcraft is installed, whether or not you want to enable the Metal Hungry Chests.")
	@Config.RequiresMcRestart
	public static boolean enableMetalHungryChests = true;

	@Config.Comment("If Refined Relocation 2 is installed, whether or not you want to enable the Metal Hungry Sorting Chests.")
	@Config.RequiresMcRestart
	public static boolean enableMetalHungrySortingChests = true;

	@Config.Comment("If Thermal Expansion is installed, what the holding enchant bounds for each chest are.")
	@Config.RequiresMcRestart
	public static Map<String, Integer> holdingEnchantBounds = new HashMap() {{
		put("copper", 1);
		put("iron", 1);
		put("silver", 2);
		put("gold", 2);
		put("diamond", 3);
		put("obsidian", 4);
	}};

	public static boolean hasThaumcraft() {
		return Loader.isModLoaded(RegistryMC.ID_THAUMCRAFT) && enableMetalHungryChests;
	}

	public static boolean hasRefinedRelocation() {
		return Loader.isModLoaded(RegistryMC.ID_THAUMCRAFT) && enableMetalHungrySortingChests;
	}

	public static boolean hasThermalExpansion() {
		return Loader.isModLoaded(RegistryMC.ID_THERMALEXPANSION);
	}
}
