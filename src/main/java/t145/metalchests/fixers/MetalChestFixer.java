package t145.metalchests.fixers;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import t145.metalchests.api.chests.IMetalChest;
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
		if (chestClass != null) {
			String id = tag.getString("id");

			tag.setString("id", IDS.getOrDefault(id, new ResourceLocation(id)).toString());

			if (tag.hasKey("Luminous")) {
				tag.removeTag("Luminous");
			}

			if (tag.hasKey(IMetalChest.TAG_INVENTORY)) {
				NBTTagList itemTags = tag.getCompoundTag(IMetalChest.TAG_INVENTORY).getTagList("Items", 10);

				for (short i = 0; i < itemTags.tagCount(); ++i) {
					itemTags.set(i, fixer.process(FixTypes.ITEM_INSTANCE, itemTags.getCompoundTagAt(i), version));
				}
			}
		}

		return tag;
	}
}
