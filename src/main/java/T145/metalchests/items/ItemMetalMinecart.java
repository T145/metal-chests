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

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailBase.EnumRailDirection;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMetalMinecart extends ItemMod {

	public static enum MinecartType implements IStringSerializable {

		/*
		 * - COPPER
		 * ~ Base Types
		 */
		COPPER(),
		COPPER_WITH_CHEST(),
		/*
		 * - COPPER
		 * ~ Metal Chest Types
		 */
		COPPER_WITH_BASE_TANK(),
		COPPER_WITH_COPPER_CHEST(),
		COPPER_WITH_IRON_CHEST(),
		COPPER_WITH_SILVER_CHEST(),
		COPPER_WITH_GOLD_CHEST(),
		COPPER_WITH_DIAMOND_CHEST(),
		COPPER_WITH_OBSIDIAN_CHEST(),
		/*
		 * - COPPER
		 * ~ Regular Types
		 */
		COPPER_WITH_WORKBENCH(),
		COPPER_WITH_FURNACE(),
		COPPER_WITH_HOPPER(),
		COPPER_WITH_TNT(),
		/*
		 * - COPPER
		 * ~ Metal Tank Types
		 */
		COPPER_WITH_COPPER_TANK(),
		COPPER_WITH_IRON_TANK(),
		COPPER_WITH_SILVER_TANK(),
		COPPER_WITH_GOLD_TANK(),
		COPPER_WITH_DIAMOND_TANK(),
		COPPER_WITH_OBSIDIAN_TANK(),
		COPPER_WITH_CRYSTAL_TANK(),

		/*
		 * - IRON
		 * ~ Base Types
		 */
		IRON(),
		IRON_WITH_CHEST(),
		/*
		 * - IRON
		 * ~ Metal Chest Types
		 */
		IRON_WITH_COPPER_CHEST(),
		IRON_WITH_IRON_CHEST(),
		IRON_WITH_SILVER_CHEST(),
		IRON_WITH_GOLD_CHEST(),
		IRON_WITH_DIAMOND_CHEST(),
		IRON_WITH_OBSIDIAN_CHEST(),
		IRON_WITH_CRYSTAL_CHEST(),
		/*
		 * - IRON
		 * ~ Regular Types
		 */
		IRON_WITH_WORKBENCH(),
		IRON_WITH_FURNACE(),
		IRON_WITH_HOPPER(),
		IRON_WITH_TNT(),
		/*
		 * - IRON
		 * ~ Metal Tank Types
		 */
		IRON_WITH_BASE_TANK(),
		IRON_WITH_COPPER_TANK(),
		IRON_WITH_IRON_TANK(),
		IRON_WITH_SILVER_TANK(),
		IRON_WITH_GOLD_TANK(),
		IRON_WITH_DIAMOND_TANK(),
		IRON_WITH_OBSIDIAN_TANK(),

		/*
		 * - SILVER
		 * ~ Base Types
		 */
		SILVER(),
		SILVER_WITH_CHEST(),
		/*
		 * - SILVER
		 * ~ Metal Chest Types
		 */
		SILVER_WITH_COPPER_CHEST(),
		SILVER_WITH_IRON_CHEST(),
		SILVER_WITH_SILVER_CHEST(),
		SILVER_WITH_GOLD_CHEST(),
		SILVER_WITH_DIAMOND_CHEST(),
		SILVER_WITH_OBSIDIAN_CHEST(),
		SILVER_WITH_CRYSTAL_CHEST(),
		/*
		 * - SILVER
		 * ~ Regular Types
		 */
		SILVER_WITH_WORKBENCH(),
		SILVER_WITH_FURNACE(),
		SILVER_WITH_HOPPER(),
		SILVER_WITH_TNT(),
		/*
		 * - SILVER
		 * ~ Metal Tank Types
		 */
		SILVER_WITH_BASE_TANK(),
		SILVER_WITH_COPPER_TANK(),
		SILVER_WITH_IRON_TANK(),
		SILVER_WITH_SILVER_TANK(),
		SILVER_WITH_GOLD_TANK(),
		SILVER_WITH_DIAMOND_TANK(),
		SILVER_WITH_OBSIDIAN_TANK(),

		/*
		 * - GOLD
		 * ~ Base Types
		 */
		GOLD(),
		GOLD_WITH_CHEST(),
		/*
		 * - GOLD
		 * ~ Metal Chest Types
		 */
		GOLD_WITH_COPPER_CHEST(),
		GOLD_WITH_IRON_CHEST(),
		GOLD_WITH_SILVER_CHEST(),
		GOLD_WITH_GOLD_CHEST(),
		GOLD_WITH_DIAMOND_CHEST(),
		GOLD_WITH_OBSIDIAN_CHEST(),
		GOLD_WITH_CRYSTAL_CHEST(),
		/*
		 * - GOLD
		 * ~ Regular Types
		 */
		GOLD_WITH_WORKBENCH(),
		GOLD_WITH_FURNACE(),
		GOLD_WITH_HOPPER(),
		GOLD_WITH_TNT(),
		/*
		 * - GOLD
		 * ~ Metal Tank Types
		 */
		GOLD_WITH_BASE_TANK(),
		GOLD_WITH_COPPER_TANK(),
		GOLD_WITH_IRON_TANK(),
		GOLD_WITH_SILVER_TANK(),
		GOLD_WITH_GOLD_TANK(),
		GOLD_WITH_DIAMOND_TANK(),
		GOLD_WITH_OBSIDIAN_TANK(),

		/*
		 * - GOLD
		 * ~ Base Types
		 */
		DIAMOND(),
		DIAMOND_WITH_CHEST(),
		/*
		 * - DIAMOND
		 * ~ Metal Chest Types
		 */
		DIAMOND_WITH_COPPER_CHEST(),
		DIAMOND_WITH_IRON_CHEST(),
		DIAMOND_WITH_SILVER_CHEST(),
		DIAMOND_WITH_GOLD_CHEST(),
		DIAMOND_WITH_DIAMOND_CHEST(),
		DIAMOND_WITH_OBSIDIAN_CHEST(),
		DIAMOND_WITH_CRYSTAL_CHEST(),
		/*
		 * - DIAMOND
		 * ~ Regular Types
		 */
		DIAMOND_WITH_WORKBENCH(),
		DIAMOND_WITH_FURNACE(),
		DIAMOND_WITH_HOPPER(),
		DIAMOND_WITH_TNT(),
		/*
		 * - DIAMOND
		 * ~ Metal Tank Types
		 */
		DIAMOND_WITH_BASE_TANK(),
		DIAMOND_WITH_COPPER_TANK(),
		DIAMOND_WITH_IRON_TANK(),
		DIAMOND_WITH_SILVER_TANK(),
		DIAMOND_WITH_GOLD_TANK(),
		DIAMOND_WITH_DIAMOND_TANK(),
		DIAMOND_WITH_OBSIDIAN_TANK(),

		/*
		 * - GOLD
		 * ~ Base Types
		 */
		OBSIDIAN(),
		OBSIDIAN_WITH_CHEST(),
		/*
		 * - OBSIDIAN
		 * ~ Metal Chest Types
		 */
		OBSIDIAN_WITH_COPPER_CHEST(),
		OBSIDIAN_WITH_IRON_CHEST(),
		OBSIDIAN_WITH_SILVER_CHEST(),
		OBSIDIAN_WITH_GOLD_CHEST(),
		OBSIDIAN_WITH_DIAMOND_CHEST(),
		OBSIDIAN_WITH_OBSIDIAN_CHEST(),
		OBSIDIAN_WITH_CRYSTAL_CHEST(),
		/*
		 * - OBSIDIAN
		 * ~ Regular Types
		 */
		OBSIDIAN_WITH_WORKBENCH(),
		OBSIDIAN_WITH_FURNACE(),
		OBSIDIAN_WITH_HOPPER(),
		OBSIDIAN_WITH_TNT(),
		/*
		 * - OBSIDIAN
		 * ~ Metal Tank Types
		 */
		OBSIDIAN_WITH_BASE_TANK(),
		OBSIDIAN_WITH_COPPER_TANK(),
		OBSIDIAN_WITH_IRON_TANK(),
		OBSIDIAN_WITH_SILVER_TANK(),
		OBSIDIAN_WITH_GOLD_TANK(),
		OBSIDIAN_WITH_DIAMOND_TANK(),
		OBSIDIAN_WITH_OBSIDIAN_TANK();

		@Override
		public String getName() {
			return name().toLowerCase();
		}
	}

	private static final IBehaviorDispenseItem MINECART_DISPENSER_BEHAVIOR = new BehaviorDefaultDispenseItem() {

		private final BehaviorDefaultDispenseItem dispenseBehavior = new BehaviorDefaultDispenseItem();

		private EnumRailDirection getRailDirection(IBlockSource source, BlockPos pos) {
			IBlockState state = source.getBlockState();
			Block block = state.getBlock();

			if (block instanceof BlockRailBase) {
				BlockRailBase rail = (BlockRailBase) block;
				return rail.getRailDirection(source.getWorld(), pos, state, null);
			} else {
				return EnumRailDirection.NORTH_SOUTH;
			}
		}

		@Override
		public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			EnumFacing front = source.getBlockState().getValue(BlockDispenser.FACING);
			BlockPos pos = source.getBlockPos().offset(front);
			World world = source.getWorld();
			IBlockState state = world.getBlockState(pos);
			double yOffset = 0.0D;

			if (BlockRailBase.isRailBlock(state)) {
				yOffset = getRailDirection(source, pos).isAscending() ? 0.6D : 0.1D;
			} else {
				if (state.getMaterial() != Material.AIR || !BlockRailBase.isRailBlock(world.getBlockState(pos.down()))) {
					return dispenseBehavior.dispense(source, stack);
				}

				yOffset -= front != EnumFacing.DOWN && getRailDirection(source, pos.down()).isAscending() ? 0.4D : 0.9D;
			}

			double d0 = source.getX() + front.getFrontOffsetX() * 1.125D;
			yOffset += Math.floor(source.getY()) + front.getFrontOffsetY();
			double d2 = source.getZ() + front.getFrontOffsetZ() * 1.125D;
			//EntityMinecart entityminecart = EntityMinecart.create(world, d0, d1 + yOffset, d2, ((ItemMinecart) stack.getItem()).minecartType);

			if (stack.hasDisplayName()) {
				//entityminecart.setCustomNameTag(stack.getDisplayName());
			}

			//world.spawnEntity(entityminecart);
			stack.shrink(1);
			return stack;
		}

		@Override
		protected void playDispenseSound(IBlockSource source) {
			source.getWorld().playEvent(1000, source.getBlockPos(), 0);
		}
	};

	public ItemMetalMinecart() {
		super("metal_minecart", MinecartType.values());
		this.setMaxStackSize(1);
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, MINECART_DISPENSER_BEHAVIOR);
	}
}
