package T145.metalchests.api.constants;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Loader;

@Config(modid = RegistryMC.ID, name = "T145/" + RegistryMC.NAME)
@Config.LangKey(RegistryMC.ID)
public class ConfigMC {

	private ConfigMC() {}

	@Config.Comment("Whether or not you want to recieve an in-game notification if an update is available.")
	public static boolean checkForUpdates = true;

	@Config.Comment("The upgrade path for metal chests. Recipes will update to reflect this.\n Current valid options are: { \"copper\", \"iron\", \"silver\", \"gold\", \"obsidian\", \"diamond\" }")
	@Config.RequiresMcRestart
	public static String[] upgradePath = new String[] { "copper", "iron", "silver", "gold", "obsidian", "diamond" };

	@Config.Comment("Whether or not all metal chest model textures are like the vanilla chest; black in the middle.\n* Hollow textures contributed by phyne")
	public static boolean hollowModelTextures = false;

	@Config.Comment("Whether or not you want to enable the Minecarts with Metal Chests.")
	@Config.RequiresMcRestart
	public static boolean enableMinecarts = true;

	@Config.Comment("Whether or not you want to enable placing Metal Chests in boats.")
	@Config.RequiresMcRestart
	public static boolean enableBoats = true;

	@Config.Comment("If Thaumcraft is installed, whether or not you want to enable the Metal Hungry Chests.")
	@Config.RequiresMcRestart
	public static boolean enableMetalHungryChests = true;

	@Config.Comment("If Refined Relocation 2 is installed, whether or not you want to enable the Metal Hungry Sorting Chests.")
	@Config.RequiresMcRestart
	public static boolean enableMetalHungrySortingChests = true;

	public static boolean hasThaumcraft() {
		return Loader.isModLoaded(RegistryMC.ID_THAUMCRAFT) && enableMetalHungryChests;
	}

	public static boolean hasRefinedRelocation() {
		return Loader.isModLoaded(RegistryMC.ID_THAUMCRAFT) && enableMetalHungrySortingChests;
	}

	public static boolean hasThermalExpansion() {
		return Loader.isModLoaded(RegistryMC.ID_THERMALEXPANSION);
	}
}
