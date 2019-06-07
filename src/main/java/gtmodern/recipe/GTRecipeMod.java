package gtmodern.recipe;

import gtmodern.GTBlocks;
import gtmodern.GTItems;
import gtmodern.material.GTMaterial;
import gtmodern.material.GTMaterialGen;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.crafting.ICraftingRecipeList;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

public class GTRecipeMod {

	static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;

	public static void recipesIC2() {
		/*
		 * Recipes for IC2-Classic
		 */
		recipes.addRecipe(Ic2Items.miningLaser.copy(), new Object[] { "Rcc", "AAC", " AA", 'A',
				Ic2Items.advancedAlloy.copy(), 'C', "circuitAdvanced", 'c',
				GTMaterialGen.getFluid(GTMaterial.Helium, 1), 'R', "dustRedstone" });
		recipes.overrideRecipe("shaped_item.itemPartIridium_-1226385265", GTMaterialGen.getIc2(Ic2Items.iridiumPlate, 1), "IAI", "ADA", "IAI", 'I', "ingotIridium", 'A', Ic2Items.advancedAlloy.copy(), 'D', "gemDiamond");
		recipes.addRecipe(Ic2Items.reactorReflectorThick.copy(), new Object[] { " P ", "PBP", " P ", 'P',
				Ic2Items.reactorReflector, 'B', GTMaterialGen.getFluid(GTMaterial.Beryllium, 1) });
		recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.luminator, 16), new Object[] { "III", "GHG", "GGG", 'G',
				"blockGlass", 'I', "ingotSilver", 'H', GTMaterialGen.getFluid(GTMaterial.Helium, 1), 'C',
				Ic2Items.insulatedCopperCable.copy() });
		recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.luminator, 16), new Object[] { "III", "GHG", "GGG", 'G',
				"blockGlass", 'I', "ingotSilver", 'H', GTMaterialGen.getFluid(GTMaterial.Mercury, 1), 'C',
				Ic2Items.insulatedCopperCable.copy() });
		recipes.addRecipe(Ic2Items.mfe.copy(), new Object[] { "XYX", "YCY", "XYX", 'C', Ic2Items.machine.copy(), 'Y',
				GTBlocks.batteryLithiumSmall, 'X', Ic2Items.doubleInsulatedGoldCable.copy() });
		recipes.addRecipe(Ic2Items.mfe.copy(), new Object[] { "XYX", "YCY", "XYX", 'C', Ic2Items.machine.copy(), 'Y',
				GTBlocks.batteryLithiumSmall, 'X', GTMaterialGen.getIc2(Ic2Items.doubleInsulatedBronzeCable, 4) });
		// Overrides
		recipes.overrideRecipe("shaped_item.itemPartIridium_1100834802", GTMaterialGen.getIc2(Ic2Items.iridiumPlate, 1), "IAI", "ADA", "IAI", 'I', "ingotIridium", 'A', Ic2Items.advancedAlloy.copy(), 'D', "gemDiamond");
		recipes.overrideRecipe("shaped_tile.blockStoneMacerator_-130868445", Ic2Items.stoneMacerator.copy(), new Object[] {
				"FXF", "CYC", "YVY", 'Y', "cobblestone", 'X', "gemDiamond", 'F', Items.FLINT, 'C', Blocks.PISTON, 'V',
				Blocks.FURNACE });
		recipes.overrideRecipe("shaped_tile.blockMacerator_127744036", Ic2Items.macerator.copy(), new Object[] { " Y ",
				"CVC", "XXX", 'Y', Ic2Items.electricCircuit.copy(), 'V', Ic2Items.machine.copy(), 'C', GTItems.motorLV,
				'X', "gemDiamond" });
		recipes.overrideRecipe("shaped_tile.blockMacerator_2072794668", Ic2Items.macerator.copy(), new Object[] { "XXX",
				"CVC", " Y ", 'Y', Ic2Items.electricCircuit.copy(), 'V', Ic2Items.machine.copy(), 'C', GTItems.motorLV,
				'X', "gemDiamond" });
	}
}
