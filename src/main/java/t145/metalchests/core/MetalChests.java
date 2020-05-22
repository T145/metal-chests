package t145.metalchests.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

@Mod(modid = MetalChests.MOD_ID, name = MetalChests.MOD_NAME, version = MetalChests.VERSION, updateJSON = MetalChests.UPDATE_JSON)
@EventBusSubscriber
public class MetalChests {

	public static final String MOD_ID = "metal-chests";
	public static final String MOD_NAME = "Metal Chests";
	public static final String VERSION = "@VERSION@";
	public static final String UPDATE_JSON = "https://raw.githubusercontent.com/T145/metal-chests/master/update.json";
	public static final Logger LOG = LogManager.getLogger(MOD_NAME);
	private static final Style GREEN_STYLE = new Style().setColor(TextFormatting.GREEN);
	private static final Style GOLD_STYLE = new Style().setColor(TextFormatting.GOLD);

	public MetalChests() {
	}

	public static ResourceLocation getResource(final String path) {
		return new ResourceLocation(MOD_ID, path);
	}

	static ForgeVersion.CheckResult getUpdateResult() {
		return ForgeVersion.getResult(Loader.instance().activeModContainer());
	}

	static String getLatestVersion() {
		return getUpdateResult().target.toString();
	}

	static boolean hasUpdate() {
		switch (getUpdateResult().status) {
		case AHEAD: case BETA:
			LOG.warn("WARNING: This is an experimental build! Please report any problems!");
			return false;
		case BETA_OUTDATED: case OUTDATED:
			return true;
		case FAILED:
			LOG.warn("Update check failed! If the game isn't offline, the issue should be reported.");
			return false;
		case PENDING:
			LOG.warn("Cannot check for updates! Found status PENDING!");
			return false;
		default:
			return false;
		}
	}

	static ITextComponent getUpdateNotification() {
		ITextComponent prefix = new TextComponentTranslation(MOD_NAME).setStyle(GREEN_STYLE);
		ITextComponent base = new TextComponentTranslation(String.format("%s.client.update", MOD_ID)).setStyle(GOLD_STYLE);
		ITextComponent postfix = new TextComponentString(String.format("%s%s%s!", TextFormatting.AQUA, getLatestVersion(), TextFormatting.GOLD));
		return prefix.appendSibling(base).appendSibling(postfix);
	}

	@SubscribeEvent
	public static void metalchests$playerLogin(final PlayerLoggedInEvent event) {
		if (hasUpdate()) {
			event.player.sendMessage(getUpdateNotification());
		}
	}
}
