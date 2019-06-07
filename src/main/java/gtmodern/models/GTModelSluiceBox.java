package gtmodern.models;

import java.util.List;

import javax.vecmath.Matrix4f;

import gtmodern.GTMod;
import gtmodern.block.GTBlockFacing;
import org.apache.commons.lang3.tuple.Pair;

import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.Ic2Icons.SpriteReloadEvent;
import ic2.core.platform.textures.Ic2Models;
import ic2.core.platform.textures.models.BaseModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(Side.CLIENT)
public class GTModelSluiceBox extends BaseModel {

	private static GTModelSluiceBox instance = null;

	public static GTModelSluiceBox getModel() {
		if (instance == null) {
			instance = new GTModelSluiceBox();
		}
		return instance;
	}

	@SubscribeEvent
	public static void resetModel(SpriteReloadEvent event) {
		instance = null;
	}

	private GTModelBaker baker;

	private GTModelSluiceBox() {
		super(Ic2Models.getBlockTransforms());
		setParticalTexture(Ic2Icons.getTextures(GTMod.MODID + "_sluicebox_particle")[0]);
	}

	@Override
	public void init() {
		baker = GTModelBaker.getBaker(Ic2Icons.getTextures(GTMod.MODID + "_sluicebox")[0]);
		// bottom main part
		baker.addModel(0, 40, 1, 0, 0, 14, 1, 16, true);
		// bottom sides
		baker.addModel(0, 18, 0, 0, 0, 1, 6, 16, true);
		baker.addModel(0, 18, 15, 0, 0, 1, 6, 16, true);
		// stripes
		baker.addModel(0, 59, 1, 1, 2, 14, 1, 1, true);
		baker.addModel(0, 59, 1, 1, 6, 14, 1, 1, true);
		// top main part
		baker.addModel(0, 0, 0, 6, 4, 16, 1, 12, true);
		// top long sides
		baker.addModel(12, 11, 1, 7, 4, 14, 6, 1, true);
		baker.addModel(12, 11, 1, 7, 15, 14, 6, 1, true);
		// top short sides
		baker.addModel(0, 0, 0, 7, 4, 1, 6, 12, true);
		baker.addModel(7, 0, 15, 7, 4, 1, 6, 12, true);
		baker.bake();
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return baker.getQuads(state == null ? EnumFacing.NORTH : state.getValue(GTBlockFacing.FACINGS));
	}

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		return Pair.of(this, PerspectiveMapWrapper.handlePerspective(this, getCamera(), cameraTransformType).getRight());
	}
}
