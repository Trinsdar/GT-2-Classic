package gtmodern.util;

import gtmodern.GTItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTCreativeTab extends CreativeTabs {

	public GTCreativeTab(String label) {
		super(label);
		setBackgroundImageName("item_search.png");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getTabIconItem() {
		return new ItemStack(GTItems.debugScanner);
	}

	@Override
	public boolean hasSearchBar() {
		return true;
	}
}
