/*******************************************************************************
 * Copyright 2018 T145
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

import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.blocks.BlockMetalChest.ChestType;
import T145.metalchests.core.ModLoader;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public class ItemMetalUpgrade extends ItemMod {

	public enum UpgradeType implements IStringSerializable {

		CHEST_WOOD_COPPER(ChestType.COPPER), CHEST_WOOD_IRON(ChestType.IRON), CHEST_WOOD_SILVER(ChestType.SILVER),
		CHEST_WOOD_GOLD(ChestType.GOLD), CHEST_WOOD_DIAMOND(ChestType.DIAMOND), CHEST_WOOD_OBSIDIAN(ChestType.OBSIDIAN),
		CHEST_COPPER_IRON(ChestType.COPPER, ChestType.IRON), CHEST_COPPER_SILVER(ChestType.COPPER, ChestType.SILVER),
		CHEST_COPPER_GOLD(ChestType.COPPER, ChestType.GOLD), CHEST_COPPER_DIAMOND(ChestType.COPPER, ChestType.DIAMOND),
		CHEST_COPPER_OBSIDIAN(ChestType.COPPER, ChestType.OBSIDIAN),
		CHEST_IRON_SILVER(ChestType.IRON, ChestType.SILVER), CHEST_IRON_GOLD(ChestType.IRON, ChestType.GOLD),
		CHEST_IRON_DIAMOND(ChestType.IRON, ChestType.DIAMOND), CHEST_IRON_OBSIDIAN(ChestType.IRON, ChestType.OBSIDIAN),
		CHEST_SILVER_GOLD(ChestType.SILVER, ChestType.GOLD), CHEST_SILVER_DIAMOND(ChestType.SILVER, ChestType.DIAMOND),
		CHEST_SILVER_OBSIDIAN(ChestType.SILVER, ChestType.OBSIDIAN),
		CHEST_GOLD_DIAMOND(ChestType.GOLD, ChestType.DIAMOND), CHEST_GOLD_OBSIDIAN(ChestType.GOLD, ChestType.OBSIDIAN),
		CHEST_DIAMOND_OBSIDIAN(ChestType.DIAMOND, ChestType.OBSIDIAN);

		@Nullable
		private ChestType chestBase;
		private ChestType chestUpgrade;

		UpgradeType(ChestType chestBase, ChestType chestUpgrade) {
			this.chestBase = chestBase;
			this.chestUpgrade = chestUpgrade;
		}

		UpgradeType(ChestType chestUpgrade) {
			this(null, chestUpgrade);
		}

		public ChestType getBase() {
			return chestBase;
		}

		public ChestType getUpgrade() {
			return chestUpgrade;
		}

		public boolean isUpgrade() {
			return ordinal() <= CHEST_WOOD_OBSIDIAN.ordinal();
		}

		public static UpgradeType byMetadata(int meta) {
			return values()[meta];
		}

		@Override
		public String getName() {
			return name().toLowerCase();
		}
	}

	public static final String NAME = "metal_upgrade";

	public ItemMetalUpgrade() {
		super(NAME, UpgradeType.values());
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if (world.isRemote || !player.isSneaking()) {
			return EnumActionResult.PASS;
		}

		ItemStack stack = player.getHeldItem(hand);
		UpgradeType upgrade = UpgradeType.byMetadata(stack.getItemDamage());
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileMetalChest && ((TileMetalChest) te).getType() == upgrade.getBase()) {
			TileMetalChest chest = (TileMetalChest) te;

			if (chest.numPlayersUsing > 0) {
				return EnumActionResult.PASS;
			}

			upgradeChest(world, pos, te, new TileMetalChest(upgrade.getUpgrade()), chest.getInventory(), chest.getFront());
		} else if (te instanceof TileEntityChest) {
			TileEntityChest chest = (TileEntityChest) te;

			if (chest.numPlayersUsing > 0) {
				return EnumActionResult.PASS;
			}

			upgradeChest(world, pos, te, new TileMetalChest(upgrade.getUpgrade()), chest.getSingleChestHandler(), world.getBlockState(pos).getValue(BlockChest.FACING));
		} else {
			return EnumActionResult.PASS;
		}

		if (!player.capabilities.isCreativeMode) {
			stack.shrink(1);
		}

		player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.4F, 0.8F);

		return EnumActionResult.SUCCESS;
	}

	private void upgradeChest(World world, BlockPos pos, TileEntity te, TileMetalChest newChest, IItemHandler inventory, EnumFacing front) {
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

		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof TileMetalChest) {
			TileMetalChest chest = (TileMetalChest) tile;
			chest.setInventory(inventory);
			chest.setFront(front);
		}
	}
}
