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
package t145.metalchests.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import T145.tbone.core.TBone;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
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
import net.minecraftforge.fml.common.Loader;
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
import net.minecraftforge.registries.IForgeRegistry;
import t145.metalchests.api.chests.UpgradeRegistry;
import t145.metalchests.api.config.ConfigMC;
import t145.metalchests.api.consts.ChestType;
import t145.metalchests.api.consts.ChestUpgrade;
import t145.metalchests.api.consts.RegistryMC;
import t145.metalchests.api.objs.BlocksMC;
import t145.metalchests.api.objs.ItemsMC;
import t145.metalchests.blocks.BlockMetalChest;
import t145.metalchests.blocks.BlockMetalChestItem;
import t145.metalchests.client.gui.GuiHandler;
import t145.metalchests.client.render.blocks.RenderMetalChest;
import t145.metalchests.entities.ai.EntityAIOcelotSitOnChest;
import t145.metalchests.items.ItemChestUpgrade;
import t145.metalchests.recipes.RecipeHandler;
import t145.metalchests.tiles.TileMetalChest;
import t145.metalchests.tiles.TileMetalSortingChest;
import t145.metalchests.tiles.TileMetalSortingHungryChest;

@Mod(modid = RegistryMC.ID, name = RegistryMC.NAME, version = MetalChests.VERSION, updateJSON = MetalChests.UPDATE_JSON,
dependencies = "required-after:tbone;after:chesttransporter;after:thaumcraft")
@EventBusSubscriber
public class MetalChests {

	public static final String VERSION = "@VERSION@";
	public static final String UPDATE_JSON = "https://raw.githubusercontent.com/T145/metalchests/master/update.json";

	@Instance(RegistryMC.ID)
	public static MetalChests instance;

	public MetalChests() {
		TBone.registerMod(RegistryMC.ID, RegistryMC.NAME);
	}

	private static void generateConfig(File cfg) throws IOException {
		FileWriter writer = new FileWriter(cfg.getAbsolutePath());
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(String.format("{\r\n" + 
				"  \"_comment_version\": \"The mod version this config was generated under. If this mismatches the current version, this config will be regenerated.\",\r\n" + 
				"  \"version\": \"%s\",\r\n" + 
				"  \"_comment_enabled_auto\": \"The enabled auto option will enable the variant if it finds its corresponding oredict name existing.\",\r\n" + 
				"  \"_comment_enabled_auto_1\": \"Automatically configured variants behave as though they do not exist when first loading the mod,\",\r\n" + 
				"  \"_comment_enabled_auto_2\": \"so label their index at the *expected* position in the order if they are loaded.\",\r\n" + 
				"  \"_comment_enabled_toggle\": \"Set enabled to true or false for explicit variant toggling.\",\r\n" + 
				"  \"_comment_size\": \"Change the number of rows and columns for chest inventories (max 8 rows x 23 cols., or 192 slots)\",\r\n" + 
				"  \"chests\": {\r\n" + 
				"    \"copper\": {\r\n" + 
				"      \"index\": 0,\r\n" + 
				"      \"enabled\": \"auto\",\r\n" + 
				"      \"rows\": 5,\r\n" + 
				"      \"cols\": 9,\r\n" + 
				"      \"holding\": 1\r\n" + 
				"    },\r\n" + 
				"    \"iron\": {\r\n" + 
				"      \"index\": 0,\r\n" + 
				"      \"enabled\": true,\r\n" + 
				"      \"rows\": 6,\r\n" + 
				"      \"cols\": 9,\r\n" + 
				"      \"holding\": 1\r\n" + 
				"    },\r\n" + 
				"    \"silver\": {\r\n" + 
				"      \"index\": 2,\r\n" + 
				"      \"enabled\": \"auto\",\r\n" + 
				"      \"rows\": 6,\r\n" + 
				"      \"cols\": 12,\r\n" + 
				"      \"holding\": 2\r\n" + 
				"    },\r\n" + 
				"    \"gold\": {\r\n" + 
				"      \"index\": 1,\r\n" + 
				"      \"enabled\": true,\r\n" + 
				"      \"rows\": 5,\r\n" + 
				"      \"cols\": 16,\r\n" + 
				"      \"holding\": 2\r\n" + 
				"    },\r\n" + 
				"    \"diamond\": {\r\n" + 
				"      \"index\": 2,\r\n" + 
				"      \"enabled\": true,\r\n" + 
				"      \"rows\": 6,\r\n" + 
				"      \"cols\": 18,\r\n" + 
				"      \"holding\": 3\r\n" + 
				"    },\r\n" + 
				"    \"obsidian\": {\r\n" + 
				"      \"index\": 3,\r\n" + 
				"      \"enabled\": true,\r\n" + 
				"      \"rows\": 6,\r\n" + 
				"      \"cols\": 18,\r\n" + 
				"      \"holding\": 4\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}", VERSION));
		writer.write(gson.toJson(je));
		writer.close();
	}

