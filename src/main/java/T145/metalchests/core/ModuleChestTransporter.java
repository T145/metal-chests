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

import T145.metalchests.api.BlocksMC;
import T145.metalchests.api.immutable.ModSupport;
import T145.metalchests.api.immutable.RegistryMC;
import cubex2.mods.chesttransporter.api.TransportableChest;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = RegistryMC.MOD_ID)
class ModuleChestTransporter {

	@Optional.Method(modid = ModSupport.ChestTransporter.MOD_ID)
	@SubscribeEvent
	public static void registerChestTransporter(final RegistryEvent.Register<TransportableChest> event) {
		final IForgeRegistry<TransportableChest> registry = event.getRegistry();

		registry.register(new TransportableMetalChest(BlocksMC.METAL_CHEST, RegistryMC.KEY_METAL_CHEST));
	}
}
