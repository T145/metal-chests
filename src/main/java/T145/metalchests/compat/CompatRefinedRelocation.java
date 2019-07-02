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
package T145.metalchests.compat;

import javax.annotation.Nullable;

import org.apache.commons.lang3.text.WordUtils;

import T145.metalchests.api.chests.IMetalChest;
import T145.metalchests.api.chests.UpgradeRegistry;
import T145.metalchests.api.config.ConfigMC;
import T145.metalchests.api.consts.ChestType;
import T145.metalchests.api.consts.RegistryMC;
import T145.metalchests.api.obj.BlocksMC;
import T145.metalchests.api.obj.ItemsMC;
import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.blocks.BlockMetalChestItem;
import T145.metalchests.client.render.blocks.RenderMetalSortingChest;
import T145.metalchests.tiles.TileMetalChest;
import T145.metalchests.tiles.TileMetalHungryChest;
import T145.metalchests.tiles.TileMetalSortingChest;
import T145.metalchests.tiles.TileMetalSortingHungryChest;
import T145.tbone.core.TBone;
import net.blay09.mods.refinedrelocation.ModBlocks;
import net.blay09.mods.refinedrelocation.item.ItemSortingUpgrade;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = RegistryMC.ID)
class CompatRefinedRelocation {

	private CompatRefinedRelocation() {}

	@Optional.Method(modid = RegistryMC.ID_RR2)
	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		final IForgeRegistry<Block> registry = event.getRegistry();

		registry.register(BlocksMC.METAL_SORTING_CHEST = new BlockMetalChest() {

			@Override
			protected void registerResource() {
				this.registerResource(RegistryMC.RESOURCE_METAL_SORTING_CHEST);
			}

			@Nullable
			@Override
			public TileEntity createTileEntity(World world, IBlockState state) {
				return new TileMetalSortingChest(state.getValue(IMetalChest.VARIANT));
			}
		});

		TBone.registerTileEntity(TileMetalSortingChest.class, RegistryMC.ID);
	}

	@Optional.Method(modid = RegistryMC.ID_RR2)
	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();

		registry.register(new BlockMetalChestItem(ChestType.values(), BlocksMC.METAL_SORTING_CHEST));
	}

	@Optional.Method(modid = RegistryMC.ID_RR2)
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		for (ChestType type : ChestType.values()) {
			TBone.registerModel(RegistryMC.ID, BlocksMC.METAL_SORTING_CHEST, type.ordinal(), TBone.getVariantName(type));
		}

		TBone.registerTileRenderer(TileMetalSortingChest.class, new RenderMetalSortingChest());
	}

	private static void registerSortingChestRecipe(Block baseChest, Block metalChest, ChestType type, String postfix) {
		GameRegistry.addShapedRecipe(RegistryMC.getResource(String.format("recipeChest%s%sAlt", WordUtils.capitalize(type.getName()), postfix)), null,
				new ItemStack(metalChest, 1, type.ordinal()),
				" a ", "bcb", " d ",
				'a', Items.WRITABLE_BOOK,
				'b', Items.REDSTONE,
				'c', new ItemStack(baseChest, 1, type.ordinal()),
				'd', Blocks.HOPPER);
	}

	@Optional.Method(modid = RegistryMC.ID_RR2)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		UpgradeRegistry.register(ItemsMC.CHEST_UPGRADE, ModBlocks.sortingChest, BlocksMC.METAL_SORTING_CHEST);

		for (ChestType type : ChestType.values()) {
			if (type.isRegistered()) {
				registerSortingChestRecipe(BlocksMC.METAL_CHEST, BlocksMC.METAL_SORTING_CHEST, type, "Sorting");

				if (ConfigMC.hasThaumcraft()) {
					registerSortingChestRecipe(BlocksMC.METAL_HUNGRY_CHEST, BlocksMC.METAL_SORTING_HUNGRY_CHEST, type, "SortingHungry");
				}
			}
		}
	}

	@Optional.Method(modid = RegistryMC.ID_RR2)
	@SubscribeEvent
	public static void onRightClick(RightClickBlock event) {
		World world = event.getWorld();

		if (world.isRemote) {
			return;
		}

		BlockPos pos = event.getPos();
		TileEntity te = world.getTileEntity(pos);

		if (te == null) {
			return;
		}

		EntityPlayer player = event.getEntityPlayer();
		ItemStack stack = event.getItemStack();

		if (player.isSneaking() && te instanceof TileMetalChest && stack.getItem() instanceof ItemSortingUpgrade) {
			te.updateContainingBlockInfo();

			TileMetalChest oldChest = (TileMetalChest) te;
			TileMetalChest newChest = te instanceof TileMetalHungryChest ? new TileMetalSortingHungryChest(oldChest.getChestType()) : new TileMetalSortingChest(oldChest.getChestType());
			Block chestBlock = newChest instanceof TileMetalSortingHungryChest ? BlocksMC.METAL_SORTING_HUNGRY_CHEST : BlocksMC.METAL_SORTING_CHEST;

			world.removeTileEntity(pos);
			world.setBlockToAir(pos);
			world.setTileEntity(pos, newChest);

			IBlockState state = chestBlock.getDefaultState().withProperty(IMetalChest.VARIANT, newChest.getChestType());
			world.setBlockState(pos, state, 3);
			world.notifyBlockUpdate(pos, state, state, 3);

			TileEntity tile = world.getTileEntity(pos);

			if (tile instanceof TileMetalChest) {
				TileMetalChest chest = (TileMetalChest) tile;
				chest.setInventory(oldChest.getInventory());
				chest.setFront(oldChest.getFront());
			}
		}
	}
}
