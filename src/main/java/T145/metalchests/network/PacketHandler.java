package T145.metalchests.network;

import T145.metalchests.api.immutable.RegistryMC;
import T145.metalchests.network.client.MessageApplyLuminousUpgrade;
import T145.metalchests.network.client.MessageApplyTrappedUpgrade;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(RegistryMC.MOD_ID);

	private static byte id;

	private PacketHandler() {}

	private static void registerMessage(Class<? extends MessageBase> clazz, Side side) {
		NETWORK.registerMessage((msg, ctx) -> {
			IThreadListener thread = FMLCommonHandler.instance().getWorldThread(ctx.netHandler);
			thread.addScheduledTask(() -> msg.process(ctx));
			return null;
		}, clazz, id++, side);
	}

	public static void registerMessages() {
		registerMessage(MessageApplyTrappedUpgrade.class, Side.CLIENT);
		registerMessage(MessageApplyLuminousUpgrade.class, Side.CLIENT);
	}

	public static NetworkRegistry.TargetPoint getTargetPoint(World world, BlockPos pos) {
		return new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 64D);
	}

	public static void sendToAllAround(MessageBase msg, World world, BlockPos pos) {
		NETWORK.sendToAllAround(msg, getTargetPoint(world, pos));
	}
}
