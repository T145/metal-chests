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

import net.minecraft.client.model.IMultipassModel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBoatChest extends ModelBase implements IMultipassModel
{
	public ModelRenderer[] boatSides = new ModelRenderer[5];
	/**
	 * An invisible layer that is rendered to make it seem like there's no water in the boat.
	 *  
	 * @see https://redd.it/3qufgo
	 * @see https://bugs.mojang.com/browse/MC-47636
	 */
	public ModelRenderer noWater;
	private final int patchList = GLAllocation.generateDisplayLists(1);

	public ModelBoatChest()
	{
		this.boatSides[0] = (new ModelRenderer(this, 0, 0)).setTextureSize(128, 64);
		this.boatSides[1] = (new ModelRenderer(this, 0, 19)).setTextureSize(128, 64);
		this.boatSides[2] = (new ModelRenderer(this, 0, 27)).setTextureSize(128, 64);
		this.boatSides[3] = (new ModelRenderer(this, 0, 35)).setTextureSize(128, 64);
		this.boatSides[4] = (new ModelRenderer(this, 0, 43)).setTextureSize(128, 64);
		int i = 32;
		int j = 6;
		int k = 20;
		int l = 4;
		int i1 = 28;
		this.boatSides[0].addBox(-14.0F, -9.0F, -3.0F, 28, 16, 3, 0.0F);
		this.boatSides[0].setRotationPoint(0.0F, 3.0F, 1.0F);
		this.boatSides[1].addBox(-13.0F, -7.0F, -1.0F, 18, 6, 2, 0.0F);
		this.boatSides[1].setRotationPoint(-15.0F, 4.0F, 4.0F);
		this.boatSides[2].addBox(-8.0F, -7.0F, -1.0F, 16, 6, 2, 0.0F);
		this.boatSides[2].setRotationPoint(15.0F, 4.0F, 0.0F);
		this.boatSides[3].addBox(-14.0F, -7.0F, -1.0F, 28, 6, 2, 0.0F);
		this.boatSides[3].setRotationPoint(0.0F, 4.0F, -9.0F);
		this.boatSides[4].addBox(-14.0F, -7.0F, -1.0F, 28, 6, 2, 0.0F);
		this.boatSides[4].setRotationPoint(0.0F, 4.0F, 9.0F);
		this.boatSides[0].rotateAngleX = ((float)Math.PI / 2F);
		this.boatSides[1].rotateAngleY = ((float)Math.PI * 3F / 2F);
		this.boatSides[2].rotateAngleY = ((float)Math.PI / 2F);
		this.boatSides[3].rotateAngleY = (float)Math.PI;
		this.noWater = (new ModelRenderer(this, 0, 0)).setTextureSize(128, 64);
		this.noWater.addBox(-14.0F, -9.0F, -3.0F, 28, 16, 3, 0.0F);
		this.noWater.setRotationPoint(0.0F, -3.0F, 1.0F);
		this.noWater.rotateAngleX = ((float)Math.PI / 2F);
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	 public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	 {
		 GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
		 EntityBoat entityboat = (EntityBoat)entityIn;
		 this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

		 for (int i = 0; i < 5; ++i)
		 {
			 this.boatSides[i].render(scale);
		 }
	 }

	 public void renderMultipass(Entity entityIn, float partialTicks, float p_187054_3_, float p_187054_4_, float p_187054_5_, float p_187054_6_, float scale)
	 {
		 GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
		 GlStateManager.colorMask(false, false, false, false);
		 this.noWater.render(scale);
		 GlStateManager.colorMask(true, true, true, true);
	 }
}
