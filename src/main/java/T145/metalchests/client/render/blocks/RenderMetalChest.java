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
import T145.metalchests.api.constants.ChestType;
import T145.metalchests.api.constants.RegistryMC;
import T145.metalchests.config.ModConfig;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMetalChest extends TileEntitySpecialRenderer<TileMetalChest> {

	public static final RenderMetalChest INSTANCE = new RenderMetalChest();
	private static final ResourceLocation OVERLAY_ENCHANT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	private static final ResourceLocation OVERLAY_TRAP = new ResourceLocation(RegistryMC.ID, "textures/entity/chest/overlay/trap.png");
	private static final ResourceLocation OVERLAY_GLOW = new ResourceLocation(RegistryMC.ID, "textures/entity/chest/overlay/glow.png");

	protected final ModelChest model = new ModelChest();

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
		return new ResourceLocation(RegistryMC.ID, String.format("textures/entity/chest/%s%s", type.getName(), ModConfig.GENERAL.hollowModelTextures ? "_h.png" : ".png"));
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
		GlStateManager.rotate(getFrontAngle(chest.getFront()), 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);
	}

	protected void preRender(IMetalChest chest, double x, double y, double z, int destroyStage, float alpha) {
		preRender(destroyStage >= 0 ? DESTROY_STAGES[destroyStage] : null, chest, x, y, z, destroyStage, alpha);
	}

	protected void postRenderModel(IMetalChest chest, float lidAngle, int destroyStage) {
		model.chestLid.rotateAngleX = (float) -(lidAngle * (Math.PI / 2F));
		model.renderAll();
	}

	protected void postRenderChest(IMetalChest chest, float lidAngle, int destroyStage) {
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	protected void postRenderOverlay(IMetalChest chest, float lidAngle, int destroyStage) {
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

	protected void postRender(IMetalChest chest, float lidAngle, double x, double y, double z, int destroyStage, float alpha) {
		postRenderModel(chest, lidAngle, destroyStage);

		if (chest.isTrapped()) {
			renderOverlay(OVERLAY_TRAP);
		}

		if (chest.isLuminous()) {
			renderOverlay(OVERLAY_GLOW);
		}

		postRenderChest(chest, lidAngle, destroyStage);

		if (destroyStage >= 0) {
			postRenderOverlay(chest, lidAngle, destroyStage);
		}

		if (chest.getEnchantLevel() > 0) {
			double spin = Minecraft.getSystemTime() / 1000D;
			float scale = 0.33333334F;

			preRender(OVERLAY_ENCHANT, chest, x, y, z, destroyStage, alpha);

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
				postRenderModel(chest, lidAngle, destroyStage);
				GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			}

			GlStateManager.matrixMode(GL11.GL_TEXTURE);
			GlStateManager.loadIdentity();
			GlStateManager.matrixMode(GL11.GL_MODELVIEW);
			GlStateManager.enableLighting();
			GlStateManager.depthMask(true);
			GlStateManager.depthFunc(GL11.GL_LEQUAL);
			GlStateManager.disableBlend();

			postRenderChest(chest, lidAngle, destroyStage);
			postRenderOverlay(chest, lidAngle, destroyStage);
		}
	}

	// allows normal chest rendering w/out a new tile instance
	public void renderStatic(IMetalChest chest, double x, double y, double z, int destroyStage, float alpha) {
		preRender(chest, x, y, z, destroyStage, alpha);
		postRender(chest, 0.0F, x, y, z, destroyStage, alpha);
	}

	protected boolean canRenderNameTag(TileMetalChest chest) {
		return chest.hasCustomName();
	}

	@Override
	public void render(TileMetalChest chest, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if (canRenderNameTag(chest)) {
			super.render(chest, x, y, z, partialTicks, destroyStage, alpha);
		}

		preRender(chest, x, y, z, destroyStage, alpha);
		float f = chest.prevLidAngle + (chest.lidAngle - chest.prevLidAngle) * partialTicks;
		f = 1.0F - f;
		f = 1.0F - f * f * f;
		postRender(chest, f, x, y, z, destroyStage, alpha);
	}
}
