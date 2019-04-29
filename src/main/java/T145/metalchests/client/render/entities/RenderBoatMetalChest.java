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
import T145.metalchests.entities.EntityBoatMetalChest;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBoatMetalChest extends RenderBoat {

	protected final ModelBase modelBoat = new ModelBoatChest();

	public RenderBoatMetalChest(RenderManager manager) {
		super(manager);
		this.shadowSize = 0.5F;
	}

	@Override
	public void doRender(EntityBoat entity, double x, double y, double z, float entityYaw, float partialTicks) {
		if (!(entity instanceof EntityBoatMetalChest)) {
			return;
		}

		EntityBoatMetalChest boat = (EntityBoatMetalChest) entity;

		GlStateManager.pushMatrix();
		this.setupTranslation(x, y, z);
		this.setupRotation(boat, entityYaw, partialTicks);
		this.bindEntityTexture(boat);

		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(boat));
		}

		this.modelBoat.render(boat, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		GlStateManager.pushMatrix();
		GlStateManager.translate(-0.5F, 0.125F, -0.5F);
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
		RenderMetalChest.INSTANCE.renderStatic(boat, 0, 0, 0, -1, 1);
		GlStateManager.popMatrix();

		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.popMatrix();

		if (!this.renderOutlines) {
			this.renderName(boat, x, y, z);
		}
	}
}
