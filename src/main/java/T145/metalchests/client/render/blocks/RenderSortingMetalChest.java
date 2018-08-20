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

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import T145.metalchests.api.immutable.BlocksMC;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.core.MetalChests;
import T145.metalchests.tiles.TileSortingMetalChest;
import net.blay09.mods.refinedrelocation.RefinedRelocationConfig;
import net.blay09.mods.refinedrelocation.client.render.ModelLidOverlay;
import net.blay09.mods.refinedrelocation.client.render.SafeTESR;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSortingMetalChest extends SafeTESR<TileSortingMetalChest> {

	private final ModelChest model = new ModelChest();
	private final ModelLidOverlay chestLid = new ModelLidOverlay();

	public RenderSortingMetalChest(Block block) {
		super(block);
	}

	public RenderSortingMetalChest() {
		super(BlocksMC.SORTING_METAL_CHEST);
	}

	protected ResourceLocation getActiveResource(ChestType type) {
		return new ResourceLocation(MetalChests.MOD_ID, "textures/entity/chest/" + type.getName() + ".png");
	}

	protected ResourceLocation getActiveOverlay(ChestType type) {
		return new ResourceLocation(MetalChests.MOD_ID, "textures/entity/chest/overlay/" + type.getName() + ".png");
	}

	@Override
	public void renderTileEntityAt(TileSortingMetalChest chest, double x, double y, double z, float partialTicks, int destroyStage, @Nullable IBlockState state) {
		GlStateManager.enableDepth();
		GlStateManager.depthFunc(GL11.GL_LEQUAL);
		GlStateManager.depthMask(true);

		if (destroyStage >= 0) {
			bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(GL11.GL_TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4f, 4f, 1f);
			GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
			GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		} else {
			bindTexture(getActiveResource(chest.getChestType()));
		}

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();

		if (destroyStage < 0) {
			GlStateManager.color(1f, 1f, 1f, 1f);
		}

		GlStateManager.translate(x, y + 1, z + 1);
		GlStateManager.scale(1f, -1f, -1f);
		GlStateManager.translate(0.5f, 0.5f, 0.5f);
		GlStateManager.rotate(RenderMetalChest.getFrontAngle(chest.getFront()), 0f, 1f, 0f);
		GlStateManager.translate(-0.5f, -0.5f, -0.5f);
		float f = chest.prevLidAngle + (chest.lidAngle - chest.prevLidAngle) * partialTicks;
		f = 1.0F - f;
		f = 1.0F - f * f * f;
		model.chestLid.rotateAngleX = (float) -(f * (Math.PI / 2F));
		model.renderAll();

		if (destroyStage == -1) {
			bindTexture(getActiveOverlay(chest.getChestType()));
			GlStateManager.translate(0f, -0.001f, 0f);
			chestLid.chestLid.rotateAngleX = model.chestLid.rotateAngleX;
			chestLid.renderAll();
		}

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		GlStateManager.color(1f, 1f, 1f, 1f);

		if (destroyStage >= 0) {
			GlStateManager.matrixMode(GL11.GL_TEXTURE);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		}
	}

	@Override
	protected boolean shouldRenderNameTag(TileSortingMetalChest chest) {
		return chest.hasCustomName() && RefinedRelocationConfig.renderChestNameTags;
	}
}
