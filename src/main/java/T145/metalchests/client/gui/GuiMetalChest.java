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

import T145.metalchests.api.consts.RegistryMC;
import T145.metalchests.containers.ContainerMetalChest;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
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

	private static final GuiResource TEX = new GuiResource("chest", 512);
	private final ContainerMetalChest container;

	public GuiMetalChest(ContainerMetalChest container) {
		super(container);
		this.container = container;
		this.allowUserInput = false;
		this.xSize = container.getWidth();
		this.ySize = container.getHeight();
	}

	@Override
	public void drawScreen(int x, int y, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(x, y, partialTicks);
		renderHoveredToolTip(x, y);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString(container.mainInv.getDisplayName().getFormattedText(), container.getBorderSide() + 1, 6, 0x404040);
		int invTitleX = container.getPlayerInvXOffset() + 1;
		int invTitleY = container.getBorderTop() + container.getContainerInvHeight() + 3;
		fontRenderer.drawString(I18n.format("container.inventory"), invTitleX, invTitleY, 0x404040);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1, 1, 1, 1);

		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;

		// Might have gone *a little* overboard with the local variables...
		int b = container.getBorderSide();
		int bTop = container.getBorderTop();
		int bBot = container.getBorderBottom();
		int w = xSize - b * 2;
		int h = container.getContainerInvHeight();
		int pw = container.getPlayerInvWidth();
		int ph = container.getPlayerInvHeight();
		int maxw = container.MAX_COLUMNS * 18;
		int maxh = container.MAX_ROWS * 18;
		int bufi = container.getBufferInventory();

		int x1 = x;
		int x2 = x + b;
		int x3 = x + xSize - b;
		int px = x + container.getPlayerInvXOffset();

		int tx1 = 4;
		int tx2 = tx1 + b + 2;
		int tx3 = tx2 + maxw + 2;
		int ty = 4;
		int tpx = 2 + b + 2 + (maxw - (pw + b * 2)) / 2 + 2;

		TEX.bind();

		// Top
		TEX.drawQuad(x1, y, tx1, ty, b, bTop);
		TEX.drawQuad(x2, y, tx2, ty, w, bTop);
		TEX.drawQuad(x3, y, tx3, ty, b, bTop);
		y += bTop;
		ty += bTop + 2;

		// Container background
		TEX.drawQuad(x1, y, tx1, ty, b, h);
		TEX.drawQuad(x2, y, tx2, ty, w, h);
		TEX.drawQuad(x3, y, tx3, ty, b, h);

		// Container slots
		TEX.drawQuad(x + container.getContainerInvXOffset(), y, tx2, 256, container.getContainerInvWidth(), h);
		y += h;
		ty += maxh + 2;

		// Space between container and player inventory
		if (container.type.getColumns() > 9) {
			int sw = (w - (pw + b * 2)) / 2;
			TEX.drawQuad(x1, y, tx1 - 2, ty, b, bBot);
			TEX.drawQuad(x2, y, tx1 + b, ty, sw, bBot);
			TEX.drawQuad(px - b, y, tpx, ty, pw + b * 2, bufi);
			TEX.drawQuad(px + pw + b, y, tx3 - sw, ty, sw, bBot);
			TEX.drawQuad(x3, y, tx3 + 2, ty, b, bBot);
		}

		ty += bufi + 2;

		if (container.type.getColumns() <= 9) {
			TEX.drawQuad(x, y, tpx, ty, pw + b * 2, bufi);
		}

		y += bufi;
		ty += bufi + 2;

		// Player inventory
		TEX.drawQuad(px - b, y, tpx, ty, pw + b * 2, ph + bBot);
	}

	@Optional.Method(modid = RegistryMC.ID_QUARK)
	@Override
	public boolean onAddChestButton(GuiButton button, int buttonType) {
		// button.x += xSize + 20;
		return true;
	}

	@Optional.Method(modid = RegistryMC.ID_QUARK)
	@Override
	public void onSearchBarAdded(GuiTextField bar) {
		bar.x = this.getGuiLeft() + (this.getXSize() - 95);
	}
}
