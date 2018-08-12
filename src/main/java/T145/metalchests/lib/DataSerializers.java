package T145.metalchests.lib;

import java.io.IOException;

import T145.metalchests.blocks.BlockMetalChest.ChestType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;

public class DataSerializers {

	private DataSerializers() {}

	public static void registerSerializers() {
		net.minecraft.network.datasync.DataSerializers.registerSerializer(CHEST_TYPE);
	}

	public static final DataSerializer<ChestType> CHEST_TYPE = new DataSerializer<ChestType>() {

		@Override
		public void write(PacketBuffer buf, ChestType value) {
			buf.writeEnumValue(value);
		}

		@Override
		public ChestType read(PacketBuffer buf) throws IOException {
			return buf.readEnumValue(ChestType.class);
		}

		@Override
		public DataParameter<ChestType> createKey(int id) {
			return new DataParameter<ChestType>(id, this);
		}

		@Override
		public ChestType copyValue(ChestType value) {
			return value;
		}
	};
}
