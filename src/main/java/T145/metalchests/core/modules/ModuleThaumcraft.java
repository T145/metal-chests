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
import T145.metalchests.api.ItemsMC;
import T145.metalchests.api.RegistryMC;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.ChestUpgrade;
import T145.metalchests.api.immutable.ModSupport;
import T145.metalchests.blocks.BlockHungryMetalChest;
import T145.metalchests.client.render.blocks.RenderHungryMetalChest;
import T145.metalchests.core.ModLoader;
import T145.metalchests.items.ItemChestUpgrade;
import T145.metalchests.tiles.TileHungryMetalChest;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.common.tiles.devices.TileHungryChest;

class ModuleThaumcraft {

    @EventBusSubscriber(modid = RegistryMC.MOD_ID)
    static class ServerLoader {

        @Optional.Method(modid = ModSupport.Thaumcraft.MOD_ID)
        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
            final IForgeRegistry<Block> registry = event.getRegistry();

            if (ModSupport.hasThaumcraft()) {
                registry.register(BlocksMC.HUNGRY_METAL_CHEST = new BlockHungryMetalChest());
                ModLoader.registerTileEntity(TileHungryMetalChest.class);
            }
        }

        @Optional.Method(modid = ModSupport.Thaumcraft.MOD_ID)
        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            final IForgeRegistry<Item> registry = event.getRegistry();

