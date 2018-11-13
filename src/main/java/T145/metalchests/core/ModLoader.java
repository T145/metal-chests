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
package T145.metalchests.core;

import java.util.HashSet;

import org.apache.commons.lang3.text.WordUtils;

import T145.metalchests.api.BlocksMC;
import T145.metalchests.api.EntitiesMC;
import T145.metalchests.api.ItemsMC;
import T145.metalchests.api.chests.UpgradeRegistry;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.ChestUpgrade;
import T145.metalchests.api.immutable.ModSupport;
import T145.metalchests.api.immutable.RegistryMC;
import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.client.render.blocks.RenderMetalChest;
import T145.metalchests.client.render.entities.RenderMinecartMetalChest;
import T145.metalchests.config.ModConfig;
import T145.metalchests.entities.EntityMinecartMetalChest;
import T145.metalchests.entities.ai.EntityAIOcelotSitOnChest;
import T145.metalchests.items.ItemChestUpgrade;
import T145.metalchests.items.ItemMetalMinecart;
import T145.metalchests.lib.items.BlockModItem;
import T145.metalchests.tiles.TileMetalChest;
import net.blay09.mods.refinedrelocation.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

public class ModLoader {

    public static void registerTileEntity(Class tileClass) {
        GameRegistry.registerTileEntity(tileClass, new ResourceLocation(RegistryMC.MOD_ID, tileClass.getSimpleName()));
    }

    public static void registerItemBlock(IForgeRegistry<Item> registry, Block block) {
        registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }

    public static void registerItemBlock(IForgeRegistry<Item> registry, Block block, Class types) {
        registry.register(new BlockModItem(block, types).setRegistryName(block.getRegistryName()));
    }

    public static String getVariantName(IStringSerializable variant) {
        return "variant=" + variant.getName();
    }

    public static Object getUpgradeBase(Object base, Item upgrade, ChestUpgrade type) {
        switch (type) {
        case WOOD_COPPER:
            return base;
        case WOOD_IRON:
            if (!ChestType.COPPER.isRegistered()) {
                return base;
            } else {
                break;
            }
        case WOOD_GOLD:
            if (!ChestType.SILVER.isRegistered()) {
                return new ItemStack(upgrade, 1, ChestUpgrade.WOOD_IRON.ordinal());
            } else {
                break;
            }
        case COPPER_IRON: case IRON_SILVER: case SILVER_GOLD: case GOLD_DIAMOND: case DIAMOND_OBSIDIAN:
            return type.getBase().getOreName();
        case COPPER_GOLD:
            if (!ChestType.SILVER.isRegistered()) {
                return new ItemStack(upgrade, 1, ChestUpgrade.COPPER_IRON.ordinal());
            } else {
                break;
            }
        case IRON_GOLD:
            if (!ChestType.SILVER.isRegistered()) {
                return type.getBase().getOreName();
            } else {
                break;
            }
        default:
            break;
        }
        return new ItemStack(upgrade, 1, type.ordinal() - 1);
    }

    @EventBusSubscriber(modid = RegistryMC.MOD_ID)
    static class ServerLoader {

        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
            final IForgeRegistry<Block> registry = event.getRegistry();

            registry.register(BlocksMC.METAL_CHEST = new BlockMetalChest());
            registerTileEntity(TileMetalChest.class);
        }

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            final IForgeRegistry<Item> registry = event.getRegistry();

            registerItemBlock(registry, BlocksMC.METAL_CHEST, ChestType.class);
            registry.register(ItemsMC.CHEST_UPGRADE = new ItemChestUpgrade(RegistryMC.RESOURCE_CHEST_UPGRADE));

            if (ModSupport.hasRefinedRelocation()) {
                UpgradeRegistry.registerChest(RegistryMC.RESOURCE_CHEST_UPGRADE, ModBlocks.sortingChest, BlocksMC.SORTING_METAL_CHEST);
            }

