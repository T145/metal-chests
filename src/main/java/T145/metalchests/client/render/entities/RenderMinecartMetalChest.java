package T145.metalchests.client.render.entities;

import T145.metalchests.client.render.blocks.RenderMetalChest;
import T145.metalchests.entities.base.EntityMinecartMetalChestBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMinecartMetalChest extends Render<EntityMinecartMetalChestBase> {

	private static final ResourceLocation MINECART_TEXTURES = new ResourceLocation("textures/entity/minecart.png");
	private final ModelMinecart model = new ModelMinecart();

	public RenderMinecartMetalChest(RenderManager manager) {
		super(manager);
		this.shadowSize = 0.5F;
	}

	@Override
	public void doRender(EntityMinecartMetalChestBase cart, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		bindEntityTexture(cart);
		long i = (long) cart.getEntityId() * 493286711L;
		i = i * i * 4392167121L + i * 98761L;
		float f = (((i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		float f1 = (((i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		float f2 = (((i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		GlStateManager.translate(f, f1, f2);
		double d0 = cart.lastTickPosX + (cart.posX - cart.lastTickPosX) * (double) partialTicks;
		double d1 = cart.lastTickPosY + (cart.posY - cart.lastTickPosY) * (double) partialTicks;
		double d2 = cart.lastTickPosZ + (cart.posZ - cart.lastTickPosZ) * (double) partialTicks;
		double d3 = 0.30000001192092896D;
		Vec3d vec3d = cart.getPos(d0, d1, d2);
		float f3 = cart.prevRotationPitch + (cart.rotationPitch - cart.prevRotationPitch) * partialTicks;

		if (vec3d != null) {
			Vec3d vec3d1 = cart.getPosOffset(d0, d1, d2, 0.30000001192092896D);
			Vec3d vec3d2 = cart.getPosOffset(d0, d1, d2, -0.30000001192092896D);

			if (vec3d1 == null) {
				vec3d1 = vec3d;
			}

			if (vec3d2 == null) {
				vec3d2 = vec3d;
			}

			x += vec3d.x - d0;
			y += (vec3d1.y + vec3d2.y) / 2.0D - d1;
			z += vec3d.z - d2;
			Vec3d vec3d3 = vec3d2.addVector(-vec3d1.x, -vec3d1.y, -vec3d1.z);

			if (vec3d3.lengthVector() != 0.0D) {
				vec3d3 = vec3d3.normalize();
				entityYaw = (float) (Math.atan2(vec3d3.z, vec3d3.x) * 180.0D / Math.PI);
				f3 = (float) (Math.atan(vec3d3.y) * 73.0D);
			}
		}

		GlStateManager.translate(x, y + 0.375F, z);
		GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1F, 0.0F);
		GlStateManager.rotate(-f3, 0.0F, 0.0F, 1F);
		float f5 = cart.getRollingAmplitude() - partialTicks;
		float f6 = cart.getDamage() - partialTicks;

		if (f6 < 0.0F) {
			f6 = 0.0F;
		}

		if (f5 > 0.0F) {
			GlStateManager.rotate(MathHelper.sin(f5) * f5 * f6 / 10.0F * cart.getRollingDirection(), 1F, 0.0F, 0.0F);
		}

		int j = cart.getDisplayTileOffset();

		if (renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(getTeamColor(cart));
		}

		GlStateManager.pushMatrix();
		GlStateManager.scale(0.75F, 0.75F, 0.75F);
		GlStateManager.translate(-0.5F, (j - 8) / 16.0F, -0.5F);
		RenderMetalChest.INSTANCE.render(cart.getChest(), 0, 0, 0, 0, -1, 1);
		GlStateManager.popMatrix();
		GlStateManager.color(1F, 1F, 1F, 1F);
		bindEntityTexture(cart);

		GlStateManager.scale(-1F, -1F, 1F);
		model.render(cart, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();

		if (renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		super.doRender(cart, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMinecartMetalChestBase entity) {
		return MINECART_TEXTURES;
	}
}