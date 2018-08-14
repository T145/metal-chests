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
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
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
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;

public class BlockMetalChest extends Block {

	enum InventorySize {
		COPPER(45),
		IRON(54),
		SILVER(72),
		GOLD(81),
		DIAMOND(108);

		private final int size;

		InventorySize(int size) {
			this.size = size;
		}
	}

	public enum ChestType implements IStringSerializable {

		COPPER(InventorySize.COPPER, MapColor.SAND, SoundType.METAL, "ingotCopper"),
		IRON(InventorySize.IRON, MapColor.IRON, SoundType.METAL, "ingotIron"),
		SILVER(InventorySize.SILVER, MapColor.SILVER, SoundType.METAL, "ingotSilver"),
		GOLD(InventorySize.GOLD, MapColor.GOLD, SoundType.METAL, "ingotGold"),
		DIAMOND(InventorySize.DIAMOND, MapColor.DIAMOND, SoundType.METAL, "gemDiamond"),
		OBSIDIAN(InventorySize.DIAMOND, Material.ROCK, MapColor.OBSIDIAN, SoundType.STONE, "obsidian");

		private final InventorySize invSize;
		private final Material material;
		private final MapColor color;
		private final SoundType sound;
		private final String dictName;

		ChestType(InventorySize invSize, Material material, MapColor color, SoundType sound, String dictName) {
			this.invSize = invSize;
			this.material = material;
			this.color = color;
			this.sound = sound;
			this.dictName = dictName;
		}

		ChestType(InventorySize invSize, MapColor color, SoundType sound, String dictName) {
			this(invSize, Material.IRON, color, sound, dictName);
		}

		@Override
		public String getName() {
			return name().toLowerCase();
		}

		public int getInventorySize() {
			return invSize.size;
		}

		public Material getMaterial() {
			return material;
		}

		public MapColor getMapColor() {
			return color;
		}

		public SoundType getSoundType() {
			return sound;
		}

		public String getOreName() {
			return dictName;
		}

		public boolean isRegistered() {
			return OreDictionary.doesOreNameExist(dictName);
		}

		public boolean isLarge() {
			return getInventorySize() > 100;
		}

		public int getRowLength() {
			return isLarge() ? 12 : 9;
		}

		public int getRowCount() {
			return getInventorySize() / getRowLength();
		}

		public static ChestType byMetadata(int meta) {
			return values()[meta]; 
		}

		public GUI getGui() {
			return GUI.byType(this);
		}

		public String getGuiId() {
			return "metalchests:" + getName() + "_chest";
		}

		public enum GUI {

			COPPER(184),
			IRON(202),
			SILVER(238),
			GOLD(256),
			DIAMOND(256),
			OBSIDIAN(256);

			private final int ySize;

			GUI(int ySize) {
				this.ySize = ySize;
			}

			public int getSizeX() {
				return ChestType.byMetadata(ordinal()).isLarge() ? 238 : 184;
			}

			public int getSizeY() {
				return ySize;
			}

			public static GUI byMetadata(int meta) {
				return values()[meta];
			}

			public static GUI byType(ChestType type) {
				return byMetadata(type.ordinal());
			}

			public ResourceLocation getGuiTexture() {
				ChestType type = ChestType.byMetadata(ordinal());
				return new ResourceLocation(MetalChests.MOD_ID, "textures/gui/" + (type.isLarge() ? "diamond" : type.getName()) + "_container.png");
			}
		}
	}

	public static final PropertyEnum<ChestType> VARIANT = PropertyEnum.<ChestType>create("variant", ChestType.class);
	public static final String NAME = "metal_chest";

	public BlockMetalChest(Material iron) {
		super(iron);
	}

	public BlockMetalChest() {
		this(Material.IRON);
		setRegistryName(new ResourceLocation(MetalChests.MOD_ID, NAME));
		setTranslationKey("metalchests:" + NAME);
		setDefaultState(blockState.getBaseState().withProperty(VARIANT, ChestType.IRON));
		setHardness(3F);
		setCreativeTab(MetalChests.TAB);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileMetalChest(state.getValue(VARIANT));
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(VARIANT).getMapColor();
	}

	@Override
	public Material getMaterial(IBlockState state) {
		return state.getValue(VARIANT).getMaterial();
	}

	@Override
	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
		return state.getValue(VARIANT).getSoundType();
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
	public boolean hasCustomBreakingProgress(IBlockState state) {
		return true;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return Blocks.ENDER_CHEST.getBoundingBox(state, world, pos);
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return getBoundingBox(state, world, pos);
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileMetalChest) {
			TileMetalChest chest = (TileMetalChest) te;

			if (chest.getType() == ChestType.OBSIDIAN) {
				return 10000F;
			}
		}

		return super.getExplosionResistance(world, pos, exploder, explosion);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileMetalChest) {
			TileMetalChest chest = (TileMetalChest) te;

			for (int i = 0; i < chest.getInventory().getSlots(); ++i) {
				ItemStack stack = chest.getInventory().getStackInSlot(i);

				if (!stack.isEmpty()) {
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				}
			}

			world.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileMetalChest) {
			TileMetalChest chest = (TileMetalChest) te;
			chest.setFront(placer.getHorizontalFacing().getOpposite());
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote && world.getTileEntity(pos) instanceof TileMetalChest && !player.isSneaking() && !isBlocked(world, pos)) {
			player.openGui(MetalChests.MOD_ID, 0, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	private boolean isBlocked(World world, BlockPos pos) {
		return isBelowSolidBlock(world, pos) || isOcelotSittingOnChest(world, pos);
	}

	private boolean isBelowSolidBlock(World world, BlockPos pos) {
		return world.getBlockState(pos.up()).doesSideBlockChestOpening(world, pos.up(), EnumFacing.DOWN);
	}

	private boolean isOcelotSittingOnChest(World world, BlockPos pos) {
		for (Entity entity : world.getEntitiesWithinAABB(EntityOcelot.class, new AxisAlignedBB(pos.getX(), (pos.getY() + 1), pos.getZ(), (pos.getX() + 1), (pos.getY() + 2), (pos.getZ() + 1)))) {
			EntityOcelot ocelot = (EntityOcelot) entity;

			if (ocelot.isSitting()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileMetalChest) {
			TileMetalChest chest = (TileMetalChest) te;
			return ItemHandlerHelper.calcRedstoneFromInventory(chest.getInventory());
		}

		return 0;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		for (ChestType type : ChestType.values()) {
			items.add(new ItemStack(this, 1, type.ordinal()));
		}
	}

	@Override
	public EnumFacing[] getValidRotations(World world, BlockPos pos) {
		return EnumFacing.Plane.VERTICAL.facings();
	}

	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		if (world.isRemote) {
			return false;
		}

		if (axis.getAxis().isVertical()) {
			TileEntity te = world.getTileEntity(pos);

			if (te instanceof TileMetalChest) {
				TileMetalChest chest = (TileMetalChest) te;
				chest.setFront(chest.getFront().rotateY());
			}

			return true;
		}

		return false;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, ChestType.byMetadata(meta));
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
