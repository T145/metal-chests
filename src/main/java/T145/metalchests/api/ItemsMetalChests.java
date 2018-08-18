package T145.metalchests.api;

import T145.metalchests.items.ItemChestUpgrade;
import T145.metalchests.items.ItemHungryChestUpgrade;
import T145.metalchests.items.ItemMetalMinecart;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder("metalchests")
public class ItemsMetalChests {

	private ItemsMetalChests() {}

	@ObjectHolder(ItemChestUpgrade.NAME)
	public static Item CHEST_UPGRADE;

	@ObjectHolder(ItemHungryChestUpgrade.NAME)
	public static Item HUNGRY_CHEST_UPGRADE;

	@ObjectHolder(ItemMetalMinecart.NAME)
	public static Item MINECART_METAL_CHEST;

}
