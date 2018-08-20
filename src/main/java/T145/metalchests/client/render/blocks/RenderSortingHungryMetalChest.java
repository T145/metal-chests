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
package T145.metalchests.client.render.blocks;

import T145.metalchests.api.immutable.BlocksMC;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.core.MetalChests;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSortingHungryMetalChest extends RenderSortingMetalChest {

	public RenderSortingHungryMetalChest() {
		super(BlocksMC.SORTING_HUNGRY_METAL_CHEST);
	}

	@Override
	protected ResourceLocation getActiveResource(ChestType type) {
		return new ResourceLocation(MetalChests.MOD_ID, "textures/entity/chest/hungry/" + type.getName() + ".png");
	}

	@Override
	protected ResourceLocation getActiveOverlay(ChestType type) {
		return new ResourceLocation(MetalChests.MOD_ID, "textures/entity/chest/hungry/overlay/" + type.getName() + ".png");
	}
}
