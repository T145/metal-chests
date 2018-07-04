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
package T145.metalchests.tiles;

import T145.metalchests.blocks.BlockMetalTank.TankType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileMetalTank extends TileMod implements IFluidHandler {

	private TankType type;

	public TileMetalTank(TankType type) {
		this.type = type;
	}

	public TileMetalTank() {
		this(TankType.BASE);
	}

	public TankType getType() {
		return type;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		type = TankType.valueOf(tag.getString("Type"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag = super.writeToNBT(tag);
		tag.setString("Type", type.toString());
		return tag;
	}

	@Override
	public IFluidTankProperties[] getTankProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}
}
