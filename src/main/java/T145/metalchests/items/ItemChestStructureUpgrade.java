package T145.metalchests.items;

import T145.metalchests.MetalChests;
import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.core.ModLoader;
import T145.metalchests.items.base.ItemBase;
import T145.metalchests.lib.MetalChestType;
import T145.metalchests.lib.MetalChestType.StructureUpgrade;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemChestStructureUpgrade extends ItemBase {

	public ItemChestStructureUpgrade() {
		super("structure_upgrade", MetalChestType.StructureUpgrade.values());
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if (world.isRemote || !player.isSneaking()) {
			return EnumActionResult.PASS;
		}

		ItemStack stack = player.getHeldItem(hand);
		TileEntity te = world.getTileEntity(pos);
		IBlockState state = world.getBlockState(pos);
		MetalChestType.StructureUpgrade upgrade = MetalChestType.StructureUpgrade.byMetadata(stack.getItemDamage());

		if (te instanceof TileMetalChest && ((TileMetalChest) te).getType() == upgrade.getBase()) {
			TileMetalChest chest = (TileMetalChest) te;

			if (chest.numPlayersUsing > 0) {
				return EnumActionResult.PASS;
			}

			NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(chest.getType().getInventorySize(), ItemStack.EMPTY);

			for (int i = 0; i < inventory.size(); i++) {
				inventory.set(i, chest.getInventory().getStackInSlot(i));
			}

			createNewChest(world, pos, te, new TileMetalChest(upgrade.getUpgrade()), inventory, chest.getFront());
		} else if (te instanceof TileEntityChest && state.getBlock() instanceof BlockChest) {
			TileEntityChest chest = (TileEntityChest) te;

			if (chest.numPlayersUsing > 0 || !upgrade.canUpgradeWood(state)) {
				return EnumActionResult.PASS;
			}

			NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(chest.getSizeInventory(), ItemStack.EMPTY);

			for (int i = 0; i < inventory.size(); i++) {
				inventory.set(i, chest.getStackInSlot(i));
			}

			createNewChest(world, pos, te, new TileMetalChest(upgrade.getUpgrade()), inventory, state.getValue(BlockChest.FACING));
		} else {
			return EnumActionResult.PASS;
		}

		if (!player.capabilities.isCreativeMode) {
			stack.shrink(1);
		}
		player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.4F, 0.8F);

		return EnumActionResult.SUCCESS;
	}

	private void createNewChest(World world, BlockPos pos, TileEntity te, TileMetalChest newChest, NonNullList<ItemStack> inventory, EnumFacing front) {
		te.updateContainingBlockInfo();

		if (te instanceof TileEntityChest) {
			((TileEntityChest) te).checkForAdjacentChests();
		}

		world.removeTileEntity(pos);
		world.setBlockToAir(pos);
		world.setTileEntity(pos, newChest);

		IBlockState newState = ModLoader.METAL_CHEST.getDefaultState().withProperty(BlockMetalChest.VARIANT, newChest.getType());
		world.setBlockState(pos, newState, 3);
		world.notifyBlockUpdate(pos, newState, newState, 3);

		TileEntity te2 = world.getTileEntity(pos);

		if (te2 instanceof TileMetalChest) {
			TileMetalChest chest = (TileMetalChest) te2;
			chest.setInventory(inventory);
			chest.setFront(front);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (tab == MetalChests.TAB) {
			for (int meta = 0; meta < getItemTypes().length; ++meta) {
				StructureUpgrade upgrade = StructureUpgrade.byMetadata(meta);

				if (upgrade.isRegistered()) {
					items.add(new ItemStack(this, 1, meta));
				}
			}
		}
	}
}