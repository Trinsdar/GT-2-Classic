package gtmodern.tool;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import gtmodern.GTConfig;
import gtmodern.GTMod;
import gtmodern.color.GTColorItemInterface;
import gtmodern.material.GTMaterial;
import gtmodern.material.GTMaterialFlag;
import gtmodern.material.GTMaterialGen;
import ic2.core.IC2;
import ic2.core.platform.registry.Ic2States;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.obj.ILayeredItemModel;
import ic2.core.platform.textures.obj.IStaticTexturedItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

public class GTToolFile extends Item implements IStaticTexturedItem, GTColorItemInterface, ILayeredItemModel {

	GTMaterial material;

	public GTToolFile(ToolMaterial tmat) {
		this.maxStackSize = 1;
		this.material = GTToolMaterial.getGTMaterial(tmat);
		this.setMaxDamage((this.material.getDurability() * 2) + 64);
		setRegistryName(this.material.getName() + "_file");
		setUnlocalizedName(GTMod.MODID + "." + this.material.getName() + "_file");
		setCreativeTab(GTMod.creativeTabGT);
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
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return GTMaterial.isRadioactive(material) ? true : super.hasEffect(stack);
	}

	@Override
	public List<Integer> getValidVariants() {
		return Arrays.asList(0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getTexture(int meta) {
		return Ic2Icons.getTextures("gtmodern_items")[18];
	}

	@Override
	public Color getColor(ItemStack stack, int index) {
		if (index == 0) {
			return GTMaterial.Wood.getColor();
		} else {
			return this.material.getColor();
		}
	}

	@Override
	public boolean isLayered(ItemStack var1) {
		return true;
	}

	@Override
	public int getLayers(ItemStack var1) {
		return 2;
	}

	@Override
	public TextureAtlasSprite getTexture(int var1, ItemStack var2) {
		return Ic2Icons.getTextures(GTMod.MODID + "_materials")[18 + var1];
	}

	public GTMaterial getMaterial() {
		return this.material;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ) {
		IBlockState state = world.getBlockState(pos);
		if (!GTConfig.harderPlates && !GTConfig.harderRods && IC2.getRefinedIron().equals("ingotRefinedIron")
				&& state == Ic2States.machine) {
			world.setBlockState(pos, GTMaterialGen.getBlock(GTMaterial.RefinedIron, GTMaterialFlag.CASING).getDefaultState());
			ItemHandlerHelper.giveItemToPlayer(player, GTMaterialGen.getSmallDust(GTMaterial.Iron, 2));
			player.setHeldItem(hand, this.getContainerItem(player.getHeldItem(hand)));
			world.playSound((EntityPlayer) null, pos, SoundEvents.BLOCK_ANVIL_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
			return EnumActionResult.SUCCESS;
		}
		return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
	}
}
