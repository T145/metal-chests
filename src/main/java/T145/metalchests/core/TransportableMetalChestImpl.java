package T145.metalchests.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.RegistryMC;
import cubex2.mods.chesttransporter.chests.TransportableChestImpl;
import joptsimple.internal.Strings;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

class TransportableMetalChestImpl extends TransportableChestImpl {

	private final String prefix;

	public TransportableMetalChestImpl(Block chestBlock, String prefix, String name) {
		super(chestBlock, -1, name);
		this.prefix = prefix;
	}

	public TransportableMetalChestImpl(Block chestBlock, String name) {
		this(chestBlock, Strings.EMPTY, name);
	}

	@Override
	public boolean copyTileEntity() {
		return true;
	}

	@Override
	public ItemStack createChestStack(ItemStack transporter) {
		ItemStack stack = super.createChestStack(transporter);
		NBTTagCompound tag = transporter.getTagCompound().getCompoundTag("ChestTile");
		String chestType = tag.getString("ChestType");
		stack.setItemDamage(ChestType.valueOf(chestType).ordinal());
		return stack;
	}

	@Override
	public ResourceLocation getChestModel(ItemStack transporter) {
		NBTTagCompound tag = transporter.getTagCompound().getCompoundTag("ChestTile");
		String chestType = tag.getString("ChestType");
		return new ResourceLocation(RegistryMC.MOD_ID, "item/chesttransporter/" + prefix + chestType.toLowerCase());
	}

	@Override
	public Collection<ResourceLocation> getChestModels() {
		List<ResourceLocation> models = new ArrayList<>();

		for (ChestType type : ChestType.values()) {
			models.add(new ResourceLocation(RegistryMC.MOD_ID, "item/chesttransporter/" + prefix + type.getName()));
		}

		return models;
	}

	@Override
	public NBTTagCompound modifyTileCompound(NBTTagCompound tag, World world, BlockPos pos, EntityPlayer player, ItemStack transporter) {
		tag.setString("Front", player.getHorizontalFacing().getOpposite().toString());
		return super.modifyTileCompound(tag, world, pos, player, transporter);
	}
}