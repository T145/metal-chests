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
import T145.metalchests.client.gui.GuiHandler;
import T145.metalchests.core.MetalChests;
import T145.metalchests.lib.DataSerializers;
import T145.metalchests.tiles.TileMetalChest;
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
import thaumcraft.api.research.ResearchCategories;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		DataSerializers.registerSerializers();
	}

	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(MetalChests.instance, new GuiHandler());
		DataFixer fixer = FMLCommonHandler.instance().getDataFixer();
		TileMetalChest.registerFixes(fixer);

		if (ModSupport.hasThaumcraft()) {
			ThaumcraftApi.registerResearchLocation(new ResourceLocation(MetalChests.MOD_ID, "research/hungry_metal_chests"));

			ResearchCategories.registerCategory("HUNGRYMETALCHESTS", "UNLOCKARTIFICE",
					new AspectList().add(Aspect.MECHANISM, 10).add(Aspect.CRAFT, 10).add(Aspect.METAL, 10).add(Aspect.TOOL, 10).add(Aspect.ENERGY, 10).add(Aspect.LIGHT, 5).add(Aspect.FLIGHT, 5).add(Aspect.TRAP, 5).add(Aspect.FIRE, 5),
					new ResourceLocation("thaumcraft", "textures/research/cat_artifice.png"),
					new ResourceLocation("thaumcraft", "textures/gui/gui_research_back_4.jpg"),
					ModSupport.Thaumcraft.BACK_OVER);
		}
	}

	public void postInit(FMLPostInitializationEvent event) {}
}
