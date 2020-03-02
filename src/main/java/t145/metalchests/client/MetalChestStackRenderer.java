package t145.metalchests.client;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import t145.metalchests.api.constants.MetalChestType;
import t145.metalchests.blocks.MetalChestBlock;

@OnlyIn(Dist.CLIENT)
public class MetalChestStackRenderer extends ItemStackTileEntityRenderer {

	@Override
	public void render(ItemStack stack, MatrixStack matrix, IRenderTypeBuffer buf, int light, int overlay) {
		Item item = stack.getItem();
		Block block = Block.getBlockFromItem(item);

		if (block instanceof MetalChestBlock) {
			BlockState state = block.getDefaultState();
			MetalChestType type = state.get(MetalChestBlock.METAL_TYPE);

			if (type != null) {
				TileEntityRendererDispatcher.instance.renderItem(state.createTileEntity(null), matrix, buf, light, overlay);
			}
		} else {
			super.render(stack, matrix, buf, light, overlay);
		}
	}
}