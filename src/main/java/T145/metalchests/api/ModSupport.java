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

import T145.metalchests.config.ModConfig;
import net.minecraftforge.fml.common.Loader;

public enum ModSupport {

	/*
	 * These are individual classes since optimizing
	 * an enum for unique interface nomenclature
	 * (for use in an annotation) is impossible.
	 * 
	 * - T145
	 */;

	public static boolean hasThaumcraft() {
		return ModConfig.GENERAL.enableHungryMetalChests && Loader.isModLoaded(Thaumcraft.MOD_ID);
	}

	public class InvTweaks {
		public static final String MOD_ID = "invtweaks";
		public static final String CHEST_CONTAINER = "invtweaks.api.container.ChestContainer";
	}

	public class Quark {
		public static final String MOD_ID = "quark";
		public static final String CHEST_BUTTON_CALLBACK = "vazkii.quark.api.IChestButtonCallback";
		public static final String DROPOFF_MANAGER = "vazkii.quark.api.IDropoffManager";
	}

	public class HoloInventory {
		public static final String MOD_ID = "holoinventory";
		public static final String NAMED_ITEM_HANDLER = "net.dries007.holoInventory.api.INamedItemHandler";
	}

	public class Railcraft {
		public static final String MOD_ID = "railcraft";
		public static final String FLUID_CART = "mods.railcraft.api.carts.IFluidCart";
		public static final String ITEM_CART = "mods.railcraft.api.carts.IItemCart";
	}

	public class Thaumcraft { // have this be an @ObjectHolder?
		public static final String MOD_ID = "thaumcraft";
	}
}
