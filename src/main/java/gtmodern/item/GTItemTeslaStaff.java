package gtmodern.item;

import java.util.Arrays;
import java.util.List;

import gtmodern.GTMod;
import ic2.api.item.ElectricItem;
import ic2.core.item.base.ItemElectricTool;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.obj.IStaticTexturedItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTItemTeslaStaff extends ItemElectricTool implements IStaticTexturedItem {

	public GTItemTeslaStaff() {
		super(0.0F, 2.0F, ToolMaterial.IRON);
		this.attackDamage = 1.0F;
		this.maxCharge = 10000000;
		this.transferLimit = 2048;
		this.operationEnergyCost = 2000000;
		this.tier = 4;
		this.setRegistryName("tesla_staff");
		this.setUnlocalizedName(GTMod.MODID + ".tesla_staff");
		this.setCreativeTab(GTMod.creativeTabGT);
	}

	@Override
	public List<Integer> getValidVariants() {
		return Arrays.asList(0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getTexture(int meta) {
		return Ic2Icons.getTextures(GTMod.MODID + "_items")[4];
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.DARK_AQUA + I18n.format(this.getUnlocalizedName().replace("item", "tooltip")));
	}

	@Override
	public EnumEnchantmentType getType(ItemStack item) {
		return EnumEnchantmentType.WEAPON;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (!(attacker instanceof EntityPlayer)) {
			return true;
		} else {
			if (ElectricItem.manager.use(stack, this.operationEnergyCost, attacker)) {
				target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), 1000.0F);
				attacker.world.addWeatherEffect(new EntityLightningBolt(attacker.world, target.lastTickPosX, target.lastTickPosY, target.lastTickPosZ, false));
			} else {
				target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), 1.0F);
			}
			return false;
		}
	}
}
