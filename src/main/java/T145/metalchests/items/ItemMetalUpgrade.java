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

import T145.metalchests.blocks.BlockMetalChest.ChestType;
import T145.metalchests.blocks.BlockMetalTank.TankType;
import net.minecraft.util.IStringSerializable;

public class ItemMetalUpgrade extends ItemMod {

	public static enum UpgradeType implements IStringSerializable {

		CHEST_WOOD_COPPER(ChestType.COPPER),
		CHEST_WOOD_IRON(ChestType.IRON),
		CHEST_WOOD_SILVER(ChestType.SILVER),
		CHEST_WOOD_GOLD(ChestType.GOLD),
		CHEST_WOOD_DIAMOND(ChestType.DIAMOND),
		CHEST_WOOD_OBSIDIAN(ChestType.OBSIDIAN),
		CHEST_COPPER_IRON(ChestType.COPPER, ChestType.IRON),
		CHEST_COPPER_SILVER(ChestType.COPPER, ChestType.SILVER),
		CHEST_COPPER_GOLD(ChestType.COPPER, ChestType.GOLD),
		CHEST_COPPER_DIAMOND(ChestType.COPPER, ChestType.DIAMOND),
		CHEST_COPPER_OBSIDIAN(ChestType.COPPER, ChestType.OBSIDIAN),
		CHEST_IRON_SILVER(ChestType.IRON, ChestType.SILVER),
		CHEST_IRON_GOLD(ChestType.IRON, ChestType.GOLD),
		CHEST_IRON_DIAMOND(ChestType.IRON, ChestType.DIAMOND),
		CHEST_IRON_OBSIDIAN(ChestType.IRON, ChestType.OBSIDIAN),
		CHEST_SILVER_GOLD(ChestType.SILVER, ChestType.GOLD),
		CHEST_SILVER_DIAMOND(ChestType.SILVER, ChestType.DIAMOND),
		CHEST_SILVER_OBSIDIAN(ChestType.SILVER, ChestType.OBSIDIAN),
		CHEST_GOLD_DIAMOND(ChestType.GOLD, ChestType.DIAMOND),
		CHEST_GOLD_OBSIDIAN(ChestType.GOLD, ChestType.OBSIDIAN),
		CHEST_DIAMOND_OBSIDIAN(ChestType.DIAMOND, ChestType.OBSIDIAN),

		TANK_BASE_COPPER(TankType.COPPER),
		TANK_BASE_IRON(TankType.IRON),
		TANK_BASE_SILVER(TankType.SILVER),
		TANK_BASE_GOLD(TankType.GOLD),
		TANK_BASE_DIAMOND(TankType.DIAMOND),
		TANK_BASE_OBSIDIAN(TankType.OBSIDIAN),
		TANK_COPPER_IRON(TankType.COPPER, TankType.IRON),
		TANK_COPPER_SILVER(TankType.COPPER, TankType.SILVER),
		TANK_COPPER_GOLD(TankType.COPPER, TankType.GOLD),
		TANK_COPPER_DIAMOND(TankType.COPPER, TankType.DIAMOND),
		TANK_COPPER_OBSIDIAN(TankType.COPPER, TankType.OBSIDIAN),
		TANK_IRON_SILVER(TankType.IRON, TankType.SILVER),
		TANK_IRON_GOLD(TankType.IRON, TankType.GOLD),
		TANK_IRON_DIAMOND(TankType.IRON, TankType.DIAMOND),
		TANK_IRON_OBSIDIAN(TankType.IRON, TankType.OBSIDIAN),
		TANK_SILVER_GOLD(TankType.SILVER, TankType.GOLD),
		TANK_SILVER_DIAMOND(TankType.SILVER, TankType.DIAMOND),
		TANK_SILVER_OBSIDIAN(TankType.SILVER, TankType.OBSIDIAN),
		TANK_GOLD_DIAMOND(TankType.GOLD, TankType.DIAMOND),
		TANK_GOLD_OBSIDIAN(TankType.GOLD, TankType.OBSIDIAN),
		TANK_DIAMOND_OBSIDIAN(TankType.DIAMOND, TankType.OBSIDIAN);

		@Nullable
		private ChestType chestBase;
		private ChestType chestUpgrade;

		@Nullable
		private TankType tankBase;
		private TankType tankUpgrade;

		UpgradeType(ChestType chestBase, ChestType chestUpgrade) {
			this.chestBase = chestBase;
			this.chestUpgrade = chestUpgrade;
		}

		UpgradeType(ChestType chestUpgrade) {
			this(null, chestUpgrade);
		}

		UpgradeType(TankType tankBase, TankType tankUpgrade) {
			this.tankBase = tankBase;
			this.tankUpgrade = tankUpgrade;
		}

		UpgradeType(TankType tankUpgrade) {
			this(null, tankUpgrade);
		}

		public boolean isChestUpgrade() {
			return tankBase == null && tankUpgrade == null;
		}

		public boolean isBaseUpgrade() {
			if (isChestUpgrade()) {
				return ordinal() <= CHEST_WOOD_OBSIDIAN.ordinal();
			} else {
				return ordinal() >= TANK_BASE_COPPER.ordinal() && ordinal() <= TANK_BASE_OBSIDIAN.ordinal();
			}
		}

		@Override
		public String getName() {
			return name().toLowerCase();
		}
	}

	public static final String NAME = "metal_upgrade";

	public ItemMetalUpgrade() {
		super(NAME, UpgradeType.values());
	}
}