            if (ModConfig.GENERAL.enableMinecarts) {
                registry.register(ItemsMC.MINECART_METAL_CHEST = new ItemMetalMinecart());
            }
        }

        @SubscribeEvent
        public static void registerEntities(final RegistryEvent.Register<EntityEntry> event) {
            final IForgeRegistry<EntityEntry> registry = event.getRegistry();

            if (ModConfig.GENERAL.enableMinecarts) {
                registry.register(EntitiesMC.MINECART_METAL_CHEST = EntityEntryBuilder.create().id(RegistryMC.KEY_MINECART_METAL_CHEST, 0).name(RegistryMC.KEY_MINECART_METAL_CHEST).entity(EntityMinecartMetalChest.class).tracker(80, 3, true).build());
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
            for (ChestType type : ChestType.values()) {
                if (type.isRegistered()) {
                    OreDictionary.registerOre("chest" + WordUtils.capitalize(type.getName()), new ItemStack(BlocksMC.METAL_CHEST, 1, type.ordinal()));
                }
            }

            for (ChestType type : ChestType.values()) {
                if (type.isRegistered()) {
                    GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, "recipe_minecart_chest_" + type.getName()), null, new ItemStack(ItemsMC.MINECART_METAL_CHEST, 1, type.ordinal()),
                            "a", "b",
                            'a', new ItemStack(BlocksMC.METAL_CHEST, 1, type.ordinal()),
                            'b', Items.MINECART);
                }
            }

            if (ChestType.COPPER.isRegistered()) {
                GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, "recipe_chest_" + ChestType.COPPER.getName()), null, new ItemStack(BlocksMC.METAL_CHEST, 1, ChestType.COPPER.ordinal()),
                        "aaa", "aba", "aaa",
                        'a', "ingotCopper",
                        'b', "chestWood");
                GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, "recipe_chest_" + ChestType.IRON.getName()), null, new ItemStack(BlocksMC.METAL_CHEST, 1, ChestType.IRON.ordinal()),
                        "aaa", "aba", "aaa",
                        'a', "ingotIron",
                        'b', new ItemStack(BlocksMC.METAL_CHEST, 1, ChestType.COPPER.ordinal()));
            } else {
                GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, "recipe_chest_" + ChestType.IRON.getName()), null, new ItemStack(BlocksMC.METAL_CHEST, 1, ChestType.IRON.ordinal()),
                        "aaa", "aba", "aaa",
                        'a', "ingotIron",
                        'b', "chestWood");
            }

            if (ChestType.SILVER.isRegistered()) {
                GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, "recipe_chest_" + ChestType.SILVER.getName()), null, new ItemStack(BlocksMC.METAL_CHEST, 1, ChestType.SILVER.ordinal()),
                        "aaa", "aba", "aaa",
                        'a', "ingotSilver",
                        'b', new ItemStack(BlocksMC.METAL_CHEST, 1, ChestType.IRON.ordinal()));
                GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, "recipe_chest_" + ChestType.GOLD.getName()), null, new ItemStack(BlocksMC.METAL_CHEST, 1, ChestType.GOLD.ordinal()),
                        "aaa", "aba", "aaa",
                        'a', "ingotGold",
                        'b', new ItemStack(BlocksMC.METAL_CHEST, 1, ChestType.SILVER.ordinal()));
            } else {
                GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, "recipe_chest_" + ChestType.GOLD.getName()), null, new ItemStack(BlocksMC.METAL_CHEST, 1, ChestType.GOLD.ordinal()),
                        "aaa", "aba", "aaa",
                        'a', "ingotGold",
                        'b', new ItemStack(BlocksMC.METAL_CHEST, 1, ChestType.IRON.ordinal()));
            }

            GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, "recipe_chest_" + ChestType.DIAMOND.getName()), null, new ItemStack(BlocksMC.METAL_CHEST, 1, ChestType.DIAMOND.ordinal()),
                    "aaa", "aba", "aaa",
                    'a', "gemDiamond",
                    'b', new ItemStack(BlocksMC.METAL_CHEST, 1, ChestType.GOLD.ordinal()));
            GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, "recipe_chest_" + ChestType.OBSIDIAN.getName()), null, new ItemStack(BlocksMC.METAL_CHEST, 1, ChestType.OBSIDIAN.ordinal()),
                    "aaa", "aba", "aaa",
                    'a', "obsidian",
                    'b', new ItemStack(BlocksMC.METAL_CHEST, 1, ChestType.DIAMOND.ordinal()));

            for (ChestUpgrade upgrade : ChestUpgrade.values()) {
                GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, "recipe_chest_upgrade_" + upgrade.getName()), null, new ItemStack(ItemsMC.CHEST_UPGRADE, 1, upgrade.ordinal()),
                        "aaa", "aaa", "baa",
                        'a', upgrade.getUpgrade().getOreName(),
                        'b', getUpgradeBase("plankWood", ItemsMC.CHEST_UPGRADE, upgrade));
            }

            if (ModSupport.hasRefinedRelocation()) {
                for (ChestType type : ChestType.values()) {
                    if (type.isRegistered()) {
                        GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, "recipe_sorting_chest_" + type.getName()), null, new ItemStack(BlocksMC.SORTING_METAL_CHEST, 1, type.ordinal()),
                                " a ", "bcb", " d ",
                                'a', Items.WRITABLE_BOOK,
                                'b', Items.REDSTONE,
                                'c', new ItemStack(BlocksMC.METAL_CHEST, 1, type.ordinal()),
                                'd', Blocks.HOPPER);

                        if (ModSupport.hasThaumcraft()) {
                            GameRegistry.addShapedRecipe(new ResourceLocation(RegistryMC.MOD_ID, "recipe_sorting_hungry_chest_" + type.getName()), null, new ItemStack(BlocksMC.SORTING_HUNGRY_METAL_CHEST, 1, type.ordinal()),
                                    " a ", "bcb", " d ",
                                    'a', Items.WRITABLE_BOOK,
                                    'b', Items.REDSTONE,
                                    'c', new ItemStack(BlocksMC.HUNGRY_METAL_CHEST, 1, type.ordinal()),
                                    'd', Blocks.HOPPER);
                        }
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerLogIn(PlayerLoggedInEvent event) {
            if (ModConfig.GENERAL.checkForUpdates && UpdateChecker.hasUpdate()) {
                event.player.sendMessage(UpdateChecker.getUpdateNotification());
            }
        }

        @SubscribeEvent
        public static void changeSittingTaskForOcelots(LivingUpdateEvent event) {
            EntityLivingBase creature = event.getEntityLiving();

            if (creature instanceof EntityOcelot && creature.ticksExisted < 5) {
                EntityOcelot ocelot = (EntityOcelot) creature;
                HashSet<EntityAITaskEntry> tasks = new HashSet<EntityAITaskEntry>();

                for (EntityAITaskEntry task : ocelot.tasks.taskEntries) {
                    if (task.action.getClass() == EntityAIOcelotSit.class) {
                        tasks.add(task);
                    }
                }

                for (EntityAITaskEntry task : tasks) {
                    ocelot.tasks.removeTask(task.action);
                    ocelot.tasks.addTask(task.priority, new EntityAIOcelotSitOnChest(ocelot, 0.4F));
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getCustomModel(Item item, String customDomain, StringBuilder variantPath) {
        if (StringUtils.isNullOrEmpty(customDomain)) {
            return new ModelResourceLocation(item.getRegistryName(), variantPath.toString());
        } else {
            return new ModelResourceLocation(RegistryMC.MOD_ID + ":" + customDomain, variantPath.toString());
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(Item item, String customDomain, int meta, String... variants) {
        StringBuilder variantPath = new StringBuilder(variants[0]);

        for (int i = 1; i < variants.length; ++i) {
            variantPath.append(',').append(variants[i]);
        }

        ModelLoader.setCustomModelResourceLocation(item, meta, getCustomModel(item, customDomain, variantPath));
    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(Block block, String customDomain, int meta, String... variants) {
        registerModel(Item.getItemFromBlock(block), customDomain, meta, variants);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(Item item, int meta, String... variants) {
        registerModel(item, null, meta, variants);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(Block block, int meta, String... variants) {
        registerModel(block, null, meta, variants);
    }

    @SideOnly(Side.CLIENT)
    public static void registerTileRenderer(Class tileClass, TileEntitySpecialRenderer tileRenderer) {
        ClientRegistry.bindTileEntitySpecialRenderer(tileClass, tileRenderer);
    }

    @EventBusSubscriber(modid = RegistryMC.MOD_ID, value = Side.CLIENT)
    static class ClientLoader {

        @SubscribeEvent
        public static void onModelRegistration(ModelRegistryEvent event) {
            for (ChestType type : ChestType.values()) {
                registerModel(BlocksMC.METAL_CHEST, type.ordinal(), getVariantName(type));

                if (ModConfig.GENERAL.enableMinecarts) {
                    registerModel(ItemsMC.MINECART_METAL_CHEST, "item_minecart", type.ordinal(), "item=" + type.getName() + "_chest");
                }
            }

            registerTileRenderer(TileMetalChest.class, RenderMetalChest.INSTANCE);

            for (ChestUpgrade type : ChestUpgrade.values()) {
                registerModel(ItemsMC.CHEST_UPGRADE, "item_chest_upgrade", type.ordinal(), "item=" + type.getName());
            }

            RenderingRegistry.registerEntityRenderingHandler(EntityMinecartMetalChest.class, manager -> new RenderMinecartMetalChest(manager));
        }
    }
}
