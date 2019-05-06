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

import java.util.List;

import javax.annotation.Nullable;

import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.RegistryMC;
import T145.metalchests.api.immutable.SupportedMods;
import T145.metalchests.config.ModConfig;
import cofh.core.init.CoreEnchantments;
import cofh.core.item.IEnchantableItem;
import cofh.core.util.helpers.ItemHelper;
import cofh.core.util.helpers.MathHelper;
import cofh.core.util.helpers.StringHelper;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(modid = SupportedMods.THERMALEXPANSION_MOD_ID, iface = SupportedMods.IFACE_ENCHANTABLE_ITEM, striprefs = true)
public class BlockModItem extends ItemBlock implements IEnchantableItem {

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

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn) {
		if (ModConfig.hasThermalExpansion() && stack.hasTagCompound()) {
			byte enchantLevel = (byte) MathHelper.clamp(EnchantmentHelper.getEnchantmentLevel(CoreEnchantments.holding, stack), 0, CoreEnchantments.holding.getMaxLevel());

			if (!(enchantLevel > 0)) {
				return;
			}

			if (enchantLevel >= ChestType.byMetadata(stack.getItemDamage()).getHoldingEnchantBound()) {
				if (StringHelper.displayShiftForDetail && !StringHelper.isShiftKeyDown()) {
					tooltip.add(StringHelper.shiftForDetails());
				}
				if (!StringHelper.isShiftKeyDown()) {
					return;
				}
				ItemHelper.addInventoryInformation(stack, tooltip);
			} else {
				tooltip.add(StringHelper.getInfoText("info.metalchests.chest_info.fail"));
			}
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return ModConfig.hasThermalExpansion() && enchantment == CoreEnchantments.holding && !stack.isItemEnchanted() && stack.getCount() == 1;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return ModConfig.hasThermalExpansion() && stack.getCount() == 1;
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {
		return ModConfig.hasThermalExpansion() ? 10 : 0;
	}

	@Optional.Method(modid = SupportedMods.THERMALEXPANSION_MOD_ID)
	@Override
	public boolean canEnchant(ItemStack stack, Enchantment enchantment) {
		return enchantment == CoreEnchantments.holding;
	}
}
