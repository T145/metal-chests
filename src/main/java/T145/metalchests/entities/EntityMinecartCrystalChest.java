package T145.metalchests.entities;

import T145.metalchests.entities.base.EntityMinecartMetalChestBase;
import T145.metalchests.lib.MetalChestType;
import net.minecraft.world.World;

public class EntityMinecartCrystalChest extends EntityMinecartMetalChestBase {

	public EntityMinecartCrystalChest(World world) {
		super(world);
	}

	public EntityMinecartCrystalChest(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	@Override
	public MetalChestType getChestType() {
		return MetalChestType.CRYSTAL;
	}
}