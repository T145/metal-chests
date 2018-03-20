package T145.metalchests.client.gui;

import T145.metalchests.containers.ContainerMetalChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMetalChest extends GuiContainer {

	private final ContainerMetalChest inventory;

	public GuiMetalChest(ContainerMetalChest inventory) {
		super(inventory);
		this.inventory = inventory;
		this.xSize = inventory.getGuiType().getSizeX();
		this.ySize = inventory.getGuiType().getSizeY();
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
		mc.getTextureManager().bindTexture(inventory.getGuiType().getGuiTexture());
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}