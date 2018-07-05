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

import javax.annotation.Nullable;

import T145.metalchests.blocks.BlockMetalTank.TankType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileMetalTank extends TileMod implements ITickable {

	private static final class InternalTank extends FluidTank {

		public InternalTank(int capacity) {
			super(capacity);
		}

		public InternalTank(@Nullable FluidStack fluidStack, int capacity) {
			super(fluidStack, capacity);
		}

		public InternalTank(Fluid fluid, int amount, int capacity) {
			super(fluid, amount, capacity);
		}

		@Override
		public void onContentsChanged() {
			if (tile instanceof TileMetalTank) {
				World world = tile.getWorld();
				BlockPos pos = tile.getPos();
				IBlockState state = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state, state, 8);
				tile.markDirty();
			}
		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			World world = tile.getWorld();
			BlockPos pos = tile.getPos();
			TileEntity tileBelow = world.getTileEntity(pos.down());

			if (tileBelow instanceof TileMetalTank) {
				TileMetalTank tank = (TileMetalTank) tileBelow;
				int fillAmount = tank.getTank().fill(resource, doFill);
				return fillAmount != 0 ? fillAmount : super.fill(resource, doFill);
			}
			return super.fill(resource, doFill);
		}
	}

	private TankType type;
	private InternalTank tank;

	public TileMetalTank(TankType type) {
		this.type = type;
		this.tank = new InternalTank(type.getCapacity());
		this.tank.setTileEntity(this);
	}

	public TileMetalTank() {
		this(TankType.BASE);
	}

	public TankType getType() {
		return type;
	}

	public InternalTank getTank() {
		return tank;
	}

	public boolean hasFluid() {
		return tank.getFluid() != null && tank.getFluidAmount() > 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		type = TankType.valueOf(tag.getString("Type"));
		tank = new InternalTank(FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("FluidTag")), type.getCapacity());
		tank.setTileEntity(this);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag = super.writeToNBT(tag);
		tag.setString("Type", type.toString());
		NBTTagCompound fluidTag = new NBTTagCompound();
		tank.getFluid().writeToNBT(fluidTag);
		fluidTag.setTag("FluidTag", fluidTag);
		return tag;
	}

	@Override
	public boolean hasCapability (Capability<?> capability, EnumFacing facing) {
		return capability.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability (Capability<T> capability, EnumFacing facing) {
		if (capability.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) {
			return (T) tank;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void update() {
		if (hasFluid()) {
			TileEntity tileBelow = world.getTileEntity(pos.down());

			if (tileBelow instanceof TileMetalTank) {
				TileMetalTank tankBelow = (TileMetalTank) tileBelow;

				if (type == tankBelow.getType()) {
					IFluidHandler handler = tileBelow.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH);
					tank.drain(handler.fill(tank.drain(tank.getCapacity(), false), true), true);
				}
			}
		}
	}
}
