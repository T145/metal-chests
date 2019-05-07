package T145.metalchests.network.client;

import java.io.IOException;

import T145.metalchests.network.MessageBase;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageApplyLuminousUpgrade extends MessageBase {

	private BlockPos pos;
	private boolean luminous;

	public MessageApplyLuminousUpgrade() {
		// DEFAULT CONSTRUCTOR REQUIRED
	}

	public MessageApplyLuminousUpgrade(BlockPos pos, boolean luminous) {
		this.pos = pos;
		this.luminous = luminous;
	}

	@Override
	public void serialize(PacketBuffer buf) {
		buf.writeBlockPos(pos);
		buf.writeBoolean(luminous);
	}

	@Override
	public void deserialize(PacketBuffer buf) throws IOException {
		pos = buf.readBlockPos();
		luminous = buf.readBoolean();
	}

	@SideOnly(Side.CLIENT)
	protected World getWorld(MessageContext ctx) {
		return Minecraft.getMinecraft().world;
	}

	@Override
	public void process(MessageContext ctx) {
		World world = getWorld(ctx);

		if (world != null) {
			TileEntity te = world.getTileEntity(pos);

			if (te instanceof TileMetalChest) {
				TileMetalChest chest = (TileMetalChest) te;
				chest.setLuminous(luminous);
			}
		}
	}
}