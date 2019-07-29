package t145.metalchests.fixers;

import java.util.HashMap;
import java.util.Map;

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

	private static final Map<String, String> IDS = new HashMap<>();

	static {
		IDS.put("metalchests:hungry_metal_chest", RegistryMC.RESOURCE_METAL_HUNGRY_CHEST.toString());
		IDS.put("metalchests:sorting_metal_chest", RegistryMC.RESOURCE_METAL_SORTING_CHEST.toString());
		IDS.put("metalchests:sorting_hungry_metal_chest", RegistryMC.RESOURCE_METAL_SORTING_HUNGRY_CHEST.toString());
	}

	private final Class<? extends TileMetalChest> chestClass;

	public MetalChestFixer(Class<? extends TileMetalChest> chestClass) {
		this.chestClass = TileMetalChest.class.isAssignableFrom(chestClass) ? chestClass : null;
	}

	@Override
	public NBTTagCompound process(IDataFixer fixer, NBTTagCompound tag, int version) {
		if (chestClass != null) {
			String id = tag.getString("id");
			ResourceLocation res = new ResourceLocation(id);

			if (res.getNamespace().contentEquals(RegistryMC.ID)) {
				RegistryMC.LOG.info("WE'RE FIXING THINGS!!!");
				String path = res.toString();

				tag.setString("id", IDS.getOrDefault(path, path));

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
		}

		return tag;
	}
}
