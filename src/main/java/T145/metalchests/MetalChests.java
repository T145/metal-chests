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
package T145.metalchests;

import java.io.IOException;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import T145.metalchests.api.chests.UpgradeRegistry;
import T145.metalchests.api.config.ConfigMC;
import T145.metalchests.api.consts.ChestType;
import T145.metalchests.api.consts.ChestUpgrade;
import T145.metalchests.api.consts.RegistryMC;
import T145.metalchests.api.obj.BlocksMC;
import T145.metalchests.api.obj.ItemsMC;
import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.blocks.BlockMetalChestItem;
import T145.metalchests.client.gui.GuiHandler;
import T145.metalchests.client.render.blocks.RenderMetalChest;
import T145.metalchests.entities.ai.EntityAIOcelotSitOnChest;
import T145.metalchests.items.ItemChestUpgrade;
import T145.metalchests.net.PacketHandlerMC;
import T145.metalchests.net.client.MessageSyncMetalChest;
import T145.metalchests.tiles.TileMetalChest;
import T145.metalchests.tiles.TileMetalHungrySortingChest;
import T145.metalchests.tiles.TileMetalSortingChest;
import T145.tbone.core.TBone;
import T145.tbone.network.TPacketHandler;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(modid = RegistryMC.ID, name = RegistryMC.NAME, version = MetalChests.VERSION, updateJSON = MetalChests.UPDATE_JSON,
dependencies = "required-after:tbone;after:chesttransporter;after:quark;after:thaumcraft;after:refinedrelocation")
@EventBusSubscriber(modid = RegistryMC.ID)
public class MetalChests {

	static final String VERSION = "@VERSION@";
	static final String UPDATE_JSON = "https://raw.githubusercontent.com/T145/metalchests/master/update.json";

	public static final Logger LOG = LogManager.getLogger(RegistryMC.ID);
	public static final TPacketHandler NETWORK = new PacketHandlerMC();
	public static final CreativeTabs TAB = new CreativeTabs(RegistryMC.ID) {

		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack createIcon() {
			return new ItemStack(BlocksMC.METAL_CHEST, 1, 1);
		}

		@SideOnly(Side.CLIENT)
		public void displayAllRelevantItems(NonNullList<ItemStack> items) {
			BlocksMC.METAL_CHEST.getSubBlocks(this, items);
			ItemsMC.CHEST_UPGRADE.getSubItems(this, items);

			if (BlocksMC.METAL_HUNGRY_CHEST != null) {
				BlocksMC.METAL_HUNGRY_CHEST.getSubBlocks(this, items);
			}

			if (BlocksMC.METAL_SORTING_CHEST != null) {
				BlocksMC.METAL_SORTING_CHEST.getSubBlocks(this, items);
			}

			if (BlocksMC.METAL_HUNGRY_SORTING_CHEST != null) {
				BlocksMC.METAL_HUNGRY_SORTING_CHEST.getSubBlocks(this, items);
			}
		}
	}.setBackgroundImageName("item_search.png");

	@Instance(RegistryMC.ID)
	public static MetalChests instance;

	public MetalChests() {
		TBone.registerMod(RegistryMC.ID, RegistryMC.NAME);
	}

	public static final DataSerializer<ChestType> CHEST_TYPE = new DataSerializer<ChestType>() {

		@Override
		public void write(PacketBuffer buf, ChestType value) {
			buf.writeEnumValue(value);
		}

		@Override
		public ChestType read(PacketBuffer buf) throws IOException {
			return buf.readEnumValue(ChestType.class);
		}

		@Override
		public DataParameter<ChestType> createKey(int id) {
			return new DataParameter<ChestType>(id, this);
		}

		@Override
		public ChestType copyValue(ChestType value) {
			return value;
		}
	};

	@SubscribeEvent
	public static void metalchests$updateConfig(OnConfigChangedEvent event) {
		if (event.getModID().equals(RegistryMC.ID)) {
			ConfigManager.sync(RegistryMC.ID, Config.Type.INSTANCE);
		}
	}

	@EventHandler
	public void metalchests$preInit(FMLPreInitializationEvent event) {
		ModMetadata meta = event.getModMetadata();
		meta.authorList.add("T145");
		meta.autogenerated = false;
		meta.credits = "The fans!";
		meta.description = "The better alternative to IronChests";
		meta.logoFile = "logo.png";
		meta.modId = RegistryMC.ID;
		meta.name = RegistryMC.NAME;
		meta.url = "https://github.com/T145/metalchests";
		meta.useDependencyInformation = false;
		meta.version = VERSION;
		NETWORK.registerMessages();
	}

	@EventHandler
	public void metalchests$init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(MetalChests.instance, new GuiHandler());

		DataFixer fixer = FMLCommonHandler.instance().getDataFixer();

