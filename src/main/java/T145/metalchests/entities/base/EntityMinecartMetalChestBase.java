package T145.metalchests.entities.base;

import T145.metalchests.MetalChests;
import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.core.ModLoader;
import T145.metalchests.entities.EntityMinecartCopperChest;
import T145.metalchests.entities.EntityMinecartCrystalChest;
import T145.metalchests.entities.EntityMinecartDiamondChest;
import T145.metalchests.entities.EntityMinecartGoldChest;
import T145.metalchests.entities.EntityMinecartIronChest;
import T145.metalchests.entities.EntityMinecartObsidianChest;
import T145.metalchests.entities.EntityMinecartSilverChest;
import T145.metalchests.lib.MetalChestType;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.minecart.MinecartInteractEvent;

public abstract class EntityMinecartMetalChestBase extends EntityMinecartChest {

	private NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
	private TileMetalChest chestInstance = new TileMetalChest(getChestType());

	public EntityMinecartMetalChestBase(World world) {
		super(world);
	}

	public EntityMinecartMetalChestBase(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public static EntityMinecartMetalChestBase create(World world, double x, double y, double z, MetalChestType type) {
		switch (type) {
		case COPPER:
			return new EntityMinecartCopperChest(world, x, y, z);
		case CRYSTAL:
			return new EntityMinecartCrystalChest(world, x, y, z);
		case DIAMOND:
			return new EntityMinecartDiamondChest(world, x, y, z);
		case GOLD:
			return new EntityMinecartGoldChest(world, x, y, z);
		case IRON:
			return new EntityMinecartIronChest(world, x, y, z);
		case OBSIDIAN:
			return new EntityMinecartObsidianChest(world, x, y, z);
		case SILVER:
			return new EntityMinecartSilverChest(world, x, y, z);
		default:
			return null;
		}
	}

	public abstract MetalChestType getChestType();

	public TileMetalChest getChestInstance() {
		chestInstance.setInventory(inventory);
		chestInstance.sortTopStacks();
		return chestInstance;
	}

	/*
	 * @Override public ItemStack getCartItem() { return null; }
	 */

	@Override
	public void killMinecart(DamageSource source) {
		this.setDead();

		if (this.world.getGameRules().getBoolean("doEntityDrops")) {
			ItemStack itemstack = new ItemStack(Items.MINECART, 1);

			if (this.hasCustomName()) {
				itemstack.setStackDisplayName(this.getCustomNameTag());
			}

			this.entityDropItem(itemstack, 0.0F);
			InventoryHelper.dropInventoryItems(this.world, this, this);
			this.entityDropItem(new ItemStack(ModLoader.METAL_CHEST, 1, getChestType().ordinal()), 0.0F);
		}
	}

	@Override
	public void setDead() {
		isDead = true;
	}

	@Override
	public int getSizeInventory() {
		return getChestType().getInventorySize();
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (MinecraftForge.EVENT_BUS.post(new MinecartInteractEvent(this, player, hand))) {
			return true;
		}

		if (!this.world.isRemote) {
			player.openGui(MetalChests.MODID, hashCode(), world, 0, 0, 0);
		}

		return true;
	}

	@Override
	public IBlockState getDefaultDisplayTile() {
		return ModLoader.METAL_CHEST.getDefaultState().withProperty(BlockMetalChest.VARIANT, getChestType());
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		this.addLoot(null);
		return inventory.get(slot);
	}

	@Override
	public ItemStack decrStackSize(int slot, int size) {
		this.addLoot(null);
		return ItemStackHelper.getAndSplit(this.inventory, slot, size);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		this.addLoot(null);
		ItemStack itemstack = this.inventory.get(index);

		if (itemstack.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			this.inventory.set(index, ItemStack.EMPTY);
			return itemstack;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.addLoot(null);
		this.inventory.set(index, stack);

		if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		if (this.hasDisplayTile()) {
			compound.setBoolean("CustomDisplayTile", true);
			IBlockState iblockstate = this.getDisplayTile();
			ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(iblockstate.getBlock());
			compound.setString("DisplayTile", resourcelocation == null ? "" : resourcelocation.toString());
			compound.setInteger("DisplayData", iblockstate.getBlock().getMetaFromState(iblockstate));
			compound.setInteger("DisplayOffset", this.getDisplayTileOffset());
		}

		if (this.getLootTable() != null) {
			compound.setString("LootTable", this.getLootTable().toString());

			// need accesstransformer for the seed (if we ever want to add in dungeon gen)
		} else {
			ItemStackHelper.saveAllItems(compound, this.inventory);
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		if (compound.getBoolean("CustomDisplayTile")) {
			Block block;

			if (compound.hasKey("DisplayTile", 8)) {
				block = Block.getBlockFromName(compound.getString("DisplayTile"));
			} else {
				block = Block.getBlockById(compound.getInteger("DisplayTile"));
			}

			int i = compound.getInteger("DisplayData");
			this.setDisplayTile(block == null ? Blocks.AIR.getDefaultState() : block.getStateFromMeta(i));
			this.setDisplayTileOffset(compound.getInteger("DisplayOffset"));
		}

		this.inventory = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

		if (compound.hasKey("LootTable", 8)) {
			this.setLootTable(new ResourceLocation(compound.getString("LootTable")), compound.getLong("LootTableSeed"));
		} else {
			ItemStackHelper.loadAllItems(compound, this.inventory);
		}
	}
}