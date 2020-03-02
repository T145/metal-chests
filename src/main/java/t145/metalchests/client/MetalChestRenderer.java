package t145.metalchests.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import t145.metalchests.api.Reference;
import t145.metalchests.blocks.MetalChestBlock;
import t145.metalchests.tiles.MetalChestTile;

@OnlyIn(Dist.CLIENT)
public class MetalChestRenderer extends ChestTileEntityRenderer<MetalChestTile> {

	public MetalChestRenderer(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	protected Material getMaterial(MetalChestTile chest, ChestType chestType) {
		if (chest instanceof MetalChestTile) {
			TileEntityType<?> tileType = chest.getType();
			ResourceLocation tileResource = tileType.getRegistryName();
			Block invBlock = ForgeRegistries.BLOCKS.getValue(tileResource);
			BlockState invState = chest.hasWorld() ? chest.getBlockState() : invBlock.getDefaultState().with(MetalChestBlock.FACING, Direction.SOUTH);

			return new Material(Atlases.CHEST_ATLAS, Reference.METAL_CHEST_MODELS[invState.get(MetalChestBlock.METAL_TYPE).ordinal()]);
		}
		return super.getMaterial(chest, ChestType.SINGLE);
	}
}
