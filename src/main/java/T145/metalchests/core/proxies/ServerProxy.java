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

import java.io.IOException;

import T145.metalchests.blocks.BlockMetalChest.ChestType;
import T145.metalchests.client.gui.GuiHandler;
import T145.metalchests.core.MetalChests;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.init.Blocks;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ServerProxy implements IProxy {

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

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		OreDictionary.registerOre("blockGlass", Blocks.GLASS);
		DataSerializers.registerSerializer(CHEST_TYPE);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(MetalChests.instance, new GuiHandler());
		DataFixer fixer = FMLCommonHandler.instance().getDataFixer();
		registerChestFixes(TileMetalChest.class, fixer);
	}

	public static void registerChestFixes(Class chestClass, DataFixer fixer) {
		fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(chestClass, new String[] { "Items" }));
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
	}
}
