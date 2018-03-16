package T145.metalchests.client.gui;

import T145.metalchests.client.gui.base.GuiMetalChestBase;
import T145.metalchests.containers.ContainerMinecartMetalChest;
import T145.metalchests.entities.EntityMinecartMetalChest;
import T145.metalchests.lib.MetalChestType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMinecartMetalChest extends GuiMetalChestBase {

	public GuiMinecartMetalChest(MetalChestType.GUI gui, EntityMinecartMetalChest cart, EntityPlayer player) {
		super(gui, new ContainerMinecartMetalChest(cart, player, gui.getSizeX(), gui.getSizeY()));
	}
}