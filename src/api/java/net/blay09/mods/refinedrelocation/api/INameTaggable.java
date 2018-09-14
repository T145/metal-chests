package net.blay09.mods.refinedrelocation.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public interface INameTaggable extends INBTSerializable<NBTTagCompound> {
    void setCustomName(String displayName);

    String getCustomName();

    boolean hasCustomName();

    @Nullable
    default ITextComponent getDisplayName() {
        return hasCustomName() ? new TextComponentString(getCustomName()) : null;
    }

    @Override
    default NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("CustomName", getCustomName());
        return compound;
    }

    @Override
    default void deserializeNBT(NBTTagCompound compound) {
        setCustomName(compound.getString("CustomName"));
    }
}
