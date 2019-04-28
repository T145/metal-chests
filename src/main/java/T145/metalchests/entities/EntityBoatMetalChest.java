package T145.metalchests.entities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import T145.metalchests.api.ItemsMC;
import T145.metalchests.api.chests.IMetalChest;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.ChestUpgrade;
import T145.metalchests.api.immutable.RegistryMC;
import T145.metalchests.core.MetalChests;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class EntityBoatMetalChest extends EntityBoat implements IMetalChest {

	private static final DataParameter<ChestType> CHEST_TYPE = EntityDataManager.<ChestType>createKey(EntityBoatMetalChest.class, MetalChests.CHEST_TYPE);
	private final ItemStackHandler inventory = new ItemStackHandler(getChestType().getInventorySize());

	public EntityBoatMetalChest(World world) {
		super(world);
	}

	public EntityBoatMetalChest(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityBoatMetalChest(EntityBoat boat) {
		this(boat.getEntityWorld(), boat.prevPosX, boat.prevPosY, boat.prevPosZ);
		this.posX = boat.posX;
		this.posY = boat.posY;
		this.posZ = boat.posZ;
		this.motionX = boat.motionX;
		this.motionY = boat.motionY;
		this.motionZ = boat.motionZ;
		this.rotationPitch = boat.rotationPitch;
		this.rotationYaw = boat.rotationYaw;
		this.setBoatType(boat.getBoatType());
	}

	public ChestType getChestType() {
		return dataManager.get(CHEST_TYPE);
	}

	public void setChestType(ChestType type) {
		dataManager.set(CHEST_TYPE, type);
	}

	@Override
	public void setInventory(IItemHandler stacks) {
		for (int slot = 0; slot < stacks.getSlots(); ++slot) {
			if (slot < getChestType().getInventorySize()) {
				inventory.setStackInSlot(slot, stacks.getStackInSlot(slot));
			}
		}
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(CHEST_TYPE, ChestType.OBSIDIAN);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setString("Type", getChestType().toString());
		tag.setTag("Inventory", inventory.serializeNBT());
		tag.setInteger("BoatType", this.getBoatType().ordinal());
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		setChestType(ChestType.valueOf(tag.getString("Type")));
		inventory.deserializeNBT(tag.getCompoundTag("Inventory"));
		setBoatType(Type.byId(tag.getInteger("BoatType")));
	}

	@Override
	public IItemHandler getInventory() {
		return inventory;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return !this.isDead && player.getDistanceSq(this) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	protected boolean canFitPassenger(@Nonnull Entity passenger) {
		return false;
	}

	@Nullable
	public Entity getControllingPassenger() {
		return null;
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (world.isRemote) {
			return true;
		}

		if (player.isSneaking()) {
			ItemStack stack = player.getHeldItem(hand);

			if (isUpgradeApplicable(stack.getItem())) {
				ChestUpgrade upgrade = ChestUpgrade.byMetadata(stack.getItemDamage());

				if (getChestType() == upgrade.getBase()) {
					setChestType(upgrade.getUpgrade());
					setInventory(inventory);

					if (!player.capabilities.isCreativeMode) {
						stack.shrink(1);
					}

					player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.4F, 0.8F);
				}
			}
		} else {
			player.openGui(RegistryMC.MOD_ID, hashCode(), world, 0, 0, 0);
		}

		return true;
	}

	@Override
	public EnumFacing getFront() {
		return EnumFacing.SOUTH;
	}

	@Override
	public void setFront(EnumFacing front) {
		// maybe allow changing the front to north
	}

	@Override
	public boolean isUpgradeApplicable(Item upgrade) {
		return upgrade.getRegistryName().equals(ItemsMC.CHEST_UPGRADE.getRegistryName());
	}
}
