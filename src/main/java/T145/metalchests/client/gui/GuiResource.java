package t145.metalchests.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import t145.metalchests.api.consts.RegistryMC;

@SideOnly(Side.CLIENT)
public class GuiResource extends ResourceLocation {

	public final int width;
	public final int height;

	public GuiResource(String location, int width, int height) {
		super(RegistryMC.ID, String.format("textures/gui/%s.png", location));
		this.width = width;
		this.height = height;
	}

	public GuiResource(String location, int bound) {
		this(location, bound, bound);
	}

	public void bind() {
		Minecraft.getMinecraft().getTextureManager().bindTexture(this);
	}

	/** Draws part of the texture to the screen. */
	public void drawQuad(int x, int y, int u, int v, int w, int h, float zLevel) {
		float scaleX = 1.0F / width;
		float scaleY = 1.0F / height;
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder vb = tess.getBuffer();
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);
		vb.pos(x + 0, y + h, zLevel).tex((u + 0) * scaleX, (v + h) * scaleY).endVertex();
		vb.pos(x + w, y + h, zLevel).tex((u + w) * scaleX, (v + h) * scaleY).endVertex();
		vb.pos(x + w, y + 0, zLevel).tex((u + w) * scaleX, (v + 0) * scaleY).endVertex();
		vb.pos(x + 0, y + 0, zLevel).tex((u + 0) * scaleX, (v + 0) * scaleY).endVertex();
		tess.draw();
	}

	/** Draws part of the texture to the screen. */
	public void drawQuad(int x, int y, int u, int v, int w, int h) {
		drawQuad(x, y, u, v, w, h, 0);
	}
}