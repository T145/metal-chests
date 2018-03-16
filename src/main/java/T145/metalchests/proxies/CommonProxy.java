package T145.metalchests.proxies;

import T145.metalchests.containers.ContainerMetalChest;
import T145.metalchests.containers.ContainerMinecartMetalChest;
import T145.metalchests.entities.EntityMinecartMetalChest;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);

		switch (ID) {
		case 0:
			return new ContainerMetalChest((TileMetalChest) te, player, 0, 0);
		default:
			Entity entity = world.getEntityByID(ID);

			if (entity instanceof EntityMinecartMetalChest) {
				EntityMinecartMetalChest cart = (EntityMinecartMetalChest) entity;
				return new ContainerMinecartMetalChest(cart, player, 0, 0);
			}

			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	public void preInit(FMLPreInitializationEvent event) {
		OreDictionary.registerOre("blockObsidian", Blocks.OBSIDIAN);
	}

	public void init(FMLInitializationEvent event) {
		DataFixer fixer = FMLCommonHandler.instance().getDataFixer();

		TileMetalChest.registerFixesChest(fixer);
		EntityMinecartMetalChest.addDataFixers(fixer);
	}

	public void postInit(FMLPostInitializationEvent event) {}
}