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

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import T145.metalchests.api.BlocksMC;
import T145.metalchests.api.ItemsMC;
import T145.metalchests.api.RegistryMC;
import T145.metalchests.api.chests.UpgradeRegistry;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.ModSupport;
import T145.metalchests.client.gui.GuiHandler;
import T145.metalchests.entities.EntityMinecartMetalChest;
import T145.metalchests.tiles.TileHungryMetalChest;
import T145.metalchests.tiles.TileMetalChest;
import T145.metalchests.tiles.TileSortingMetalChest;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;

@Mod(modid = RegistryMC.MOD_ID, name = RegistryMC.MOD_NAME, version = MetalChests.VERSION, updateJSON = MetalChests.UPDATE_JSON, dependencies = "after:thaumcraft")
public class MetalChests {

    static final String VERSION = "@VERSION@";
    static final String UPDATE_JSON = "https://raw.githubusercontent.com/T145/metalchests/master/update.json";

    public static final Logger LOG = LogManager.getLogger(RegistryMC.MOD_ID);

    public static final CreativeTabs TAB = new CreativeTabs(RegistryMC.MOD_ID) {

        @Override
        @SideOnly(Side.CLIENT)
        public boolean hasSearchBar() {
            return true;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(BlocksMC.METAL_CHEST, 1, 1);
        }

        @SideOnly(Side.CLIENT)
        public void displayAllRelevantItems(NonNullList<ItemStack> items) {
            BlocksMC.METAL_CHEST.getSubBlocks(this, items);
            ItemsMC.CHEST_UPGRADE.getSubItems(this, items);

            if (BlocksMC.HUNGRY_METAL_CHEST != null) {
                BlocksMC.HUNGRY_METAL_CHEST.getSubBlocks(this, items);
            }

            if (ItemsMC.HUNGRY_CHEST_UPGRADE != null) {
                ItemsMC.HUNGRY_CHEST_UPGRADE.getSubItems(this, items);
            }

            if (BlocksMC.SORTING_METAL_CHEST != null) {
                BlocksMC.SORTING_METAL_CHEST.getSubBlocks(this, items);
            }

            if (BlocksMC.SORTING_HUNGRY_METAL_CHEST != null) {
                BlocksMC.SORTING_HUNGRY_METAL_CHEST.getSubBlocks(this, items);
            }

            ItemsMC.MINECART_METAL_CHEST.getSubItems(this, items);
        }
    }.setBackgroundImageName("item_search.png");

    @Instance(RegistryMC.MOD_ID)
    public static MetalChests instance;

    public static final DataSerializer<ChestType> CHEST_TYPE = new DataSerializer<ChestType>() {

        @Override
        public void write(PacketBuffer buf, ChestType value) {
            buf.writeEnumValue(value);
        }

        @Override
        public ChestType read(PacketBuffer buf) throws IOException {
            return buf.readEnumValue(ChestType.class);
        }

        @Override
        public DataParameter<ChestType> createKey(int id) {
            return new DataParameter<ChestType>(id, this);
        }

        @Override
        public ChestType copyValue(ChestType value) {
            return value;
        }
    };

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModMetadata meta = event.getModMetadata();
        meta.authorList.add("T145");
        meta.autogenerated = false;
        meta.credits = "The fans!";
        meta.description = "The better alternative to IronChests";
        meta.logoFile = "logo.png";
        meta.modId = RegistryMC.MOD_ID;
        meta.name = RegistryMC.MOD_NAME;
        meta.url = "https://github.com/T145/metalchests";
        meta.useDependencyInformation = false;
        meta.version = VERSION;
        DataSerializers.registerSerializer(CHEST_TYPE);
    }

    private void registerFixes(DataFixer fixer, Class tileClass) {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(tileClass, new String[] { "Items" }));
    }

    private void registerEntityFixes(DataFixer fixer, Class entityClass) {
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(entityClass, new String[] { "Items" }));
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(MetalChests.instance, new GuiHandler());

        DataFixer fixer = FMLCommonHandler.instance().getDataFixer();

        registerFixes(fixer, TileMetalChest.class);
        registerEntityFixes(fixer, EntityMinecartMetalChest.class);

        if (ModSupport.hasRefinedRelocation()) {
            registerFixes(fixer, TileSortingMetalChest.class);
        }

        if (ModSupport.hasThaumcraft()) {
            registerFixes(fixer, TileHungryMetalChest.class);

            ThaumcraftApi.registerResearchLocation(new ResourceLocation(RegistryMC.MOD_ID, "research/hungry_metal_chests"));

            ResearchCategories.registerCategory("HUNGRYMETALCHESTS", "UNLOCKARTIFICE",
                    new AspectList().add(Aspect.MECHANISM, 10).add(Aspect.CRAFT, 10).add(Aspect.METAL, 10).add(Aspect.TOOL, 10).add(Aspect.ENERGY, 10).add(Aspect.LIGHT, 5).add(Aspect.FLIGHT, 5).add(Aspect.TRAP, 5).add(Aspect.FIRE, 5),
                    new ResourceLocation("thaumcraft", "textures/research/rd_chest.png"),
                    new ResourceLocation("thaumcraft", "textures/gui/gui_research_back_4.jpg"),
                    ModSupport.Thaumcraft.BACK_OVER);
        }

        if (ModSupport.hasThaumcraft() && ModSupport.hasRefinedRelocation()) {
            registerFixes(fixer, TileSortingMetalChest.class);
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        for (ItemStack stack : OreDictionary.getOres("chestWood")) {
            UpgradeRegistry.registerChest(RegistryMC.RESOURCE_CHEST_UPGRADE, Block.getBlockFromItem(stack.getItem()), BlocksMC.METAL_CHEST);
        }
    }
}
