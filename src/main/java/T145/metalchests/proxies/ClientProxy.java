package T145.metalchests.proxies;

import T145.metalchests.client.gui.GuiMetalChest;
import T145.metalchests.lib.MetalChestType;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);

		switch (ID) {
		case 0:
			TileMetalChest chest = (TileMetalChest) te;
			MetalChestType.GUI gui = MetalChestType.GUI.byType(chest.getType());
			return new GuiMetalChest(gui, chest, player);
		default:
			return null;
		}
	}

	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}

	public void init(FMLInitializationEvent event) {
		super.init(event);
	}

	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}
}