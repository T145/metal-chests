package T145.metalchests.blocks;

import javax.annotation.Nullable;

import T145.metalchests.MetalChests;
import T145.metalchests.lib.ProjectTableType;
import T145.metalchests.tiles.TileProjectTable;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class BlockProjectTable extends BlockContainer {

	public static final PropertyEnum<ProjectTableType> VARIANT = PropertyEnum.<ProjectTableType>create("variant", ProjectTableType.class);
	public static final PropertyDirection FRONT = BlockHorizontal.FACING;

	public BlockProjectTable() {
		super(Material.WOOD);
		setRegistryName(new ResourceLocation(MetalChests.MODID, "project_table"));
		setDefaultState(blockState.getBaseState().withProperty(VARIANT, ProjectTableType.WOOD));
		setUnlocalizedName("metalchests:project_table");
		setHardness(3F);
		setCreativeTab(MetalChests.TAB);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileProjectTable(ProjectTableType.values()[meta]);
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
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileProjectTable) {
			TileProjectTable table = (TileProjectTable) te;

			if (table.getType() == ProjectTableType.OBSIDIAN) {
				return 10000F;
			}
		}

		return super.getExplosionResistance(world, pos, exploder, explosion);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileProjectTable) {
			TileProjectTable table = (TileProjectTable) te;

			dropInventoryItems(world, pos, table.getUpperInventory());
			dropInventoryItems(world, pos, table.getLowerInventory());

			world.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(world, pos, state);
	}

	private void dropInventoryItems(World world, BlockPos pos, ItemStackHandler inventory) {
		for (int i = 0; i < inventory.getSlots(); ++i) {
			ItemStack stack = inventory.getStackInSlot(i);

			if (!stack.isEmpty()) {
				InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
			}
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileProjectTable) {
			EnumFacing front = EnumFacing.getHorizontal(MathHelper.floor((placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();
			((TileProjectTable) te).setFront(front);
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity te = world.getTileEntity(pos);

		if (!world.isRemote && te instanceof TileProjectTable) {
			TileProjectTable table = (TileProjectTable) te;

			if (!player.isSneaking()) {
				player.openGui(MetalChests.MODID, 1, world, pos.getX(), pos.getY(), pos.getZ());
			}
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

		if (te instanceof TileProjectTable) {
			TileProjectTable table = (TileProjectTable) te;
			return ItemHandlerHelper.calcRedstoneFromInventory(table.getLowerInventory());
		}

		return 0;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		for (ProjectTableType type : ProjectTableType.values()) {
			if (type.isRegistered()) {
				items.add(new ItemStack(this, 1, type.ordinal()));
			}
		}
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileProjectTable) {
			TileProjectTable table = (TileProjectTable) te;
			state = state.withProperty(FRONT, table.getFront());
		}

		return state;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, ProjectTableType.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT, FRONT);
	}
}