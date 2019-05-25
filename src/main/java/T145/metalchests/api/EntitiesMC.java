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
package T145.metalchests.api;

import T145.metalchests.api.constants.RegistryMC;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(RegistryMC.ID)
public class EntitiesMC {

	private EntitiesMC() {}

	@ObjectHolder(RegistryMC.KEY_MINECART_METAL_CHEST)
	public static EntityEntry MINECART_METAL_CHEST;

	@ObjectHolder(RegistryMC.KEY_BOAT_METAL_CHEST)
	public static EntityEntry BOAT_METAL_CHEST;
}
