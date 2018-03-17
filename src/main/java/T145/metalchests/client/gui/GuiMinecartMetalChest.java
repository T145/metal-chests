package T145.metalchests.client.gui;

import T145.metalchests.containers.ContainerMinecartMetalChest;
import T145.metalchests.lib.MetalChestType;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMinecartMetalChest extends GuiContainer {

	private MetalChestType.GUI type;

	public GuiMinecartMetalChest(MetalChestType.GUI type, IInventory player, IInventory chest) {
		super(new ContainerMinecartMetalChest(player, chest, MetalChestType.byMetadata(type.ordinal()), type.getSizeX(), type.getSizeY()));
		this.type = type;
		this.xSize = type.getSizeX();
		this.ySize = type.getSizeY();
		this.allowUserInput = false;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(this.type.getGuiTexture());
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}
}