/*******************************************************************************
 * Copyright 2018-2019 T145
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
package T145.metalchests.items;

import T145.metalchests.api.constants.ChestType;
import T145.metalchests.api.constants.RegistryMC;
import T145.metalchests.core.MetalChests;
import T145.metalchests.entities.EntityMinecartMetalChest;
import T145.tbone.dispenser.BehaviorDispenseMinecart;
import T145.tbone.items.TItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMetalMinecart extends TItem {

	private static final BehaviorDispenseMinecart DISPENSE_BEHAVIOR = new BehaviorDispenseMinecart() {

		@Override
		public EntityMinecart getMinecartEntity(World world, double x, double y, double z, ItemStack stack) {
			return new EntityMinecartMetalChest(world, x, y, z, ChestType.byMetadata(stack.getItemDamage()));
		}
	};

	public ItemMetalMinecart() {
		super(ChestType.TIERS, RegistryMC.RESOURCE_MINECART_METAL_CHEST, MetalChests.TAB);
		this.setMaxStackSize(Items.CHEST_MINECART.getItemStackLimit());
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return DISPENSE_BEHAVIOR.placeStack(player, hand, world, pos);
	}
}
