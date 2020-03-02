package t145.metalchests.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {

	static final Pair<ServerConfig, ForgeConfigSpec> SPEC = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
	public static final ServerConfig INSTANCE = SPEC.getKey();
	public static final ForgeConfigSpec CONFIG = SPEC.getValue();

	public final ForgeConfigSpec.BooleanValue hardMode;

	ServerConfig(ForgeConfigSpec.Builder builder) {
		hardMode = builder.comment("Changes the inventory size of wood chests to one row.", "Metal Chests then add a row per tier.", "This allows container code on the backend to be minimal, b/c the default chest container creation schemes are used.").define("hardMode", true);
	}
}
