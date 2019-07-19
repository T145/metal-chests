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
package t145.metalchests.api.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Loader;
import t145.metalchests.api.consts.RegistryMC;

@Config(modid = RegistryMC.ID, name = "T145/" + RegistryMC.NAME)
@Config.LangKey(RegistryMC.ID)
public class ConfigMC {

	@Config.Comment("Whether or not the JSON config will be regenerated on mod update.")
	public static boolean regenConfig = true;

	@Config.Comment("Whether or not all metal chest model textures are like the vanilla chest; black in the middle.\n* Hollow textures contributed by phyne")
	public static boolean hollowModelTextures;

	@Config.Comment("Whether or not chest container names are modelled after their block type (\"Chest\") or metal type (\"Iron Chest\")")
	public static boolean archaicNaming;

	private ConfigMC() {}

	public static boolean hasThaumcraft() {
		return Loader.isModLoaded(RegistryMC.ID_THAUMCRAFT);
	}

	public static boolean hasRefinedRelocation() {
		return Loader.isModLoaded(RegistryMC.ID_RR2);
	}

	public static boolean hasThermalExpansion() {
		return Loader.isModLoaded(RegistryMC.ID_THERMALEXPANSION);
	}
}
