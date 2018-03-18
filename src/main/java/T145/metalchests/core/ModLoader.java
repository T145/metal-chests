package T145.metalchests.core;

import java.util.HashSet;

import T145.metalchests.MetalChests;
import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.blocks.base.BlockItemBase;
import T145.metalchests.client.render.blocks.RenderMetalChest;
import T145.metalchests.client.render.entities.RenderMinecartMetalChest;
import T145.metalchests.entities.EntityMinecartCopperChest;
import T145.metalchests.entities.EntityMinecartCrystalChest;
import T145.metalchests.entities.EntityMinecartDiamondChest;
import T145.metalchests.entities.EntityMinecartGoldChest;
import T145.metalchests.entities.EntityMinecartIronChest;
import T145.metalchests.entities.EntityMinecartObsidianChest;
import T145.metalchests.entities.EntityMinecartSilverChest;
import T145.metalchests.entities.ai.EntityAIOcelotSitOnChest;
import T145.metalchests.entities.base.EntityMinecartMetalChestBase;
import T145.metalchests.items.ItemChestStructureUpgrade;
import T145.metalchests.items.ItemMinecartMetalChest;
import T145.metalchests.items.base.ItemBase;
import T145.metalchests.lib.MetalChestType;
import T145.metalchests.lib.MetalChestType.StructureUpgrade;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.minecart.MinecartInteractEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(MetalChests.MODID)
public class ModLoader {

	public static final BlockMetalChest METAL_CHEST = new BlockMetalChest();

	public static final ItemBase MINECART_METAL_CHEST = new ItemMinecartMetalChest();
	public static final ItemBase CHEST_UPGRADE_STRUCTURE = new ItemChestStructureUpgrade(); 

	@EventBusSubscriber(modid = MetalChests.MODID)
	public static class ServerLoader {

		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			final IForgeRegistry<Block> registry = event.getRegistry();
			registry.register(METAL_CHEST);
			registerTileEntity(TileMetalChest.class);
		}

