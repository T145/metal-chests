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
package T145.metalchests.items;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import T145.metalchests.api.BlocksMC;
import T145.metalchests.api.chests.IMetalChest;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.ChestUpgrade;
import T145.metalchests.lib.items.ItemMod;
import net.minecraft.block.BlockChest;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemChestUpgrade extends ItemMod {

    public ItemChestUpgrade(ResourceLocation registryName) {
        super(registryName, ChestUpgrade.values());
        setMaxStackSize(1);
    }

    private final Map<Class, IMetalChest> defaultChests = new HashMap<>();

    public void addDefaultChest(Class tileClass, IMetalChest tileUpgrade) {
        defaultChests.put(tileClass, tileUpgrade);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (world.isRemote || !player.isSneaking()) {
            return EnumActionResult.PASS;
        }

        TileEntity te = world.getTileEntity(pos);
        ItemStack stack = player.getHeldItem(hand);
        ChestUpgrade upgrade = ChestUpgrade.byMetadata(stack.getItemDamage());

        if (te instanceof IMetalChest) {
            IMetalChest chest = (IMetalChest) te;

            if (chest.getChestType() == upgrade.getBase() && chest.canApplyUpgrade(upgrade, te, stack)) {
                ItemStackHandler oldInventory = (ItemStackHandler) chest.getInventory();

                chest.setChestType(upgrade.getUpgrade());
                te.markDirty();

                IBlockState state = createBlockState(te, upgrade.getUpgrade());
                world.setBlockState(pos, state, 3);
                world.notifyBlockUpdate(pos, state, state, 3);

                chest.setInventory(oldInventory);
            } else {
                return EnumActionResult.FAIL;
            }
        } else if (defaultChests.containsKey(te.getClass())) {
            IMetalChest chest = defaultChests.get(te.getClass());
            EnumFacing front = getFrontFromProperties(world, pos);
            IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            ItemStackHandler newInv = new ItemStackHandler(upgrade.getUpgrade().getInventorySize());

            for (int slot = 0; slot < inv.getSlots(); ++slot) {
                newInv.setStackInSlot(slot, inv.getStackInSlot(slot));
            }

            te.updateContainingBlockInfo();

            if (te instanceof TileEntityChest) {
                TileEntityChest vanillaChest = (TileEntityChest) te;

                if (vanillaChest.getChestType() == BlockChest.Type.TRAP) {
                    return EnumActionResult.FAIL;
                }

                vanillaChest.checkForAdjacentChests();
            }

            world.removeTileEntity(pos);
            world.setBlockToAir(pos);
            world.setTileEntity(pos, chest.createTileEntity(upgrade.getUpgrade()));

            IBlockState state = createBlockState(te, upgrade.getUpgrade());
            world.setBlockState(pos, state, 3);
            world.notifyBlockUpdate(pos, state, state, 3);

            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof IMetalChest) {
                IMetalChest metalChest = (IMetalChest) tile;
                metalChest.setInventory(newInv);
                metalChest.setFront(front);
            }
        } else {
            return EnumActionResult.PASS;
        }

        if (!player.capabilities.isCreativeMode) {
            stack.shrink(1);
        }

        player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.4F, 0.8F);

        return EnumActionResult.SUCCESS;
    }

    private IBlockState createBlockState(TileEntity oldTile, ChestType upgrade) {
        if (oldTile instanceof IMetalChest) {
            return oldTile.getBlockType().getDefaultState().withProperty(IMetalChest.VARIANT, upgrade);
        } else {
            return BlocksMC.METAL_CHEST.getDefaultState().withProperty(IMetalChest.VARIANT, upgrade);
        }
    }

    @Nullable
    private EnumFacing getFrontFromProperties(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);

        for (IProperty<?> prop : state.getProperties().keySet()) {
            if ((prop.getName().equals("facing") || prop.getName().equals("rotation")) && prop.getValueClass() == EnumFacing.class) {
                IProperty<EnumFacing> facingProperty = (IProperty<EnumFacing>) prop;
                return state.getValue(facingProperty);
            }
        }

        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void prepareCreativeTab(NonNullList<ItemStack> items) {
        for (ChestUpgrade upgrade : ChestUpgrade.values()) {
            if (upgrade.isRegistered()) {
                items.add(new ItemStack(this, 1, upgrade.ordinal()));
            }
        }
    }
}
