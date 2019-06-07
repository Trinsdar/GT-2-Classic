package gtmodern.tile;

import java.util.ArrayList;
import java.util.List;

import gtmodern.itemblock.GTItemBlockBattery;
import ic2.api.item.ElectricItem;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.util.obj.IItemContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GTTileBlockCustom extends TileEntityBlock implements IItemContainer {

	/*
	 * This class is extremely simple and designed todo one thing only. Store the
	 * integer and itemstack info from a GT ItemBlock instance whenever the block is
	 * placed.
	 */
	private ItemStack drop = ItemStack.EMPTY;

	public GTTileBlockCustom() {
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setTag("drop", drop.writeToNBT(new NBTTagCompound()));
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.drop = new ItemStack(nbt.getCompoundTag("drop"));
	}

	@Override
	public void onLoaded() {
		updateActive();
		super.onLoaded();
	}

	public ItemStack getItem() {
		return this.drop;
	}

	public void setItem(ItemStack item) {
		this.drop = item.copy();
	}

	public void updateActive() {
		if (this.drop.getItem() instanceof GTItemBlockBattery && ElectricItem.manager.getCharge(this.drop) > 1000) {
			this.setActive(true);
		} else {
			this.setActive(false);
		}
	}

	@Override
	public List<ItemStack> getDrops() {
		ArrayList<ItemStack> drops = new ArrayList<>();
		Item item = drop.getItem();
		drops.add(new ItemStack(item, 1).copy());
		return drops;
	}
}
