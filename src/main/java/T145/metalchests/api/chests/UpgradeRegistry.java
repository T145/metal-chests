package T145.metalchests.api.chests;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class UpgradeRegistry {

    private static final Map<String, HashMap<Class<? extends TileEntity>, Block>> chests = new HashMap<>();

    public static void registerChest(String upgradeName, Class<? extends TileEntity> chestTile, Block destTile) {
        if (chests.containsKey(upgradeName)) {
            HashMap<Class<? extends TileEntity>, Block> map = chests.get(upgradeName);
            map.put(chestTile, destTile);
        } else {
            HashMap<Class<? extends TileEntity>, Block> map = new HashMap<>();
            map.put(chestTile, destTile);
            chests.put(upgradeName, map);
        }
    }

    public static boolean hasChest(String upgradeName, Class<? extends TileEntity> chestTile) {
        HashMap<Class<? extends TileEntity>, Block> map = chests.get(upgradeName);
        return map.containsKey(chestTile) && map.get(chestTile) != null;
    }

    public static Block getMetalChestBlock(String upgradeName, Class<? extends TileEntity> chestTile) {
        return chests.get(upgradeName).get(chestTile);
    }
}