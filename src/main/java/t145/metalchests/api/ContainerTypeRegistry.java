package t145.metalchests.api;

import static t145.metalchests.lib.Reference.*;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.registries.ObjectHolder;
import t145.metalchests.containers.MetalChestContainer;

@ObjectHolder(MOD_ID)
public class ContainerTypeRegistry {

	@ObjectHolder(METAL_CHEST_CONTAINER_ID)
	public static final ContainerType<MetalChestContainer> METAL_CHEST_CONTAINER_TYPE = null;

	private ContainerTypeRegistry() {
		throw new UnsupportedOperationException();
	}
}
