package T145.metalchests.client.gui;

import T145.metalchests.containers.ContainerMinecartMetalChest;
import T145.metalchests.entities.EntityMinecartMetalChest;
import T145.metalchests.lib.MetalChestType;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMinecartMetalChest extends GuiContainer {

	private final MetalChestType.GUI gui;

	public GuiMinecartMetalChest(MetalChestType.GUI gui, EntityMinecartMetalChest cart, EntityPlayer player) {
		super(new ContainerMinecartMetalChest(cart, player, gui.getSizeX(), gui.getSizeY()));
		this.gui = gui;
		this.xSize = gui.getSizeX();
		this.ySize = gui.getSizeY();
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
		mc.getTextureManager().bindTexture(gui.getGuiTexture());
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}