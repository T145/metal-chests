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

	// last two parameters are a guess
	@Override
	public void func_228364_a_(ItemStack stack, MatrixStack matrix, IRenderTypeBuffer buf, int partialTicks, int alpha) {
		Item item = stack.getItem();
		Block block = Block.getBlockFromItem(item);

		if (block instanceof MetalChestBlock) {
			BlockState state = block.getDefaultState();
			MetalChestType type = state.get(MetalChestBlock.METAL_TYPE);

			if (type != null) {
				TileEntityRendererDispatcher.instance.func_228852_a_(state.createTileEntity(null), matrix, buf, partialTicks, alpha);
			}
		} else {
			super.func_228364_a_(stack, matrix, buf, partialTicks, alpha);
		}
	}
}