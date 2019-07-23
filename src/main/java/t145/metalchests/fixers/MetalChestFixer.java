package t145.metalchests.fixers;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import t145.metalchests.api.consts.RegistryMC;

public class MetalChestFixer implements IDataWalker {

	private static final Object2ObjectOpenHashMap<ResourceLocation, ResourceLocation> IDS = new Object2ObjectOpenHashMap<>();

	static {
		IDS.put(RegistryMC.getResource("hungry_metal_chest"), RegistryMC.RESOURCE_METAL_HUNGRY_CHEST);
		IDS.put(RegistryMC.getResource("sorting_metal_chest"), RegistryMC.RESOURCE_METAL_SORTING_CHEST);
		IDS.put(RegistryMC.getResource("sorting_hungry_metal_chest"), RegistryMC.RESOURCE_METAL_SORTING_HUNGRY_CHEST);
	}

	private final ResourceLocation key;

	public MetalChestFixer(Class<?> entityClass) {
		if (Entity.class.isAssignableFrom(entityClass)) {
			this.key = EntityList.getKey((Class<Entity>) entityClass);
		} else if (TileEntity.class.isAssignableFrom(entityClass)) {
			this.key = TileEntity.getKey((Class<TileEntity>) entityClass);
		} else {
			this.key = null;
		}
	}

	@Override
	public NBTTagCompound process(IDataFixer fixer, NBTTagCompound tag, int version) {
		if ((new ResourceLocation(tag.getString("id"))).equals(this.key)) {
			tag = this.filteredProcess(fixer, tag, version);
		}

		return tag;
	}

	public NBTTagCompound filteredProcess(IDataFixer fixer, NBTTagCompound tag, int version) {
		tag = DataFixesManager.processInventory(fixer, tag, version, "Items");

		String id = tag.getString("id");

		tag.setString("id", IDS.getOrDefault(id, new ResourceLocation(id)).toString());

		if (tag.hasKey("Luminous")) {
			tag.removeTag("Luminous");
		}

		return tag;
	}
}
