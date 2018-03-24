package T145.metalchests.proxies;

import T145.metalchests.containers.ContainerMetalChest;
import T145.metalchests.containers.ContainerProjectTable;
import T145.metalchests.entities.base.EntityMinecartMetalChestBase;
import T145.metalchests.tiles.TileMetalChest;
import T145.metalchests.tiles.TileProjectTable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy implements IGuiHandler {

	protected final MutableBlockPos pos = new MutableBlockPos();

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		pos.setPos(x, y, z);

		switch (ID) {
		case 0:
			TileMetalChest chest = (TileMetalChest) world.getTileEntity(pos);
			return new ContainerMetalChest(chest, player, chest.getType());
		case 1:
			TileProjectTable table = (TileProjectTable) world.getTileEntity(pos);
			return new ContainerProjectTable(table, player);
		default:
			Entity entity = world.getEntityByID(ID);

			if (entity instanceof EntityMinecartMetalChestBase) {
				EntityMinecartMetalChestBase cart = (EntityMinecartMetalChestBase) entity;
				return new ContainerMetalChest(cart, player, cart.getChestType());
			}

			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	public void preInit(FMLPreInitializationEvent event) {
		OreDictionary.registerOre("blockGlass", Blocks.GLASS);
		OreDictionary.registerOre("blockObsidian", Blocks.OBSIDIAN);
	}

	public void init(FMLInitializationEvent event) {
		DataFixer fixer = FMLCommonHandler.instance().getDataFixer();

		TileMetalChest.registerFixesChest(fixer);
		EntityMinecart.registerFixesMinecart(fixer, EntityMinecartMetalChestBase.class);
	}

	public void postInit(FMLPostInitializationEvent event) {}
}