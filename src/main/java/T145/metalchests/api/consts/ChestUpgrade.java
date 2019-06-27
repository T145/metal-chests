/*******************************************************************************
 * Copyright 2018-2019 T145
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
package T145.metalchests.api.consts;

import java.util.ArrayDeque;
import java.util.LinkedList;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import T145.metalchests.api.obj.ItemsMC;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ChestUpgrade implements IStringSerializable {

	public static final LinkedList<ChestUpgrade> TIERS = new LinkedList<>();

	static {
		ChestType.TIERS.forEach(type -> TIERS.add(new ChestUpgrade(null, type)));

		ArrayDeque<ChestType> temp = new ArrayDeque<>(ChestType.TIERS);

		while (!temp.isEmpty()) {
			ChestType base = temp.remove();

			temp.forEach(upgrade -> {
				TIERS.add(new ChestUpgrade(base, upgrade));
			});
		}
	}

	private final ChestType base;
	private final ChestType upgrade;

	private ChestUpgrade(@Nullable ChestType base, ChestType upgrade) {
		this.base = base;
		this.upgrade = upgrade;
	}

	@Nullable
	public ChestType getBase() {
		return base;
	}

	public ChestType getUpgrade() {
		return upgrade;
	}

	@Override
	public String getName() {
		String upgradeName = WordUtils.capitalize(upgrade.getName());
		return base == null ? String.format("wood%s", upgradeName) : String.format("%s%s", base.getName(), upgradeName);
	}

	public boolean isForWood() {
		return base == null;
	}

	public boolean isRegistered() {
		return base == null ? upgrade.isRegistered() : base.isRegistered() && upgrade.isRegistered();
	}

	public static ChestUpgrade byMetadata(int meta) {
		return TIERS.get(meta);
	}

	public static Object getBaseIngredient(Item upgrade, Object base, short meta) {
		ChestUpgrade curr = ChestUpgrade.byMetadata(meta);

		if (curr.isForWood()) {
			if (meta == 0) {
				return base;
			}

			return new ItemStack(upgrade, 1, meta - 1);
		} else {
			ChestUpgrade prior = ChestUpgrade.byMetadata(meta - 1);

			if (prior.getBase() == curr.getBase()) {
				return new ItemStack(upgrade, 1, meta - 1);
			} else {
				return curr.getBase().getOreName();
			}
		}
	}

	public static void registerRecipes(Item upgrade, Object base, String postfix) {
		for (short i = 0; i < TIERS.size(); ++i) {
			ChestUpgrade type = ChestUpgrade.byMetadata(i);
			String recipeName = String.format("upgrade%s%s", WordUtils.capitalize(type.getName()), postfix);

			GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.ID, String.format("recipe%s", WordUtils.capitalize(recipeName))), RegistryMC.RECIPE_GROUP,
					new ItemStack(upgrade, 1, i),
					"aaa", "aaa", "baa",
					'a', type.getUpgrade().getOreName(),
					'b', getBaseIngredient(upgrade, base, i));
		}
	}

	public static void registerRecipes() {
		registerRecipes(ItemsMC.CHEST_UPGRADE, "plankWood", StringUtils.EMPTY);
	}
}
