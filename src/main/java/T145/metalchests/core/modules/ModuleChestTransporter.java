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
package T145.metalchests.core.modules;

import java.util.Collection;
import java.util.Collections;

import T145.metalchests.api.BlocksMC;
import T145.metalchests.api.RegistryMC;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.ModSupport;
import cubex2.mods.chesttransporter.api.TransportableChest;
import cubex2.mods.chesttransporter.chests.TransportableChestOld;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import thaumcraft.api.blocks.BlocksTC;

@EventBusSubscriber(modid = RegistryMC.MOD_ID)
class ModuleChestTransporter {

    static class TransportableMetalChest extends TransportableChest {

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

    @Optional.Method(modid = ModSupport.ChestTransporter.MOD_ID)
    @SubscribeEvent
    public static void registerChestTransporter(final RegistryEvent.Register<TransportableChest> event) {
        final IForgeRegistry<TransportableChest> registry = event.getRegistry();

        for (ChestType type : ChestType.values()) {
            TransportableMetalChest chest = new TransportableMetalChest(BlocksMC.METAL_CHEST, type, new ResourceLocation(RegistryMC.MOD_ID, "item/chesttransporter/" + type.getName()));
            registry.register(chest);
            // ChestRegistry.registerMinecart(EntityMinecartMetalChest.class, chest);
        }

        if (ModSupport.hasThaumcraft()) {
            registry.register(new TransportableChestOld(BlocksTC.hungryChest, -1, 1, "vanilla"));

            for (ChestType type : ChestType.values()) {
                registry.register(new TransportableMetalChest(BlocksMC.HUNGRY_METAL_CHEST, type, new ResourceLocation(RegistryMC.MOD_ID, "item/chesttransporter/hungry/" + type.getName())));
            }
        }
    }
}
