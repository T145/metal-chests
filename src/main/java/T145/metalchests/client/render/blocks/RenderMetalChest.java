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
package T145.metalchests.client.render.blocks;

import org.lwjgl.opengl.GL11;

import T145.metalchests.api.chests.IMetalChest;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.RegistryMC;
import T145.metalchests.config.ModConfig;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMetalChest extends TileEntitySpecialRenderer<TileMetalChest> {

	public static final RenderMetalChest INSTANCE = new RenderMetalChest();

	protected final ModelChest model = new ModelChest();
	protected static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");

	public static int getFrontAngle(EnumFacing front) {
		switch (front) {
		case NORTH:
			return 180;
		case WEST:
			return 90;
		case EAST:
			return -90;
		default:
			return 0;
		}
	}

	protected ResourceLocation getActiveResource(ChestType type) {
		return new ResourceLocation(RegistryMC.MOD_ID, String.format("textures/entity/chest/%s%s", type.getName(), ModConfig.GENERAL.hollowModelTextures ? "_h.png" : ".png"));
	}

	private void preRender(ChestType type, EnumFacing dir, double x, double y, double z, int destroyStage, float alpha) {
		if (destroyStage >= 0) {
			bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(GL11.GL_TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F, 4.0F, 1.0F);
			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		} else {
			bindTexture(getActiveResource(type));
		}

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
		GlStateManager.translate(x, y + 1.0F, z + 1.0F);
		GlStateManager.scale(1.0F, -1.0F, -1.0F);
		GlStateManager.translate(0.5F, 0.5F, 0.5F);
		GlStateManager.rotate(getFrontAngle(dir), 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);
	}

	private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

	private void postRender(float lidAngle, int destroyStage) {
		model.chestLid.rotateAngleX = (float) -(lidAngle * (Math.PI / 2F));
		model.renderAll();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		if (destroyStage >= 0) {
			GlStateManager.matrixMode(GL11.GL_TEXTURE);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		}
	}

	// allows normal chest rendering w/out a new tile instance
	public void renderStatic(ChestType type, EnumFacing front, double x, double y, double z, int destroyStage, float alpha) {
		preRender(type, front, x, y, z, destroyStage, alpha);
		postRender(0.0F, destroyStage);
	}

	public void renderStatic(IMetalChest chest, double x, double y, double z, int destroyStage, float alpha) {
		renderStatic(chest.getChestType(), chest.getFront(), x, y, z, destroyStage, alpha);
	}

	@Override
	public void render(TileMetalChest chest, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		preRender(chest.getChestType(), chest.getFront(), x, y, z, destroyStage, alpha);
		float f = chest.prevLidAngle + (chest.lidAngle - chest.prevLidAngle) * partialTicks;
		f = 1.0F - f;
		f = 1.0F - f * f * f;

		postRender(f, destroyStage);

		if (chest.holdingEnchantLevel > 0) {
			double spin = Minecraft.getSystemTime() / 1000D;
			bindTexture(ENCHANTED_ITEM_GLINT_RES);
			GlStateManager.matrixMode(GL11.GL_TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F, 4.0F, 1.0F);
			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(GL11.GL_MODELVIEW);
			GlStateManager.pushMatrix();
			GlStateManager.enableRescaleNormal();
			GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
			GlStateManager.translate(x, y + 1.0F, z + 1.0F);
			GlStateManager.scale(1.0F, -1.0F, -1.0F);
			GlStateManager.translate(0.5F, 0.5F, 0.5F);
			GlStateManager.rotate(getFrontAngle(chest.getFront()), 0.0F, 1.0F, 0.0F);
			GlStateManager.translate(-0.5F, -0.5F, -0.5F);
			// render model
			GlStateManager.enableBlend();
			GlStateManager.depthFunc(514);
			GlStateManager.depthMask(false);
			float f1 = 0.5F;
			GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);

			for (int i = 0; i < 2; ++i) {
				GlStateManager.disableLighting();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
				float f2 = 0.76F;
				GlStateManager.color(0.38F, 0.19F, 0.608F, 1.0F);
				GlStateManager.matrixMode(5890);
				GlStateManager.loadIdentity();
				float f3 = 0.33333334F;
				GlStateManager.scale(0.33333334F, 0.33333334F, 0.33333334F);
				GlStateManager.rotate(30.0F - (float) i * 60.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.translate(0.0F, ((float) (spin * 40D /* % 360 */) + partialTicks) * (0.001F + (float) i * 0.003F) * 20.0F, 0.0F);
				GlStateManager.matrixMode(5888);
				model.chestLid.rotateAngleX = (float) -(chest.lidAngle * (Math.PI / 2F));
				model.renderAll();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			}

			GlStateManager.matrixMode(5890);
			GlStateManager.loadIdentity();
			GlStateManager.matrixMode(5888);
			GlStateManager.enableLighting();
			GlStateManager.depthMask(true);
			GlStateManager.depthFunc(515);
			GlStateManager.disableBlend();

			GlStateManager.disableRescaleNormal();
			GlStateManager.popMatrix();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.matrixMode(GL11.GL_TEXTURE);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		}
	}
}
