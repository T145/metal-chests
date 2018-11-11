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

import org.apache.commons.lang3.text.WordUtils;

import T145.metalchests.api.BlocksMC;
import T145.metalchests.api.chests.IMetalChest;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.ModSupport;
import T145.metalchests.api.immutable.RegistryMC;
import T145.metalchests.blocks.BlockSortingMetalChest;
import T145.metalchests.client.render.blocks.RenderSortingMetalChest;
import T145.metalchests.core.ModLoader;
import T145.metalchests.tiles.TileHungryMetalChest;
import T145.metalchests.tiles.TileMetalChest;
import T145.metalchests.tiles.TileSortingHungryMetalChest;
import T145.metalchests.tiles.TileSortingMetalChest;
import net.blay09.mods.refinedrelocation.item.ItemSortingUpgrade;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

public class ModuleRefinedRelocation {

    @EventBusSubscriber(modid = RegistryMC.MOD_ID)
    static class ServerLoader {

        @Optional.Method(modid = ModSupport.RefinedRelocation.MOD_ID)
        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
            if (ModSupport.hasRefinedRelocation()) {
                final IForgeRegistry<Block> registry = event.getRegistry();

                registry.register(BlocksMC.SORTING_METAL_CHEST = new BlockSortingMetalChest());
                ModLoader.registerTileEntity(TileSortingMetalChest.class);
            }
        }

        @Optional.Method(modid = ModSupport.RefinedRelocation.MOD_ID)
        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            if (ModSupport.hasRefinedRelocation()) {
                final IForgeRegistry<Item> registry = event.getRegistry();

                ModLoader.registerItemBlock(registry, BlocksMC.SORTING_METAL_CHEST, ChestType.class);
            }
        }

        @Optional.Method(modid = ModSupport.RefinedRelocation.MOD_ID)
        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
            if (ModSupport.hasRefinedRelocation()) {
                for (ChestType type : ChestType.values()) {
                    if (type.isRegistered()) {
                        OreDictionary.registerOre("chestSorting" + WordUtils.capitalize(type.getName()), new ItemStack(BlocksMC.SORTING_METAL_CHEST, 1, type.ordinal()));
                    }
                }
            }
        }

        @Optional.Method(modid = ModSupport.RefinedRelocation.MOD_ID)
        @SubscribeEvent
        public static void processInitialInteract(PlayerInteractEvent event) {
            if (event.getWorld().isRemote) {
                return;
            }

            ItemStack stack = event.getItemStack();

            if (stack.getItem() instanceof ItemSortingUpgrade) {
                World world = event.getWorld();
                BlockPos pos = event.getPos();
                TileEntity te = world.getTileEntity(pos);

                if (te instanceof TileMetalChest) {
                    TileMetalChest oldChest = (TileMetalChest) te;
                    TileMetalChest newChest;
                    Block block;

                    if (te instanceof TileHungryMetalChest) {
                        block = BlocksMC.SORTING_HUNGRY_METAL_CHEST;
                        newChest = new TileSortingHungryMetalChest(oldChest.getChestType());
                    } else {
                        block = BlocksMC.SORTING_METAL_CHEST;
                        newChest = new TileSortingMetalChest(oldChest.getChestType());
                    }

                    te.updateContainingBlockInfo();

                    world.removeTileEntity(pos);
                    world.setBlockToAir(pos);
                    world.setTileEntity(pos, newChest);

                    IBlockState state = block.getDefaultState().withProperty(IMetalChest.VARIANT, newChest.getChestType());
                    world.setBlockState(pos, state, 3);
                    world.notifyBlockUpdate(pos, state, state, 3);

                    TileEntity tile = world.getTileEntity(pos);

                    if (tile instanceof TileMetalChest) {
                        TileMetalChest chest = (TileMetalChest) tile;
                        chest.setInventory(oldChest.getInventory());
                        chest.setFront(oldChest.getFront());
                    }
                }
            }
        }
    }

    @EventBusSubscriber(modid = RegistryMC.MOD_ID, value = Side.CLIENT)
    static class ClientLoader {

        @Optional.Method(modid = ModSupport.RefinedRelocation.MOD_ID)
        @SubscribeEvent
        public static void onModelRegistration(ModelRegistryEvent event) {
            if (ModSupport.hasRefinedRelocation()) {
                for (ChestType type : ChestType.values()) {
                    ModLoader.registerModel(BlocksMC.SORTING_METAL_CHEST, type.ordinal(), ModLoader.getVariantName(type));
                }

                ModLoader.registerTileRenderer(TileSortingMetalChest.class, new RenderSortingMetalChest());

                if (ModSupport.hasThaumcraft()) {
                    for (ChestType type : ChestType.values()) {
                        ModLoader.registerModel(BlocksMC.SORTING_HUNGRY_METAL_CHEST, type.ordinal(), ModLoader.getVariantName(type));
                    }

                    ModLoader.registerTileRenderer(TileSortingHungryMetalChest.class, new RenderSortingMetalChest(BlocksMC.SORTING_HUNGRY_METAL_CHEST) {

                        @Override
                        protected ResourceLocation getActiveResource(ChestType type) {
                            return new ResourceLocation(RegistryMC.MOD_ID, "textures/entity/chest/hungry/" + type.getName() + ".png");
                        }

                        @Override
                        protected ResourceLocation getActiveOverlay(ChestType type) {
                            return new ResourceLocation(RegistryMC.MOD_ID, "textures/entity/chest/hungry/overlay/" + type.getName() + ".png");
                        }
                    });
                }
            }
        }
    }
}
