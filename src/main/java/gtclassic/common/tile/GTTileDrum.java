package gtclassic.common.tile;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.interfaces.IGTDebuggableTile;
import gtclassic.api.interfaces.IGTItemContainerTile;
import gtclassic.api.interfaces.IGTRecolorableStorageTile;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.common.GTBlocks;
import ic2.api.classic.network.adv.NetworkField;
import ic2.core.IC2;
import ic2.core.block.base.tile.TileEntityMachine;
import ic2.core.block.base.util.info.misc.IWrench;
import ic2.core.fluid.IC2Tank;
import ic2.core.platform.registry.Ic2Sounds;
import ic2.core.util.misc.StackUtil;
import ic2.core.util.obj.IClickable;
import ic2.core.util.obj.IItemContainer;
import ic2.core.util.obj.ITankListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;

public class GTTileDrum extends TileEntityMachine implements ITankListener, IItemContainer, IClickable,
		IGTDebuggableTile, ITickable, IGTRecolorableStorageTile, IGTItemContainerTile {

	private IC2Tank tank;
	private boolean flow = false;
	@NetworkField(index = 9)
	public int color;
	public static final String NBT_COLOR = "color";
	public static final String NBT_TANK = "tank";
	public static final String NBT_FLOW = "flow";

	public GTTileDrum() {
		super(0);
		this.color = 16383998;
		this.tank = new IC2Tank(32000);
		this.tank.addListener(this);
		this.addNetworkFields(new String[] { NBT_COLOR });
	}

	@Override
	public boolean canSetFacing(EntityPlayer player, EnumFacing facing) {
		return false;
	}

	public void onTankChanged(IFluidTank tank) {
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.tank.readFromNBT(nbt.getCompoundTag(NBT_TANK));
		if (nbt.hasKey(NBT_COLOR)) {
			this.color = nbt.getInteger(NBT_COLOR);
		} else {
			this.color = 16383998;
		}
		this.flow = nbt.getBoolean(NBT_FLOW);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger(NBT_COLOR, this.color);
		this.tank.writeToNBT(this.getTag(nbt, NBT_TANK));
		nbt.setBoolean(NBT_FLOW, this.flow);
		return nbt;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? true
				: super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
				? CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.tank)
				: super.getCapability(capability, facing);
	}

	public void setFlow(boolean canFlow) {
		this.flow = canFlow;
	}

	@Override
	public boolean canRemoveBlock(EntityPlayer player) {
		if (player.isSneaking() && player.getHeldItemMainhand().getItem() instanceof IWrench) {
			this.flow = !this.flow;
			if (this.isSimulating()) {
				String msg = this.flow ? "Will fill adjacent tanks" : "Wont fill adjacent tanks";
				IC2.platform.messagePlayer(player, msg);
				IC2.audioManager.playOnce(player, Ic2Sounds.wrenchUse);
			}
			return false;
		}
		return true;
	}

	public IC2Tank getTankInstance() {
		return this.tank;
	}

	@Override
	public boolean hasLeftClick() {
		return false;
	}

	@Override
	public boolean hasRightClick() {
		return true;
	}

	@Override
	public void onLeftClick(EntityPlayer var1, Side var2) {
	}

	@Override
	public boolean onRightClick(EntityPlayer player, EnumHand hand, EnumFacing enumFacing, Side side) {
		return GTHelperFluid.doClickableFluidContainerThings(player, hand, world, pos, this.tank);
	}

	@Override
	public void update() {
		if (this.flow && world.getTotalWorldTime() % 10 == 0 && this.tank.getFluid() != null) {
			EnumFacing side = updateSideForOutput();
			IFluidHandler fluidTile = FluidUtil.getFluidHandler(world, this.getPos().offset(side), side.getOpposite());
			if (fluidTile != null && FluidUtil.tryFluidTransfer(fluidTile, this.tank, 500, true) != null) {
				// empty if transfered method
			}
		}
	}
	
	private EnumFacing updateSideForOutput() {
		if (this.tank.getFluid() != null && this.tank.getFluid().getFluid().isGaseous()) {
			return EnumFacing.UP;
		}
		return EnumFacing.DOWN;
	}

	@Override
	public void getData(Map<String, Boolean> data) {
		FluidStack fluid = this.tank.getFluid();
		boolean fluidNotNull = fluid != null;
		if (fluidNotNull) {
			String type = fluid.getFluid().isGaseous() ? "Is gaseous will flow upward" : "Is fluid will flow downward";
			data.put(fluid.amount + "mB of " + fluid.getLocalizedName(), false);
			data.put(type, false);
		} else {
			data.put("Empty", false);
		}
		String msg = this.flow ? "Will fill adjacent tanks" : "Wont fill adjacent tanks";
		data.put(msg, false);
	}

	@Override
	public void onNetworkUpdate(String field) {
		if (field.equals(NBT_COLOR)) {
			this.world.markBlockRangeForRenderUpdate(this.getPos(), this.getPos());
		}
		super.onNetworkUpdate(field);
	}

	@Override
	public void setTileColor(int color) {
		this.color = color;
	}

	@Override
	public Color getTileColor() {
		return new Color(this.color);
	}

	@Override
	public boolean isColored() {
		return this.color != 16383998;
	}

	public List<ItemStack> getDrops() {
		List<ItemStack> list = new ArrayList<>();
		ItemStack stack = GTMaterialGen.get(GTBlocks.tileDrum);
		NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
		boolean data = false;
		if (this.tank.getFluid() != null) {
			nbt.setTag("Fluid", this.tank.getFluid().writeToNBT(new NBTTagCompound()));
			data = true;
		}
		if (isColored()) {
			nbt.setInteger(NBT_COLOR, this.color);
			data = true;
		}
		if (this.flow) {
			nbt.setBoolean(NBT_FLOW, this.flow);
			data = true;
		}
		if (!data) {
			stack.setTagCompound(null);
		}
		list.add(stack);
		return list;
	}

	@Override
	public List<ItemStack> getInventoryDrops() {
		return getDrops();
	}
}