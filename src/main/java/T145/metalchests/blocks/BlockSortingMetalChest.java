package T145.metalchests.blocks;

import T145.metalchests.core.MetalChests;
import net.minecraft.util.ResourceLocation;

public class BlockSortingMetalChest extends BlockMetalChest {

	public static final String NAME = "sorting_metal_chest";
	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(MetalChests.MOD_ID, "sorting_metal_chest");

	public BlockSortingMetalChest() {
		super(REGISTRY_NAME);
	}
}