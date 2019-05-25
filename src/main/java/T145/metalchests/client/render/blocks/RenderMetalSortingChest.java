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
package T145.metalchests.client.render.blocks;

import T145.metalchests.api.chests.IMetalChest;
import T145.metalchests.api.constants.ChestType;
import T145.metalchests.api.constants.RegistryMC;
import T145.metalchests.tiles.TileMetalChest;
import net.blay09.mods.refinedrelocation.RefinedRelocationConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMetalSortingChest extends RenderMetalChest {

	protected ResourceLocation getActiveOverlay(ChestType type) {
		return new ResourceLocation(RegistryMC.ID, String.format("textures/entity/chest/overlay/sorting_%s.png", type.getName()));
	}

	@Override
	protected boolean canRenderNameTag(TileMetalChest chest) {
		return RefinedRelocationConfig.renderChestNameTags && super.canRenderNameTag(chest);
	}

	@Override
	protected void postRenderChest(IMetalChest chest) {
		this.renderOverlay(getActiveOverlay(chest.getChestType()));
		super.postRenderChest(chest);
	}
}
