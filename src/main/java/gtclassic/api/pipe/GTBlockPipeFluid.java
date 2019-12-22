package gtclassic.api.pipe;

import gtclassic.GTMod;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.pipe.GTHelperPipes.GTPipeFluidCapacity;
import gtclassic.api.pipe.GTHelperPipes.GTPipeModel;
import ic2.core.block.base.tile.TileEntityBlock;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class GTBlockPipeFluid extends GTBlockPipeBase {

	GTPipeModel type;
	GTPipeFluidCapacity size;

	public GTBlockPipeFluid(GTMaterial mat, GTPipeModel type, GTPipeFluidCapacity size) {
		super(mat, type.getSizes());
		this.type = type;
		this.size = size;
		setRegistryName(mat.getName() + "_fluidpipe" + type.getSuffix());
		setUnlocalizedName(GTMod.MODID + "." + mat.getName() + "fluidpipe" + type.getPrefix());
		setCreativeTab(GTMod.creativeTabGT);
	}

	@Override
	public String getLocalizedName() {
		return net.minecraft.util.text.translation.I18n.translateToLocalFormatted("part.fluid_pipe" + type.getSuffix(), net.minecraft.util.text.translation.I18n.translateToLocal("material." + mat.getDisplayName()).trim());
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.BLUE + I18n.format("Fluid Capacity:  " + this.size.getSize() + " mB/sec"));
	}

	@Override
	public TileEntityBlock createNewTileEntity(World worldIn, int meta) {
		switch (this.size) {
		case S800:
			return new GTTilePipeFluid.GTTileFluidPipe800();
		case S1600:
			return new GTTilePipeFluid.GTTileFluidPipe1600();
		case S3200:
			return new GTTilePipeFluid.GTTileFluidPipe3200();
		case S6400:
			return new GTTilePipeFluid.GTTileFluidPipe6400();
		case S12800:
			return new GTTilePipeFluid.GTTileFluidPipe12800();
		case SMAX1:
			return new GTTilePipeFluid.GTTileFluidPipeMax1();
		case SMAX2:
			return new GTTilePipeFluid.GTTileFluidPipeMax2();
		case SMAX3:
			return new GTTilePipeFluid.GTTileFluidPipeMax3();
		default:
			return new TileEntityBlock();
		}
	}

	public GTPipeModel getPipeType() {
		return this.type;
	}

	public GTPipeFluidCapacity getCapacity() {
		return this.size;
	}
}
