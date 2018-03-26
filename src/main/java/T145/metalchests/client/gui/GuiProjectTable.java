package T145.metalchests.client.gui;

import java.io.IOException;

import T145.metalchests.MetalChests;
import T145.metalchests.containers.ContainerProjectTable;
import T145.metalchests.lib.ProjectTableType;
import T145.metalchests.tiles.TileProjectTable;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiProjectTable extends GuiContainer implements IRecipeShownListener {

	private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/crafting_table.png");
	private static final ResourceLocation PROJECT_TABLE_GUI_TEXTURES = new ResourceLocation(MetalChests.MODID, "textures/gui/project_table.png");

	private final TileProjectTable table;
	private final EntityPlayer player;
	private final GuiRecipeBook recipeBookGui = new GuiRecipeBook();

	private GuiButtonImage recipeButton;
	private GuiButtonImage chestButton;
	private int recipeButtonY = 60;
	private int chestButtonY = 40;
	private boolean widthTooNarrow;

	public GuiProjectTable(TileProjectTable table, EntityPlayer player) {
		super(new ContainerProjectTable(table, player));
		this.table = table;
		this.player = player;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.widthTooNarrow = this.width < 379;
		this.recipeBookGui.func_194303_a(this.width, this.height, this.mc, this.widthTooNarrow, table.getCrafter());
		this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width, this.xSize);
		this.recipeButton = new GuiButtonImage(10, this.guiLeft + 5, this.height / 2 - recipeButtonY, 20, 18, 0, 168, 19, CRAFTING_TABLE_GUI_TEXTURES);
		this.buttonList.add(this.recipeButton);
		this.chestButton = new GuiButtonImage(11, this.guiLeft + 5, this.height / 2 - chestButtonY, 20, 18, 0, 168, 19, PROJECT_TABLE_GUI_TEXTURES);
		this.buttonList.add(chestButton);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		this.recipeBookGui.tick();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();

		if (this.recipeBookGui.isVisible() && this.widthTooNarrow) {
			this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
			this.recipeBookGui.render(mouseX, mouseY, partialTicks);
		} else {
			this.recipeBookGui.render(mouseX, mouseY, partialTicks);
			super.drawScreen(mouseX, mouseY, partialTicks);
			this.recipeBookGui.renderGhostRecipe(this.guiLeft, this.guiTop, true, partialTicks);
		}

		if (this.chestButton.visible) {
			GlStateManager.color(1F, 1F, 1F, 1F);
			this.chestButton.drawButton(mc, mouseX, mouseY, partialTicks);
			RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.disableLighting();
			mc.getRenderItem().renderItemIntoGUI(table.getChestStack(), chestButton.x + 1, chestButton.y);
			GlStateManager.enableLighting();
			RenderHelper.disableStandardItemLighting();
		}

		this.renderHoveredToolTip(mouseX, mouseY);
		this.recipeBookGui.renderTooltip(this.guiLeft, this.guiTop, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString(I18n.format("container.crafting"), 28, 6, 4210752);
		this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(CRAFTING_TABLE_GUI_TEXTURES);
		int i = this.guiLeft;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY) {
		return (!this.widthTooNarrow || !this.recipeBookGui.isVisible()) && super.isPointInRegion(rectX, rectY, rectWidth, rectHeight, pointX, pointY);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (!this.recipeBookGui.mouseClicked(mouseX, mouseY, mouseButton)) {
			if (!this.widthTooNarrow || !this.recipeBookGui.isVisible()) {
				super.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
	}

	@Override
	protected boolean hasClickedOutside(int p_193983_1_, int p_193983_2_, int p_193983_3_, int p_193983_4_) {
		boolean flag = p_193983_1_ < p_193983_3_ || p_193983_2_ < p_193983_4_ || p_193983_1_ >= p_193983_3_ + this.xSize || p_193983_2_ >= p_193983_4_ + this.ySize;
		return this.recipeBookGui.hasClickedOutside(p_193983_1_, p_193983_2_, this.guiLeft, this.guiTop, this.xSize, this.ySize) && flag;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 10) {
			this.recipeBookGui.initVisuals(this.widthTooNarrow, table.getCrafter());
			this.recipeBookGui.toggleVisibility();
			this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width, this.xSize);
			this.recipeButton.setPosition(this.guiLeft + 5, this.height / 2 - this.recipeButtonY);
			this.chestButton.setPosition(this.guiLeft + 5, this.height / 2 - this.chestButtonY);
		}

		if (button.id == 11) {
			BlockPos pos = table.getPos();
			MetalChests.LOG.info("OPENING!");
			
			if (table.getType() == ProjectTableType.WOOD) {
				
			} else {
				player.openGui(MetalChests.MODID, 2, table.getWorld(), pos.getX(), pos.getY(), pos.getZ());
			}
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (!this.recipeBookGui.keyPressed(typedChar, keyCode)) {
			super.keyTyped(typedChar, keyCode);
		}
	}

	@Override
	protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
		super.handleMouseClick(slotIn, slotId, mouseButton, type);
		this.recipeBookGui.slotClicked(slotIn);
	}

	@Override
	public void recipesUpdated() {
		this.recipeBookGui.recipesUpdated();
	}

	@Override
	public void onGuiClosed() {
		this.recipeBookGui.removed();
		super.onGuiClosed();
	}

	@Override
	public GuiRecipeBook func_194310_f() {
		return this.recipeBookGui;
	}
}