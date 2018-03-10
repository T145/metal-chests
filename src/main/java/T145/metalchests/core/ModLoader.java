package T145.metalchests.core;

import T145.metalchests.MetalChests;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;

@ObjectHolder(MetalChests.MODID)
public class ModLoader {

	@EventBusSubscriber(modid = MetalChests.MODID)
	public static class ServerLoader {
	}

	@EventBusSubscriber(modid = MetalChests.MODID, value = Side.CLIENT)
	public static class ClientLoader {
	}
}