		TBone.registerInventoryFixes(fixer, FixTypes.BLOCK_ENTITY, TileMetalChest.class);
		TBone.registerInventoryFixes(fixer, FixTypes.ENTITY, TileMetalChest.class);

		if (ConfigMC.hasRefinedRelocation()) {
			TBone.registerInventoryFixes(fixer, FixTypes.BLOCK_ENTITY, TileMetalSortingChest.class);
		}

		if (ConfigMC.hasThaumcraft() && ConfigMC.hasRefinedRelocation()) {
			TBone.registerInventoryFixes(fixer, FixTypes.BLOCK_ENTITY, TileMetalHungrySortingChest.class);
		}
	}

	@EventHandler
	public void metalchests$postInit(FMLPostInitializationEvent event) {
		OreDictionary.getOres("chestWood").forEach(stack -> {
			UpgradeRegistry.registerChest(Block.getBlockFromItem(stack.getItem()), BlocksMC.METAL_CHEST);
		});

		UpgradeRegistry.registerChest(Blocks.TRAPPED_CHEST, BlocksMC.METAL_CHEST);
	}

	@SubscribeEvent
	public static void metalchests$registerSerializers(final RegistryEvent.Register<DataSerializerEntry> event) {
		final IForgeRegistry<DataSerializerEntry> registry = event.getRegistry();

		registry.register(new DataSerializerEntry(CHEST_TYPE).setRegistryName(RegistryMC.ID, "chest_type"));
	}

	@SubscribeEvent
	public static void metalchests$registerBlocks(final RegistryEvent.Register<Block> event) {
		final IForgeRegistry<Block> registry = event.getRegistry();

		registry.register(BlocksMC.METAL_CHEST = new BlockMetalChest());
		TBone.registerTileEntity(TileMetalChest.class, RegistryMC.ID);
	}

	@SubscribeEvent
	public static void metalchests$registerItems(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();

		registry.register(new BlockMetalChestItem(ChestType.TIERS, BlocksMC.METAL_CHEST));
		registry.register(ItemsMC.CHEST_UPGRADE = new ItemChestUpgrade());
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void metalchests$registerRecipes(RegistryEvent.Register<IRecipe> event) {
		ChestType.registerRecipes();
		ChestUpgrade.registerRecipes();
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void metalchests$registerModels(ModelRegistryEvent event) {
		for (ChestType type : ChestType.values()) {
			TBone.registerModel(RegistryMC.ID, BlocksMC.METAL_CHEST, type.ordinal(), TBone.getVariantName(type));
		}

		TBone.registerTileRenderer(TileMetalChest.class, RenderMetalChest.INSTANCE);

		for (int i = 0; i < ChestUpgrade.TIERS.size(); ++i) {
			ChestUpgrade type = ChestUpgrade.TIERS.get(i);
			TBone.registerModel(RegistryMC.ID, ItemsMC.CHEST_UPGRADE, "item_chest_upgrade", i, String.format("item=%s", type.getName()));
		}
	}

	@SubscribeEvent
	public static void metalchests$tickOcelot(LivingUpdateEvent event) {
		EntityLivingBase creature = event.getEntityLiving();

		if (creature instanceof EntityOcelot && creature.ticksExisted < 5) {
			EntityOcelot ocelot = (EntityOcelot) creature;
			HashSet<EntityAITaskEntry> tasks = new HashSet<EntityAITaskEntry>();

			for (EntityAITaskEntry task : ocelot.tasks.taskEntries) {
				if (task.action.getClass() == EntityAIOcelotSit.class) {
					tasks.add(task);
				}
			}

			for (EntityAITaskEntry task : tasks) {
				ocelot.tasks.removeTask(task.action);
				ocelot.tasks.addTask(task.priority, new EntityAIOcelotSitOnChest(ocelot, 0.4F));
			}
		}
	}

	@SubscribeEvent
	public static void metalchests$activateMetalChest(RightClickBlock event) {
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

		if (player.isSneaking() && te instanceof TileMetalChest) {
			TileMetalChest chest = (TileMetalChest) te;
			boolean hasRedstone = stack.getItem().equals(Items.REDSTONE);
			boolean hasGlowstone = stack.getItem().equals(Items.GLOWSTONE_DUST);

			if (hasRedstone) {
				if (chest.isTrapped()) {
					chest.setTrapped(false);
				} else {
					if (!player.capabilities.isCreativeMode) {
						stack.shrink(1);
					}
					chest.setTrapped(true);
				}
				NETWORK.sendToAllAround(new MessageSyncMetalChest(pos, chest));
			}

			if (hasGlowstone) {
				if (chest.isLuminous()) {
					chest.setLuminous(false);
				} else {
					if (!player.capabilities.isCreativeMode) {
						stack.shrink(1);
					}
					chest.setLuminous(true);
				}
				NETWORK.sendToAllAround(new MessageSyncMetalChest(pos, chest));
			}
		}
	}
}
