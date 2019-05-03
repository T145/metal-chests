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
package T145.metalchests.client.gui;

import T145.metalchests.containers.ContainerMetalChest;
import T145.metalchests.core.ModSupport;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.quark.api.IChestButtonCallback;
import vazkii.quark.api.IItemSearchBar;

@Optional.InterfaceList({
	@Optional.Interface(modid = ModSupport.QUARK_MOD_ID, iface = ModSupport.IFACE_CHEST_BUTTON_CALLBACK, striprefs = true),
	@Optional.Interface(modid = ModSupport.QUARK_MOD_ID, iface = ModSupport.IFACE_SEARCH_BAR, striprefs = true)
})
@SideOnly(Side.CLIENT)
public class GuiMetalChest extends GuiContainer implements IChestButtonCallback, IItemSearchBar {

	private final ContainerMetalChest inventory;

	public GuiMetalChest(ContainerMetalChest inventory) {
		super(inventory);
		this.inventory = inventory;
		this.xSize = inventory.getGui().getSizeX();
		this.ySize = inventory.getGui().getSizeY();
		this.allowUserInput = false;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(inventory.getGui().getGuiTexture());
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

	@Optional.Method(modid = ModSupport.QUARK_MOD_ID)
	@Override
	public boolean onAddChestButton(GuiButton button, int buttonType) {
		return true;
	}

	@Optional.Method(modid = ModSupport.QUARK_MOD_ID)
	@Override
	public void onSearchBarAdded(GuiTextField bar) {
		int xOffset = this.getXSize() - 95;
		bar.y = this.getGuiTop() - 4;
		xOffset -= 4;
		bar.x = this.getGuiLeft() + xOffset;
	}
}
