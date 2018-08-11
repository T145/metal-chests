package T145.metalchests.entities;

import javax.annotation.Nullable;

import T145.metalchests.api.ModSupport;
import T145.metalchests.api.containers.IContainer;
import T145.metalchests.api.containers.IInventoryHandler;
import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.blocks.BlockMetalChest.ChestType;
import T145.metalchests.client.gui.GuiMetalChest;
import T145.metalchests.containers.ContainerMetalChest;
import T145.metalchests.core.MetalChests;
import T145.metalchests.core.ModLoader;
import T145.metalchests.items.ItemChestUpgrade;
import T145.metalchests.items.ItemChestUpgrade.ChestUpgrade;
import mods.railcraft.api.carts.IItemCart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

@Optional.Interface(modid = ModSupport.Railcraft.MOD_ID, iface = ModSupport.Railcraft.ITEM_CART, striprefs = true)
public class EntityMetalMinecart extends EntityMinecart implements IInventoryHandler, IContainer, IItemCart {

	private static final DataParameter<ChestType> CHEST_TYPE = EntityDataManager.<ChestType>createKey(EntityMinecart.class, MetalChests.proxy.CHEST_TYPE);

	private ItemStackHandler inventory = new ItemStackHandler();

	public EntityMetalMinecart(World world) {
		super(world);
	}

	public EntityMetalMinecart(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public ChestType getChestType() {
		return dataManager.get(CHEST_TYPE);
	}

	public void setChestType(ChestType type) {
		this.dataManager.set(CHEST_TYPE, type);
	}

	public void setInventory(IItemHandler stacks) {
		inventory = new ItemStackHandler(getChestType().getInventorySize());

		for (int slot = 0; slot < stacks.getSlots(); ++slot) {
			if (slot < getChestType().getInventorySize()) {
				inventory.setStackInSlot(slot, stacks.getStackInSlot(slot));
			}
		}
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(CHEST_TYPE, ChestType.IRON);
		inventory = new ItemStackHandler(getChestType().getInventorySize()); // just in case
	}

	/*@Override
	public ItemStack getCartItem() {
		return new ItemStack(ModLoader.MINECART_METAL_CHEST, 1, getChestType().ordinal());
	}*/

	@Override
	public void killMinecart(DamageSource source) {
		super.killMinecart(source);

		if (world.getGameRules().getBoolean("doEntityDrops")) {
			for (int i = 0; i < inventory.getSlots(); ++i) {
				ItemStack stack = inventory.getStackInSlot(i);

				if (!stack.isEmpty()) {
					InventoryHelper.spawnItemStack(world, posX, posY, posZ, stack);
				}
			}

			entityDropItem(new ItemStack(ModLoader.METAL_CHEST, 1, getChestType().ordinal()), 0.0F);
		}
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (super.processInitialInteract(player, hand) || world.isRemote) {
			return true;
		}

		if (player.isSneaking()) {
			ItemStack stack = player.getHeldItem(hand);

			if (stack.getItem() instanceof ItemChestUpgrade) {
				ItemChestUpgrade upgradeItem = (ItemChestUpgrade) stack.getItem();
				ChestUpgrade upgrade = ChestUpgrade.byMetadata(stack.getItemDamage());

				if (getChestType() == upgrade.getBase()) {
					EntityMetalMinecart newCart = new EntityMetalMinecart(world, posX, posY, posZ);
					newCart.setChestType(upgrade.getUpgrade());
					newCart.setInventory(inventory);

					setDropItemsWhenDead(false);
					setDead();

					world.spawnEntity(newCart);

					if (!player.capabilities.isCreativeMode) {
						stack.shrink(1);
					}

					world.playSound(player, getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.4F, 0.8F);
				}
			}
		} else {
			player.openGui(MetalChests.MOD_ID, 2, world, 0, 0, 0);
		}

		return true;
	}


	@Override
	public EntityMinecart.Type getType() {
		return EntityMinecart.Type.CHEST;
	}

	@Override
	public IBlockState getDefaultDisplayTile() {
		return ModLoader.METAL_CHEST.getDefaultState().withProperty(BlockMetalChest.VARIANT, getChestType());
	}

	@Override
	public int getDefaultDisplayTileOffset() {
		return 8;
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setTag("Inventory", inventory.serializeNBT());
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		inventory.deserializeNBT(tag.getCompoundTag("Inventory"));
	}

	@Override
	protected void applyDrag() {
		float f = 0.98F;

		int i = 15 - ItemHandlerHelper.calcRedstoneFromInventory(inventory);
		f += (float) i * 0.001F;

		motionX *= f;
		motionY *= 0.0D;
		motionZ *= f;
	}

	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) inventory;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public IItemHandler getInventory() {
		return inventory;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (isDead) {
			return false;
		} else {
			return player.getDistanceSq(this) <= 64.0D;
		}
	}

	@Override
	public ContainerMetalChest getContainer(EntityPlayer player) {
		return new ContainerMetalChest(this, player, getChestType());
	}

	@Override
	public GuiContainer getGui(EntityPlayer player) {
		return new GuiMetalChest(getContainer(player));
	}

	@Optional.Method(modid = ModSupport.Railcraft.MOD_ID)
	@Override
	public boolean canPassItemRequests() {
		return true;
	}

	@Optional.Method(modid = ModSupport.Railcraft.MOD_ID)
	@Override
	public boolean canAcceptPushedItem(EntityMinecart requester, ItemStack stack) {
		return !ItemHandlerHelper.insertItemStacked(inventory, stack, true).isEmpty();
	}

	@Optional.Method(modid = ModSupport.Railcraft.MOD_ID)
	@Override
	public boolean canProvidePulledItem(EntityMinecart requester, ItemStack stack) {
		ItemStack result = ItemStack.EMPTY;

		for (int i = 0; i < inventory.getSlots(); ++i) {
			ItemStack extractedStack = inventory.extractItem(i, stack.getCount(), true);

			if (ItemStack.areItemStacksEqual(extractedStack, stack)) {
				result = extractedStack;
			}
		}

		return !result.isEmpty();
	}
}
