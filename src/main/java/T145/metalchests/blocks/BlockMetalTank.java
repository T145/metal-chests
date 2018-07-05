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
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMetalTank extends Block {

	public static enum TankType implements IStringSerializable {

		BASE(16, "blockGlass"),
		COPPER(20, "ingotCopper"),
		IRON(24, "ingotIron"),
		SILVER(28, "ingotSilver"),
		GOLD(32, "ingotGold"),
		DIAMOND(36, "gemDiamond"),
		OBSIDIAN(36, "obsidian");

		private final String dictName;
		private final int bucketVolume;

		TankType(int bucketVolume, String dictName) {
			this.bucketVolume = bucketVolume;
			this.dictName = dictName;
		}

		public int getBucketVolume() {
			return bucketVolume;
		}

		public int getCapacity() {
			return bucketVolume * 16;
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
	public static final AxisAlignedBB BOX = new AxisAlignedBB(2 / 16D, 0 / 16D, 2 / 16D, 14 / 16D, 16 / 16D, 14 / 16D);

	public BlockMetalTank() {
		super(Material.GLASS);
		setRegistryName(new ResourceLocation(MetalChests.MOD_ID, NAME));
		setDefaultState(blockState.getBaseState().withProperty(VARIANT, TankType.BASE));
		setUnlocalizedName("metalchests:" + NAME);
		setHardness(3F);
		setCreativeTab(MetalChests.TAB);
		setSoundType(SoundType.GLASS);
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

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BOX;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side.getAxis() != Axis.Y || !(world.getBlockState(pos.offset(side)).getBlock() instanceof BlockMetalTank);
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileMetalTank) {
			TileMetalTank tank = (TileMetalTank) te;

			if (tank.getType() == TankType.OBSIDIAN) {
				return 10000F;
			}
		}

		return super.getExplosionResistance(world, pos, exploder, explosion);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity te = world.getTileEntity(pos);

		if (!world.isRemote && te instanceof TileMetalTank) {
			TileMetalTank tank = (TileMetalTank) te;

			// check if it's a fluid container and can fill this tank
		}
		return true;
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileMetalTank) {
			TileMetalTank tank = (TileMetalTank) te;
		}

		return 0;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		for (TankType type : TankType.values()) {
			items.add(new ItemStack(this, 1, type.ordinal()));
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, TankType.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}
}
