package t145.metalchests.client.gui;

import net.minecraft.client.gui.ScreenManager.IScreenFactory;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import t145.metalchests.containers.MetalChestContainer;

public class MetalChestScreenFactory implements IScreenFactory<MetalChestContainer, MetalChestScreen> {

	@Override
	public MetalChestScreen create(MetalChestContainer container, PlayerInventory inv, ITextComponent title) {
		return new MetalChestScreen(container, inv);
	}

}
