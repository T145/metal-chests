package T145.metalchests.proxies;

import T145.metalchests.entities.base.EntityMinecartMetalChestBase;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
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
		TileEntity te = world.getTileEntity(pos);

		switch (ID) {
		case 0:
			TileMetalChest chest = (TileMetalChest) te;
			return chest.createContainer(null, player);
		default:
			Entity entity = world.getEntityByID(ID);

			if (entity instanceof EntityMinecartMetalChestBase) {
				EntityMinecartMetalChestBase cart = (EntityMinecartMetalChestBase) entity;
				return cart.createContainer(null, player);
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
		TileMetalChest.registerFixesChest(FMLCommonHandler.instance().getDataFixer());
	}

	public void postInit(FMLPostInitializationEvent event) {}
}