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
package T145.metalchests.api;

import T145.metalchests.api.immutable.ItemNames;
import net.minecraft.item.Item;
<<<<<<< HEAD:src/main/java/T145/metalchests/api/immutable/ItemsMC.java
import net.minecraft.util.ResourceLocation;

public class ItemsMC {
=======
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder("metalchests")
public class ItemsMetalChests {
>>>>>>> parent of e893474... Updated the API:src/main/java/T145/metalchests/api/ItemsMetalChests.java

	private ItemsMetalChests() {}

<<<<<<< HEAD:src/main/java/T145/metalchests/api/immutable/ItemsMC.java
	public static final String KEY_CHEST_UPGRADE = "chest_upgrade";
	public static final String KEY_HUNGRY_CHEST_UPGRADE = "hungry_chest_upgrade";
	public static final String KEY_METAL_MINECART = "minecart_metal_chest";

	public static final ResourceLocation REGISTRY_CHEST_UPGRADE = new ResourceLocation(MetalChests.MOD_ID, KEY_CHEST_UPGRADE);
	public static final ResourceLocation REGISTRY_HUNGRY_CHEST_UPGRADE = new ResourceLocation(MetalChests.MOD_ID, KEY_CHEST_UPGRADE);
	public static final ResourceLocation REGISTRY_METAL_MINECART = new ResourceLocation(MetalChests.MOD_ID, KEY_CHEST_UPGRADE);

	public static Item CHEST_UPGRADE;
	public static Item HUNGRY_CHEST_UPGRADE;
=======
	@ObjectHolder(ItemNames.CHEST_UPGRADE)
	public static Item CHEST_UPGRADE;

	@ObjectHolder(ItemNames.HUNGRY_CHEST_UPGRADE)
	public static Item HUNGRY_CHEST_UPGRADE;

	@ObjectHolder(ItemNames.METAL_MINECART)
>>>>>>> parent of e893474... Updated the API:src/main/java/T145/metalchests/api/ItemsMetalChests.java
	public static Item MINECART_METAL_CHEST;

}
