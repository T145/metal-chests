/*******************************************************************************
 * Copyright 2019 T145
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

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
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

	private final InventorySize invSize;
	private final Material material;
	private final MapColor color;
	private final SoundType sound;
	private final String dictName;

	ChestType(InventorySize invSize, Material material, MapColor color, SoundType sound, String dictName) {
		this.invSize = invSize;
		this.material = material;
		this.color = color;
		this.sound = sound;
		this.dictName = dictName;
	}

	ChestType(InventorySize invSize, MapColor color, SoundType sound, String dictName) {
		this(invSize, Material.IRON, color, sound, dictName);
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
		return dictName;
	}

	public boolean isRegistered() {
		return OreDictionary.doesOreNameExist(dictName) && !OreDictionary.getOres(dictName).isEmpty();
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

	public GUI getGui() {
		return GUI.byType(this);
	}

	public String getGuiId() {
		return "metalchests:" + getName() + "_chest";
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
			return new ResourceLocation("metalchests", "textures/gui/" + (type.isLarge() ? "diamond" : type.getName()) + "_container.png");
		}
	}
}
