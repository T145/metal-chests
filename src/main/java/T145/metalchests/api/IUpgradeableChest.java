package T145.metalchests.api;

import T145.metalchests.blocks.BlockMetalChest.ChestType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;

public interface IUpgradeableChest {

	ChestType getChestType();

	void setChestType(ChestType chestType);

	IBlockState createBlockState();

	TileEntity createTileEntity();
}