	private static JsonObject getJsonObj(File cfg) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		return (JsonObject) new JsonParser().parse(new FileReader(cfg));
	}

	private static JsonObject loadSettings() {
		File cfg = new File(String.format("%s/T145/%s.json", Loader.instance().getConfigDir(), RegistryMC.NAME));

		try {
			if (cfg.exists()) {
				JsonObject obj = getJsonObj(cfg);

				if (VERSION.contentEquals(obj.get("version").getAsString())) {
					return obj;
				} else if (ConfigMC.regenConfig) {
					generateConfig(cfg);
				}
			} else {
				generateConfig(cfg);
			}

			return getJsonObj(cfg);
		} catch (JsonIOException | JsonSyntaxException | IOException err) {
			RegistryMC.LOG.catching(err);
		}
		return null;
	}

	@SubscribeEvent
	public static void metalchests$updateConfig(OnConfigChangedEvent event) {
		if (event.getModID().equals(RegistryMC.ID)) {
			ConfigManager.sync(RegistryMC.ID, Config.Type.INSTANCE);
		}
	}

	@EventHandler
	public void metalchests$preInit(FMLPreInitializationEvent event) {
		ChestType.setTiers(loadSettings());
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
	}

	@EventHandler
	public void metalchests$init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(MetalChests.instance, new GuiHandler());

		DataFixer fixer = FMLCommonHandler.instance().getDataFixer();

		TBone.registerInventoryFixes(fixer, FixTypes.BLOCK_ENTITY, TileMetalChest.class);

		if (ConfigMC.hasRefinedRelocation()) {
			TBone.registerInventoryFixes(fixer, FixTypes.BLOCK_ENTITY, TileMetalSortingChest.class);
		}

		if (ConfigMC.hasThaumcraft() && ConfigMC.hasRefinedRelocation()) {
			TBone.registerInventoryFixes(fixer, FixTypes.BLOCK_ENTITY, TileMetalSortingHungryChest.class);
		}
	}

	@EventHandler
	public void metalchests$postInit(FMLPostInitializationEvent event) {
		OreDictionary.getOres("chestWood").forEach(stack -> {
			UpgradeRegistry.register(ItemsMC.CHEST_UPGRADE, Block.getBlockFromItem(stack.getItem()), BlocksMC.METAL_CHEST);
		});

		OreDictionary.getOres("chestTrapped").forEach(stack -> {
			UpgradeRegistry.register(ItemsMC.CHEST_UPGRADE, Block.getBlockFromItem(stack.getItem()), BlocksMC.METAL_CHEST);
		});
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
		registry.register(ItemsMC.CHEST_UPGRADE = new ItemChestUpgrade(RegistryMC.RESOURCE_CHEST_UPGRADE));
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void metalchests$registerRecipes(RegistryEvent.Register<IRecipe> event) {
		ChestType.postInit();
		RecipeHandler.registerChests("chestWood", BlocksMC.METAL_CHEST, StringUtils.EMPTY);
		RecipeHandler.registerUpgrades(ItemsMC.CHEST_UPGRADE, "plankWood", StringUtils.EMPTY);
	}

	@SubscribeEvent
	public static void metalchests$registerOre(final OreDictionary.OreRegisterEvent event) {
		ChestType.attemptRegister(event.getName());
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void metalchests$registerModels(ModelRegistryEvent event) {
		ChestType.TIERS.forEach(type -> TBone.registerModel(RegistryMC.ID, BlocksMC.METAL_CHEST, type.ordinal(), TBone.getVariantName(type)));
		TBone.registerTileRenderer(TileMetalChest.class, RenderMetalChest.INSTANCE);

		for (short i = 0; i < ChestUpgrade.TIERS.size(); ++i) {
			ChestUpgrade type = ChestUpgrade.TIERS.get(i);
			TBone.registerModel(RegistryMC.ID, ItemsMC.CHEST_UPGRADE, String.format("item_%s", RegistryMC.KEY_CHEST_UPGRADE), i, String.format("item=%s", type.getName()));
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
		BlockPos pos = event.getPos();
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileMetalChest) {
			EntityPlayer player = event.getEntityPlayer();

			if (player.isSneaking()) {
				TileMetalChest chest = (TileMetalChest) te;
				ItemStack stack = event.getItemStack();
				boolean hasRedstone = stack.getItem().equals(Items.REDSTONE);

				if (hasRedstone) {
					if (chest.isTrapped()) {
						chest.setTrapped(false);
					} else {
						if (!player.capabilities.isCreativeMode) {
							stack.shrink(1);
						}
						chest.setTrapped(true);
					}
					event.setCanceled(true);
				}
			}
		}
	}
}
