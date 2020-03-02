package t145.metalchests.client;

import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MetalChestRenderer<T extends TileEntity & IChestLid> extends ChestTileEntityRenderer<T> {

	public MetalChestRenderer(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}
}
