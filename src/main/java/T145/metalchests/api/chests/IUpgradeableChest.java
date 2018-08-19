package T145.metalchests.api.chests;

import T145.metalchests.api.immutable.ChestType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;

public interface IUpgradeableChest {

	ChestType getChestType();

	void setChestType(ChestType chestType);

	IBlockState createBlockState();

	TileEntity createTileEntity();
}
