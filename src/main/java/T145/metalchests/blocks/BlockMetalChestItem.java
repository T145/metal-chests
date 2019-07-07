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
package t145.metalchests.blocks;

import java.util.List;

import javax.annotation.Nullable;

import T145.tbone.items.TItemBlock;
import cofh.core.init.CoreEnchantments;
import cofh.core.item.IEnchantableItem;
import cofh.core.util.helpers.MathHelper;
import cofh.core.util.helpers.StringHelper;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Optional;
import t145.metalchests.api.config.ConfigMC;
import t145.metalchests.api.consts.ChestType;
import t145.metalchests.api.consts.RegistryMC;

@Optional.Interface(modid = RegistryMC.ID_THERMALEXPANSION, iface = RegistryMC.IFACE_ENCHANTABLE_ITEM, striprefs = true)
public class BlockMetalChestItem extends TItemBlock implements IEnchantableItem {

	public BlockMetalChestItem(List<ChestType> types, Block block) {
		super(types, block);
	}

	@Nullable
	@Override
	public String getCreatorModId(ItemStack stack) {
		return RegistryMC.ID;
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		if (hasSubtypes) {
			return String.format("%s.%s", super.getTranslationKey(), ChestType.values()[stack.getItemDamage()].getName());
		}
		return super.getTranslationKey(stack);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn) {
		if (ConfigMC.hasThermalExpansion() && stack.hasTagCompound()) {
			byte enchantLevel = (byte) MathHelper.clamp(EnchantmentHelper.getEnchantmentLevel(CoreEnchantments.holding, stack), 0, CoreEnchantments.holding.getMaxLevel());

			if (enchantLevel <= 0) {
				return;
			}

			ChestType type = ChestType.values()[stack.getItemDamage()];

			if (enchantLevel >= type.getHolding()) {
				NBTTagCompound tag = stack.getTagCompound().getCompoundTag("Inventory");
				NBTTagList tagList = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND);

				if (tagList.tagCount() > 0) {
					if (StringHelper.displayShiftForDetail && !StringHelper.isShiftKeyDown()) {
						tooltip.add(StringHelper.shiftForDetails());
					}

					if (!StringHelper.isShiftKeyDown()) {
						return;
					}

					for (int i = 0; i < tagList.tagCount(); i++) {
						NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
						int slot = itemTags.getInteger("Slot");
						ItemStack slotStack = new ItemStack(itemTags);

						if (slot >= 0 && slot < type.getInventorySize()) {
							tooltip.add(String.format("    %sx %s", itemTags.getInteger("Count"), slotStack.getDisplayName()));
						}
					}
				}
			} else {
				tooltip.add(StringHelper.getInfoText("info.metalchests.chest_info.fail"));
			}
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return ConfigMC.hasThermalExpansion() && enchantment == CoreEnchantments.holding && !stack.isItemEnchanted() && stack.getCount() == 1;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return ConfigMC.hasThermalExpansion() && stack.getCount() == 1;
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {
		return ConfigMC.hasThermalExpansion() ? 10 : 0;
	}

	@Optional.Method(modid = RegistryMC.ID_THERMALEXPANSION)
	@Override
	public boolean canEnchant(ItemStack stack, Enchantment enchantment) {
		return enchantment == CoreEnchantments.holding;
	}
}
