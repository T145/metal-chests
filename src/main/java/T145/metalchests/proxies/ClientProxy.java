package T145.metalchests.proxies;

import T145.metalchests.client.gui.GuiMetalChest;
import T145.metalchests.client.gui.GuiProjectTable;
import T145.metalchests.containers.ContainerMetalChest;
import T145.metalchests.containers.ContainerProjectTable;
import T145.metalchests.entities.base.EntityMinecartMetalChestBase;
import T145.metalchests.tiles.TileMetalChest;
import T145.metalchests.tiles.TileProjectTable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		pos.setPos(x, y, z);

		switch (ID) {
		case 0:
			TileMetalChest chest = (TileMetalChest) world.getTileEntity(pos);
			ContainerMetalChest chestContainer = new ContainerMetalChest(chest, player, chest.getType());
			return new GuiMetalChest(chestContainer);
		case 1:
			TileProjectTable table = (TileProjectTable) world.getTileEntity(pos);
			return new GuiProjectTable(table, player, world);
		default:
			Entity entity = world.getEntityByID(ID);

			if (entity instanceof EntityMinecartMetalChestBase) {
				EntityMinecartMetalChestBase cart = (EntityMinecartMetalChestBase) entity;
				ContainerMetalChest cartContainer = new ContainerMetalChest(cart, player, cart.getChestType());
				return new GuiMetalChest(cartContainer);
			}

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