/*******************************************************************************
 * Copyright 2019 T145
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
package T145.metalchests.blocks;

import javax.annotation.Nullable;

import T145.metalchests.api.immutable.RegistryMC;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class BlockModItem extends ItemBlock {

	private final Class<? extends Enum<? extends IStringSerializable>> blockTypes;

	public BlockModItem(Block block, Class<? extends Enum<? extends IStringSerializable>> blockTypes) {
		super(block);
		this.blockTypes = blockTypes;
		setHasSubtypes(blockTypes != null);
	}

	public BlockModItem(Block block) {
		this(block, null);
	}

	@Nullable
	@Override
	public String getCreatorModId(ItemStack stack) {
		return RegistryMC.MOD_ID;
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		StringBuilder name = new StringBuilder(super.getTranslationKey());

		if (hasSubtypes) {
			name.append('.').append(blockTypes.getEnumConstants()[stack.getMetadata()].name().toLowerCase());
		}

		return name.toString();
	}
}
