package t145.metalchests.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import t145.metalchests.client.MetalChestStackRenderer;

public class MetalChestItem extends BlockItem {

	public MetalChestItem(Block block) {
		super(block, new Properties().group(ItemGroup.DECORATIONS).setISTER(() -> MetalChestStackRenderer::new));
	}
}
