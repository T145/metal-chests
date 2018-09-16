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
package T145.metalchests.api.immutable;

import javax.annotation.Nullable;

import net.minecraft.util.IStringSerializable;

public enum ChestUpgrade implements IStringSerializable {

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

    ChestUpgrade(ChestType base, ChestType upgrade) {
        this.base = base;
        this.upgrade = upgrade;
    }

    ChestUpgrade(ChestType upgrade) {
        this(null, upgrade);
    }

    public ChestType getBase() {
        return base;
    }

    public ChestType getUpgrade() {
        return upgrade;
    }

    public static ChestUpgrade byMetadata(int meta) {
        return values()[meta];
    }

    public boolean isRegistered() {
        return base == null ? upgrade.isRegistered() : base.isRegistered() && upgrade.isRegistered();
    }

    public ChestUpgrade getPriorUpgrade() {
        return values()[ordinal() > 0 ? ordinal() - 1 : 0];
    }

    @Override
    public String getName() {
        return name().toLowerCase();
    }
}
