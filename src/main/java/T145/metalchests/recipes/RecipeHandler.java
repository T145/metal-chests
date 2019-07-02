package T145.metalchests.recipes;

import org.apache.commons.lang3.text.WordUtils;

import T145.metalchests.api.consts.ChestType;
import T145.metalchests.api.consts.ChestUpgrade;
import T145.metalchests.api.consts.RegistryMC;
import T145.metalchests.api.obj.ItemsMC;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.ShapedArcaneRecipe;

public class RecipeHandler {

	private RecipeHandler() {}

	public static void registerChests(Object baseChest, Block metalChest, String postfix) {
		for (short meta = 0; meta < ChestType.values().length; ++meta) {
			ChestType type = ChestType.values()[meta];
			ChestType trueType = ChestType.byOre(type.getOre());
			ItemStack result = new ItemStack(metalChest, 1, trueType.ordinal());
			String recipeName = String.format("chest%s%s", WordUtils.capitalize(type.getName()), postfix);

			GameRegistry.addShapedRecipe(RegistryMC.getResource(String.format("recipe%s", WordUtils.capitalize(recipeName))), RegistryMC.RECIPE_GROUP,
					result,
					"aaa", "aba", "aaa",
					'a', type.getOre(),
					'b', meta == 0 ? baseChest : new ItemStack(metalChest, 1, ChestType.values()[meta - 1].ordinal()));

			OreDictionary.registerOre("chest", result);
			OreDictionary.registerOre(recipeName, result);
		}
	}

	private static Object getBaseIngredient(Item upgrade, Object base, short meta) {
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
				return curr.getBase().getOre();
			}
		}
	}

	public static void registerUpgrades(Item upgrade, Object base, String postfix) {
		for (short i = 0; i < ChestUpgrade.TIERS.size(); ++i) {
			ChestUpgrade type = ChestUpgrade.byMetadata(i);
			String recipeName = String.format("upgrade%s%s", WordUtils.capitalize(type.getName()), postfix);

			GameRegistry.addShapedRecipe(RegistryMC.getResource(String.format("recipe%s", WordUtils.capitalize(recipeName))), RegistryMC.RECIPE_GROUP,
					new ItemStack(upgrade, 1, i),
					"aaa", "aaa", "baa",
					'a', type.getUpgrade().getOre(),
					'b', getBaseIngredient(upgrade, base, i));
		}
	}

	@Optional.Method(modid = RegistryMC.ID_THAUMCRAFT)
	public static void registerHungryChests(Object baseChest, Block metalChest, String postfix) {
		for (short meta = 0; meta < ChestType.values().length; ++meta) {
			ChestType type = ChestType.values()[meta];
			ChestType trueType = ChestType.byOre(type.getOre());
			ItemStack result = new ItemStack(metalChest, 1, trueType.ordinal());
			String recipeName = String.format("chest%s%s", WordUtils.capitalize(type.getName()), postfix);

			ThaumcraftApi.addArcaneCraftingRecipe(RegistryMC.getResource(recipeName),
					new ShapedArcaneRecipe(RegistryMC.RECIPE_GROUP, "HUNGRYCHEST", 15,
							new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1),
							result,
							"aaa", "aba", "aaa",
							'a', type.getOre(),
							'b', meta == 0 ? baseChest : new ItemStack(metalChest, 1, ChestType.values()[meta - 1].ordinal())));

			OreDictionary.registerOre("chest", result);
			OreDictionary.registerOre(recipeName, result);
		}
	}

	@Optional.Method(modid = RegistryMC.ID_THAUMCRAFT)
	public static void registerHungryUpgrades() {
		for (short i = 0; i < ChestUpgrade.TIERS.size(); ++i) {
			ChestUpgrade type = ChestUpgrade.byMetadata(i);
			String recipeName = String.format("upgrade%s%s", WordUtils.capitalize(type.getName()), "Hungry");

			ThaumcraftApi.addArcaneCraftingRecipe(RegistryMC.getResource(recipeName),
					new ShapedArcaneRecipe(RegistryMC.RECIPE_GROUP, "HUNGRYCHEST", 15,
							new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1),
							new ItemStack(ItemsMC.HUNGRY_CHEST_UPGRADE, 1, i),
							"aaa", "aaa", "baa",
							'a', type.getUpgrade().getOre(),
							'b', getBaseIngredient(ItemsMC.HUNGRY_CHEST_UPGRADE, BlocksTC.plankGreatwood, i)));
		}
	}
}
