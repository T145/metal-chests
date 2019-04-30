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

import javax.annotation.Nullable;

import T145.metalchests.api.chests.IMetalChest;
import T145.metalchests.api.chests.UpgradeRegistry;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.ChestUpgrade;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
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
import net.minecraftforge.items.ItemStackHandler;

public class ItemChestUpgrade extends ItemMod {

	public ItemChestUpgrade(ResourceLocation registryName) {
		super(registryName, ChestUpgrade.values());
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if (world.isRemote || !player.isSneaking()) {
			return EnumActionResult.PASS;
		}

		TileEntity te = world.getTileEntity(pos);
		ItemStack stack = player.getHeldItem(hand);
		ChestUpgrade upgrade = ChestUpgrade.byMetadata(stack.getItemDamage());

		if (te instanceof IMetalChest) {
			IMetalChest chest = (IMetalChest) te;

			if (chest.getChestType() == upgrade.getBase()) {
				ItemStackHandler oldInv = (ItemStackHandler) chest.getInventory();

				chest.setChestType(upgrade.getUpgrade());
				te.markDirty();

				IBlockState state = createBlockState(te.getBlockType(), upgrade.getUpgrade());
				world.setBlockState(pos, state, 3);
				world.notifyBlockUpdate(pos, state, state, 3);

				chest.setInventory(oldInv);
			} else {
				return EnumActionResult.FAIL;
			}
		} else if (UpgradeRegistry.hasChest(te.getBlockType())) {
			te.updateContainingBlockInfo();

			EnumFacing front = getFrontFromProperties(world, pos);
			IItemHandler inv = getChestInventory(te);

			if (te instanceof TileEntityChest) {
				TileEntityChest vanillaChest = (TileEntityChest) te;

				if (vanillaChest.getChestType() == BlockChest.Type.TRAP) {
					return EnumActionResult.FAIL;
				}

				vanillaChest.checkForAdjacentChests();
			}

			Block block = te.getBlockType();

			world.removeTileEntity(pos);
			world.setBlockToAir(pos);

			IBlockState state = createBlockState(block, upgrade.getUpgrade());

			world.setTileEntity(pos, block.createTileEntity(world, state));
			world.setBlockState(pos, state, 3);
			world.notifyBlockUpdate(pos, state, state, 3);

			TileEntity tile = world.getTileEntity(pos);

			if (tile instanceof IMetalChest) {
				IMetalChest metalChest = (IMetalChest) tile;
				metalChest.setChestType(upgrade.getUpgrade());
				metalChest.setInventory(inv);
				metalChest.setFront(front);
				tile.markDirty();
			}
		} else {
			return EnumActionResult.PASS;
		}

		if (!player.capabilities.isCreativeMode) {
			stack.shrink(1);
		}

		player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.4F, 0.8F);

		return EnumActionResult.SUCCESS;
	}

	private IItemHandler getChestInventory(TileEntity te) {
		if (te instanceof TileEntityChest) {
			return ((TileEntityChest) te).getSingleChestHandler();
		} else {
			return te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		}
	}

	private IBlockState createBlockState(Block upgradeBlock, ChestType upgrade) {
		return upgradeBlock.getDefaultState().withProperty(IMetalChest.VARIANT, upgrade);
	}

	@Nullable
	private EnumFacing getFrontFromProperties(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);

		for (IProperty<?> prop : state.getProperties().keySet()) {
			if ((prop.getName().equals("facing") || prop.getName().equals("rotation")) && prop.getValueClass() == EnumFacing.class) {
				IProperty<EnumFacing> facingProperty = (IProperty<EnumFacing>) prop;
				return state.getValue(facingProperty);
			}
		}

		return null;
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
