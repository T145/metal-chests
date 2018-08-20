package T145.metalchests.api.chests;

import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.ChestUpgrade;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public interface IMetalChest extends IInventoryHandler {

	ChestType getChestType();

	void setChestType(ChestType chestType);

	boolean canApplyUpgrade(ChestUpgrade upgrade, TileEntity chest, ItemStack upgradeStack);

	IBlockState createBlockState(ChestType chestType);

	TileEntity createTileEntity(ChestType chestType);

	EnumFacing getFront();

	void setFront(EnumFacing front);
}
