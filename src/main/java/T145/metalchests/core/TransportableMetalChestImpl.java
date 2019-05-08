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
package T145.metalchests.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.RegistryMC;
import cubex2.mods.chesttransporter.chests.TransportableChestImpl;
import joptsimple.internal.Strings;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

class TransportableMetalChestImpl extends TransportableChestImpl {

	private final String prefix;

	public TransportableMetalChestImpl(Block chestBlock, String prefix, String name) {
		super(chestBlock, -1, name);
		this.prefix = prefix;
	}

	public TransportableMetalChestImpl(Block chestBlock, String name) {
		this(chestBlock, Strings.EMPTY, name);
	}

	@Override
	public boolean copyTileEntity() {
		return true;
	}

	@Override
	public ItemStack createChestStack(ItemStack transporter) {
		ItemStack stack = super.createChestStack(transporter);
		NBTTagCompound tag = transporter.getTagCompound().getCompoundTag("ChestTile");
		stack.setItemDamage(ChestType.valueOf(tag.getString("ChestType")).ordinal());
		return stack;
	}

	@Override
	public ResourceLocation getChestModel(ItemStack transporter) {
		NBTTagCompound tag = transporter.getTagCompound().getCompoundTag("ChestTile");
		return new ResourceLocation(RegistryMC.ID, String.format("item/chesttransporter/%s%s", prefix, tag.getString("ChestType").toLowerCase()));
	}

	@Override
	public Collection<ResourceLocation> getChestModels() {
		List<ResourceLocation> models = new ArrayList<>();

		for (ChestType type : ChestType.values()) {
			models.add(new ResourceLocation(RegistryMC.ID, String.format("item/chesttransporter/%s%s", prefix, type.getName())));
		}

		return models;
	}

	@Override
	public NBTTagCompound modifyTileCompound(NBTTagCompound tag, World world, BlockPos pos, EntityPlayer player, ItemStack transporter) {
		tag.setString("Front", player.getHorizontalFacing().getOpposite().toString());
		return super.modifyTileCompound(tag, world, pos, player, transporter);
	}
}
