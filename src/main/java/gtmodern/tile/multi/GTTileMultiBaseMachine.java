package gtmodern.tile.multi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import gtmodern.tile.GTTileBaseMachine;
import gtmodern.util.int3;
import gtmodern.util.energy.EnergyConsumer;
import gtmodern.util.energy.MultiBlockHelper;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.energy.tile.IMetaDelegate;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;

public abstract class GTTileMultiBaseMachine extends GTTileBaseMachine implements IMetaDelegate {

	public boolean lastState;
	public boolean firstCheck = true;
	List<IEnergyTile> lastPositions = null;

	public GTTileMultiBaseMachine(int slots, int upgrades, int defaultinput, int maxinput) {
		super(slots, upgrades, defaultinput, 100, maxinput);
	}

	@Override
	public boolean canWork() {
		boolean superCall = super.canWork();
		if (superCall) {
			if (world.getTotalWorldTime() % 256 == 0 || firstCheck) {
				boolean lastCheck = lastState;
				lastState = checkStructure();
				firstCheck = false;
				if (lastCheck != lastState) {
					if (addedToEnergyNet) {
						MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
					}
					lastPositions = null;
					MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
					addedToEnergyNet = true;
					MultiBlockHelper.INSTANCE.removeCore(getWorld(), getPos());
					if (lastState) {
						MultiBlockHelper.INSTANCE.addCore(getWorld(), getPos(), new ArrayList<BlockPos>(provideStructure().keySet()));
					}
				}
			}
			superCall = superCall && lastState;
		}
		return superCall;
	}

	// Needs to Inlcude current Offset
	public Map<BlockPos, IBlockState> provideStructure() {
		return new Object2ObjectLinkedOpenHashMap<BlockPos, IBlockState>();
	}

	public boolean checkStructure() {
		if (!world.isAreaLoaded(pos, 3)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean hasGui(EntityPlayer player) {
		return checkStructure();
	}

	@Override
	public TileEntity getImportTile() {
		int3 dir = new int3(getPos(), getFacing());
		return world.getTileEntity(dir.left(2).asBlockPos());
	}

	@Override
	public TileEntity getExportTile() {
		int3 dir = new int3(getPos(), getFacing());
		return world.getTileEntity(dir.right(2).asBlockPos());
	}

	@Override
	public List<IEnergyTile> getSubTiles() {
		if (lastPositions == null) {
			lastPositions = new ArrayList<IEnergyTile>();
			lastPositions.add(this);
			if (checkStructure()) {
				for (Entry<BlockPos, IBlockState> entry : provideStructure().entrySet()) {
					if (entry.getKey().equals(getPos()) || world.getBlockState(entry.getKey()) != entry.getValue()) {
						continue;
					}
					lastPositions.add(new EnergyConsumer(getWorld(), entry.getKey(), this));
				}
			}
		}
		return lastPositions;
	}

	@Override
	public void onUnloaded() {
		MultiBlockHelper.INSTANCE.removeCore(getWorld(), getPos());
		super.onUnloaded();
	}
}
