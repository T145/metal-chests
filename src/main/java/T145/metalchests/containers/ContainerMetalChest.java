package T145.metalchests.containers;

import T145.metalchests.containers.base.ContainerMetalChestBase;
import T145.metalchests.tiles.TileMetalChest;
import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;

@ChestContainer(isLargeChest = true)
public class ContainerMetalChest extends ContainerMetalChestBase {

	private final TileMetalChest chest;

	public ContainerMetalChest(TileMetalChest chest, EntityPlayer player, int xSize, int ySize) {
		super(chest.getType(), chest.getInventory(), player, xSize, ySize);
		this.chest = chest;
		chest.openInventory(player);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return chest.isUsableByPlayer(player);
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		chest.closeInventory(player);
	}
}