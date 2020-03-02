package t145.metalchests.api;

import net.minecraft.util.ResourceLocation;

public enum Reference {;

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

	public static final ResourceLocation[] METAL_CHEST_MODELS = new ResourceLocation[] {
			getResource("textures/entity/chest/copper.png"),
			getResource("textures/entity/chest/iron.png"),
			getResource("textures/entity/chest/silver.png"),
			getResource("textures/entity/chest/gold.png"),
			getResource("textures/entity/chest/diamond.png"),
			getResource("textures/entity/chest/obsidian.png")
	};

	public static final ResourceLocation[] HOLLOW_METAL_CHEST_MODELS = new ResourceLocation[] {
			getResource("textures/entity/chest/copper_h.png"),
			getResource("textures/entity/chest/iron_h.png"),
			getResource("textures/entity/chest/silver_h.png"),
			getResource("textures/entity/chest/gold_h.png"),
			getResource("textures/entity/chest/diamond_h.png"),
			getResource("textures/entity/chest/obsidian_h.png")
	};

	public static final ResourceLocation[] METAL_HUNGRY_CHEST_MODELS = new ResourceLocation[] {
			getResource("textures/entity/chest/hungry/copper.png"),
			getResource("textures/entity/chest/hungry/iron.png"),
			getResource("textures/entity/chest/hungry/silver.png"),
			getResource("textures/entity/chest/hungry/gold.png"),
			getResource("textures/entity/chest/hungry/diamond.png"),
			getResource("textures/entity/chest/hungry/obsidian.png")
	};

	public static final ResourceLocation[] SORTING_OVERLAY_MODELS = new ResourceLocation[] {
			getResource("textures/entity/chest/overlay/sorting_copper.png"),
			getResource("textures/entity/chest/overlay/sorting_iron.png"),
			getResource("textures/entity/chest/overlay/sorting_silver.png"),
			getResource("textures/entity/chest/overlay/sorting_gold.png"),
			getResource("textures/entity/chest/overlay/sorting_diamond.png"),
			getResource("textures/entity/chest/overlay/sorting_obsidian.png")
	};

	public static final ResourceLocation[] SORTING_HUNGRY_OVERLAY_MODELS = new ResourceLocation[] {
			getResource("textures/entity/chest/hungry/overlay/sorting_copper.png"),
			getResource("textures/entity/chest/hungry/overlay/sorting_iron.png"),
			getResource("textures/entity/chest/hungry/overlay/sorting_silver.png"),
			getResource("textures/entity/chest/hungry/overlay/sorting_gold.png"),
			getResource("textures/entity/chest/hungry/overlay/sorting_diamond.png"),
			getResource("textures/entity/chest/hungry/overlay/sorting_obsidian.png")
	};

	public static ResourceLocation getResource(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
