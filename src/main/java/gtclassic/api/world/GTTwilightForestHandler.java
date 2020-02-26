package gtclassic.api.world;

import gtclassic.GTMod;
import gtclassic.common.GTBlocks;
import gtclassic.common.GTConfig;
import ic2.core.IC2;
import ic2.core.platform.registry.Ic2States;
import net.minecraft.world.World;
import twilightforest.world.TFWorld;
import twilightforest.world.feature.TFGenCaveStalactite;

public class GTTwilightForestHandler {

	public static void initStalactites() {
		GTMod.logger.info("Adding ores to Twilight Forest hollow hills");
		if (GTConfig.generation.iridiumGenerate) {
			TFGenCaveStalactite.addStalactite(3, GTBlocks.oreIridium.getDefaultState(), 0.5F, 4, 16, 30);
		}
		if (GTConfig.generation.sheldoniteGenerate) {
			TFGenCaveStalactite.addStalactite(2, GTBlocks.oreSheldonite.getDefaultState(), 0.5F, 8, 1, 12);
		}
		if (GTConfig.generation.sapphireGenerate) {
			TFGenCaveStalactite.addStalactite(2, GTBlocks.oreSapphire.getDefaultState(), 0.6F, 6, 1, 20);
		}
		if (GTConfig.generation.rubyGenerate) {
			TFGenCaveStalactite.addStalactite(2, GTBlocks.oreRuby.getDefaultState(), 0.6F, 6, 1, 20);
		}
		if (GTConfig.generation.bauxiteGenerate) {
			TFGenCaveStalactite.addStalactite(1, GTBlocks.oreBauxite.getDefaultState(), 0.8F, 12, 1, 24);
		}
		if (IC2.config.getFlag("WorldGenOreCopper")) {
			TFGenCaveStalactite.addStalactite(1, Ic2States.copperOre, 0.7F, 9, 1, 24);
		}
		if (IC2.config.getFlag("WorldGenOreTin")) {
			TFGenCaveStalactite.addStalactite(1, Ic2States.tinOre, 0.7F, 9, 1, 24);
		}
		if (IC2.config.getFlag("WorldGenOreSilver")) {
			TFGenCaveStalactite.addStalactite(1, Ic2States.silverOre, 0.5F, 8, 1, 12);
		}
		if (IC2.config.getFlag("WorldGenOreUranium")) {
			TFGenCaveStalactite.addStalactite(3, Ic2States.uraniumOre, 0.5F, 3, 12, 15);
		}
	}

	public static boolean isTwilightForest(World world) {
		return TFWorld.isTwilightForest(world);
	}
}
