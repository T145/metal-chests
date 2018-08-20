package T145.metalchests.api;

import T145.metalchests.core.MetalChests;
import net.minecraft.util.ResourceLocation;

public class RegistryMC {

	private RegistryMC() {}

	public static final String KEY_METAL_CHEST = "metal_chest";
	public static final String KEY_HUNGRY_METAL_CHEST = "hungry_metal_chest";
	public static final String KEY_SORTING_METAL_CHEST = "sorting_metal_chest";
	public static final String KEY_SORTING_HUNGRY_METAL_CHEST = "sorting_hungry_metal_chest";

	public static final ResourceLocation RESOURCE_METAL_CHEST = new ResourceLocation(MetalChests.MOD_ID, KEY_METAL_CHEST);
	public static final ResourceLocation RESOURCE_HUNGRY_METAL_CHEST = new ResourceLocation(MetalChests.MOD_ID, KEY_HUNGRY_METAL_CHEST);
	public static final ResourceLocation RESOURCE_SORTING_METAL_CHEST = new ResourceLocation(MetalChests.MOD_ID, KEY_SORTING_METAL_CHEST);
	public static final ResourceLocation RESOURCE_SORTING_HUNGRY_METAL_CHEST = new ResourceLocation(MetalChests.MOD_ID, KEY_SORTING_HUNGRY_METAL_CHEST);

	public static final String KEY_CHEST_UPGRADE = "chest_upgrade";
	public static final String KEY_HUNGRY_CHEST_UPGRADE = "hungry_chest_upgrade";
	public static final String KEY_MINECART_METAL_CHEST = "minecart_metal_chest";

	public static final ResourceLocation RESOURCE_CHEST_UPGRADE = new ResourceLocation(MetalChests.MOD_ID, KEY_CHEST_UPGRADE);
	public static final ResourceLocation RESOURCE_HUNGRY_CHEST_UPGRADE = new ResourceLocation(MetalChests.MOD_ID, KEY_HUNGRY_CHEST_UPGRADE);
	public static final ResourceLocation RESOURCE_MINECART_METAL_CHEST = new ResourceLocation(MetalChests.MOD_ID, KEY_MINECART_METAL_CHEST);
}
