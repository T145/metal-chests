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
package T145.metalchests.api.chests;

import T145.metalchests.api.immutable.ChestType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;

public interface IUpgradeableChest {

	ChestType getChestType();

	void setChestType(ChestType chestType);

	IBlockState createBlockState();

	TileEntity createTileEntity();
}