            if (ModSupport.hasThaumcraft()) {
                ModLoader.registerItemBlock(registry, BlocksMC.HUNGRY_METAL_CHEST, ChestType.class);

                ItemChestUpgrade upgrade = new ItemChestUpgrade(RegistryMC.RESOURCE_HUNGRY_CHEST_UPGRADE);
                upgrade.addDefaultChest(TileHungryChest.class, new TileHungryMetalChest());
                ItemsMC.HUNGRY_CHEST_UPGRADE = upgrade;
                registry.register(ItemsMC.HUNGRY_CHEST_UPGRADE);
            }
        }

        @Optional.Method(modid = ModSupport.Thaumcraft.MOD_ID)
        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
            if (ModSupport.hasThaumcraft()) {
                for (ChestType type : ChestType.values()) {
                    if (type.isRegistered()) {
                        OreDictionary.registerOre("chestHungry" + WordUtils.capitalize(type.getName()), new ItemStack(BlocksMC.HUNGRY_METAL_CHEST, 1, type.ordinal()));
                    }
                }

                if (ChestType.COPPER.isRegistered()) {
                    ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(RegistryMC.MOD_ID, "HungryCopperChest"),
                            new ShapedArcaneRecipe(ModSupport.Thaumcraft.DEFAULT_GROUP, "HUNGRYMETALCHESTS", 15, new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1),
                                    new ItemStack(BlocksMC.HUNGRY_METAL_CHEST, 1, 0),
                                    "III", "ICI", "III",
                                    'I', "ingotCopper",
                                    'C', new ItemStack(BlocksTC.hungryChest)));
                    ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(RegistryMC.MOD_ID, "HungryIronChest"),
                            new ShapedArcaneRecipe(ModSupport.Thaumcraft.DEFAULT_GROUP, "HUNGRYMETALCHESTS", 15, new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1),
                                    new ItemStack(BlocksMC.HUNGRY_METAL_CHEST, 1, 1),
                                    "III", "ICI", "III",
                                    'I', "ingotIron",
                                    'C', new ItemStack(BlocksMC.HUNGRY_METAL_CHEST, 1, 0)));
                } else {
                    ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(RegistryMC.MOD_ID, "HungryIronChest"),
                            new ShapedArcaneRecipe(ModSupport.Thaumcraft.DEFAULT_GROUP, "HUNGRYMETALCHESTS", 15, new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1),
                                    new ItemStack(BlocksMC.HUNGRY_METAL_CHEST, 1, 1),
                                    "III", "ICI", "III",
                                    'I', "ingotIron",
                                    'C', new ItemStack(BlocksTC.hungryChest)));
                }

                if (ChestType.SILVER.isRegistered()) {
                    ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(RegistryMC.MOD_ID, "HungrySilverChest"),
                            new ShapedArcaneRecipe(ModSupport.Thaumcraft.DEFAULT_GROUP, "HUNGRYMETALCHESTS", 15, new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1),
                                    new ItemStack(BlocksMC.HUNGRY_METAL_CHEST, 1, 2),
                                    "III", "ICI", "III",
                                    'I', "ingotSilver",
                                    'C', new ItemStack(BlocksMC.HUNGRY_METAL_CHEST, 1, 1)));
                    ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(RegistryMC.MOD_ID, "HungryGoldChest"),
                            new ShapedArcaneRecipe(ModSupport.Thaumcraft.DEFAULT_GROUP, "HUNGRYMETALCHESTS", 15, new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1),
                                    new ItemStack(BlocksMC.HUNGRY_METAL_CHEST, 1, 3),
                                    "III", "ICI", "III",
                                    'I', "ingotGold",
                                    'C', new ItemStack(BlocksMC.HUNGRY_METAL_CHEST, 1, 2)));
                } else {
                    ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(RegistryMC.MOD_ID, "HungryGoldChest"),
                            new ShapedArcaneRecipe(ModSupport.Thaumcraft.DEFAULT_GROUP, "HUNGRYMETALCHESTS", 15, new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1),
                                    new ItemStack(BlocksMC.HUNGRY_METAL_CHEST, 1, 3),
                                    "III", "ICI", "III",
                                    'I', "ingotGold",
                                    'C', new ItemStack(BlocksMC.HUNGRY_METAL_CHEST, 1, 1)));
                }

                ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(RegistryMC.MOD_ID, "HungryDiamondChest"),
                        new ShapedArcaneRecipe(ModSupport.Thaumcraft.DEFAULT_GROUP, "HUNGRYMETALCHESTS", 15, new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1),
                                new ItemStack(BlocksMC.HUNGRY_METAL_CHEST, 1, 4),
                                "III", "ICI", "III",
                                'I', "gemDiamond",
                                'C', new ItemStack(BlocksMC.HUNGRY_METAL_CHEST, 1, 3)));
                ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(RegistryMC.MOD_ID, "HungryObsidianChest"),
                        new ShapedArcaneRecipe(ModSupport.Thaumcraft.DEFAULT_GROUP, "HUNGRYMETALCHESTS", 15, new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1),
                                new ItemStack(BlocksMC.HUNGRY_METAL_CHEST, 1, 5),
                                "III", "ICI", "III",
                                'I', "obsidian",
                                'C', new ItemStack(BlocksMC.HUNGRY_METAL_CHEST, 1, 4)));

                for (ChestUpgrade upgrade : ChestUpgrade.values()) {
                    switch (upgrade) {
                    case COPPER_GOLD:
                    case IRON_GOLD:
                    case WOOD_GOLD:
                        ThaumcraftApi.addArcaneCraftingRecipe(
                                new ResourceLocation(RegistryMC.MOD_ID, upgrade.getName() + "_hungry_chest_upgrade"),
                                new ShapedArcaneRecipe(ModSupport.Thaumcraft.DEFAULT_GROUP, "HUNGRYMETALCHESTSUPGRADES", 15, new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1),
                                        new ItemStack(ItemsMC.HUNGRY_CHEST_UPGRADE, 1, upgrade.ordinal()),
                                        "III", "III", "CII",
                                        'I', upgrade.getUpgrade().getOreName(),
                                        'C', new ItemStack(ItemsMC.HUNGRY_CHEST_UPGRADE, 1, ChestType.SILVER.isRegistered() ? upgrade.getPriorUpgrade().ordinal() : upgrade.getPriorUpgrade().getPriorUpgrade().ordinal())));
                        break;
                    case WOOD_IRON:
                        ThaumcraftApi.addArcaneCraftingRecipe(
                                new ResourceLocation(RegistryMC.MOD_ID, upgrade.getName() + "_hungry_chest_upgrade"),
                                new ShapedArcaneRecipe(ModSupport.Thaumcraft.DEFAULT_GROUP, "HUNGRYMETALCHESTSUPGRADES", 15, new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1),
                                        new ItemStack(ItemsMC.HUNGRY_CHEST_UPGRADE, 1, upgrade.ordinal()),
                                        "III", "III", "CII",
                                        'I', upgrade.getUpgrade().getOreName(),
                                        'C', ChestType.COPPER.isRegistered() ? new ItemStack(ItemsMC.HUNGRY_CHEST_UPGRADE, 1, upgrade.getPriorUpgrade().ordinal()) : new ItemStack(BlocksTC.plankGreatwood)));
                        break;
                    default:
                        ThaumcraftApi.addArcaneCraftingRecipe(
                                new ResourceLocation(RegistryMC.MOD_ID, upgrade.getName() + "_hungry_chest_upgrade"),
                                new ShapedArcaneRecipe(ModSupport.Thaumcraft.DEFAULT_GROUP, "HUNGRYMETALCHESTSUPGRADES", 15, new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1),
                                        new ItemStack(ItemsMC.HUNGRY_CHEST_UPGRADE, 1, upgrade.ordinal()),
                                        "III", "III", "CII",
                                        'I', upgrade.getUpgrade().getOreName(),
                                        'C', getBaseIngredient(upgrade)));
                        break;
                    }
                }
            }
        }

        private static Object getBaseIngredient(ChestUpgrade upgrade) {
            ChestUpgrade prior = upgrade.getPriorUpgrade();
            int priorIndex = prior.ordinal();

            if (priorIndex == 0) {
                return new ItemStack(BlocksTC.plankGreatwood);
            } else {
                if (prior.getBase() != upgrade.getBase()) {
                    return upgrade.getBase().getOreName();
                } else {
                    return new ItemStack(ItemsMC.HUNGRY_CHEST_UPGRADE, 1, priorIndex);
                }
            }
        }
    }

    @EventBusSubscriber(modid = RegistryMC.MOD_ID, value = Side.CLIENT)
    static class ClientLoader {

        @Optional.Method(modid = ModSupport.Thaumcraft.MOD_ID)
        @SubscribeEvent
        public static void onModelRegistration(ModelRegistryEvent event) {
            if (ModSupport.hasThaumcraft()) {
                for (ChestType type : ChestType.values()) {
                    ModLoader.registerModel(BlocksMC.HUNGRY_METAL_CHEST, type.ordinal(), ModLoader.getVariantName(type));
                }

                ModLoader.registerTileRenderer(TileHungryMetalChest.class, new RenderHungryMetalChest());

                for (ChestUpgrade type : ChestUpgrade.values()) {
                    ModLoader.registerModel(ItemsMC.HUNGRY_CHEST_UPGRADE, "item_hungry_chest_upgrade", type.ordinal(), "item=" + type.getName());
                }
            }
        }
    }
}
