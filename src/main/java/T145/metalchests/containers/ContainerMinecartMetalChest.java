package T145.metalchests.containers;

import T145.metalchests.containers.base.ContainerMetalChestBase;
import T145.metalchests.entities.base.EntityMinecartMetalChestBase;
import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;

@ChestContainer(isLargeChest = true)
public class ContainerMinecartMetalChest extends ContainerMetalChestBase {

	private final EntityMinecartMetalChestBase cart;

	public ContainerMinecartMetalChest(EntityMinecartMetalChestBase cart, EntityPlayer player) {
		super(cart.getChestType());
		this.cart = cart;
		cart.openInventory(player);
		layoutInventory(this, null, cart);
		layoutPlayerInventory(player);
	}

	@Override
	protected void onSlotChanged() {
		cart.updateChestInstance();
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		cart.closeInventory(player);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return cart.isUsableByPlayer(player);
	}
}