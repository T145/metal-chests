package cubex2.mods.chesttransporter.api;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class TransportableChest extends IForgeRegistryEntry.Impl<TransportableChest>
{
    /**
     * Gets whether the whole tile entity should be copied instead of just the chest contents. Useful when the chest has
     * another functionality besides being a chest.
     */
    public boolean copyTileEntity()
    {
        return false;
    }

    public abstract boolean canGrabChest(World world, BlockPos pos, IBlockState state, EntityPlayer player, ItemStack transporter);

    public abstract boolean canPlaceChest(World world, BlockPos pos, EntityPlayer player, ItemStack transporter);

    /**
     * Create the stack that is being used to place the chest.
     */
    public abstract ItemStack createChestStack(ItemStack transporter);

    /**
     * This is called before the chest is being removed from the world. Use this to get more data from the chest if needed.
     */
    public void preRemoveChest(World world, BlockPos pos, EntityPlayer player, ItemStack transporter)
    {
        // do nothing
    }

    /**
     * This is called after the chest has been placed.
     */
    public void onChestPlaced(World world, BlockPos pos, EntityPlayer player, ItemStack transporter)
    {
        // do nothing
    }

    /**
     * This is called before the tile entity is being created, if using the copyTileEntity functionality. Return the
     * NBTTagCompound that is being used to create the tile entity.
     *
     * @param nbt The NBT copied from the chest's tile entity.
     */
    public NBTTagCompound modifyTileCompound(NBTTagCompound nbt, World world, BlockPos pos, EntityPlayer player, ItemStack transporter)
    {
        return nbt;
    }

    /**
     * Get all model locations that are being used in {@link #getChestModel(ItemStack)}
     */
    public abstract Collection<ResourceLocation> getChestModels();

    /**
     * Get the model that is being used when the transporter grabbed a chest. This is the model that is being added to the
     * handle.
     */
    public abstract ResourceLocation getChestModel(ItemStack stack);

    public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag advanced)
    {
        // do nothing
    }
}
