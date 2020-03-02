package t145.metalchests.client;

import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import t145.metalchests.tiles.MetalChestTile;

@OnlyIn(Dist.CLIENT)
public class MetalChestRenderer<T extends TileEntity & IChestLid> extends ChestTileEntityRenderer<T> {

	public MetalChestRenderer(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	protected Material getMaterial(T chest, ChestType chestType) {
		if (chest instanceof MetalChestTile) {
			return new Material(Atlases.CHEST_ATLAS, new ResourceLocation("metalchests:textures/entity/copper"));
		}
		return super.getMaterial(chest, ChestType.SINGLE);
	}
}
