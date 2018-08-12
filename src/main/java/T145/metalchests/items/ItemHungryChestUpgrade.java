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

import T145.metalchests.blocks.BlockHungryMetalChest;
import T145.metalchests.blocks.BlockMetalChest.ChestType;
import T145.metalchests.core.ModLoader;
import T145.metalchests.lib.items.ItemMod;
import T145.metalchests.tiles.TileHungryMetalChest;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import thaumcraft.common.tiles.devices.TileHungryChest;

public class ItemHungryChestUpgrade extends ItemMod {

	public enum HungryChestUpgrade implements IStringSerializable {

		WOOD_COPPER(ChestType.COPPER),
		WOOD_IRON(ChestType.IRON),
		WOOD_SILVER(ChestType.SILVER),
		WOOD_GOLD(ChestType.GOLD),
		WOOD_DIAMOND(ChestType.DIAMOND),
		WOOD_OBSIDIAN(ChestType.OBSIDIAN),
		COPPER_IRON(ChestType.COPPER, ChestType.IRON),
		COPPER_SILVER(ChestType.COPPER, ChestType.SILVER),
		COPPER_GOLD(ChestType.COPPER, ChestType.GOLD),
		COPPER_DIAMOND(ChestType.COPPER, ChestType.DIAMOND),
		COPPER_OBSIDIAN(ChestType.COPPER, ChestType.OBSIDIAN),
		IRON_SILVER(ChestType.IRON, ChestType.SILVER),
		IRON_GOLD(ChestType.IRON, ChestType.GOLD),
		IRON_DIAMOND(ChestType.IRON, ChestType.DIAMOND),
		IRON_OBSIDIAN(ChestType.IRON, ChestType.OBSIDIAN),
		SILVER_GOLD(ChestType.SILVER, ChestType.GOLD),
		SILVER_DIAMOND(ChestType.SILVER, ChestType.DIAMOND),
		SILVER_OBSIDIAN(ChestType.SILVER, ChestType.OBSIDIAN),
		GOLD_DIAMOND(ChestType.GOLD, ChestType.DIAMOND),
		GOLD_OBSIDIAN(ChestType.GOLD, ChestType.OBSIDIAN),
		DIAMOND_OBSIDIAN(ChestType.DIAMOND, ChestType.OBSIDIAN);

		@Nullable
		private ChestType base;
		private ChestType upgrade;

		HungryChestUpgrade(ChestType base, ChestType upgrade) {
			this.base = base;
			this.upgrade = upgrade;
		}

		HungryChestUpgrade(ChestType upgrade) {
			this(null, upgrade);
		}

		public ChestType getBase() {
			return base;
		}

		public ChestType getUpgrade() {
			return upgrade;
		}

		public static HungryChestUpgrade byMetadata(int meta) {
			return values()[meta];
		}

		@Override
		public String getName() {
			return name().toLowerCase();
		}
	}

	public static final String NAME = "hungry_chest_upgrade";

	public ItemHungryChestUpgrade() {
		super(NAME, HungryChestUpgrade.values());
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if (world.isRemote || !player.isSneaking()) {
			return EnumActionResult.PASS;
		}

		ItemStack stack = player.getHeldItem(hand);
		HungryChestUpgrade upgrade = HungryChestUpgrade.byMetadata(stack.getItemDamage());
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileHungryMetalChest && ((TileHungryMetalChest) te).getType() == upgrade.getBase()) {
			TileHungryMetalChest chest = (TileHungryMetalChest) te;

			if (chest.numPlayersUsing > 0) {
				return EnumActionResult.PASS;
			}

			upgradeChest(world, pos, te, new TileHungryMetalChest(upgrade.getUpgrade()), chest.getInventory(), chest.getFront());
		} else if (te instanceof TileHungryChest) {
			TileHungryChest chest = (TileHungryChest) te;

			if (chest.numPlayersUsing > 0) {
				return EnumActionResult.PASS;
			}

			upgradeChest(world, pos, te, new TileHungryMetalChest(upgrade.getUpgrade()), chest.getSingleChestHandler(), world.getBlockState(pos).getValue(BlockChest.FACING));
		} else {
			return EnumActionResult.PASS;
		}

		if (!player.capabilities.isCreativeMode) {
			stack.shrink(1);
		}

		player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.4F, 0.8F);

		return EnumActionResult.SUCCESS;
	}

	private void upgradeChest(World world, BlockPos pos, TileEntity te, TileHungryMetalChest newChest, IItemHandler inventory, EnumFacing front) {
		te.updateContainingBlockInfo();

		if (te instanceof TileHungryChest) {
			((TileHungryChest) te).checkForAdjacentChests();
		}

		world.removeTileEntity(pos);
		world.setBlockToAir(pos);
		world.setTileEntity(pos, newChest);

		IBlockState state = ModLoader.HUNGRY_METAL_CHEST.getDefaultState().withProperty(BlockHungryMetalChest.VARIANT, newChest.getType());
		world.setBlockState(pos, state, 3);
		world.notifyBlockUpdate(pos, state, state, 3);

		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof TileHungryMetalChest) {
			TileHungryMetalChest chest = (TileHungryMetalChest) tile;
			chest.setInventory(inventory);
			chest.setFront(front);
		}
	}
}
