package T145.metalchests.items;

import T145.metalchests.entities.EntityMinecartMetalChest;
import T145.metalchests.items.base.ItemBase;
import T145.metalchests.lib.MetalChestType;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMinecartMetalChest extends ItemBase {

	public ItemMinecartMetalChest() {
		super("minecart_metal_chest", MetalChestType.values());
		this.maxStackSize = 1;
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, MINECART_DISPENSER_BEHAVIOR);
	}

	private static final IBehaviorDispenseItem MINECART_DISPENSER_BEHAVIOR = new BehaviorDefaultDispenseItem() {

		private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();

		@Override
		public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			EnumFacing enumfacing = (EnumFacing) source.getBlockState().getValue(BlockDispenser.FACING);
			World world = source.getWorld();
			double d0 = source.getX() + enumfacing.getFrontOffsetX() * 1.125D;
			double d1 = Math.floor(source.getY()) + enumfacing.getFrontOffsetY();
			double d2 = source.getZ() + enumfacing.getFrontOffsetZ() * 1.125D;
			BlockPos blockpos = source.getBlockPos().offset(enumfacing);
			IBlockState iblockstate = world.getBlockState(blockpos);
			BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getBlock() instanceof BlockRailBase ? ((BlockRailBase) iblockstate.getBlock()).getRailDirection(world, blockpos, iblockstate, null) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
			double d3;

			if (BlockRailBase.isRailBlock(iblockstate)) {
				if (blockrailbase$enumraildirection.isAscending()) {
					d3 = 0.6D;
				} else {
					d3 = 0.1D;
				}
			} else {
				if (iblockstate.getMaterial() != Material.AIR || !BlockRailBase.isRailBlock(world.getBlockState(blockpos.down()))) {
					return this.behaviourDefaultDispenseItem.dispense(source, stack);
				}

				IBlockState iblockstate1 = world.getBlockState(blockpos.down());
				BlockRailBase.EnumRailDirection blockrailbase$enumraildirection1 = iblockstate1.getBlock() instanceof BlockRailBase ? ((BlockRailBase) iblockstate1.getBlock()).getRailDirection(world, blockpos.down(), iblockstate1, null) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;

				if (enumfacing != EnumFacing.DOWN && blockrailbase$enumraildirection1.isAscending()) {
					d3 = -0.4D;
				} else {
					d3 = -0.9D;
				}
			}

			EntityMinecartMetalChest cart = new EntityMinecartMetalChest(world, d0, d1 + d3, d2);
			cart.setChestType(MetalChestType.byMetadata(stack.getItemDamage()));

			if (stack.hasDisplayName()) {
				cart.setCustomNameTag(stack.getDisplayName());
			}

			world.spawnEntity(cart);
			stack.shrink(1);
			return stack;
		}

		@Override
		public void playDispenseSound(IBlockSource source) {
			source.getWorld().playEvent(1000, source.getBlockPos(), 0);
		}
	};

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState iblockstate = world.getBlockState(pos);

		if (!BlockRailBase.isRailBlock(iblockstate)) {
			return EnumActionResult.FAIL;
		} else {
			ItemStack itemstack = player.getHeldItem(hand);

			if (!world.isRemote) {
				BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getBlock() instanceof BlockRailBase ? ((BlockRailBase) iblockstate.getBlock()).getRailDirection(world, pos, iblockstate, null) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
				double d0 = 0.0D;

				if (blockrailbase$enumraildirection.isAscending()) {
					d0 = 0.5D;
				}

				EntityMinecartMetalChest cart = new EntityMinecartMetalChest(world, pos.getX() + 0.5D, pos.getY() + 0.0625D + d0, pos.getZ() + 0.5D);
				cart.setChestType(MetalChestType.byMetadata(itemstack.getItemDamage()));

				if (itemstack.hasDisplayName()) {
					cart.setCustomNameTag(itemstack.getDisplayName());
				}

				world.spawnEntity(cart);
			}

			itemstack.shrink(1);
			return EnumActionResult.SUCCESS;
		}
	}
}