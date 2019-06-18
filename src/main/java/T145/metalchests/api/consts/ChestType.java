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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import T145.metalchests.api.config.ConfigMC;
import T145.metalchests.api.obj.BlocksMC;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public enum ChestType implements IStringSerializable {

	COPPER(InventorySize.COPPER, MapColor.SAND, SoundType.METAL, "ingotCopper"),
	IRON(InventorySize.IRON, MapColor.IRON, SoundType.METAL, "ingotIron"),
	SILVER(InventorySize.SILVER, MapColor.SILVER, SoundType.METAL, "ingotSilver"),
	GOLD(InventorySize.GOLD, MapColor.GOLD, SoundType.METAL, "ingotGold"),
	DIAMOND(InventorySize.DIAMOND, MapColor.DIAMOND, SoundType.METAL, "gemDiamond"),
	OBSIDIAN(InventorySize.DIAMOND, Material.ROCK, MapColor.OBSIDIAN, SoundType.STONE, "obsidian");

	enum InventorySize {
		COPPER(45), IRON(54), SILVER(72), GOLD(81), DIAMOND(108);

		private final int size;

		InventorySize(int size) {
			this.size = size;
		}
	}

	public enum GUI {

		COPPER(184), IRON(202), SILVER(238), GOLD(256), DIAMOND(256), OBSIDIAN(256);

		private final int ySize;

		GUI(int ySize) {
			this.ySize = ySize;
		}

		public int getSizeX() {
			return ChestType.byMetadata(ordinal()).isLarge() ? 238 : 184;
		}

		public int getSizeY() {
			return ySize;
		}

		public static GUI byMetadata(int meta) {
			return values()[meta];
		}

		public static GUI byType(ChestType type) {
			return byMetadata(type.ordinal());
		}

		public ResourceLocation getGuiTexture() {
			ChestType type = ChestType.byMetadata(ordinal());
			return new ResourceLocation(RegistryMC.ID, String.format("textures/gui/%s_container.png", type.isLarge() ? "diamond" : type.getName()));
		}
	}

	public static final List<ChestType> TIERS = new ArrayList<>(ChestType.values().length);
	private static final LinkedHashSet<ChestType> TYPES;

	static {
		for (String upgrade : ConfigMC.upgradePath) {
			ChestType type = ChestType.valueOf(upgrade.toUpperCase());

			if (type.hasOre()) {
				TIERS.add(type);
			}
		}

		TYPES = new LinkedHashSet<>(TIERS);
	}

	private final InventorySize invSize;
	private final Material material;
	private final MapColor color;
	private final SoundType sound;
	private final String oreName;

	ChestType(InventorySize invSize, Material material, MapColor color, SoundType sound, String oreName) {
		this.invSize = invSize;
		this.material = material;
		this.color = color;
		this.sound = sound;
		this.oreName = oreName;
	}

	ChestType(InventorySize invSize, MapColor color, SoundType sound, String oreName) {
		this(invSize, Material.IRON, color, sound, oreName);
	}

	@Override
	public String getName() {
		return name().toLowerCase();
	}

	public int getInventorySize() {
		return invSize.size;
	}

	public Material getMaterial() {
		return material;
	}

	public MapColor getMapColor() {
		return color;
	}

	public SoundType getSoundType() {
		return sound;
	}

	public String getOreName() {
		return oreName;
	}

	public int getHoldingEnchantBound() {
		return ConfigMC.holdingEnchantBounds.get(getName());
	}

	public boolean hasOre() {
		return OreDictionary.doesOreNameExist(oreName);
	}

	public boolean isRegistered() {
		return hasOre() && TYPES.contains(this);
	}

	public boolean isLarge() {
		return getInventorySize() > 100;
	}

	public int getRowLength() {
		return isLarge() ? 12 : 9;
	}

	public int getRowCount() {
		return getInventorySize() / getRowLength();
	}

	public static ChestType byMetadata(int meta) {
		return values()[meta];
	}

	public static ChestType byOreName(String dictName) {
		return Arrays.stream(values()).filter(type -> type.getOreName() == dictName).findFirst().get();
	}

	public GUI getGui() {
		return GUI.byType(this);
	}

	public String getIdName() {
		return String.format("%s:%s_chest", RegistryMC.ID, getName());
	}

	public ResourceLocation getId() {
		return new ResourceLocation(getIdName());
	}

	public static void registerRecipes(Object baseChest, Block metalChest, String postfix) {
		for (short meta = 0; meta < TYPES.size(); ++meta) {
			ChestType type = TIERS.get(meta);
			ChestType trueType = ChestType.byOreName(type.getOreName());
			ItemStack result = new ItemStack(metalChest, 1, trueType.ordinal());

			GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.ID, String.format("recipe_%s_%s", type.getName(), postfix)), RegistryMC.RECIPE_GROUP,
					result,
					"aaa", "aba", "aaa",
					'a', type.getOreName(),
					'b', meta == 0 ? baseChest : new ItemStack(metalChest, 1, TIERS.get(meta - 1).ordinal()));

			// may want to register OreDictionary entries here
		}
	}

	public static void registerRecipes(Object baseChest, Block metalChest) {
		registerRecipes(baseChest, metalChest, "chest");
	}

	public static void registerRecipes(Block metalChest) {
		registerRecipes("chestWood", metalChest);
	}

	public static void registerRecipes() {
		registerRecipes(BlocksMC.METAL_CHEST);
	}
}
