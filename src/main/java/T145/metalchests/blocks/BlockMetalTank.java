package T145.metalchests.blocks;

import T145.metalchests.MetalChests;
import T145.metalchests.lib.MetalChestType;
import T145.metalchests.lib.MetalTankType;
import T145.metalchests.tiles.TileMetalTank;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMetalTank extends BlockContainer {

	public static final PropertyEnum<MetalTankType> VARIANT = PropertyEnum.<MetalTankType>create("variant", MetalTankType.class);

	public BlockMetalTank() {
		super(Material.GLASS, MapColor.QUARTZ);
		setRegistryName(new ResourceLocation(MetalChests.MODID, "metal_tank"));
		setDefaultState(blockState.getBaseState().withProperty(VARIANT, MetalTankType.BASE));
		setUnlocalizedName("metalchests:metal_tank");
		setHardness(3F);
		setSoundType(SoundType.GLASS);
		setCreativeTab(MetalChests.TAB);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileMetalTank(MetalTankType.byMetadata(meta));
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
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
	public int damageDropped(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		for (MetalChestType type : MetalChestType.values()) {
			if (type.isRegistered()) {
				items.add(new ItemStack(this, 1, type.ordinal()));
			}
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, MetalTankType.byMetadata(meta));
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