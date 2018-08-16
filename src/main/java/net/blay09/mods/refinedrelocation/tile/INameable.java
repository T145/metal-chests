package net.blay09.mods.refinedrelocation.tile;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public interface INameable {
	void setCustomName(String displayName);
	String getCustomName();
	boolean hasCustomName();
	default ITextComponent getDisplayName() {
		return hasCustomName() ? new TextComponentString(getCustomName()) : new TextComponentTranslation(getUnlocalizedName());
	}
	String getUnlocalizedName();
}
