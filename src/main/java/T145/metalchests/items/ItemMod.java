package T145.metalchests.items;

import T145.metalchests.core.MetalChests;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMod extends Item {

	private final Enum<? extends IStringSerializable>[] types;

	public ItemMod(String name, Enum<? extends IStringSerializable>[] types) {
		this.types = types;
		setRegistryName(new ResourceLocation(MetalChests.MOD_ID, name));
		setUnlocalizedName(MetalChests.MOD_ID + ':' + name);
		setHasSubtypes(types != null);
		setCreativeTab(MetalChests.TAB);
	}

	public Enum<? extends IStringSerializable>[] getTypes() {
		return types;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (hasSubtypes) {
			return super.getUnlocalizedName() + "." + ((IStringSerializable) types[stack.getMetadata()]).getName();
		}
		return super.getUnlocalizedName(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (hasSubtypes) {
			for (int meta = 0; meta < types.length; ++meta) {
				items.add(new ItemStack(this, 1, meta));
			}
		} else {
			items.add(new ItemStack(this));
		}
	}
}