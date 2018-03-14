package T145.metalchests.client.render.blocks;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.google.common.primitives.SignedBytes;

import T145.metalchests.MetalChests;
import T145.metalchests.lib.MetalChestType;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMetalChest extends TileEntitySpecialRenderer<TileMetalChest> {

	private final ModelChest model = new ModelChest();
	private final Random rand = new Random();
	private RenderEntityItem itemRenderer;

	private static EntityItem hovering = new EntityItem(null);
	private static float[][] shifts = {
			{ 0.3F, 0.45F, 0.3F }, { 0.7F, 0.45F, 0.3F }, { 0.3F, 0.45F, 0.7F },
			{ 0.7F, 0.45F, 0.7F }, { 0.3F, 0.1F,  0.3F }, { 0.7F, 0.1F,  0.3F },
			{ 0.3F, 0.1F,  0.7F }, { 0.7F, 0.1F,  0.7F }, { 0.5F, 0.32F, 0.5F }
	};

	@Override
	public void render(TileMetalChest chest, double x, double y, double z, float partialTicks, int destroyStage, float partial) {
		if (chest == null || chest.isInvalid()) {
			return;
		}

		if (destroyStage >= 0) {
			bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(GL11.GL_TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4F, 4F, 1F);
			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		} else {
			bindTexture(new ResourceLocation(MetalChests.MODID, "textures/entity/chest/" + chest.getType().getName() + ".png"));
		}

		GlStateManager.pushMatrix();

		if (chest.getType() == MetalChestType.CRYSTAL) {
			GlStateManager.disableCull();
		}

		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.translate(x, y + 1F, z + 1F);
		GlStateManager.scale(1F, -1F, -1F);
		GlStateManager.translate(0.5F, 0.5F, 0.5F);
		GlStateManager.rotate(getRotationAngle(chest.getFront()), 0F, 1F, 0F);
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);

		if (chest.getType() == MetalChestType.CRYSTAL) {
			GlStateManager.scale(1F, 0.99F, 1F);
		}

		float f = chest.prevLidAngle + (chest.lidAngle - chest.prevLidAngle) * partialTicks;
		f = 1.0F - f;
		f = 1.0F - f * f * f;
		model.chestLid.rotateAngleX = -(f * ((float) Math.PI / 2F));
		model.renderAll();

		if (destroyStage >= 0) {
			GlStateManager.matrixMode(GL11.GL_TEXTURE);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		}

		if (chest.getType() == MetalChestType.CRYSTAL) {
			GlStateManager.enableCull();
		}

		GlStateManager.popMatrix();
		GlStateManager.color(1F, 1F, 1F, 1F);

		if (chest.getType() == MetalChestType.CRYSTAL && chest.getDistanceSq(rendererDispatcher.entityX, rendererDispatcher.entityY, rendererDispatcher.entityZ) < 128d) {
			rand.setSeed(254L);

			float shiftX;
			float shiftY;
			float shiftZ;
			int shift = 0;
			float blockScale = 0.70F;
			float timeD = (float) (360D * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL) - partialTicks;

			if (chest.getTopStacks().getStackInSlot(1).isEmpty()) {
				shift = 8;
				blockScale = 0.85F;
			}

			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);

			hovering.setWorld(getWorld());
			hovering.hoverStart = 0F;

			for (ItemStack stack : chest.getTopStacks().getStacks()) {
				if (shift > shifts.length) {
					break;
				}

				if (stack.isEmpty()) {
					shift++;
					continue;
				}

				shiftX = shifts[shift][0];
				shiftY = shifts[shift][1];
				shiftZ = shifts[shift][2];
				shift++;

				GlStateManager.pushMatrix();
				GlStateManager.translate(shiftX, shiftY, shiftZ);
				GlStateManager.rotate(timeD, 0F, 1F, 0F);
				GlStateManager.scale(blockScale, blockScale, blockScale);

				hovering.setItem(stack);

				if (itemRenderer == null) {
					itemRenderer = new RenderEntityItem(Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().getRenderItem()) {

						@Override
						public int getModelCount(ItemStack stack) {
							return SignedBytes.saturatedCast(Math.min(stack.getCount() / 32, 15) + 1);
						}

						@Override
						public boolean shouldBob() {
							return false;
						}

						@Override
						public boolean shouldSpreadItems() {
							return true;
						}
					};
				}

				itemRenderer.doRender(hovering, 0D, 0D, 0D, 0F, partialTicks);
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();
		}
	}

	private int getRotationAngle(EnumFacing front) {
		switch (front) {
		case NORTH:
			return 180;
		case WEST:
			return 90;
		case EAST:
			return -90;
		default:
			return 0;
		}
	}
}