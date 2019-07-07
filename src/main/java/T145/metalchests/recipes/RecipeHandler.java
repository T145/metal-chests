package t145.metalchests.recipes;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import t145.metalchests.api.consts.ChestType;
import t145.metalchests.api.consts.ChestUpgrade;
import t145.metalchests.api.consts.RegistryMC;
import t145.metalchests.api.obj.ItemsMC;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.ShapedArcaneRecipe;

public class RecipeHandler {

	private RecipeHandler() {}

	private static Object getChestBase(Block chest, Object base, ChestType type) {
		if (type.getIndex() == 0) {
			return base;
		} else {
			return new ItemStack(chest, 1, ChestType.TIERS.get(type.getIndex() - 1).ordinal());
		}
	}

	private static String getOre(ChestType type, String postfix) {
		return String.format("chest%s%s", WordUtils.capitalize(type.getName()), postfix);
	}

	private static ResourceLocation getChestResource(ChestType type, String postfix) {
		return RegistryMC.getResource(String.format("recipe%s", WordUtils.capitalize(getOre(type, postfix))));
	}

	public static void registerChests(Object baseChest, Block metalChest, String postfix) {
		ChestType.TIERS.forEach(type -> {
			ItemStack result = new ItemStack(metalChest, 1, type.ordinal());

			GameRegistry.addShapedRecipe(getChestResource(type, StringUtils.EMPTY), RegistryMC.RECIPE_GROUP, result,
					"aaa", "aba", "aaa", 'a', type.getOre(), 'b', getChestBase(metalChest, baseChest, type));

			OreDictionary.registerOre("chest", result);
			OreDictionary.registerOre(getOre(type, postfix), result);
		});
	}

	@Optional.Method(modid = RegistryMC.ID_THAUMCRAFT)
	public static void registerHungryChests(Object baseChest, Block metalChest, String postfix) {
		for (ChestType type : ChestType.TIERS) {
			ItemStack result = new ItemStack(metalChest, 1, type.ordinal());

			ThaumcraftApi.addArcaneCraftingRecipe(getChestResource(type, postfix), new ShapedArcaneRecipe(RegistryMC.RECIPE_GROUP, "HUNGRYCHEST", 15, new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1), result,
					"aaa", "aba", "aaa", 'a', type.getOre(), 'b', getChestBase(metalChest, baseChest, type)));

			OreDictionary.registerOre("chest", result);
			OreDictionary.registerOre(getOre(type, postfix), result);
		}
	}

	private static Object getUpgradeBase(Item upgrade, Object base, ChestUpgrade curr) {
		short meta = (short) curr.ordinal();

		if (curr.isForWood()) {
			if (meta == 0) {
				return base;
			}

			return new ItemStack(upgrade, 1, meta - 1);
		} else {
			ChestUpgrade prior = ChestUpgrade.TIERS.get(meta - 1);

			if (prior.getBase() == curr.getBase()) {
				return new ItemStack(upgrade, 1, meta - 1);
			} else {
				return curr.getBase().getOre();
			}
		}
	}

	private static ResourceLocation getUpgradeResource(ChestUpgrade type, String postfix) {
		return RegistryMC.getResource(String.format("recipeUpgrade%s%s", WordUtils.capitalize(type.getName()), postfix));
	}

	public static void registerUpgrades(Item upgrade, Object base, String postfix) {
		ChestUpgrade.TIERS.forEach(type -> GameRegistry.addShapedRecipe(getUpgradeResource(type, postfix), RegistryMC.RECIPE_GROUP, new ItemStack(upgrade, 1, type.ordinal()),
				"aaa", "aaa", "baa", 'a', type.getUpgrade().getOre(), 'b', getUpgradeBase(upgrade, base, type)));
	}

	@Optional.Method(modid = RegistryMC.ID_THAUMCRAFT)
	public static void registerHungryUpgrades() {
		for (ChestUpgrade type : ChestUpgrade.TIERS) {
			ThaumcraftApi.addArcaneCraftingRecipe(getUpgradeResource(type, "Hungry"),
					new ShapedArcaneRecipe(RegistryMC.RECIPE_GROUP, "HUNGRYCHEST", 15, new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1), new ItemStack(ItemsMC.HUNGRY_CHEST_UPGRADE, 1, type.ordinal()),
							"aaa", "aaa", "baa", 'a', type.getUpgrade().getOre(), 'b', getUpgradeBase(ItemsMC.HUNGRY_CHEST_UPGRADE, BlocksTC.plankGreatwood, type)));
		}
	}
}
