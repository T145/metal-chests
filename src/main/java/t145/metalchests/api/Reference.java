package t145.metalchests.api;

import net.minecraft.util.ResourceLocation;

public class Reference {

	public static final String MOD_ID = "metalchests";
	public static final String MOD_NAME = "MetalChests";
	public static final String MOD_VERSION = "@VERSION@";
	public static final String UPDATE_JSON = "https://raw.githubusercontent.com/T145/metalchests/master/update.json";

	public static final String COPPER_CHEST_ID = "copper_chest";
	public static final String IRON_CHEST_ID = "iron_chest";
	public static final String SILVER_CHEST_ID = "silver_chest";
	public static final String GOLD_CHEST_ID = "gold_chest";
	public static final String DIAMOND_CHEST_ID = "diamond_chest";
	public static final String OBSIDIAN_CHEST_ID = "obsidian_chest";
	public static final String EMERALD_CHEST_ID = "emerald_chest";

	public static final ResourceLocation[] METAL_CHEST_MODELS = new ResourceLocation[] {
			getResource("entity/copper"),
			getResource("entity/iron"),
			getResource("entity/silver"),
			getResource("entity/gold"),
			getResource("entity/diamond"),
			getResource("entity/obsidian"),
			getResource("entity/emerald")
	};

	private Reference() {
		throw new UnsupportedOperationException();
	}

	public static ResourceLocation getResource(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
