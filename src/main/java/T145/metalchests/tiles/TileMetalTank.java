package T145.metalchests.tiles;

import T145.metalchests.blocks.BlockMetalTank.TankType;
import net.minecraft.nbt.NBTTagCompound;

public class TileMetalTank extends TileMod {

	private TankType type;

	public TileMetalTank(TankType type) {
		this.type = type;
	}

	public TileMetalTank() {
		this(TankType.BASE);
	}

	public TankType getType() {
		return type;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		type = TankType.valueOf(tag.getString("Type"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag = super.writeToNBT(tag);
		tag.setString("Type", type.toString());
		return tag;
	}
}