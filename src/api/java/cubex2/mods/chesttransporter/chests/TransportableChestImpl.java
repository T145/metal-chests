package cubex2.mods.chesttransporter.chests;

import java.util.Collection;
import java.util.Collections;

import cubex2.mods.chesttransporter.api.TransportableChest;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TransportableChestImpl extends TransportableChest
{
    protected final Block chestBlock;
    protected final int chestMeta;
    protected final String name;

    public TransportableChestImpl(Block chestBlock, int chestMeta, String name)
    {
        this.chestBlock = chestBlock;
        this.chestMeta = chestMeta;
        this.name = name;
        setRegistryName(name);
    }

    @Override
    public boolean canGrabChest(World world, BlockPos pos, IBlockState state, EntityPlayer player, ItemStack transporter)
    {
        Block block = state.getBlock();
        int meta = block.getMetaFromState(state);

        return block == chestBlock && (chestMeta == -1 || chestMeta == meta);
    }

    @Override
    public boolean canPlaceChest(World world, BlockPos pos, EntityPlayer player, ItemStack transporter)
    {
        return true;
    }

    public Block getChestBlock()
    {
        return chestBlock;
    }

    public int getChestMetadata()
    {
        return chestMeta;
    }

    @Override
    public ItemStack createChestStack(ItemStack transporter)
    {
        if (getChestMetadata() == -1)
            return new ItemStack(getChestBlock());
        return new ItemStack(getChestBlock(), 1, getChestMetadata());
    }

    @Override
    public Collection<ResourceLocation> getChestModels()
    {
        return Collections.singleton(new ResourceLocation("chesttransporter:item/" + name));
    }

    @Override
    public ResourceLocation getChestModel(ItemStack stack)
    {
        return new ResourceLocation("chesttransporter:item/" + name);
    }

    protected static ResourceLocation locationFromName(String iconName)
    {
        return new ResourceLocation("chesttransporter:item/" + iconName);
    }


}
