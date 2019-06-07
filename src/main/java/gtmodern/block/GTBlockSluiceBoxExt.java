package gtmodern.block;

import gtmodern.GTMod;
import gtmodern.models.GTModelSluiceBoxExt;
import gtmodern.util.GTValues;
import ic2.core.platform.textures.models.BaseModel;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTBlockSluiceBoxExt extends GTBlockFacing {

	static final AxisAlignedBB SLUICE_EXT = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.4D, 1.0D);

	public GTBlockSluiceBoxExt() {
		super(Material.WOOD);
		setRegistryName(GTValues.sluiceBoxExtension.getUnlocalized().replaceAll("tile.gtmodern.", ""));
		setUnlocalizedName(GTValues.sluiceBoxExtension);
		setCreativeTab(GTMod.creativeTabGT);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BaseModel getModelFromState(IBlockState state) {
		return GTModelSluiceBoxExt.getModel();
	}

	@Override
	public EnumFacing[] getAllowedRotations() {
		return EnumFacing.HORIZONTALS;
	}

	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return SLUICE_EXT;
	}
}
