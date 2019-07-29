package t145.metalchests.data;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;
import t145.metalchests.api.chests.IMetalChest;
import t145.metalchests.api.config.ConfigMC;
import t145.metalchests.api.consts.ChestType;
import t145.metalchests.api.consts.RegistryMC;

public class MetalChestFixer implements IFixableData {

	private static final Map<String, String> IDS = new HashMap<>();

	static {
		IDS.put("metalchests:hungry_metal_chest", RegistryMC.RESOURCE_METAL_HUNGRY_CHEST.toString());
		IDS.put("metalchests:sorting_metal_chest", RegistryMC.RESOURCE_METAL_SORTING_CHEST.toString());
		IDS.put("metalchests:sorting_hungry_metal_chest", RegistryMC.RESOURCE_METAL_SORTING_HUNGRY_CHEST.toString());
	}

	private final int version;

	public MetalChestFixer(final int version) {
		this.version = version;
	}

	@Override
	public int getFixVersion() {
		if (ConfigMC.forceDataFixing) {
			return version + 1;
		}

		return version;
	}

	@Override
	public NBTTagCompound fixTagCompound(NBTTagCompound tag) {
		String id = tag.getString("id");
		RegistryMC.LOG.info("WE BE RUNNING!");

		tag.setString("id", IDS.getOrDefault(id, id));

		if (tag.hasKey("Luminous")) {
			tag.removeTag("Luminous");
		}

		if (tag.hasKey(IMetalChest.TAG_INVENTORY) && tag.hasKey(IMetalChest.TAG_CHEST_TYPE)) {
			ChestType type = ChestType.valueOf(tag.getString(IMetalChest.TAG_CHEST_TYPE));
			tag.getCompoundTag(IMetalChest.TAG_INVENTORY).setInteger("Size", type.getInventorySize());
		}

		return tag;
	}
}
