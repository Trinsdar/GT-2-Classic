package gtmodern.recipe;

import gtmodern.ore.GTOreRegistry;
import gtmodern.tile.GTTileBath;
import gtmodern.tile.GTTileCentrifuge;
import gtmodern.tile.GTTileElectrolyzer;
import gtmodern.tile.GTTileRoaster;
import gtmodern.tile.GTTileShredder;
import gtmodern.tile.GTTileSmelter;
import gtmodern.tile.multi.GTTileMultiBlastFurnace;
import gtmodern.tile.multi.GTTileMultiBloomery;
import gtmodern.tile.multi.GTTileMultiChemicalReactor;
import gtmodern.tile.multi.GTTileMultiCryogenicSeparator;
import gtmodern.tile.multi.GTTileMultiFusion;
import gtmodern.tile.multi.GTTileMultiLeadChamber;
import gtmodern.tile.multi.GTTileMultiRefractory;

public class GTRecipe {

	/*
	 * For now this set of recipes is heavily broken apart which allows me to
	 * reconfigure them with clarity. After the progression is finalized, all
	 * recipes with be in this class
	 */
	public static void init() {
		GTOreRegistry.oreDirectSmelting();
		GTRecipeCauldron.recipesCauldron();
		GTRecipeCircuitry.recipesCircutry();
		GTRecipeIterators.recipeIterators1();
		GTRecipeIterators.recipeIterators2();
		GTRecipeIterators.recipeIterators3();
		GTRecipeIteratorsTools.recipeIteratorsTools();
		GTRecipeMod.recipesIC2();
		GTRecipeProcessing.recipesProcessing();
		GTRecipeProcessing.recipesByproducts();
		GTRecipeProcessing.recipesInteractions();
		GTRecipeShaped.recipeShaped1();
		GTRecipeShaped.recipeShaped2();
		GTRecipeShaped.recipeShaped3();
		GTRecipeShapeless.recipeShapeless1();
		// below is more how things will go
		GTTileBath.init();
		GTTileCentrifuge.init();
		GTTileElectrolyzer.init();
		GTTileShredder.init();
		GTTileSmelter.init();
		GTTileRoaster.init();
		GTTileMultiBlastFurnace.init();
		GTTileMultiBloomery.init();
		GTTileMultiChemicalReactor.init();
		GTTileMultiFusion.init();
		GTTileMultiRefractory.init();
		GTTileMultiCryogenicSeparator.init();
		GTTileMultiLeadChamber.init();
	}
}
