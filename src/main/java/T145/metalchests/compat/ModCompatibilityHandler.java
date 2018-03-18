package T145.metalchests.compat;

import T145.metalchests.compat.holoinventory.TileRequest;
import net.dries007.holoInventory.HoloInventory;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;

public class ModCompatibilityHandler {

	public ModCompatibilityHandler() {}

	public static void init() {
		if (Loader.isModLoaded("holoinventory")) {
			HoloInventory.getSnw().registerMessage(TileRequest.Handler.class, TileRequest.class, 1, Side.SERVER);
		}
	}
}