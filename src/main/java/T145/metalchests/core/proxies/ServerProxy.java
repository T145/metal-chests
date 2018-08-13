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

import T145.metalchests.client.gui.GuiHandler;
import T145.metalchests.core.MetalChests;
import T145.metalchests.lib.DataSerializers;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.init.Blocks;
import net.minecraft.util.datafix.DataFixer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ServerProxy implements IProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		OreDictionary.registerOre("blockGlass", Blocks.GLASS);
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
	}
}
