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

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import t145.metalchests.api.chests.IMetalChest;
import t145.metalchests.api.config.ConfigMC;
import t145.metalchests.api.consts.ChestType;
import t145.metalchests.api.consts.RegistryMC;
import t145.metalchests.tiles.TileMetalChest;
import t145.tbone.lib.ChestHelper;

@SideOnly(Side.CLIENT)
public class RenderMetalChest extends TileEntitySpecialRenderer<TileMetalChest> {

	public static final RenderMetalChest INSTANCE = new RenderMetalChest();
	protected final ModelChest model = new ModelChest();

	protected ResourceLocation getActiveResource(ChestType type) {
		return ConfigMC.hollowModelTextures ? RegistryMC.HOLLOW_METAL_CHEST_MODELS[type.ordinal()] : RegistryMC.METAL_CHEST_MODELS[type.ordinal()];
	}

	protected void preRender(ResourceLocation overlay, IMetalChest chest, double x, double y, double z, int destroyStage, float alpha) {
		if (overlay != null) {
			bindTexture(overlay);
			GlStateManager.matrixMode(GL11.GL_TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F, 4.0F, 1.0F);
			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		} else {
			bindTexture(getActiveResource(chest.getChestType()));
		}

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
		GlStateManager.translate(x, y + 1.0F, z + 1.0F);
		GlStateManager.scale(1.0F, -1.0F, -1.0F);
		GlStateManager.translate(0.5F, 0.5F, 0.5F);
		GlStateManager.rotate(ChestHelper.getFrontAngle(chest.getFront()), 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);
	}

	protected void postRenderModel(IMetalChest chest, float partialTicks) {
		model.chestLid.rotateAngleX = chest.getChestAnimator().getLidAngle(partialTicks);
		model.renderAll();
	}

	protected void postRenderChest(IMetalChest chest) {
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	protected void postRenderOverlay(IMetalChest chest) {
		GlStateManager.matrixMode(GL11.GL_TEXTURE);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
	}

	protected void renderOverlay(ResourceLocation overlay) {
		float scale = 1.002F;

		bindTexture(overlay);
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.scale(scale, scale, scale);
		GlStateManager.translate(-0.001F, -0.001F, -0.001F);
		model.renderAll();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public void renderChest(IMetalChest chest, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		preRender(destroyStage >= 0 ? DESTROY_STAGES[destroyStage] : null, chest, x, y, z, destroyStage, alpha);
		postRenderModel(chest, partialTicks);

		if (chest.isTrapped()) {
			renderOverlay(RegistryMC.OVERLAY_TRAP);
		}

		postRenderChest(chest);

		if (destroyStage >= 0) {
			postRenderOverlay(chest);
		}

		if (chest.getEnchantLevel() > 0) {
			double spin = Minecraft.getSystemTime() / 1000D;
			float scale = 0.33333334F;

			preRender(RegistryMC.OVERLAY_ENCHANT, chest, x, y, z, destroyStage, alpha);

			GlStateManager.enableBlend();
			GlStateManager.depthFunc(GL11.GL_EQUAL);
			GlStateManager.depthMask(false);
			GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);

			for (int i = 0; i < 2; ++i) {
				GlStateManager.disableLighting();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
				GlStateManager.color(0.38F, 0.19F, 0.608F, 1.0F);
				GlStateManager.matrixMode(GL11.GL_TEXTURE);
				GlStateManager.loadIdentity();
				GlStateManager.scale(scale, scale, scale);
				GlStateManager.rotate(30.0F - i * 60.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.translate(0.0F, (spin * 32D) * (0.001F + i * 0.003F) * 20.0F, 0.0F);
				GlStateManager.matrixMode(GL11.GL_MODELVIEW);
				postRenderModel(chest, partialTicks);
				GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			}

			GlStateManager.matrixMode(GL11.GL_TEXTURE);
			GlStateManager.loadIdentity();
			GlStateManager.matrixMode(GL11.GL_MODELVIEW);
			GlStateManager.enableLighting();
			GlStateManager.depthMask(true);
			GlStateManager.depthFunc(GL11.GL_LEQUAL);
			GlStateManager.disableBlend();

			postRenderChest(chest);
			postRenderOverlay(chest);
		}
	}

	// allows normal chest rendering w/out a new tile instance
	public void renderStatic(IMetalChest chest, double x, double y, double z, int destroyStage, float alpha) {
		renderChest(chest, x, y, z, 0.0F, destroyStage, alpha);
	}

	protected boolean canRenderNameTag(TileMetalChest chest) {
		return chest.hasCustomName();
	}

	@Override
	public void render(TileMetalChest chest, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if (canRenderNameTag(chest)) {
			super.render(chest, x, y, z, partialTicks, destroyStage, alpha);
		}

		renderChest(chest, x, y, z, partialTicks, destroyStage, alpha);
	}
}
