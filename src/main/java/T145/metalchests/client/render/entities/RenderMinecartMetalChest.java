/*******************************************************************************
 * Copyright 2019 T145
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
package T145.metalchests.client.render.entities;

import T145.metalchests.client.render.blocks.RenderMetalChest;
import T145.metalchests.entities.EntityMinecartMetalChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMinecartMetalChest extends RenderMinecart<EntityMinecartMetalChest> {

	public RenderMinecartMetalChest(RenderManager manager) {
		super(manager);
	}

	@Override
	protected void renderCartContents(EntityMinecartMetalChest cart, float partialTicks, IBlockState state) {
		RenderMetalChest.INSTANCE.renderStatic(cart.getChestType(), 0, 0, -1, -1, 1);
	}
}
