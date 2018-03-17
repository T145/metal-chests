package T145.metalchests.entities;

import T145.metalchests.entities.base.EntityMinecartMetalChestBase;
import T145.metalchests.lib.MetalChestType;
import net.minecraft.world.World;

public class EntityMinecartGoldChest extends EntityMinecartMetalChestBase {

	public EntityMinecartGoldChest(World world) {
		super(world);
	}

	public EntityMinecartGoldChest(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	@Override
	public MetalChestType getChestType() {
		return MetalChestType.GOLD;
	}
}