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
package T145.metalchests.blocks;

import javax.annotation.Nullable;

import T145.metalchests.api.chests.IMetalChest;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.RegistryMC;
import T145.metalchests.config.ModConfig;
import T145.metalchests.core.MetalChests;
import T145.metalchests.tiles.TileMetalChest;
import cofh.core.init.CoreEnchantments;
import cofh.core.util.helpers.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

public class BlockMetalChest extends Block {

	public BlockMetalChest() {
		super(Material.IRON);
		registerResource();
		setDefaultState(blockState.getBaseState().withProperty(IMetalChest.VARIANT, ChestType.IRON));
		setHardness(3F);
		setCreativeTab(MetalChests.TAB);
	}

	protected void registerResource(ResourceLocation resource) {
		setRegistryName(resource);
		setTranslationKey(resource.toString());
	}

	protected void registerResource() {
		this.registerResource(RegistryMC.RESOURCE_METAL_CHEST);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileMetalChest(state.getValue(IMetalChest.VARIANT));
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
		return state.getValue(IMetalChest.VARIANT).getMapColor();
	}

	@Override
	public Material getMaterial(IBlockState state) {
		return state.getValue(IMetalChest.VARIANT).getMaterial();
	}

	@Override
	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
		return state.getValue(IMetalChest.VARIANT).getSoundType();
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

			if (chest.getChestType() == ChestType.OBSIDIAN) {
				return 10000F;
			}
		}

		return super.getExplosionResistance(world, pos, exploder, explosion);
	}

	public static ItemStack getDropStack(IMetalChest chest, Block chestBlock) {
		ItemStack stack = new ItemStack(chestBlock, 1, chest.getChestType().ordinal());

		if (ModConfig.hasThermalExpansion()) {
			NBTTagCompound tag = new NBTTagCompound();

			if (chest.getEnchantLevel() > 0) {
				CoreEnchantments.addEnchantment(tag, CoreEnchantments.holding, chest.getEnchantLevel());
			}

			if (chest.getEnchantLevel() >= chest.getChestType().getHoldingEnchantBound()) {
				chest.writeInventoryTag(tag);
			}

			if (!tag.isEmpty()) {
				stack.setTagCompound(tag);
			}
		}

		return stack;
	}

	private ItemStack getDropStack(IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileMetalChest) {
			TileMetalChest chest = (TileMetalChest) te;
			return getDropStack(chest, chest.getBlockType());
		}

		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return getDropStack(world, pos);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		drops.add(getDropStack(world, pos));
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if (willHarvest) {
			return true; // If it will harvest, delay deletion of the block until after getDrops
		}
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack tool) {
		super.harvestBlock(world, player, pos, state, te, tool);
		world.setBlockToAir(pos);
	}

	public static void dropItems(IMetalChest chest, World world, BlockPos pos) {
		if (!ModConfig.hasThermalExpansion() || chest.getEnchantLevel() < chest.getChestType().getHoldingEnchantBound()) {
			for (int i = 0; i < chest.getInventory().getSlots(); ++i) {
				ItemStack stack = chest.getInventory().getStackInSlot(i);

				if (!stack.isEmpty()) {
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				}
			}
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileMetalChest) {
			TileMetalChest chest = (TileMetalChest) te;

			dropItems(chest, world, pos);
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

			if (ModConfig.hasThermalExpansion() && stack.getTagCompound() != null) {
				chest.setEnchantLevel((byte) MathHelper.clamp(EnchantmentHelper.getEnchantmentLevel(CoreEnchantments.holding, stack), 0, CoreEnchantments.holding.getMaxLevel()));

				if (stack.getTagCompound().hasKey("Inventory")) {
					chest.readInventoryTag(stack.getTagCompound());
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote && world.getTileEntity(pos) instanceof TileMetalChest && !player.isSneaking() && !isBlocked(world, pos)) {
			player.openGui(RegistryMC.MOD_ID, 0, world, pos.getX(), pos.getY(), pos.getZ());
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
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileMetalChest) {
			return ((TileMetalChest) te).isTrapped() && side != null;
		}

		return false;
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileMetalChest) {
			TileMetalChest chest = (TileMetalChest) te;

			if (chest.isTrapped()) {
				return MathHelper.clamp(chest.numPlayersUsing, 0, 15);
			}
		}

		return 0;
	}

	@Override
	public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.UP ? state.getWeakPower(world, pos, side) : 0;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(IMetalChest.VARIANT).ordinal();
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		for (ChestType type : ChestType.values()) {
			if (type.isRegistered()) {
				items.add(new ItemStack(this, 1, type.ordinal()));
			}
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
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileMetalChest && ((TileMetalChest) te).isLuminous()) {
			return 15;
		}

		return 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(IMetalChest.VARIANT, ChestType.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(IMetalChest.VARIANT).ordinal();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, IMetalChest.VARIANT);
	}

	@Override
	public boolean eventReceived(IBlockState state, World world, BlockPos pos, int id, int param) {
		super.eventReceived(state, world, pos, id, param);
		TileEntity te = world.getTileEntity(pos);
		return te != null && te.receiveClientEvent(id, param);
	}
}
