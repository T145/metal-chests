package t145.metalchests.fixers;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import t145.metalchests.api.consts.RegistryMC;
import t145.metalchests.tiles.TileMetalChest;

public class MetalChestFixer implements IDataWalker {

	private static final Object2ObjectOpenHashMap<ResourceLocation, ResourceLocation> IDS = new Object2ObjectOpenHashMap<>();

	static {
		IDS.put(RegistryMC.getResource("hungry_metal_chest"), RegistryMC.RESOURCE_METAL_HUNGRY_CHEST);
		IDS.put(RegistryMC.getResource("sorting_metal_chest"), RegistryMC.RESOURCE_METAL_SORTING_CHEST);
		IDS.put(RegistryMC.getResource("sorting_hungry_metal_chest"), RegistryMC.RESOURCE_METAL_SORTING_HUNGRY_CHEST);
	}

	private final Class<? extends TileMetalChest> chestClass;

	public MetalChestFixer(Class<? extends TileMetalChest> chestClass) {
		if (TileMetalChest.class.isAssignableFrom(chestClass)) {
			this.chestClass = chestClass;
		} else {
			this.chestClass = null;
		}
	}

	@Override
	public NBTTagCompound process(IDataFixer fixer, NBTTagCompound tag, int version) {
		RegistryMC.LOG.info(chestClass);
		if (chestClass != null) {
			RegistryMC.LOG.info("WE BE FIXING THINGS!");
			String id = tag.getString("id");

			tag.setString("id", IDS.getOrDefault(id, new ResourceLocation(id)).toString());

			if (tag.hasKey("Luminous")) {
				tag.removeTag("Luminous");
			}

			tag = DataFixesManager.processInventory(fixer, tag, version, "Items");
		}

		return tag;
	}
}
