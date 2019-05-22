/*******************************************************************************
 * Copyright 2019 T145
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package T145.metalchests.items;

import T145.metalchests.api.chests.IMetalChest;
import T145.metalchests.api.chests.UpgradeRegistry;
import T145.metalchests.api.constants.ChestType;
import T145.metalchests.api.constants.ChestUpgrade;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ItemChestUpgrade extends ItemMod {

	public ItemChestUpgrade(ResourceLocation registryName) {
		super(registryName, ChestUpgrade.values());
		setMaxStackSize(1);
	}

	private boolean canUpdateChest(TileEntity te, ChestUpgrade upgradeType) {
		if (te instanceof IMetalChest) {
			IMetalChest chest = (IMetalChest) te;

			if (chest.getChestType() != upgradeType.getBase() || chest.getChestAnimator().isOpen()) {
				return false;
			}

			return true;
		} else if (UpgradeRegistry.hasChest(te.getBlockType())) {
			te.updateContainingBlockInfo();

			if (te instanceof TileEntityChest) {
				TileEntityChest vanillaChest = (TileEntityChest) te;

				if (vanillaChest.lidAngle > 0) {
					return false;
				}
			}

			if (te instanceof TileEntityEnderChest) {
				TileEntityEnderChest enderChest = (TileEntityEnderChest) te;

				if (enderChest.lidAngle > 0) {
					return false;
				}
			}

			return true;
		}

		return false;
	}

	private IBlockState createBlockState(Block upgradeBlock, ChestType upgrade) {
		return upgradeBlock.getDefaultState().withProperty(IMetalChest.VARIANT, upgrade);
	}

	private EnumFacing getBlockFront(EntityPlayer player, World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);

		for (IProperty<?> prop : state.getProperties().keySet()) {
			if ((prop.getName().equals("facing") || prop.getName().equals("rotation")) && prop.getValueClass() == EnumFacing.class) {
				IProperty<EnumFacing> facingProperty = (IProperty<EnumFacing>) prop;
				return state.getValue(facingProperty);
			}
		}

		return player.getHorizontalFacing().getOpposite();
	}

	private IItemHandler getChestInventory(TileEntity te) {
		if (te instanceof TileEntityChest) {
			return ((TileEntityChest) te).getSingleChestHandler();
		} else {
			return te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		}
	}

	private boolean updateChest(ChestType upgrade, TileEntity te, EntityPlayer player, World world, BlockPos pos) {
		Block block = te.getBlockType();

		if (te instanceof IMetalChest) {
			IMetalChest chest = (IMetalChest) te;
			chest.setChestType(upgrade);
			world.setBlockState(pos, createBlockState(block, upgrade), 3); // mark for NBT update
			te.markDirty(); // mark for render update
			return true;
		} else {
			boolean trapped = block.canProvidePower(world.getBlockState(te.getPos()));
			EnumFacing front = getBlockFront(player, world, pos);
			IItemHandler inv = getChestInventory(te);
			block = UpgradeRegistry.getDestTile(block);
			IBlockState state = createBlockState(block, upgrade);

			world.removeTileEntity(pos);
			world.setBlockToAir(pos);
			world.setTileEntity(pos, block.createTileEntity(world, state));
			world.setBlockState(pos, state, 3); // mark for NBT update

			te = world.getTileEntity(pos);

			if (te instanceof IMetalChest) {
				IMetalChest chest = (IMetalChest) te;
				chest.setChestType(upgrade);
				chest.setTrapped(trapped);
				chest.setFront(front);
				chest.setInventory(inv);
				te.markDirty(); // mark for render update
				return true;
			}

			return false;
		}
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if (world.isRemote || player.isSneaking()) {
			return EnumActionResult.PASS;
		}

		TileEntity te = world.getTileEntity(pos);
		ItemStack stack = player.getHeldItem(hand);
		ChestUpgrade upgrade = ChestUpgrade.byMetadata(stack.getItemDamage());

		if (!canUpdateChest(te, upgrade) || !updateChest(upgrade.getUpgrade(), te, player, world, pos)) {
			return EnumActionResult.FAIL;
		}

		if (!player.capabilities.isCreativeMode) {
			stack.shrink(1);
		}

		player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.4F, 0.8F);

		return EnumActionResult.SUCCESS;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void prepareCreativeTab(NonNullList<ItemStack> items) {
		for (ChestUpgrade upgrade : ChestUpgrade.values()) {
			if (upgrade.isRegistered()) {
				items.add(new ItemStack(this, 1, upgrade.ordinal()));
			}
		}
	}
}
