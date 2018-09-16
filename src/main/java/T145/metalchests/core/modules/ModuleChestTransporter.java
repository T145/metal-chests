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

import T145.metalchests.api.BlocksMC;
import T145.metalchests.api.RegistryMC;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.ModSupport;
import T145.metalchests.compat.chesttransporter.TransportableMetalChest;
import cubex2.mods.chesttransporter.api.TransportableChest;
import cubex2.mods.chesttransporter.chests.TransportableChestOld;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import thaumcraft.api.blocks.BlocksTC;

@EventBusSubscriber(modid = RegistryMC.MOD_ID)
class ModuleChestTransporter {

	@Optional.Method(modid = ModSupport.ChestTransporter.MOD_ID)
	@SubscribeEvent
	public static void registerChestTransporter(final RegistryEvent.Register<TransportableChest> event) {
		final IForgeRegistry<TransportableChest> registry = event.getRegistry();

		for (ChestType type : ChestType.values()) {
			TransportableMetalChest chest = new TransportableMetalChest(BlocksMC.METAL_CHEST, type, new ResourceLocation(RegistryMC.MOD_ID, "item/chesttransporter/" + type.getName()));
			registry.register(chest);
			//ChestRegistry.registerMinecart(EntityMinecartMetalChest.class, chest);
		}

		if (ModSupport.hasThaumcraft()) {
			registry.register(new TransportableChestOld(BlocksTC.hungryChest, -1, 1, "vanilla"));

			for (ChestType type : ChestType.values()) {
				registry.register(new TransportableMetalChest(BlocksMC.HUNGRY_METAL_CHEST, type, new ResourceLocation(RegistryMC.MOD_ID, "item/chesttransporter/hungry/" + type.getName())));
			}
		}
	}
}
