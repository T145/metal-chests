package T145.metalchests.network.client;

import java.io.IOException;

import T145.metalchests.api.chests.IMetalChest;
import T145.metalchests.network.MessageBase;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSyncMetalChest extends MessageBase {

	protected BlockPos pos;
	protected boolean trapped;
	protected boolean luminous;
	protected byte enchantLevel;

	public MessageSyncMetalChest() {
		// DEFAULT CONSTRUCTOR REQUIRED
	}

	protected void setChestData(IMetalChest chest) {
		this.trapped = chest.isTrapped();
		this.luminous = chest.isLuminous();
		this.enchantLevel = chest.getEnchantLevel();
	}

	public MessageSyncMetalChest(BlockPos pos, IMetalChest chest) {
		this.pos = pos;
		this.setChestData(chest);
	}

	@Override
	public void serialize(PacketBuffer buf) {
		buf.writeBlockPos(pos);
		buf.writeBoolean(trapped);
		buf.writeBoolean(luminous);
		buf.writeByte(enchantLevel);
	}

	@Override
	public void deserialize(PacketBuffer buf) throws IOException {
		pos = buf.readBlockPos();
		trapped = buf.readBoolean();
		luminous = buf.readBoolean();
		enchantLevel = buf.readByte();
	}

	protected void syncChestData(IMetalChest chest) {
		chest.setTrapped(trapped);
		chest.setLuminous(luminous);
		chest.setEnchantLevel(enchantLevel);
	}

	@Override
	public void process(MessageContext ctx) {
		World world = getClientWorld();

		if (world != null) {
			TileEntity te = world.getTileEntity(pos);

			if (te instanceof TileMetalChest) {
				this.syncChestData((TileMetalChest) te);
			}
		}
	}
}
