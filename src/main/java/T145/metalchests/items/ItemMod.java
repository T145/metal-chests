/*******************************************************************************
 * Copyright 2018-2019 T145
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package T145.metalchests.items;

import T145.metalchests.api.constants.RegistryMC;
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

	protected final Object[] types;

	public ItemMod(ResourceLocation registryName, Object[] types) {
		this.types = types;
		setRegistryName(registryName);
		setTranslationKey(registryName.toString());
		setHasSubtypes(types != null);
		setCreativeTab(MetalChests.TAB);
	}

	public ItemMod(ResourceLocation registryName) {
		this(registryName, null);
	}

	public Object[] getTypes() {
		return types;
	}

	@Override
	public String getCreatorModId(ItemStack stack) {
		return RegistryMC.ID;
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		if (hasSubtypes && types[0] instanceof IStringSerializable) {
			return String.format("%s.%s", super.getTranslationKey(), ((IStringSerializable) types[stack.getMetadata()]).getName());
		}
		return super.getTranslationKey(stack);
	}

	@SideOnly(Side.CLIENT)
	public void prepareCreativeTab(NonNullList<ItemStack> items) {
		for (int meta = 0; meta < types.length; ++meta) {
			items.add(new ItemStack(this, 1, meta));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (tab == MetalChests.TAB) {
			if (hasSubtypes) {
				prepareCreativeTab(items);
			} else {
				items.add(new ItemStack(this));
			}
		}
	}
}