		private static void registerTileEntity(Class tileClass) {
			GameRegistry.registerTileEntity(tileClass, tileClass.getSimpleName());
		}

		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			final IForgeRegistry<Item> registry = event.getRegistry();
			registerItemBlock(registry, METAL_CHEST, MetalChestType.class);
			registry.register(MINECART_METAL_CHEST);
			registry.register(CHEST_UPGRADE_STRUCTURE);
		}

		private static void registerItemBlock(IForgeRegistry<Item> registry, Block block) {
			registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		}

		private static void registerItemBlock(IForgeRegistry<Item> registry, Block block, Class types) {
			registry.register(new BlockItemBase(block, types).setRegistryName(block.getRegistryName()));
		}

		@SubscribeEvent
		public static void registerEntities(final RegistryEvent.Register<EntityEntry> event) {
			final EntityEntry[] entries = {
					createBuilder("MinecartCopperChest")
					.entity(EntityMinecartCopperChest.class)
					.tracker(80, 3, true)
					.build(),
					createBuilder("MinecartIronChest")
					.entity(EntityMinecartIronChest.class)
					.tracker(80, 3, true)
					.build(),
					createBuilder("MinecartSilverChest")
					.entity(EntityMinecartSilverChest.class)
					.tracker(80, 3, true)
					.build(),
					createBuilder("MinecartGoldChest")
					.entity(EntityMinecartGoldChest.class)
					.tracker(80, 3, true)
					.build(),
					createBuilder("MinecartDiamondChest")
					.entity(EntityMinecartDiamondChest.class)
					.tracker(80, 3, true)
					.build(),
					createBuilder("MinecartObsidianChest")
					.entity(EntityMinecartObsidianChest.class)
					.tracker(80, 3, true)
					.build(),
					createBuilder("MinecartCrystalChest")
					.entity(EntityMinecartCrystalChest.class)
					.tracker(80, 3, true)
					.build()
			};

			for (EntityEntry entry : entries) {
				event.getRegistry().register(entry);
			}
		}

		private static int entityID = 0;

		private static <E extends Entity> EntityEntryBuilder<E> createBuilder(final String name) {
			final EntityEntryBuilder<E> builder = EntityEntryBuilder.create();
			final ResourceLocation registryName = new ResourceLocation(MetalChests.MODID, name);
			return builder.id(registryName, entityID++).name(MetalChests.MODID + ":" + name);
		}

		@SubscribeEvent
		public void changeSittingTaskForOcelots(LivingUpdateEvent event) {
			EntityLivingBase creature = event.getEntityLiving();

			if (creature instanceof EntityOcelot && creature.ticksExisted < 5) {
				EntityOcelot ocelot = (EntityOcelot) creature;
				HashSet<EntityAITaskEntry> hashset = new HashSet<EntityAITaskEntry>();

				for (EntityAITaskEntry task : ocelot.tasks.taskEntries) {
					if (task.action.getClass() == EntityAIOcelotSit.class) {
						hashset.add(task);
					}
				}

				for (EntityAITaskEntry task : hashset) {
					ocelot.tasks.removeTask(task.action);
					ocelot.tasks.addTask(task.priority, new EntityAIOcelotSitOnChest(ocelot, 0.4F));
				}
			}
		}

		@SubscribeEvent
		public static void upgradeMinecartChests(MinecartInteractEvent event) {
			World world = event.getMinecart().world;

			if (!world.isRemote) {
				EntityPlayer player = event.getPlayer();
				ItemStack stack = player.getHeldItem(event.getHand());

				if (player.isSneaking() && stack.getItem() instanceof ItemChestStructureUpgrade) {
					ItemChestStructureUpgrade upgradeItem = (ItemChestStructureUpgrade) stack.getItem();
					StructureUpgrade upgrade = StructureUpgrade.byMetadata(stack.getItemDamage());
					EntityMinecart cart = event.getMinecart();

					if (cart instanceof EntityMinecartChest) {
						if (upgrade.canUpgradeWood(Blocks.CHEST.getDefaultState())) {
							EntityMinecartChest normalCart = (EntityMinecartChest) cart;
							EntityMinecartMetalChestBase metalCart = EntityMinecartMetalChestBase.create(world, normalCart.posX, normalCart.posY, normalCart.posZ, upgrade.getUpgrade());

							for (int i = 0; i < normalCart.getSizeInventory(); ++i) {
								metalCart.setInventorySlotContents(i, normalCart.getStackInSlot(i));
							}

							normalCart.dropContentsWhenDead = false;
							normalCart.setDead();

							world.spawnEntity(metalCart);

							if (!player.capabilities.isCreativeMode) {
								stack.shrink(1);
							}
							player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.4F, 0.8F);
						}
					}
				}
			}
		}
	}

	@EventBusSubscriber(modid = MetalChests.MODID, value = Side.CLIENT)
	public static class ClientLoader {

		@SubscribeEvent
		public static void onModelRegistration(ModelRegistryEvent event) {
			for (MetalChestType type : MetalChestType.values()) {
				registerBlockModel(METAL_CHEST, type.ordinal(), type);
				registerItemModel(MINECART_METAL_CHEST, type.ordinal(), "minecarts/" + type.getName());
			}

			registerTileRenderer(TileMetalChest.class, RenderMetalChest.INSTANCE);

			for (MetalChestType.StructureUpgrade type : MetalChestType.StructureUpgrade.values()) {
				registerItemModel(CHEST_UPGRADE_STRUCTURE, type.ordinal(), "upgrades/" + type.getName());
			}

			RenderingRegistry.registerEntityRenderingHandler(EntityMinecartCopperChest.class, manager -> new RenderMinecartMetalChest(manager));
			RenderingRegistry.registerEntityRenderingHandler(EntityMinecartIronChest.class, manager -> new RenderMinecartMetalChest(manager));
			RenderingRegistry.registerEntityRenderingHandler(EntityMinecartSilverChest.class, manager -> new RenderMinecartMetalChest(manager));
			RenderingRegistry.registerEntityRenderingHandler(EntityMinecartGoldChest.class, manager -> new RenderMinecartMetalChest(manager));
			RenderingRegistry.registerEntityRenderingHandler(EntityMinecartDiamondChest.class, manager -> new RenderMinecartMetalChest(manager));
			RenderingRegistry.registerEntityRenderingHandler(EntityMinecartObsidianChest.class, manager -> new RenderMinecartMetalChest(manager));
			RenderingRegistry.registerEntityRenderingHandler(EntityMinecartCrystalChest.class, manager -> new RenderMinecartMetalChest(manager));
		}

		public static String getVariantName(IStringSerializable variant) {
			return "variant=" + variant.getName();
		}

		public static void registerBlockModel(Block block, int meta, String path, String variant) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(MetalChests.MODID + ":" + path, variant));
		}

		public static void registerBlockModel(Block block, int meta, String variant) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(block.getRegistryName(), variant));
		}

		public static void registerBlockModel(Block block, int meta, IStringSerializable variant) {
			registerBlockModel(block, meta, getVariantName(variant));
		}

		public static void registerItemModel(Item item, int meta, String path) {
			ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(MetalChests.MODID + ":" + path, "inventory"));
		}

		public static void registerItemModel(Item item, int meta) {
			ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}

		public static void registerTileRenderer(Class tileClass, TileEntitySpecialRenderer tileRenderer) {
			ClientRegistry.bindTileEntitySpecialRenderer(tileClass, tileRenderer);
		}
	}
}