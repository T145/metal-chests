/*******************************************************************************
 * Copyright 2018 T145
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package T145.metalchests.core;

import T145.metalchests.api.immutable.RegistryMC;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class UpdateChecker {

	private UpdateChecker() {}

	private static ForgeVersion.CheckResult getResult() {
		return ForgeVersion.getResult(FMLCommonHandler.instance().findContainerFor(RegistryMC.MOD_ID));
	}

	public static boolean hasUpdate() {
		ForgeVersion.CheckResult result = getResult();

		if (result.status == ForgeVersion.Status.PENDING) {
			MetalChests.LOG.warn("Cannot check for updates! Found status PENDING!");
			return false;
		}

		return result.status.isAnimated();
	}

	private static String getLatestVersion() {
		return getResult().target.toString();
	}

	public static ITextComponent getUpdateNotification() {
		ITextComponent prefix = new TextComponentTranslation("metalchests.client.update.prefix").setStyle(new Style().setColor(TextFormatting.GREEN));
		ITextComponent base = new TextComponentTranslation("metalchests.client.update").setStyle(new Style().setColor(TextFormatting.GOLD));
		ITextComponent postfix = new TextComponentString(TextFormatting.AQUA + getLatestVersion() + TextFormatting.GOLD + "!");
		return prefix.appendSibling(base).appendSibling(postfix);
	}
}
