package T145.metalchests.blocks;

import javax.annotation.Nullable;

import T145.metalchests.core.MetalChests;
import T145.metalchests.items.ItemChestUpgrade;
import T145.metalchests.tiles.TileSortingMetalChest;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.network.VanillaPacketHandler;
import net.blay09.mods.refinedrelocation.tile.INameable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSortingMetalChest extends BlockMetalChest {

	public static final String NAME = "sorting_metal_chest";
	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(MetalChests.MOD_ID, "sorting_metal_chest");

	public BlockSortingMetalChest() {
		super(REGISTRY_NAME);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileSortingMetalChest(state.getValue(VARIANT));
	}

	// because blay just couldn't make this `public static`
	protected boolean tryNameBlock(EntityPlayer player, ItemStack heldItem, IBlockAccess world, BlockPos pos) {
		if (!heldItem.isEmpty() && heldItem.getItem() == Items.NAME_TAG && heldItem.hasDisplayName()) {
			TileEntity te = world.getTileEntity(pos);

			if (te instanceof INameable) {
				((INameable) te).setCustomName(heldItem.getDisplayName());
				VanillaPacketHandler.sendTileEntityUpdate(te);

				if (!player.capabilities.isCreativeMode) {
					heldItem.shrink(1);
				}
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			ItemStack stack = player.getHeldItem(hand);

			if (tryNameBlock(player, stack, world, pos)) {
				return true;
			}

			if (player.isSneaking() && !(stack.getItem() instanceof ItemChestUpgrade)) {
				TileEntity te = world.getTileEntity(pos);

				if (te != null) {
					RefinedRelocationAPI.openRootFilterGui(player, te);
				}
			}

			return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
		}

		return true;
	}
}