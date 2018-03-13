package T145.metalchests.lib;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class AccessibleStackHandler extends ItemStackHandler {

	public AccessibleStackHandler(int size) {
		super(size);
	}

	public int size() {
		return stacks.size();
	}

	public NonNullList<ItemStack> getStacks() {
		return stacks;
	}

	@Override
	public void onLoad() {}

	@Override
	public void onContentsChanged(int slot) {}
}