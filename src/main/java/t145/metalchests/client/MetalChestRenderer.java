package t145.metalchests.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import t145.metalchests.api.Reference;
import t145.metalchests.blocks.MetalChestBlock;
import t145.metalchests.tiles.MetalChestTile;

@OnlyIn(Dist.CLIENT)
public class MetalChestRenderer<T extends TileEntity & IChestLid> extends ChestTileEntityRenderer<T> {

	public MetalChestRenderer(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	protected Material getMaterial(T chest, ChestType chestType) {
		if (chest instanceof MetalChestTile) {
			Block block = ForgeRegistries.BLOCKS.getValue(chest.getType().getRegistryName());
			BlockState state = chest.hasWorld() ? chest.getBlockState() : block.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);

			return new Material(Atlases.CHEST_ATLAS, Reference.METAL_CHEST_MODELS[state.get(MetalChestBlock.METAL_TYPE).ordinal()]);
		}
		return super.getMaterial(chest, ChestType.SINGLE);
	}
}
