package T145.metalchests.entities;

import T145.metalchests.api.IInventoryHandler;
import T145.metalchests.api.ModSupport;
import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.blocks.BlockMetalChest.ChestType;
import T145.metalchests.containers.ContainerMetalChest;
import T145.metalchests.core.MetalChests;
import T145.metalchests.core.ModLoader;
import T145.metalchests.items.ItemChestUpgrade;
import T145.metalchests.items.ItemChestUpgrade.ChestUpgrade;
import T145.metalchests.lib.DataSerializers;
import T145.metalchests.tiles.TileMetalChest;
import mods.railcraft.api.carts.IItemCart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.minecart.MinecartInteractEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class EntityMinecartMetalChest extends EntityMinecartChest implements IInventoryHandler, IItemCart {

	private static final DataParameter<ChestType> CHEST_TYPE = EntityDataManager.<ChestType>createKey(EntityMinecartMetalChest.class, DataSerializers.CHEST_TYPE);

	public EntityMinecartMetalChest(World world) {
		super(world);
	}

	public EntityMinecartMetalChest(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public ChestType getChestType() {
		return dataManager.get(CHEST_TYPE);
	}

	public void setChestType(ChestType type) {
		dataManager.set(CHEST_TYPE, type);
		this.minecartContainerItems = NonNullList.<ItemStack>withSize(type.getInventorySize(), ItemStack.EMPTY);
	}

	public TileMetalChest getMetalChest() {
		return new TileMetalChest(getChestType());
	}

	public void setInventory(NonNullList<ItemStack> stacks) {
		for (int slot = 0; slot < stacks.size(); ++slot) {
			setInventorySlotContents(slot, stacks.get(slot));
		}
	}

	public static void registerFixes(DataFixer fixer) {
		EntityMinecartContainer.addDataFixers(fixer, EntityMinecartMetalChest.class);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(CHEST_TYPE, ChestType.IRON);
	}

	@Override
	public ItemStack getCartItem() {
		return new ItemStack(ModLoader.MINECART_METAL_CHEST, 1, getChestType().ordinal());
	}

	@Override
	public void killMinecart(DamageSource source) {
		this.setDead();

		if (this.world.getGameRules().getBoolean("doEntityDrops")) {
			ItemStack stack = new ItemStack(Items.MINECART, 1);

			if (this.hasCustomName()) {
				stack.setStackDisplayName(this.getCustomNameTag());
			}

			this.entityDropItem(stack, 0.0F);
			InventoryHelper.dropInventoryItems(this.world, this, this);
			entityDropItem(new ItemStack(ModLoader.METAL_CHEST, 1, getChestType().ordinal()), 0.0F);
		}
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (MinecraftForge.EVENT_BUS.post(new MinecartInteractEvent(this, player, hand)) || world.isRemote) {
			return true;
		}

		if (player.isSneaking()) {
			ItemStack stack = player.getHeldItem(hand);

			if (stack.getItem() instanceof ItemChestUpgrade) {
				ChestUpgrade upgrade = ChestUpgrade.byMetadata(stack.getItemDamage());

				// we may not even need to make a new cart entity, just change its data
				if (getChestType() == upgrade.getBase()) {
					EntityMinecartMetalChest cart = new EntityMinecartMetalChest(world, posX, posY, posZ);
					cart.setChestType(upgrade.getUpgrade());
					cart.setInventory(minecartContainerItems);

					setDropItemsWhenDead(false);
					setDead();

					world.spawnEntity(cart);

					if (!player.capabilities.isCreativeMode) {
						stack.shrink(1);
					}

					world.playSound(player, getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.4F, 0.8F);
				}
			}
		} else {
			player.openGui(MetalChests.MOD_ID, hashCode(), world, 0, 0, 0);
		}

		return true;
	}

	@Override
	public int getSizeInventory() {
		return getChestType().getInventorySize();
	}

	@Override
	public IBlockState getDefaultDisplayTile() {
		return ModLoader.METAL_CHEST.getDefaultState().withProperty(BlockMetalChest.VARIANT, getChestType());
	}

	@Override
	public String getName() {
		if (hasCustomName()) {
			return getCustomNameTag();
		} else {
			return I18n.translateToLocal("item.metalchests:minecart_metal_chest." + getChestType().getName() + ".name");
		}
	}

	@Override
	public String getGuiID() {
		return getChestType().getGuiId();
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer player) {
		this.addLoot(player);
		return new ContainerMetalChest(this, player, getChestType());
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setString("Type", getChestType().toString());
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		setChestType(ChestType.valueOf(tag.getString("Type")));
	}

	@Override
	public IItemHandler getInventory() {
		return getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
	}

	@Optional.Method(modid = ModSupport.Railcraft.MOD_ID)
	@Override
	public boolean canPassItemRequests() {
		return true;
	}

	@Optional.Method(modid = ModSupport.Railcraft.MOD_ID)
	@Override
	public boolean canAcceptPushedItem(EntityMinecart requester, ItemStack stack) {
		//return !ItemHandlerHelper.insertItemStacked(inventory, stack, true).isEmpty();
		return true;
	}

	@Optional.Method(modid = ModSupport.Railcraft.MOD_ID)
	@Override
	public boolean canProvidePulledItem(EntityMinecart requester, ItemStack stack) {
		/*ItemStack result = ItemStack.EMPTY;

		for (int i = 0; i < inventory.getSlots(); ++i) {
			ItemStack extractedStack = inventory.extractItem(i, stack.getCount(), true);

			if (ItemStack.areItemStacksEqual(extractedStack, stack)) {
				result = extractedStack;
			}
		}

		return !result.isEmpty();*/
		return true;
	}
}
