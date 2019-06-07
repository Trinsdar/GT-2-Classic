package gtmodern.util;

import gtmodern.tile.GTTileBookshelf;
import ic2.core.inventory.filters.IFilter;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GTBookshelfFilter implements IFilter {

	GTTileBookshelf bookshelf;

	public GTBookshelfFilter(GTTileBookshelf tile) {
		this.bookshelf = tile;
	}

	public boolean matches(ItemStack stack) {
		return stack.getItem() == Items.BOOK || stack.hasEffect();
	}
}
