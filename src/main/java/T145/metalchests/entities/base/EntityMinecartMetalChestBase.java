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
import T145.metalchests.items.ItemChestStructureUpgrade;
import T145.metalchests.lib.MetalChestType;
import T145.metalchests.lib.MetalChestType.StructureUpgrade;
import T145.metalchests.tiles.TileMetalChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.minecart.MinecartInteractEvent;

public abstract class EntityMinecartMetalChestBase extends EntityMinecartChest {

	private TileMetalChest chestInstance = new TileMetalChest(getChestType());

	public EntityMinecartMetalChestBase(World world) {
		super(world);
		minecartContainerItems = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
	}

	public EntityMinecartMetalChestBase(World world, double x, double y, double z) {
		super(world, x, y, z);
		minecartContainerItems = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
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
		case OBSIDIAN:
			return new EntityMinecartObsidianChest(world, x, y, z);
		case SILVER:
			return new EntityMinecartSilverChest(world, x, y, z);
		default:
			return new EntityMinecartIronChest(world, x, y, z);
		}
	}

	public abstract MetalChestType getChestType();

	public TileMetalChest getChestInstance() {
		return chestInstance;
	}

	public void updateChestInstance() {
		chestInstance.setInventory(minecartContainerItems);
		chestInstance.sortTopStacks();
	}

	public static void registerFixesMinecartChest(DataFixer fixer) {
		EntityMinecartContainer.addDataFixers(fixer, EntityMinecartMetalChestBase.class);
	}

	@Override
	public ItemStack getCartItem() {
		return new ItemStack(ModLoader.MINECART_METAL_CHEST, 1, getChestType().ordinal());
	}

	@Override
	public void killMinecart(DamageSource source) {
		setDead();

		if (world.getGameRules().getBoolean("doEntityDrops")) {
			ItemStack itemstack = new ItemStack(Items.MINECART, 1);

			if (hasCustomName()) {
				itemstack.setStackDisplayName(getCustomNameTag());
			}

			entityDropItem(itemstack, 0.0F);
			InventoryHelper.dropInventoryItems(world, this, this);
			entityDropItem(new ItemStack(ModLoader.METAL_CHEST, 1, getChestType().ordinal()), 0.0F);
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

		if (!world.isRemote) {
			ItemStack stack = player.getHeldItem(hand);

			if (player.isSneaking() && stack.getItem() instanceof ItemChestStructureUpgrade) {
				ItemChestStructureUpgrade upgradeItem = (ItemChestStructureUpgrade) stack.getItem();
				StructureUpgrade upgrade = StructureUpgrade.byMetadata(stack.getItemDamage());

				if (getChestType() == upgrade.getBase()) {
					EntityMinecartMetalChestBase newCart = create(world, posX, posY, posZ, upgrade.getUpgrade());

					for (int i = 0; i < getSizeInventory(); ++i) {
						newCart.setInventorySlotContents(i, getStackInSlot(i));
					}

					dropContentsWhenDead = false;
					setDead();

					world.spawnEntity(newCart);

					if (!player.capabilities.isCreativeMode) {
						stack.shrink(1);
					}

					player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.4F, 0.8F);
					return true;
				}
			}

			player.openGui(MetalChests.MODID, hashCode(), world, 0, 0, 0);
		}

		return true;
	}

	@Override
	public IBlockState getDefaultDisplayTile() {
		return ModLoader.METAL_CHEST.getDefaultState().withProperty(BlockMetalChest.VARIANT, getChestType());
	}
}