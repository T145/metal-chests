/*******************************************************************************
 * Copyright 2019 T145
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
import T145.metalchests.api.ItemsMC;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.ChestUpgrade;
import T145.metalchests.api.immutable.ModSupport;
import T145.metalchests.api.immutable.RegistryMC;
import T145.metalchests.client.render.blocks.RenderMetalChest;
import T145.metalchests.client.render.blocks.RenderSortingMetalChest;
import T145.metalchests.tiles.TileHungryMetalChest;
import T145.metalchests.tiles.TileSortingHungryMetalChest;
import T145.metalchests.tiles.TileSortingMetalChest;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = RegistryMC.MOD_ID, value = Side.CLIENT)
class LoaderClient {

	private LoaderClient() {}

	@Optional.Method(modid = ModSupport.RefinedRelocation.MOD_ID)
	@SubscribeEvent
	public static void registerRefinedRelocationModels(ModelRegistryEvent event) {
		if (ModSupport.hasRefinedRelocation()) {
			for (ChestType type : ChestType.values()) {
				ModLoader.registerModel(BlocksMC.SORTING_METAL_CHEST, type.ordinal(), ModLoader.getVariantName(type));
			}

			ModLoader.registerTileRenderer(TileSortingMetalChest.class, new RenderSortingMetalChest());

			if (ModSupport.hasThaumcraft()) {
				for (ChestType type : ChestType.values()) {
					ModLoader.registerModel(BlocksMC.SORTING_HUNGRY_METAL_CHEST, type.ordinal(), ModLoader.getVariantName(type));
				}

				ModLoader.registerTileRenderer(TileSortingHungryMetalChest.class, new RenderSortingMetalChest(BlocksMC.SORTING_HUNGRY_METAL_CHEST) {

					@Override
					protected ResourceLocation getActiveResource(ChestType type) {
						return new ResourceLocation(RegistryMC.MOD_ID, "textures/entity/chest/hungry/" + type.getName() + ".png");
					}

					@Override
					protected ResourceLocation getActiveOverlay(ChestType type) {
						return new ResourceLocation(RegistryMC.MOD_ID, "textures/entity/chest/hungry/overlay/" + type.getName() + ".png");
					}
				});
			}
		}
	}

	@Optional.Method(modid = ModSupport.Thaumcraft.MOD_ID)
	@SubscribeEvent
	public static void registerThaumcraftModels(ModelRegistryEvent event) {
		if (ModSupport.hasThaumcraft()) {
			for (ChestType type : ChestType.values()) {
				ModLoader.registerModel(BlocksMC.HUNGRY_METAL_CHEST, type.ordinal(), ModLoader.getVariantName(type));
			}

			ModLoader.registerTileRenderer(TileHungryMetalChest.class, new RenderMetalChest() {

				@Override
				protected ResourceLocation getActiveResource(ChestType type) {
					return new ResourceLocation(RegistryMC.MOD_ID, "textures/entity/chest/hungry/" + type.getName() + ".png");
				}
			});

			for (ChestUpgrade type : ChestUpgrade.values()) {
				ModLoader.registerModel(ItemsMC.HUNGRY_CHEST_UPGRADE, "item_hungry_chest_upgrade", type.ordinal(), "item=" + type.getName());
			}
		}
	}
}
