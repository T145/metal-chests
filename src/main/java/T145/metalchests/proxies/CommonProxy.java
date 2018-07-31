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
package T145.metalchests.proxies;

import T145.metalchests.containers.ContainerMetalChest;
import T145.metalchests.core.MetalChests;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy implements IGuiHandler {

	final MutableBlockPos pos = new MutableBlockPos(BlockPos.ORIGIN);

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		pos.setPos(x, y, z);

		switch (ID) {
		case 0:
			TileMetalChest chest = (TileMetalChest) world.getTileEntity(pos);
			return new ContainerMetalChest(chest, player, chest.getType());
		default:
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	public void preInit(FMLPreInitializationEvent event) {
		OreDictionary.registerOre("blockGlass", Blocks.GLASS);
		OreDictionary.registerOre("blockObsidian", Blocks.OBSIDIAN);
	}

	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(MetalChests.instance, this);
		DataFixer fixer = FMLCommonHandler.instance().getDataFixer();
		TileMetalChest.registerFixes(fixer);
	}

	public void postInit(FMLPostInitializationEvent event) {}
}
