package T145.metalchests.network.client;

import java.io.IOException;

import T145.metalchests.entities.base.EntityMinecartMetalChestBase;
import T145.metalchests.network.base.MessageBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageSyncMinecartInventory extends MessageBase {

	private int entityId;

	public MessageSyncMinecartInventory(int entityId) {
		this.entityId = entityId;
	}

	public MessageSyncMinecartInventory() {}

	@Override
	public void serialize(PacketBuffer buffer) {
		buffer.writeInt(entityId);
	}

	@Override
	public void deserialize(PacketBuffer buffer) throws IOException {
		entityId = buffer.readInt();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage process(MessageContext context) {
		World world = Minecraft.getMinecraft().world;
		Entity ent = world.getEntityByID(entityId);

		if (ent instanceof EntityMinecartMetalChestBase) {
			EntityMinecartMetalChestBase cart = (EntityMinecartMetalChestBase) ent;
			cart.onSlotChanged();
		}

		return null;
	}
}