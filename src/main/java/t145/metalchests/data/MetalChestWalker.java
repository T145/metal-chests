package t145.metalchests.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import t145.metalchests.api.chests.IMetalChest;
import t145.metalchests.tiles.TileMetalChest;

public class MetalChestWalker implements IDataWalker {

	private final Class<? extends TileMetalChest> chestClass;

	public MetalChestWalker(Class<? extends TileMetalChest> chestClass) {
		this.chestClass = TileMetalChest.class.isAssignableFrom(chestClass) ? chestClass : null;
	}

	@Override
	public NBTTagCompound process(IDataFixer fixer, NBTTagCompound tag, int version) {
		if (chestClass != null && tag.hasKey(IMetalChest.TAG_INVENTORY)) {
			NBTTagList itemTags = tag.getCompoundTag(IMetalChest.TAG_INVENTORY).getTagList("Items", 10);

			for (short i = 0; i < itemTags.tagCount(); ++i) {
				itemTags.set(i, fixer.process(FixTypes.ITEM_INSTANCE, itemTags.getCompoundTagAt(i), version));
			}
		}

		return tag;
	}
}
