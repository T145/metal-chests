package T145.metalchests.core.compat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.text.WordUtils;

import T145.metalchests.api.BlocksMC;
import T145.metalchests.api.chests.ChestAnimator;
import T145.metalchests.api.chests.IMetalChest;
import T145.metalchests.api.chests.UpgradeRegistry;
import T145.metalchests.api.constants.ChestType;
import T145.metalchests.api.constants.RegistryMC;
import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.client.render.blocks.RenderMetalChest;
import T145.metalchests.config.ModConfig;
import T145.metalchests.core.MetalChests;
import T145.metalchests.tiles.TileMetalHungryChest;
import T145.metalchests.tiles.TileMetalHungrySortingChest;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import thaumcraft.api.blocks.BlocksTC;

@EventBusSubscriber(modid = RegistryMC.ID)
class CompatThaumcraft {

	private CompatThaumcraft() {}

	private static ItemStack tryToInsertStack(IItemHandler inv, @Nonnull ItemStack stack) {
		for (int slot = 0; slot < inv.getSlots(); ++slot) {
			if (!inv.getStackInSlot(slot).isEmpty()) {
				stack = inv.insertItem(slot, stack, false);

				if (stack.isEmpty()) {
					return ItemStack.EMPTY;
				}
			}
		}

		for (int slot = 0; slot < inv.getSlots(); ++slot) {
			stack = inv.insertItem(slot, stack, false);

			if (stack.isEmpty()) {
				return ItemStack.EMPTY;
			}
		}

		return stack;
	}

	private static void tryToEatItem(World world, BlockPos pos, IBlockState state, Entity entity, Block receiver) {
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof IMetalChest && entity instanceof EntityItem && !entity.isDead) {
			IMetalChest chest = (IMetalChest) te;
			EntityItem item = (EntityItem) entity;
			ItemStack stack = item.getItem();
			ItemStack leftovers = tryToInsertStack(chest.getInventory(), stack);

			if (leftovers == null || leftovers.getCount() != stack.getCount()) {
				entity.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.25F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
				world.addBlockEvent(pos, receiver, ChestAnimator.EVENT_CHEST_NOM, 2);
			}

			if (leftovers != null) {
				item.setItem(leftovers);
			} else {
				entity.setDead();
			}
		}
	}

	@Optional.Method(modid = RegistryMC.ID_THAUMCRAFT)
	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		final IForgeRegistry<Block> registry = event.getRegistry();

		registry.register(BlocksMC.METAL_HUNGRY_CHEST = new BlockMetalChest() {

			@Override
			protected void registerResource() {
				this.registerResource(RegistryMC.RESOURCE_METAL_HUNGRY_CHEST);
			}

			@Nullable
			@Override
			public TileEntity createTileEntity(World world, IBlockState state) {
				return new TileMetalHungryChest(state.getValue(IMetalChest.VARIANT));
			}

			@Override
			public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
				tryToEatItem(world, pos, state, entity, this);
			}
		});

		MetalChests.registerTileEntity(TileMetalHungryChest.class);

		if (ModConfig.hasRefinedRelocation()) {
			registry.register(BlocksMC.METAL_HUNGRY_SORTING_CHEST = new BlockMetalChest() {

				protected void registerResource() {
					this.registerResource(RegistryMC.RESOURCE_METAL_HUNGRY_SORTING_CHEST);
				}

				@Nullable
				@Override
				public TileEntity createTileEntity(World world, IBlockState state) {
					return new TileMetalHungrySortingChest(state.getValue(IMetalChest.VARIANT));
				}

				@Override
				public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
					tryToEatItem(world, pos, state, entity, this);
				}
			});

			MetalChests.registerTileEntity(TileMetalHungrySortingChest.class);
		}
	}

	@Optional.Method(modid = RegistryMC.ID_THAUMCRAFT)
	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();

		MetalChests.registerItemBlock(registry, BlocksMC.METAL_HUNGRY_CHEST, ChestType.class);

		if (ModConfig.hasRefinedRelocation()) {
			MetalChests.registerItemBlock(registry, BlocksMC.METAL_HUNGRY_SORTING_CHEST, ChestType.class);
		}
	}

	@Optional.Method(modid = RegistryMC.ID_THAUMCRAFT)
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		for (ChestType type : ChestType.values()) {
			MetalChests.registerModel(BlocksMC.METAL_HUNGRY_CHEST, type.ordinal(), MetalChests.getVariantName(type));
		}

		MetalChests.registerTileRenderer(TileMetalHungryChest.class, new RenderMetalChest() {

			@Override
			protected ResourceLocation getActiveResource(ChestType type) {
				return new ResourceLocation(RegistryMC.ID, String.format("textures/entity/chest/hungry/%s.png", type.getName()));
			}
		});
	}

	@Optional.Method(modid = RegistryMC.ID_THAUMCRAFT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		MetalChests.registerMetalChestRecipes(BlocksTC.hungryChest, BlocksMC.METAL_HUNGRY_CHEST, "hungry_chest");
		UpgradeRegistry.registerChest(BlocksTC.hungryChest, BlocksMC.METAL_HUNGRY_CHEST);

		for (ChestType type : ChestType.values()) {
			if (type.isRegistered()) {
				String capitalizedName = WordUtils.capitalize(type.getName());
				ItemStack stack = new ItemStack(BlocksMC.METAL_HUNGRY_CHEST, 1, type.ordinal());

				OreDictionary.registerOre("chest", stack);
				OreDictionary.registerOre(String.format("chestHungry%s", capitalizedName), stack);

				if (ModConfig.hasRefinedRelocation()) {
					stack = new ItemStack(BlocksMC.METAL_HUNGRY_SORTING_CHEST, 1, type.ordinal());

					OreDictionary.registerOre("chest", stack);
					OreDictionary.registerOre(String.format("chestSortingHungry%s", capitalizedName), stack);
				}
			}
		}
	}
}
