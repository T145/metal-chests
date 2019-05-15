package T145.metalchests.core;

import T145.metalchests.api.BlocksMC;
import T145.metalchests.api.ItemsMC;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.ChestUpgrade;
import T145.metalchests.api.immutable.RegistryMC;
import T145.metalchests.client.render.blocks.RenderMetalChest;
import T145.metalchests.client.render.blocks.RenderMetalSortingChest;
import T145.metalchests.client.render.entities.RenderBoatMetalChest;
import T145.metalchests.client.render.entities.RenderMinecartMetalChest;
import T145.metalchests.config.ModConfig;
import T145.metalchests.entities.EntityBoatMetalChest;
import T145.metalchests.entities.EntityMinecartMetalChest;
import T145.metalchests.tiles.TileMetalChest;
import T145.metalchests.tiles.TileMetalHungryChest;
import T145.metalchests.tiles.TileMetalHungrySortingChest;
import T145.metalchests.tiles.TileMetalSortingChest;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = RegistryMC.ID, value = Side.CLIENT)
class LoaderClient {

	private LoaderClient() {}

	static ModelResourceLocation getCustomModel(Item item, String customDomain, StringBuilder variantPath) {
		if (StringUtils.isNullOrEmpty(customDomain)) {
			return new ModelResourceLocation(item.getRegistryName(), variantPath.toString());
		} else {
			return new ModelResourceLocation(String.format("%s:%s", RegistryMC.ID, customDomain), variantPath.toString());
		}
	}

	static void registerModel(Item item, String customDomain, int meta, String... variants) {
		StringBuilder variantPath = new StringBuilder(variants[0]);

		for (int i = 1; i < variants.length; ++i) {
			variantPath.append(',').append(variants[i]);
		}

		ModelLoader.setCustomModelResourceLocation(item, meta, getCustomModel(item, customDomain, variantPath));
	}

	static void registerModel(Block block, String customDomain, int meta, String... variants) {
		registerModel(Item.getItemFromBlock(block), customDomain, meta, variants);
	}

	static void registerModel(Item item, int meta, String... variants) {
		registerModel(item, null, meta, variants);
	}

	static void registerModel(Block block, int meta, String... variants) {
		registerModel(block, null, meta, variants);
	}

	static void registerTileRenderer(Class tileClass, TileEntitySpecialRenderer tileRenderer) {
		ClientRegistry.bindTileEntitySpecialRenderer(tileClass, tileRenderer);
	}

	static String getVariantName(IStringSerializable variant) {
		return String.format("variant=%s", variant.getName());
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		for (ChestType type : ChestType.values()) {
			registerModel(BlocksMC.METAL_CHEST, type.ordinal(), getVariantName(type));

			if (ModConfig.GENERAL.enableMinecarts) {
				registerModel(ItemsMC.MINECART_METAL_CHEST, "item_minecart", type.ordinal(), String.format("item=%s_chest", type.getName()));
			}
		}

		registerTileRenderer(TileMetalChest.class, RenderMetalChest.INSTANCE);

		if (ModConfig.hasThaumcraft()) {
			for (ChestType type : ChestType.values()) {
				registerModel(BlocksMC.METAL_HUNGRY_CHEST, type.ordinal(), getVariantName(type));
			}

			registerTileRenderer(TileMetalHungryChest.class, new RenderMetalChest() {

				@Override
				protected ResourceLocation getActiveResource(ChestType type) {
					return new ResourceLocation(RegistryMC.ID, String.format("textures/entity/chest/hungry/%s.png", type.getName()));
				}
			});
		}

		for (ChestUpgrade type : ChestUpgrade.values()) {
			registerModel(ItemsMC.CHEST_UPGRADE, "item_chest_upgrade", type.ordinal(), String.format("item=%s", type.getName()));
		}

		if (ModConfig.hasRefinedRelocation()) {
			for (ChestType type : ChestType.values()) {
				registerModel(BlocksMC.METAL_SORTING_CHEST, type.ordinal(), getVariantName(type));
			}

			registerTileRenderer(TileMetalSortingChest.class, new RenderMetalSortingChest());

			if (ModConfig.hasThaumcraft()) {
				for (ChestType type : ChestType.values()) {
					registerModel(BlocksMC.METAL_HUNGRY_SORTING_CHEST, type.ordinal(), getVariantName(type));
				}

				registerTileRenderer(TileMetalHungrySortingChest.class, new RenderMetalSortingChest() {

					@Override
					protected ResourceLocation getActiveResource(ChestType type) {
						return new ResourceLocation(RegistryMC.ID, String.format("textures/entity/chest/hungry/%s.png", type.getName()));
					}

					@Override
					protected ResourceLocation getActiveOverlay(ChestType type) {
						return new ResourceLocation(RegistryMC.ID, String.format("textures/entity/chest/hungry/overlay/sorting_%s.png", type.getName()));
					}
				});
			}
		}

		RenderingRegistry.registerEntityRenderingHandler(EntityMinecartMetalChest.class, manager -> new RenderMinecartMetalChest(manager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBoatMetalChest.class, manager -> new RenderBoatMetalChest(manager));
	}
}
