package t145.metalchests.containers;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.IContainerFactory;
import t145.metalchests.tiles.MetalChestTile;

public class MetalChestContainerFactory implements IContainerFactory<MetalChestContainer> {

	@Override
	public MetalChestContainer create(int windowId, PlayerInventory inv, PacketBuffer data) {
		World world = inv.player.world;
		BlockPos pos = data.readBlockPos();
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof MetalChestTile) {
			return new MetalChestContainer(windowId, inv, (MetalChestTile) te);
		}

		return null;
	}
}
