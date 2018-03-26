package T145.metalchests.tiles;

import java.util.Iterator;

import javax.annotation.Nonnull;

import T145.metalchests.api.IInventoryHandler;
import T145.metalchests.containers.InventoryProjectTableCrafting;
import T145.metalchests.core.ModLoader;
import T145.metalchests.lib.ProjectTableType;
import T145.metalchests.tiles.base.TileBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileProjectTable extends TileBase implements IInventoryHandler {

	private final ProjectTableType type;
	private final ItemStackHandler craftMatrix;
	private final ItemStackHandler inventory;
	private final InventoryProjectTableCrafting crafter;
	private final InventoryCraftResult craftingResult;
	private EnumFacing front;

	public TileProjectTable(ProjectTableType type) {
		this.type = type;
		this.craftMatrix = new ItemStackHandler(9) {

			@Override
			protected void onContentsChanged(int slot) {
				TileProjectTable.this.updateRecipe();
			}
		};
		this.inventory = new ItemStackHandler(type.getInventorySize());
		this.crafter = new InventoryProjectTableCrafting(craftMatrix);
		this.craftingResult = new InventoryCraftResult();
		this.setFront(EnumFacing.NORTH);
	}

	public TileProjectTable() {
		this(ProjectTableType.WOOD);
	}

	public ProjectTableType getType() {
		return type;
	}

	public ItemStackHandler getCraftMatrix() {
		return craftMatrix;
	}

	public InventoryProjectTableCrafting getCrafter() {
		return crafter;
	}

	public InventoryCraftResult getCraftingResult() {
		return craftingResult;
	}

	public void setCraftingResult(ItemStack result) {
		craftingResult.setInventorySlotContents(0, result);
	}

	public void emptyResult() {
		setCraftingResult(ItemStack.EMPTY);
	}

	public EnumFacing getFront() {
		return front;
	}

	public void setFront(EnumFacing front) {
		this.front = front;
	}

	@Override
	public ItemStackHandler getInventory() {
		return craftMatrix;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (world.getTileEntity(pos) != this) {
			return false;
		} else {
			return player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (side == EnumFacing.UP) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(craftMatrix);
			} else {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
			}
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void readPacketNBT(NBTTagCompound tag) {
		setFront(EnumFacing.byName(tag.getString("Front")));
		craftMatrix.deserializeNBT(tag.getCompoundTag("CraftMatrix"));
		inventory.deserializeNBT(tag.getCompoundTag("Inventory"));
		setCraftingResult(new ItemStack(tag.getCompoundTag("Result")));
	}

	@Override
	public void writePacketNBT(NBTTagCompound tag) {
		tag.setString("Front", front.toString());
		tag.setTag("CraftMatrix", craftMatrix.serializeNBT());
		tag.setTag("Inventory", inventory.serializeNBT());
		tag.setTag("Result", craftingResult.getStackInSlot(0).writeToNBT(new NBTTagCompound()));
	}

	public void updateRecipe() {
		setCraftingResult(findCraftingResult());
		world.scheduleBlockUpdate(pos, getBlockType(), 0, 1);
		world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		markDirty();
	}

	public ItemStack findCraftingResult() {
		Iterator<IRecipe> recipeIterator = CraftingManager.REGISTRY.iterator();
		IRecipe recipe;

		do {
			if (!recipeIterator.hasNext()) {
				return ItemStack.EMPTY;
			}
			recipe = recipeIterator.next();
		} while (!recipe.matches(crafter, world));

		craftingResult.setRecipeUsed(recipe);
		return recipe.getCraftingResult(crafter);
	}

	public ItemStack getChestStack() {
		if (type == ProjectTableType.WOOD) {
			return new ItemStack(Blocks.CHEST);
		} else {
			return new ItemStack(ModLoader.METAL_CHEST, 1, type.ordinal() - 1);
		}
	}
}