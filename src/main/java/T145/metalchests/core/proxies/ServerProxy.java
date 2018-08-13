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
package T145.metalchests.core.proxies;

import T145.metalchests.api.ModSupport;
import T145.metalchests.blocks.BlockMetalChest.ChestType;
import T145.metalchests.client.gui.GuiHandler;
import T145.metalchests.core.MetalChests;
import T145.metalchests.core.ModLoader;
import T145.metalchests.lib.DataSerializers;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.ShapedArcaneRecipe;

public class ServerProxy implements IProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		DataSerializers.registerSerializers();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(MetalChests.instance, new GuiHandler());
		DataFixer fixer = FMLCommonHandler.instance().getDataFixer();
		TileMetalChest.registerFixes(fixer);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		if (ModSupport.hasThaumcraft()) {
			if (ChestType.COPPER.isRegistered()) {
				ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(MetalChests.MOD_ID, "HungryCopperChest"), new ShapedArcaneRecipe(new ResourceLocation(""), "HUNGRYCOPPERCHEST", 15,
						new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1),
						new ItemStack(ModLoader.HUNGRY_METAL_CHEST, 1, 0),
						"III", "ICI", "III",
						'I', "ingotCopper",
						'C', new ItemStack(BlocksTC.hungryChest)
					)
				);
				ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(MetalChests.MOD_ID, "HungryIronChest"), new ShapedArcaneRecipe(new ResourceLocation(""), "HUNGRYCOPPERCHEST", 15,
						new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1),
						new ItemStack(ModLoader.HUNGRY_METAL_CHEST, 1, 1),
						"III", "ICI", "III",
						'I', "ingotIron",
						'C', new ItemStack(ModLoader.HUNGRY_METAL_CHEST, 1, 0)
					)
				);
			} else {
				ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(MetalChests.MOD_ID, "HungryIronChest"), new ShapedArcaneRecipe(new ResourceLocation(""), "HUNGRYCOPPERCHEST", 15,
						new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1),
						new ItemStack(ModLoader.HUNGRY_METAL_CHEST, 1, 1),
						"III", "ICI", "III",
						'I', "ingotIron",
						'C', new ItemStack(BlocksTC.hungryChest)
					)
				);
			}
			
			if (ChestType.SILVER.isRegistered()) {
				ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(MetalChests.MOD_ID, "HungrySilverChest"), new ShapedArcaneRecipe(new ResourceLocation(""), "HUNGRYCOPPERCHEST", 15,
						new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1),
						new ItemStack(ModLoader.HUNGRY_METAL_CHEST, 1, 2),
						"III", "ICI", "III",
						'I', "ingotSilver",
						'C', new ItemStack(ModLoader.HUNGRY_METAL_CHEST, 1, 1)
					)
				);
				ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(MetalChests.MOD_ID, "HungryGoldChest"), new ShapedArcaneRecipe(new ResourceLocation(""), "HUNGRYCOPPERCHEST", 15,
						new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1),
						new ItemStack(ModLoader.HUNGRY_METAL_CHEST, 1, 3),
						"III", "ICI", "III",
						'I', "ingotGold",
						'C', new ItemStack(ModLoader.HUNGRY_METAL_CHEST, 1, 2)
					)
				);
			} else {
				ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(MetalChests.MOD_ID, "HungryGoldChest"), new ShapedArcaneRecipe(new ResourceLocation(""), "HUNGRYCOPPERCHEST", 15,
						new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1),
						new ItemStack(ModLoader.HUNGRY_METAL_CHEST, 1, 3),
						"III", "ICI", "III",
						'I', "ingotGold",
						'C', new ItemStack(ModLoader.HUNGRY_METAL_CHEST, 1, 1)
					)
				);
			}
			
			ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(MetalChests.MOD_ID, "HungryDiamondChest"), new ShapedArcaneRecipe(new ResourceLocation(""), "HUNGRYCOPPERCHEST", 15,
					new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1),
					new ItemStack(ModLoader.HUNGRY_METAL_CHEST, 1, 4),
					"III", "ICI", "III",
					'I', "gemDiamond",
					'C', new ItemStack(ModLoader.HUNGRY_METAL_CHEST, 1, 3)
				)
			);
			ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(MetalChests.MOD_ID, "HungryObsidianChest"), new ShapedArcaneRecipe(new ResourceLocation(""), "HUNGRYCOPPERCHEST", 15,
					new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1),
					new ItemStack(ModLoader.HUNGRY_METAL_CHEST, 1, 5),
					"III", "ICI", "III",
					'I', "obsidian",
					'C', new ItemStack(ModLoader.HUNGRY_METAL_CHEST, 1, 4)
				)
			);
		}
	}
}
