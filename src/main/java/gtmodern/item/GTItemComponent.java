package gtmodern.item;

import java.util.Arrays;
import java.util.List;

import gtmodern.GTMod;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.obj.IStaticTexturedItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTItemComponent extends Item implements IStaticTexturedItem {

	String name;
	int x;
	int y;
	boolean containeritem;

	public GTItemComponent(String name, int x, int y, boolean containeritem) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.containeritem = containeritem;
		setRegistryName(this.name.toLowerCase());
		setUnlocalizedName(GTMod.MODID + "." + this.name.toLowerCase());
		setCreativeTab(GTMod.creativeTabGT);
	}

	@Override
	public boolean hasContainerItem(ItemStack itemStack) {
		return this.containeritem;
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack) {
		return itemStack.copy();
	}

	@Override
	public List<Integer> getValidVariants() {
		return Arrays.asList(0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getTexture(int i) {
		return Ic2Icons.getTextures(GTMod.MODID + "_items")[(this.y * 16) + this.x];
	}
}
