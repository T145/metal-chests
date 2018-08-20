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
package T145.metalchests.tiles;

import T145.metalchests.api.BlocksMetalChests;
import T145.metalchests.api.ItemsMetalChests;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.ChestUpgrade;
import T145.metalchests.blocks.BlockMetalChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;

public class TileHungryMetalChest extends TileMetalChest {

	public TileHungryMetalChest(ChestType chestType) {
		super(chestType);
	}

	public TileHungryMetalChest() {
		super();
	}

	public static void registerFixes(DataFixer fixer) {
		fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileHungryMetalChest.class, new String[] { "Items" }));
	}

	@Override
	public boolean canApplyUpgrade(ChestUpgrade upgrade, TileEntity chest, ItemStack upgradeStack) {
		return upgrade.getBase() == chestType && chest instanceof TileHungryMetalChest && upgradeStack.getItem().getRegistryName().equals(ItemsMetalChests.HUNGRY_CHEST_UPGRADE.getRegistryName());
	}

	@Override
	public IBlockState createBlockState(ChestType chestType) {
		return BlocksMetalChests.HUNGRY_METAL_CHEST.getDefaultState().withProperty(BlockMetalChest.VARIANT, chestType);
	}

	@Override
	public TileEntity createTileEntity(ChestType chestType) {
		return new TileHungryMetalChest(chestType);
	}

	@Override
	public boolean receiveClientEvent(int id, int data) {
		switch (id) {
		case 2:
			if (lidAngle < data / 10F) {
				lidAngle = data / 10F;
			}
			return true;
		default:
			return super.receiveClientEvent(id, data);
		}
	}

	@Override
	public String getTranslationKey() {
		return "tile.metalchests:hungry_metal_chest." + chestType.getName() + ".name";
	}
}
