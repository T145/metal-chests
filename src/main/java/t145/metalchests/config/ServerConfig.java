package t145.metalchests.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import t145.metalchests.containers.MetalChestContainer;

public class ServerConfig {

	static final Pair<ServerConfig, ForgeConfigSpec> SPEC = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
	public static final ServerConfig INSTANCE = SPEC.getKey();
	public static final ForgeConfigSpec CONFIG = SPEC.getValue();

	public final ForgeConfigSpec.BooleanValue hardMode;
	public final ForgeConfigSpec.IntValue numOfCopperRows;
	public final ForgeConfigSpec.IntValue numOfCopperColumns;
	public final ForgeConfigSpec.IntValue numOfIronRows;
	public final ForgeConfigSpec.IntValue numOfIronColumns;
	public final ForgeConfigSpec.IntValue numOfSilverRows;
	public final ForgeConfigSpec.IntValue numOfSilverColumns;
	public final ForgeConfigSpec.IntValue numOfGoldRows;
	public final ForgeConfigSpec.IntValue numOfGoldColumns;
	public final ForgeConfigSpec.IntValue numOfDiamondRows;
	public final ForgeConfigSpec.IntValue numOfDiamondColumns;
	public final ForgeConfigSpec.IntValue numOfObsidianRows;
	public final ForgeConfigSpec.IntValue numOfObsidianColumns;
	public final ForgeConfigSpec.IntValue numOfEmeraldRows;
	public final ForgeConfigSpec.IntValue numOfEmeraldColumns;

	ServerConfig(ForgeConfigSpec.Builder builder) {
		hardMode = builder.comment("Changes the inventory size of wood chests to one row.", "Metal Chests then add a row per tier.", "This allows container code on the backend to be minimal, b/c the default chest container creation schemes are used.").define("hardMode", false);

		numOfCopperRows = builder.defineInRange("Row count:", 5, 1, MetalChestContainer.MAX_ROWS);
		numOfCopperColumns = builder.defineInRange("Column count:", 9, 1, MetalChestContainer.MAX_COLUMNS);

		numOfIronRows = builder.defineInRange("Row count:", 6, 1, MetalChestContainer.MAX_ROWS);
		numOfIronColumns = builder.defineInRange("Column count:", 9, 1, MetalChestContainer.MAX_COLUMNS);

		numOfSilverRows = builder.defineInRange("Row count:", 6, 1, MetalChestContainer.MAX_ROWS);
		numOfSilverColumns = builder.defineInRange("Column count:", 12, 1, MetalChestContainer.MAX_COLUMNS);

		numOfGoldRows = builder.defineInRange("Row count:", 5, 1, MetalChestContainer.MAX_ROWS);
		numOfGoldColumns = builder.defineInRange("Column count:", 16, 1, MetalChestContainer.MAX_COLUMNS);

		numOfDiamondRows = builder.defineInRange("Row count:", 6, 1, MetalChestContainer.MAX_ROWS);
		numOfDiamondColumns = builder.defineInRange("Column count:", 18, 1, MetalChestContainer.MAX_COLUMNS);

		numOfObsidianRows = builder.defineInRange("Row count:", 6, 1, MetalChestContainer.MAX_ROWS);
		numOfObsidianColumns = builder.defineInRange("Column count:", 18, 1, MetalChestContainer.MAX_COLUMNS);

		numOfEmeraldRows = builder.defineInRange("Row count:", 6, 1, MetalChestContainer.MAX_ROWS);
		numOfEmeraldColumns = builder.defineInRange("Column count:", 18, 1, MetalChestContainer.MAX_COLUMNS);
	}
}
