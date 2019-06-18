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
package T145.metalchests.api.obj;

import T145.metalchests.api.consts.RegistryMC;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(RegistryMC.ID)
public class BlocksMC {

	private BlocksMC() {}

	@ObjectHolder(RegistryMC.KEY_METAL_CHEST)
	public static Block METAL_CHEST;

	@ObjectHolder(RegistryMC.KEY_METAL_HUNGRY_CHEST)
	public static Block METAL_HUNGRY_CHEST;

	@ObjectHolder(RegistryMC.KEY_METAL_SORTING_CHEST)
	public static Block METAL_SORTING_CHEST;

	@ObjectHolder(RegistryMC.KEY_METAL_HUNGRY_SORTING_CHEST)
	public static Block METAL_HUNGRY_SORTING_CHEST;
}
