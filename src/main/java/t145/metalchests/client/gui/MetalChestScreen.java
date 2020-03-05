package t145.metalchests.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import t145.metalchests.containers.MetalChestContainer;

@OnlyIn(Dist.CLIENT)
public class MetalChestScreen extends ContainerScreen<MetalChestContainer> implements IHasContainer<MetalChestContainer> {

	private static final ScreenResource CHEST_GUI_TEXTURE = new ScreenResource("chest", 512);

	public MetalChestScreen(MetalChestContainer container, PlayerInventory inv) {
		super(container, inv, container.getMetalType().getTitle());
	}

	@Override
	public void render(int x, int y, float partialTicks) {
		this.renderBackground();
		super.render(x, y, partialTicks);
		this.renderHoveredToolTip(x, y);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		font.drawString(this.getTitle().getFormattedText(), container.getBorderSide() + 1, 6, 0x404040);
		int invTitleX = container.getPlayerInvXOffset() + 1;
		int invTitleY = container.getBorderTop() + container.getContainerInvHeight() + 3;
		font.drawString(this.playerInventory.getDisplayName().getFormattedText(), invTitleX, invTitleY, 0x404040);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1F, 1F, 1F, 1F);

		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		int b = container.getBorderSide();
		int bTop = container.getBorderTop();
		int bBot = container.getBorderBottom();
		int w = xSize - b * 2;
		int h = container.getContainerInvHeight();
		int pw = container.getPlayerInvWidth();
		int ph = container.getPlayerInvHeight();
		int maxw = MetalChestContainer.MAX_COLUMNS * 18;
		int maxh = MetalChestContainer.MAX_ROWS * 18;
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

		CHEST_GUI_TEXTURE.bind();

		// Top
		CHEST_GUI_TEXTURE.drawQuad(x1, y, tx1, ty, b, bTop);
		CHEST_GUI_TEXTURE.drawQuad(x2, y, tx2, ty, w, bTop);
		CHEST_GUI_TEXTURE.drawQuad(x3, y, tx3, ty, b, bTop);
		y += bTop;
		ty += bTop + 2;

		// Container background
		CHEST_GUI_TEXTURE.drawQuad(x1, y, tx1, ty, b, h);
		CHEST_GUI_TEXTURE.drawQuad(x2, y, tx2, ty, w, h);
		CHEST_GUI_TEXTURE.drawQuad(x3, y, tx3, ty, b, h);

		// Container slots
		CHEST_GUI_TEXTURE.drawQuad(x + container.getContainerInvXOffset(), y, tx2, 292, container.getContainerInvWidth(), h);
		y += h;
		ty += maxh + 2;

		// Space between container and player inventory
		if (container.getMetalType().getColumns() > 9) {
			int sw = (w - (pw + b * 2)) / 2;
			CHEST_GUI_TEXTURE.drawQuad(x1, y, tx1 - 2, ty, b, bBot);
			CHEST_GUI_TEXTURE.drawQuad(x2, y, tx1 + b, ty, sw, bBot);
			CHEST_GUI_TEXTURE.drawQuad(px - b, y, tpx, ty, pw + b * 2, bufi);
			CHEST_GUI_TEXTURE.drawQuad(px + pw + b, y, tx3 - sw, ty, sw, bBot);
			CHEST_GUI_TEXTURE.drawQuad(x3, y, tx3 + 2, ty, b, bBot);
		}

		ty += bufi + 2;

		if (container.getMetalType().getColumns() <= 9) {
			CHEST_GUI_TEXTURE.drawQuad(x, y, tpx, ty, pw + b * 2, bufi);
		}

		y += bufi;
		ty += bufi + 2;

		// Player inventory
		CHEST_GUI_TEXTURE.drawQuad(px - b, y, tpx, ty, pw + b * 2, ph + bBot);
	}
}
