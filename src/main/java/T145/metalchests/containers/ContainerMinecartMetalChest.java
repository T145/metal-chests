package T145.metalchests.containers;

import T145.metalchests.containers.base.ContainerMetalChestBase;
import T145.metalchests.entities.EntityMinecartMetalChest;
import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;

@ChestContainer(isLargeChest = true)
public class ContainerMinecartMetalChest extends ContainerMetalChestBase {

	private final EntityMinecartMetalChest cart;

	public ContainerMinecartMetalChest(EntityMinecartMetalChest cart, EntityPlayer player, int xSize, int ySize) {
		super(cart.getChestType(), cart.getInventory(), player, xSize, ySize);
		this.cart = cart;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return cart.isUsableByPlayer(player);
	}
}