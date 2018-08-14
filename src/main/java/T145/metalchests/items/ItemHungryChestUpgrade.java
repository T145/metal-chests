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

import T145.metalchests.core.ModLoader;
import T145.metalchests.tiles.TileHungryMetalChest;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.common.tiles.devices.TileHungryChest;

public class ItemHungryChestUpgrade extends ItemChestUpgrade {

	public static final String NAME = "hungry_chest_upgrade";

	public ItemHungryChestUpgrade() {
		super(NAME);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if (world.isRemote || !player.isSneaking()) {
			return EnumActionResult.PASS;
		}

		ItemStack stack = player.getHeldItem(hand);
		ChestUpgrade upgrade = ChestUpgrade.byMetadata(stack.getItemDamage());
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileHungryMetalChest && ((TileHungryMetalChest) te).getType() == upgrade.getBase()) {
			TileHungryMetalChest chest = (TileHungryMetalChest) te;

			if (chest.numPlayersUsing > 0) {
				return EnumActionResult.PASS;
			}

			upgradeChest(world, pos, ModLoader.HUNGRY_METAL_CHEST, te, new TileHungryMetalChest(upgrade.getUpgrade()), chest.getInventory(), chest.getFront());
		} else if (te instanceof TileHungryChest) {
			TileHungryChest chest = (TileHungryChest) te;

			if (chest.numPlayersUsing > 0) {
				return EnumActionResult.PASS;
			}

			upgradeChest(world, pos, ModLoader.HUNGRY_METAL_CHEST, te, new TileHungryMetalChest(upgrade.getUpgrade()), chest.getSingleChestHandler(), world.getBlockState(pos).getValue(BlockChest.FACING));
		} else {
			return EnumActionResult.PASS;
		}

		if (!player.capabilities.isCreativeMode) {
			stack.shrink(1);
		}

		player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.4F, 0.8F);

		return EnumActionResult.SUCCESS;
	}
}
