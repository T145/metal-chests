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
package T145.metalchests.blocks;

import javax.annotation.Nullable;

import T145.metalchests.core.MetalChests;
import T145.metalchests.tiles.TileMetalTank;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BlockMetalTank extends Block {

	public static enum TankType implements IStringSerializable {

		BASE("blockGlass"),
		COPPER("ingotCopper"),
		IRON("ingotIron"),
		SILVER("ingotSilver"),
		GOLD("ingotGold"),
		DIAMOND("gemDiamond"),
		OBSIDIAN("obsidian");

		private final String dictName;

		TankType(String dictName) {
			this.dictName = dictName;
		}

		@Override
		public String getName() {
			return name().toLowerCase();
		}

		public static TankType byMetadata(int meta) {
			return values()[meta];
		}
	}

	public static final PropertyEnum<TankType> VARIANT = PropertyEnum.<TankType>create("variant", TankType.class);
	public static final String NAME = "metal_tank";

	public BlockMetalTank() {
		super(Material.GLASS);
		setRegistryName(new ResourceLocation(MetalChests.MOD_ID, NAME));
		setDefaultState(blockState.getBaseState().withProperty(VARIANT, TankType.BASE));
		setUnlocalizedName("metalchests:" + NAME);
		setHardness(3F);
		setCreativeTab(MetalChests.TAB);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileMetalTank(state.getValue(VARIANT));
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
}
