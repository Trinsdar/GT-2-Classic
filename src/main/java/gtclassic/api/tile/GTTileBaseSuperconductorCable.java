package gtclassic.api.tile;

import gtclassic.api.interfaces.IGTDebuggableTile;
import gtclassic.api.interfaces.IGTItemContainerTile;
import gtclassic.api.interfaces.IGTRecolorableStorageTile;
import gtclassic.api.material.GTMaterialGen;
import ic2.api.classic.energy.tile.IAnchorConductor;
import ic2.api.classic.energy.tile.IEnergyConductorColored;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.energy.tile.IMetaDelegate;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.IC2;
import ic2.core.RotationList;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.block.wiring.BlockCable;
import ic2.core.block.wiring.tile.TileEntityCable;
import ic2.core.energy.EnergyNetLocal;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.util.misc.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.MinecraftForge;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class GTTileBaseSuperconductorCable extends TileEntityBlock implements IEnergyConductorColored, IGTRecolorableStorageTile, INetworkTileEntityEventListener, IAnchorConductor, IGTItemContainerTile, IGTDebuggableTile {

	@NetworkField(index = 8)
	public RotationList connection;
	@NetworkField(
			index = 9
	)
	public RotationList anchors;
	protected boolean addedToEnergyNet;
	@NetworkField(index = 10)
	public int color;
	private int prevColor = 0;
	private static final String NBT_COLOR = "color";
	int size;

	public GTTileBaseSuperconductorCable(int size) {
		this.color = 16383998;
		this.connection = RotationList.EMPTY;
		this.anchors = RotationList.EMPTY;
		this.size = size;
		this.addNetworkFields("connection", "anchor", NBT_COLOR);
	}

	@Override
	public void onLoaded() {
		super.onLoaded();
		updateConnections();
		if (!this.addedToEnergyNet && this.isSimulating()) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			this.addedToEnergyNet = true;
		}
	}

	@Override
	public void onUnloaded() {
		if (this.addedToEnergyNet && this.isSimulating()) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			this.addedToEnergyNet = false;
		}
		super.onUnloaded();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey(NBT_COLOR)) {
			this.color = nbt.getInteger(NBT_COLOR);
		} else {
			this.color = 16383998;
		}
		this.anchors = RotationList.ofNumber(nbt.getByte("Anchors"));
		this.isActive = nbt.getBoolean("active");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger(NBT_COLOR, this.color);
		nbt.setBoolean("active", isActive);
		nbt.setByte("Anchors", (byte)this.anchors.getCode());
		return nbt;
	}

	@Override
	public void onNetworkUpdate(String field) {
		super.onNetworkUpdate(field);
		if (orString(field, "anchors", "color", "connection")) {
			this.prevColor = this.color;
			this.world.markBlockRangeForRenderUpdate(this.getPos(), this.getPos());
		}
	}

	public boolean orString(String compare, String... strings){
		for (String string : strings){
			if (compare.equals(string)){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
		return this.connection.contains(side);
	}

	@Override
	public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
		return this.connection.contains(side);
	}

	@Override
	public double getConductionLoss() {
		return 0.001D;
	}

	@Override
	public void removeConductor() {
		boolean burn = EnergyNetLocal.burn && this.world.rand.nextFloat() < EnergyNetLocal.chance;
		this.world.setBlockToAir(this.getPos());
		this.getNetwork().initiateTileEntityEvent(this, 0, true);
		if (burn) {
			this.world.setBlockState(this.getPos(), Blocks.FIRE.getDefaultState());
		}
	}

	@Override
	public void removeInsulation() {
		// empty method since ic2c cables never break down insulation from too much energy.
	}

	public void updateConnections() {
		for (EnumFacing facing : EnumFacing.VALUES) {
			BlockPos sidedPos = pos.offset(facing);
			if (world.isBlockLoaded(sidedPos)) {
				world.neighborChanged(sidedPos, Blocks.AIR, pos);
			}
		}
		if (world.isBlockLoaded(pos)) {
			world.neighborChanged(pos, Blocks.AIR, pos);
		}
	}

	public Vec3i getConnections() {
		return new Vec3i(this.connection.getCode(), this.anchors.getCode(), this.connection.getCode() << 6 | this.anchors.getCode());
	}

	@Override
	public boolean canUpdate() {
		return this.isSimulating();
	}

	@Override
	public void onBlockUpdate(Block block) {
		super.onBlockUpdate(block);
		if (!this.isRendering()) {
			RotationList newList = RotationList.EMPTY;
			EnumFacing[] var3 = EnumFacing.VALUES;
			int var4 = var3.length;
			for (int var5 = 0; var5 < var4; ++var5) {
				EnumFacing dir = var3[var5];
				IEnergyTile tile = EnergyNet.instance.getSubTile(this.getWorld(), this.getPos().offset(dir));
				if (!(tile instanceof IEnergyAcceptor) && !(tile instanceof IEnergyEmitter)) {
					tile = EnergyNet.instance.getTile(this.getWorld(), this.getPos().offset(dir));
				}
				if (tile != null && this.canConnect(tile, dir)) {
					newList = newList.add(dir);
				}
			}
			if (this.connection.getCode() != newList.getCode()) {
				this.connection = newList;
				this.getNetwork().updateTileEntityField(this, "connection");
			}
		}
	}

	public boolean canConnect(IEnergyTile tile, EnumFacing side) {
		if (this.anchors.contains(side)) {
			return false;
		} else if (tile instanceof TileEntityCable) {
			return canInteractWithIc2Cable((TileEntityCable) tile, side.getOpposite());
		} else if (tile instanceof GTTileBaseSuperconductorCable) {
			return canInteractWithCable((GTTileBaseSuperconductorCable) tile, side.getOpposite());
		} else if (tile instanceof IAnchorConductor && ((IAnchorConductor) tile).hasAnchor(side.getOpposite())) {
			return false;
		} else if (tile instanceof IEnergyConductorColored) {
			return this.canInteractWithAPICable((IEnergyConductorColored)tile);
		} else if (tile instanceof IEnergyAcceptor && !(tile instanceof IEnergyEmitter)) {
			return ((IEnergyAcceptor) tile).acceptsEnergyFrom(this, side.getOpposite());
		} else if (tile instanceof IEnergyEmitter && !(tile instanceof IEnergyAcceptor)) {
			return ((IEnergyEmitter) tile).emitsEnergyTo(this, side.getOpposite());
		} else if (tile instanceof IEnergyAcceptor && tile instanceof IEnergyEmitter) {
			return ((IEnergyEmitter) tile).emitsEnergyTo(this, side.getOpposite())
					|| ((IEnergyAcceptor) tile).acceptsEnergyFrom(this, side.getOpposite());
		} else {
			return tile instanceof IMetaDelegate || tile instanceof INetworkTileEntityEventListener;
		}
	}

	public boolean canInteractWithIc2Cable(TileEntityCable cable, EnumFacing side) {
		if (cable.hasAnchor(side)) {
			return false;
		} else {
			return this.getConductorColor() == WireColor.Blank || cable.getConductorColor() == WireColor.Blank || this.getConductorColor() == cable.getConductorColor();
		}
	}

	public boolean canInteractWithCable(GTTileBaseSuperconductorCable cable, EnumFacing side) {
		if (cable.hasAnchor(side)) {
			return false;
		} else {
			return this.color == cable.color || cable.getConductorColor() == WireColor.Blank || this.getConductorColor() == WireColor.Blank;
		}
	}

	public boolean canInteractWithAPICable(IEnergyConductorColored cable) {
		return this.getConductorColor() == WireColor.Blank || cable.getConductorColor() == WireColor.Blank || this.getConductorColor() == cable.getConductorColor();
	}

	public abstract Block getBlockDrop();

	@Override
	public List<ItemStack> getDrops() {
		List<ItemStack> list = new ArrayList<>();

		ItemStack block = GTMaterialGen.get(this.getBlockDrop());
		if (this.isColored()) {
			NBTTagCompound nbt = StackUtil.getOrCreateNbtData(block);
			nbt.setInteger("color", this.color);
		}
		list.add(block);
		list.addAll(getInventoryDrops());
		return list;
	}

	@Override
	public List<ItemStack> getInventoryDrops() {
		List<ItemStack> list = new ArrayList<>();
		for(int i = 0; i < 6; ++i) {
			if (this.anchors.contains(EnumFacing.getFront(i))) {
				list.add(Ic2Items.miningPipe.copy());
			}
		}
		return list;
	}

	@Override
	public boolean hasSpecialAction(EntityPlayer player, EnumFacing facing, Vec3d hit) {
		EnumFacing side = (new BlockCable.ClickHelper(hit, (float) (this.size / 16.0D))).getFacing(facing);
		return side != null && this.anchors.contains(side);
	}

	@Override
	public EnumActionResult doSpecialAction(EntityPlayer player, EnumFacing facing, Vec3d hit) {
		EnumFacing side = (new BlockCable.ClickHelper(hit, (float) (this.size / 16.0D))).getFacing(facing);
		if (this.isRendering()) {
			return EnumActionResult.PASS;
		} else if (side != null && this.removeAnchor(side)) {
			ItemStack pipe = Ic2Items.miningPipe.copy();
			if (!player.inventory.addItemStackToInventory(pipe)) {
				player.dropItem(pipe, true);
			}

			return EnumActionResult.SUCCESS;
		} else {
			return super.doSpecialAction(player, facing, hit);
		}
	}

	@Override
	public void setTileColor(int color) {
		if (this.addedToEnergyNet) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
		}

		this.addedToEnergyNet = false;
		this.color = color;
		if (this.isColored() && !isActive){
			this.setActive(true);
		} else if (!this.isColored() && isActive){
			this.setActive(false);
		}
		MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
		this.addedToEnergyNet = true;
		if (color != this.prevColor) {
			this.getNetwork().updateTileEntityField(this, NBT_COLOR);
		}
		this.prevColor = color;

	}

	@Override
	public Color getTileColor() {
		return new Color(this.color);
	}

	@Override
	public boolean isColored() {
		return this.color != 16383998;
	}

	@Override
	public boolean addAnchor(EnumFacing enumFacing) {
		if (this.anchors.contains(enumFacing)) {
			return false;
		} else if (this.isRendering()) {
			return true;
		} else {
			if (this.addedToEnergyNet) {
				MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			}

			this.addedToEnergyNet = false;
			this.anchors = this.anchors.add(enumFacing);
			this.getNetwork().updateTileEntityField(this, "anchors");
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			this.addedToEnergyNet = true;
			return true;
		}
	}

	@Override
	public boolean removeAnchor(EnumFacing enumFacing) {
		if (this.anchors.notContains(enumFacing)) {
			return false;
		} else {
			if (this.addedToEnergyNet) {
				MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			}

			this.addedToEnergyNet = false;
			this.anchors = this.anchors.remove(enumFacing);
			this.getNetwork().updateTileEntityField(this, "anchors");
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			this.addedToEnergyNet = true;
			return true;
		}
	}

	@Override
	public boolean hasAnchor(EnumFacing enumFacing) {
		return this.anchors.contains(enumFacing);
	}

	@Override
	public WireColor getConductorColor() {
		return color == 16383998 ? WireColor.Blank : WireColor.fromColor(getColorFromColorValue());
	}

	public EnumDyeColor getColorFromColorValue(){
		switch (color){
			case 1908001 : return EnumDyeColor.BLACK;
			case 11546150 : return EnumDyeColor.RED;
			case 6192150 : return EnumDyeColor.GREEN;
			case 8606770 : return EnumDyeColor.BROWN;
			case 3949738 : return EnumDyeColor.BLUE;
			case 8991416 : return EnumDyeColor.PURPLE;
			case 1481884 : return EnumDyeColor.CYAN;
			case 10329495 : return EnumDyeColor.SILVER;
			case 4673362 : return EnumDyeColor.GRAY;
			case 15961002 : return EnumDyeColor.PINK;
			case 8439583 : return EnumDyeColor.LIME;
			case 16701501 : return EnumDyeColor.YELLOW;
			case 3847130 : return EnumDyeColor.LIGHT_BLUE;
			case 13061821 : return EnumDyeColor.MAGENTA;
			case 16351261 : return EnumDyeColor.ORANGE;
			default: return EnumDyeColor.WHITE;
		}
	}

	@Override
	public void onNetworkEvent(int event) {
		switch(event) {
			case 0:
				this.world.playSound(null, this.getPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.8F);

				for(int l = 0; l < 8; ++l) {
					this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)this.getPos().getX() + Math.random(), (double)this.getPos().getY() + 1.2D, (double)this.getPos().getZ() + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
				}

				return;
			default:
				IC2.platform.displayError("An unknown event type was received over multiplayer.\nThis could happen due to corrupted data or a bug.\n\n(Technical information: event ID " + event + ", tile entity below)\nT: " + this + " (" + this.getPos() + ")");
		}
	}

	@Override
	public void getData(Map<String, Boolean> data) {
		for (EnumFacing facing : anchors){
			data.put("Anchor on side" + facing.getName(), true);
		}
	}
}
