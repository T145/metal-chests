package T145.metalchests.network.client;

import T145.metalchests.api.chests.IMetalChest;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSyncMetalChestEntity extends MessageSyncMetalChest {

	private int entityId;

	public MessageSyncMetalChestEntity() {
		// DEFAULT CONSTRUCTOR REQUIRED
	}

	private MessageSyncMetalChestEntity(BlockPos pos, IMetalChest chest) {
		super(pos, chest);
	}

	public MessageSyncMetalChestEntity(int entityId, IMetalChest chest) {
		this.entityId = entityId;
		this.setChestData(chest);
	}

	@Override
	public void process(MessageContext ctx) {
		World world = getClientWorld();

		if (world != null) {
			Entity entity = world.getEntityByID(entityId);

			if (entity instanceof IMetalChest) {
				this.syncChestData((IMetalChest) entity);
			}
		}
	}
}
