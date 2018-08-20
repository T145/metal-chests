/*******************************************************************************
 * Copyright 2018 T145
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
package T145.metalchests.api.immutable;

import T145.metalchests.core.MetalChests;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class BlocksMC {

	private BlocksMC() {}

	public static final String KEY_METAL_CHEST = "metal_chest";
	public static final String KEY_HUNGRY_METAL_CHEST = "hungry_metal_chest";
	public static final String KEY_SORTING_METAL_CHEST = "sorting_metal_chest";
	public static final String KEY_SORTING_HUNGRY_METAL_CHEST = "sorting_hungry_metal_chest";

	public static final ResourceLocation REGISTRY_METAL_CHEST = new ResourceLocation(MetalChests.MOD_ID, KEY_METAL_CHEST);
	public static final ResourceLocation REGISTRY_HUNGRY_METAL_CHEST = new ResourceLocation(MetalChests.MOD_ID, KEY_HUNGRY_METAL_CHEST);
	public static final ResourceLocation REGISTRY_SORTING_METAL_CHEST = new ResourceLocation(MetalChests.MOD_ID, KEY_SORTING_METAL_CHEST);
	public static final ResourceLocation REGISTRY_SORTING_HUNGRY_METAL_CHEST = new ResourceLocation(MetalChests.MOD_ID, KEY_SORTING_HUNGRY_METAL_CHEST);

	public static Block METAL_CHEST;
	public static Block HUNGRY_METAL_CHEST;
	public static Block SORTING_METAL_CHEST;
	public static Block SORTING_HUNGRY_METAL_CHEST;

}
