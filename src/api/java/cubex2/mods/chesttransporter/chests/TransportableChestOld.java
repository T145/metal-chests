package cubex2.mods.chesttransporter.chests;

import net.minecraft.block.Block;

public class TransportableChestOld extends TransportableChestImpl
{
    protected final int transporterDV;

    public TransportableChestOld(Block chestBlock, int chestMeta, int transporterDV, String name)
    {
        super(chestBlock, chestMeta, name);
        this.transporterDV = transporterDV;
    }

    public int getTransporterDV()
    {
        return transporterDV;
    }
}
