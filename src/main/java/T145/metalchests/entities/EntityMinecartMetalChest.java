package T145.metalchests.entities;

import javax.annotation.Nullable;

import T145.metalchests.MetalChests;
import T145.metalchests.blocks.BlockMetalChest;
import T145.metalchests.core.ModLoader;
import T145.metalchests.lib.MetalChestType;
import T145.metalchests.lib.SimpleItemStackHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class EntityMinecartMetalChest extends EntityMinecart {

	private static MetalChestType type;
	private static SimpleItemStackHandler inventory;

	public EntityMinecartMetalChest(World world) {
		super(setChestData(MetalChestType.IRON, world));
	}

	public EntityMinecartMetalChest(World world, double x, double y, double z) {
		super(setChestData(MetalChestType.IRON, world), x, y, z);
	}

	public EntityMinecartMetalChest(MetalChestType type, World world) {
		super(setChestData(type, world));
	}

	public EntityMinecartMetalChest(MetalChestType type, World world, double x, double y, double z) {
		super(setChestData(type, world), x, y, z);
	}

	private static World setChestData(MetalChestType chestType, World world) {
		type = chestType;
		inventory = new SimpleItemStackHandler(chestType.getInventorySize());
		return world;
	}

	public MetalChestType getChestType() {
		return type;
	}

	public SimpleItemStackHandler getInventory() {
		return inventory;
	}

	@Override
	public void killMinecart(DamageSource source) {
		super.killMinecart(source);

		if (world.getGameRules().getBoolean("doEntityDrops")) {
			inventory.dropItems(world, getPosition());
			entityDropItem(new ItemStack(ModLoader.METAL_CHEST, 1, type.ordinal()), 0.0F);
		}
	}

	@Override
	public EntityMinecart.Type getType() {
		return EntityMinecart.Type.CHEST;
	}

	@Override
	public IBlockState getDefaultDisplayTile() {
		return ModLoader.METAL_CHEST.getDefaultState().withProperty(BlockMetalChest.VARIANT, type);
	}

	@Override
	public int getDefaultDisplayTileOffset() {
		return 8;
	}

	public boolean isUsableByPlayer(EntityPlayer player) {
		if (isDead) {
			return false;
		} else {
			return player.getDistanceSq(this) <= 64.0D;
		}
	}

	public static void addDataFixers(DataFixer fixer) {
		EntityMinecart.registerFixesMinecart(fixer, EntityMinecartMetalChest.class);
		fixer.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(EntityMinecartMetalChest.class, new String[] { "Items" }));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setTag("Inventory", inventory.serializeNBT());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		inventory.deserializeNBT(tag.getCompoundTag("Inventory"));
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (super.processInitialInteract(player, hand)) {
			return true;
		}

		if (!world.isRemote) {
			player.openGui(MetalChests.MODID, hashCode(), world, 0, 0, 0);
		}

		return true;
	}

	@Override
	public void applyDrag() {
		float f = 0.98F;

		int i = 15 - inventory.calcRedstone();
		f += (float) i * 0.001F;

		motionX *= (double) f;
		motionY *= 0.0D;
		motionZ *= (double) f;
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
}