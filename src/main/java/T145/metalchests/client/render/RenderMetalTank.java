package T145.metalchests.client.render;

import T145.metalchests.tiles.TileMetalChest;
import T145.metalchests.tiles.TileMetalTank;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMetalTank extends TileEntitySpecialRenderer<TileMetalTank> {
	
	@Override
	public void render(TileMetalTank tank, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		
	}
}