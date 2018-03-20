package T145.metalchests.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.IItemHandler;

public interface IInventoryHandler {

	IItemHandler getInventory();

	void openInventory(EntityPlayer player);

	void closeInventory(EntityPlayer player);

	boolean isUsableByPlayer(EntityPlayer player);

	void onSlotChanged();
}