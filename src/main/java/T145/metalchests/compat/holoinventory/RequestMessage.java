package T145.metalchests.compat.holoinventory;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class RequestMessage implements IMessage {

	int dim;

	RequestMessage() {}

	RequestMessage(int dim) {
		this.dim = dim;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dim = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dim);
	}
}