package gtmodern.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gtmodern.tile.GTTileFacing;
import ic2.core.block.base.BlockCommonContainer;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.platform.textures.obj.ICustomModeledBlock;
import ic2.core.platform.textures.obj.IFacingBlock;
import ic2.core.util.helpers.BlockStateContainerIC2;
import ic2.core.util.obj.IBlockStateLoader;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class GTBlockFacing extends BlockCommonContainer
		implements IBlockStateLoader, ICustomModeledBlock, IFacingBlock {

	public static PropertyDirection FACINGS = PropertyDirection.create("facing", Arrays.asList(EnumFacing.VALUES));

	public GTBlockFacing(Material materialIn) {
		super(materialIn);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainerIC2(this, FACINGS);
	}

	@Override
	public EnumFacing getRotation(IBlockState state) {
		return state.getValue(FACINGS);
	}

	@Override
	public boolean hasRotation(IBlockState state) {
		return true;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACINGS).getIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACINGS, EnumFacing.getFront(meta & 7));
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileEntityBlock block = (TileEntityBlock) worldIn.getTileEntity(pos);
		if (block != null) {
			return state.withProperty(FACINGS, block.getFacing());
		}
		return state.withProperty(FACINGS, EnumFacing.NORTH);
	}

	@Override
	public IBlockState getStateFromStack(ItemStack stack) {
		return getStateFromMeta(stack.getMetadata());
	}

	@Override
	public IBlockState getDefaultBlockState() {
		return getDefaultState().withProperty(FACINGS, EnumFacing.NORTH);
	}

	@Override
	public List<IBlockState> getValidStates(IBlockState defaultState) {
		setDefaultState(defaultState);
		setDefaultState(getDefaultBlockState());
		return getValidModelStates();
	}

	public abstract EnumFacing[] getAllowedRotations();

	@Override
	public List<IBlockState> getValidModelStates() {
		List<IBlockState> states = new ArrayList<>();
		for (EnumFacing facing : EnumFacing.VALUES) {
			states.add(getDefaultState().withProperty(FACINGS, facing));
		}
		return states;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new GTTileFacing(getAllowedRotations());
	}
}
