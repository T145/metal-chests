/*******************************************************************************
 * Copyright 2018 T145
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
package T145.metalchests.compat.chesttransporter;

import java.util.Collection;
import java.util.Collections;

import T145.metalchests.blocks.BlockMetalChest.ChestType;
import cubex2.mods.chesttransporter.api.TransportableChest;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TransportableMetalChest extends TransportableChest {

	private final Block chestBlock;
	private final ChestType type;
	private final ResourceLocation resource;

	public TransportableMetalChest(Block chestBlock, ChestType type, ResourceLocation resource) {
		this.chestBlock = chestBlock;
		this.type = type;
		this.resource = resource;
		setRegistryName(resource);
	}

	@Override
	public boolean canGrabChest(World world, BlockPos pos, IBlockState state, EntityPlayer player, ItemStack transporter) {
		Block block = state.getBlock();
		return block == chestBlock && block.getMetaFromState(state) == type.ordinal();
	}

	@Override
	public boolean canPlaceChest(World world, BlockPos pos, EntityPlayer player, ItemStack transporter) {
		return true;
	}

	@Override
	public ItemStack createChestStack(ItemStack transporter) {
		return new ItemStack(chestBlock, 1, type.ordinal());
	}

	@Override
	public Collection<ResourceLocation> getChestModels() {
		return Collections.singleton(resource);
	}

	@Override
	public ResourceLocation getChestModel(ItemStack stack) {
		return resource;
	}

	@Override
	public boolean copyTileEntity() {
		return true;
	}

	@Override
	public NBTTagCompound modifyTileCompound(NBTTagCompound tag, World world, BlockPos pos, EntityPlayer player, ItemStack transporter) {
		tag.setString("Front", player.getHorizontalFacing().getOpposite().toString());
		return super.modifyTileCompound(tag, world, pos, player, transporter);
	}
}
