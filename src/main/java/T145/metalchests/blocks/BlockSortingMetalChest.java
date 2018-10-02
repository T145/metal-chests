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
package T145.metalchests.blocks;

import javax.annotation.Nullable;

import T145.metalchests.api.RegistryMC;
import T145.metalchests.items.ItemChestUpgrade;
import T145.metalchests.tiles.TileSortingMetalChest;
import net.blay09.mods.refinedrelocation.api.INameTaggable;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.network.VanillaPacketHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSortingMetalChest extends BlockMetalChest {

    @Override
    protected void registerResource() {
        this.registerResource(RegistryMC.RESOURCE_SORTING_METAL_CHEST);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileSortingMetalChest(state.getValue(VARIANT));
    }

    // because blay just couldn't make this `public static`
    protected boolean tryNameBlock(EntityPlayer player, ItemStack heldItem, IBlockAccess world, BlockPos pos) {
        if (!heldItem.isEmpty() && heldItem.getItem() == Items.NAME_TAG && heldItem.hasDisplayName()) {
            TileEntity te = world.getTileEntity(pos);

            if (te instanceof INameTaggable) {
                ((INameTaggable) te).setCustomName(heldItem.getDisplayName());
                VanillaPacketHandler.sendTileEntityUpdate(te);

                if (!player.capabilities.isCreativeMode) {
                    heldItem.shrink(1);
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            ItemStack stack = player.getHeldItem(hand);

            if (tryNameBlock(player, stack, world, pos)) {
                return true;
            }

            if (player.isSneaking() && !(stack.getItem() instanceof ItemChestUpgrade)) {
                TileEntity te = world.getTileEntity(pos);

                if (te != null) {
                    RefinedRelocationAPI.openRootFilterGui(player, te);
                }
            }

            return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
        }

        return true;
    }
}
