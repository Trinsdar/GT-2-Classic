package gtmodern.color;

import java.awt.Color;

import gtmodern.itemblock.GTItemBlockRare;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class GTColorItemBlock extends GTItemBlockRare implements GTColorItemInterface {

	Block block;

	public GTColorItemBlock(Block block) {
		super(block);
		this.block = block;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Color getColor(ItemStack stack, int index) {
		if (this.block instanceof GTColorBlockInterface) {
			return ((GTColorBlockInterface) block).getColor(this.block, index);
		} else {
			return null;
		}
	}
}
