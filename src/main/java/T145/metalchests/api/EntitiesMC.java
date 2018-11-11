package T145.metalchests.api;

import T145.metalchests.api.immutable.RegistryMC;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(RegistryMC.MOD_ID)
public class EntitiesMC {

    private EntitiesMC() {}

    @ObjectHolder(RegistryMC.KEY_MINECART_METAL_CHEST)
    public static EntityEntry MINECART_METAL_CHEST;
}
