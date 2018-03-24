package T145.metalchests.tiles;

import javax.annotation.Nonnull;

import T145.metalchests.lib.ProjectTableType;
import T145.metalchests.tiles.base.TileBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileProjectTable extends TileBase {

	private final ProjectTableType type;
	private final ItemStackHandler externalInventory;
	private final ItemStackHandler internalInventory;
	private EnumFacing front;

	public TileProjectTable(ProjectTableType type) {
		this.type = type;
		this.externalInventory = new ItemStackHandler(9);
		this.internalInventory = new ItemStackHandler(type.getInventorySize());
		this.setFront(EnumFacing.NORTH);
	}

	public TileProjectTable() {
		this(ProjectTableType.WOOD);
	}

	public ProjectTableType getType() {
		return type;
	}

	public ItemStackHandler getUpperInventory() {
		return externalInventory;
	}

	public ItemStackHandler getLowerInventory() {
		return internalInventory;
	}

	public void setFront(EnumFacing front) {
		this.front = front;
	}

	public EnumFacing getFront() {
		return front;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (side == EnumFacing.UP) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(externalInventory);
			} else {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(internalInventory);
			}
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void readPacketNBT(NBTTagCompound tag) {
		this.setFront(EnumFacing.byName(tag.getString("Front")));
		externalInventory.deserializeNBT(tag.getCompoundTag("ExternalInventory"));
		externalInventory.deserializeNBT(tag.getCompoundTag("InternalInventory"));
	}

	@Override
	public void writePacketNBT(NBTTagCompound tag) {
		tag.setString("Front", front.toString());
		tag.setTag("ExternalInventory", externalInventory.serializeNBT());
		tag.setTag("InternalInventory", externalInventory.serializeNBT());
	}
}