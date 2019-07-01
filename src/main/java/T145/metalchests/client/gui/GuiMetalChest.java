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
package T145.metalchests.client.gui;

import org.lwjgl.opengl.GL11;

import T145.metalchests.api.consts.RegistryMC;
import T145.metalchests.containers.ContainerMetalChest;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.quark.api.IChestButtonCallback;
import vazkii.quark.api.IItemSearchBar;

@Optional.InterfaceList({
	@Optional.Interface(modid = RegistryMC.ID_QUARK, iface = RegistryMC.IFACE_CHEST_BUTTON_CALLBACK, striprefs = true),
	@Optional.Interface(modid = RegistryMC.ID_QUARK, iface = RegistryMC.IFACE_SEARCH_BAR, striprefs = true)
})
@SideOnly(Side.CLIENT)
public class GuiMetalChest extends GuiContainer implements IChestButtonCallback, IItemSearchBar {

	private final int invSize;

	public GuiMetalChest(ContainerMetalChest container) {
		super(container);
		this.allowUserInput = false;
		this.invSize = container.getType().getInventorySize();
		this.xSize = 14 + 18 * MathHelper.clamp(invSize, 9, 14);
		this.ySize = 112 + 18 * MathHelper.clamp(invSize, 2, 9);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	public void drawSizedTexturedModalRect(int x, int y, int u, int v, int width, int height, float texW, float texH) {
		float texU = 1 / texW;
		float texV = 1 / texH;
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(x, y + height, this.zLevel).tex((u) * texU, (v + height) * texV).endVertex();
		buffer.pos(x + width, y + height, this.zLevel).tex((u + width) * texU, (v + height) * texV).endVertex();
		buffer.pos(x + width, y, this.zLevel).tex((u + width) * texU, (v) * texV).endVertex();
		buffer.pos(x, y, this.zLevel).tex((u) * texU, (v) * texV).endVertex();
		Tessellator.getInstance().draw();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1, 1, 1, 1);
		mc.renderEngine.bindTexture(RegistryMC.getResource(String.format("textures/gui/%s.png", invSize)));

		if (xSize > 256 || ySize > 256) {
			drawSizedTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize, 512, 512);
		} else {
			drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		}
	}

	@Optional.Method(modid = RegistryMC.ID_QUARK)
	@Override
	public boolean onAddChestButton(GuiButton button, int buttonType) {
		button.x += xSize + 20;
		return true;
	}

	@Optional.Method(modid = RegistryMC.ID_QUARK)
	@Override
	public void onSearchBarAdded(GuiTextField bar) {
		int xOffset = this.getXSize() - 95;
		bar.y = this.getGuiTop() - 4;
		xOffset -= 4;
		bar.x = this.getGuiLeft() + xOffset;
	}
}
