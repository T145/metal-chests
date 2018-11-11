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
package T145.metalchests.api.immutable;

import net.minecraft.util.ResourceLocation;

public class RegistryMC {

    private RegistryMC() {}

    public static final String MOD_ID = "metalchests";
    public static final String MOD_NAME = "MetalChests";
    public static final ResourceLocation MOD_RESOURCE = new ResourceLocation(RegistryMC.MOD_ID);

    public static final String KEY_METAL_CHEST = "metal_chest";
    public static final String KEY_HUNGRY_METAL_CHEST = "hungry_metal_chest";
    public static final String KEY_SORTING_METAL_CHEST = "sorting_metal_chest";
    public static final String KEY_SORTING_HUNGRY_METAL_CHEST = "sorting_hungry_metal_chest";

    public static final ResourceLocation RESOURCE_METAL_CHEST = new ResourceLocation(MOD_ID, KEY_METAL_CHEST);
    public static final ResourceLocation RESOURCE_HUNGRY_METAL_CHEST = new ResourceLocation(MOD_ID, KEY_HUNGRY_METAL_CHEST);
    public static final ResourceLocation RESOURCE_SORTING_METAL_CHEST = new ResourceLocation(MOD_ID, KEY_SORTING_METAL_CHEST);
    public static final ResourceLocation RESOURCE_SORTING_HUNGRY_METAL_CHEST = new ResourceLocation(MOD_ID, KEY_SORTING_HUNGRY_METAL_CHEST);

    public static final String KEY_CHEST_UPGRADE = "chest_upgrade";
    public static final String KEY_HUNGRY_CHEST_UPGRADE = "hungry_chest_upgrade";
    public static final String KEY_MINECART_METAL_CHEST = "minecart_metal_chest";

    public static final ResourceLocation RESOURCE_CHEST_UPGRADE = new ResourceLocation(MOD_ID, KEY_CHEST_UPGRADE);
    public static final ResourceLocation RESOURCE_HUNGRY_CHEST_UPGRADE = new ResourceLocation(MOD_ID, KEY_HUNGRY_CHEST_UPGRADE);
    public static final ResourceLocation RESOURCE_MINECART_METAL_CHEST = new ResourceLocation(MOD_ID, KEY_MINECART_METAL_CHEST);
}
