package gtmodern.tool;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import gtmodern.GTMod;
import gtmodern.color.GTColorItemInterface;
import gtmodern.material.GTMaterial;
import ic2.core.item.tool.ItemToolWrench;
import ic2.core.platform.textures.Ic2Icons;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTToolWrench extends ItemToolWrench implements GTColorItemInterface {

	GTMaterial material;

	public GTToolWrench(ToolMaterial tmat) {
		this.maxStackSize = 1;
		this.material = GTToolMaterial.getGTMaterial(tmat);
		this.setMaxDamage(this.material.getDurability() + 32);
		setRegistryName(this.material.getName() + "_wrench");
		setUnlocalizedName(GTMod.MODID + "." + this.material.getName() + "_wrench");
		setCreativeTab(GTMod.creativeTabGT);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return GTMaterial.isRadioactive(material) ? true : super.hasEffect(stack);
	}

	@Override
	public boolean hasContainerItem(ItemStack itemStack) {
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack) {
		ItemStack copy = itemStack.copy();
		return copy.attemptDamageItem(1, itemRand, null) ? ItemStack.EMPTY : copy;
	}

	@Override
	public List<Integer> getValidVariants() {
		return Arrays.asList(0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getTexture(int i) {
		return Ic2Icons.getTextures(GTMod.MODID + "_materials")[20];
	}

	@Override
	public Color getColor(ItemStack stack, int index) {
		return this.material.getColor();
	}

	public GTMaterial getMaterial() {
		return this.material;
	}

	@Override
	public boolean isWrench(ItemStack var1) {
		return true;
	}
}
