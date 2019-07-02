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

import java.util.Map;

import com.google.gson.JsonObject;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;

public enum ChestType implements IStringSerializable {

	COPPER("ingotCopper", MapColor.SAND),
	IRON("ingotIron", MapColor.IRON),
	SILVER("ingotSilver", MapColor.SILVER),
	GOLD("ingotGold", MapColor.GOLD),
	DIAMOND("gemDiamond", MapColor.DIAMOND),
	OBSIDIAN("obsidian", Material.ROCK, MapColor.OBSIDIAN, SoundType.STONE);

	private static final Map<String, Integer> ORES = new Object2ObjectOpenHashMap<>(values().length);

	private static boolean isEnabled(ChestType type, JsonObject obj) {
		String enabled = obj.getAsJsonPrimitive("enabled").getAsString();
		type.auto = enabled.contentEquals("auto");
		return Boolean.parseBoolean(enabled);
	}

	public static void setTiers(JsonObject settings) {
		ChestType[] temp = new ChestType[values().length];

		for (ChestType type : values()) {
			JsonObject props = settings.getAsJsonObject("chests").getAsJsonObject(type.getName());
			int index = props.getAsJsonPrimitive("index").getAsInt();

			type.setRegistered(isEnabled(type, props));
			type.setRows(props.getAsJsonPrimitive("rows").getAsInt());
			type.setCols(props.getAsJsonPrimitive("cols").getAsInt());
			type.setHolding(props.getAsJsonPrimitive("holding").getAsByte());
			temp[index] = type;
			ORES.put(type.ore, index);
		}

		// the metal chest block & item block *need* access to values(),
		// specifically item block's getTranslationKey & block's getStateFromMeta()
		// (anything else causes crashes)
		System.arraycopy(temp, 0, values(), 0, temp.length);
	}

	private final String ore;
	private final Material material;
	private final MapColor color;
	private final SoundType sound;
	private boolean registered;
	private boolean auto;
	private int rows;
	private int cols;
	private byte holding;

	ChestType(String ore, Material material, MapColor color, SoundType sound) {
		this.ore = ore;
		this.material = material;
		this.color = color;
		this.sound = sound;
	}

	ChestType(String ore, MapColor color, SoundType sound) {
		this(ore, Material.IRON, color, sound);
	}

	ChestType(String ore, MapColor color) {
		this(ore, color, SoundType.METAL);
	}

	@Override
	public String getName() {
		return name().toLowerCase();
	}

	public String getOre() {
		return ore;
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

	void setRows(int rows) {
		this.rows = rows;
	}

	public int getRows() {
		return rows;
	}

	void setCols(int cols) {
		this.cols = cols;
	}

	public int getColumns() {
		return cols;
	}

	public int getInventorySize() {
		return rows * cols;
	}

	void setHolding(byte holding) {
		this.holding = holding;
	}

	public byte getHolding() {
		return holding;
	}

	void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public boolean isRegistered() {
		return registered;
	}

	public static boolean hasOre(String ore) {
		return ORES.containsKey(ore);
	}

	public static ChestType byOre(String ore) {
		return values()[ORES.get(ore)];
	}

	public static void attemptRegister(String ore) {
		if (hasOre(ore)) {
			ChestType type = byOre(ore);

			if (type.auto && !type.registered) {
				type.setRegistered(true);
			}
		}
	}
}
