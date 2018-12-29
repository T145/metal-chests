package T145.metalchests.api.chests;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class UpgradeRegistry {

	private static final Map<String, HashMap<Block, Block>> chests = new HashMap<>();

	public static void registerChest(ResourceLocation resource, Block chestTile, Block destTile) {
		String upgradeName = resource.toString();

		if (chests.containsKey(upgradeName)) {
			HashMap<Block, Block> map = chests.get(upgradeName);
			map.put(chestTile, destTile);
		} else {
			HashMap<Block, Block> map = new HashMap<>();
			map.put(chestTile, destTile);
			chests.put(upgradeName, map);
		}
	}

	public static boolean hasChest(ResourceLocation resource, Block chestTile) {
		HashMap<Block, Block> map = chests.get(resource.toString());
		return map.containsKey(chestTile) && map.get(chestTile) != null;
	}

	public static Block getChest(ResourceLocation resource, Block chestTile) {
		return chests.get(resource.toString()).get(chestTile);
	}
}