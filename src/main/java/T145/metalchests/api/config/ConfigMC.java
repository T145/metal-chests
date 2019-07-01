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
package T145.metalchests.api.config;

import T145.metalchests.api.consts.RegistryMC;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Loader;

@Config(modid = RegistryMC.ID, name = "T145/" + RegistryMC.NAME)
@Config.LangKey(RegistryMC.ID)
public class ConfigMC {

	private ConfigMC() {}

	@Config.Comment("Whether or not all metal chest model textures are like the vanilla chest; black in the middle.\n* Hollow textures contributed by phyne")
	public static boolean hollowModelTextures = false;

	public static boolean hasThaumcraft() {
		return Loader.isModLoaded(RegistryMC.ID_THAUMCRAFT);
	}

	public static boolean hasRefinedRelocation() {
		return Loader.isModLoaded(RegistryMC.ID_THAUMCRAFT);
	}

	public static boolean hasThermalExpansion() {
		return Loader.isModLoaded(RegistryMC.ID_THERMALEXPANSION);
	}
}
