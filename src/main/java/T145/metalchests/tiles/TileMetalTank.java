package T145.metalchests.tiles;

import T145.metalchests.lib.MetalTankType;
import T145.metalchests.tiles.base.TileBase;

public class TileMetalTank extends TileBase {

	private final MetalTankType type;

	public TileMetalTank(MetalTankType type) {
		this.type = type;
	}

	public TileMetalTank() {
		this(MetalTankType.BASE);
	}

	public MetalTankType getType() {
		return type;
	}
}