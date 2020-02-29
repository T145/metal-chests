package t145.metalchests.api.registries;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum ModReference {
	;

	public static final String MOD_ID = "metalchests";
	public static final String MOD_NAME = "MetalChests";
	public static final String MOD_VERSION = "@VERSION@";
	public static final String UPDATE_JSON = "https://raw.githubusercontent.com/T145/metalchests/master/update.json";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static final String METAL_CHEST_ID = "metal_chest";
	public static final String METAL_CHEST_TILE_TYPE_ID = "metal_chest_tile";
}
