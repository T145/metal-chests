package T145.metalchests.client.gui;

import T145.metalchests.client.gui.base.GuiMetalChestBase;
import T145.metalchests.containers.ContainerMetalChest;
import T145.metalchests.lib.MetalChestType;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMetalChest extends GuiMetalChestBase {

	public GuiMetalChest(MetalChestType.GUI gui, TileMetalChest chest, EntityPlayer player) {
		super(gui, new ContainerMetalChest(chest, player, gui.getSizeX(), gui.getSizeY()));
	}
}