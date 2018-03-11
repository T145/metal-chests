package T145.metalchests.items.base;

import T145.metalchests.MetalChests;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBase extends Item {

	private final Enum<? extends IStringSerializable>[] itemTypes;

	public ItemBase(String name, Enum<? extends IStringSerializable>[] itemTypes) {
		this.itemTypes = itemTypes;
		setRegistryName(new ResourceLocation(MetalChests.MODID, name));
		setUnlocalizedName(MetalChests.MODID + ':' + name);
		setCreativeTab(CreativeTabs.DECORATIONS);
		setHasSubtypes(itemTypes != null);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (hasSubtypes) {
			return super.getUnlocalizedName() + "." + ((IStringSerializable) itemTypes[stack.getMetadata()]).getName();
		}
		return super.getUnlocalizedName(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (hasSubtypes) {
			for (int meta = 0; meta < itemTypes.length; ++meta) {
				items.add(new ItemStack(this, 1, meta));
			}
		} else {
			super.getSubItems(tab, items);
		}
	}
}