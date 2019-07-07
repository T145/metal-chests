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
package t145.metalchests.client.render.blocks;

import net.blay09.mods.refinedrelocation.RefinedRelocationConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import t145.metalchests.api.chests.IMetalChest;
import t145.metalchests.api.consts.ChestType;
import t145.metalchests.api.consts.RegistryMC;
import t145.metalchests.tiles.TileMetalChest;

@SideOnly(Side.CLIENT)
public class RenderMetalSortingChest extends RenderMetalChest {

	protected ResourceLocation getActiveOverlay(ChestType type) {
		return RegistryMC.getResource(String.format("textures/entity/chest/overlay/sorting_%s.png", type.getName()));
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
