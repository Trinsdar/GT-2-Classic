package gtmodern.util.jei.wrapper;

import java.util.ArrayList;

import gtmodern.util.recipe.GTMultiInputRecipeList.MultiRecipe;
import ic2.api.recipe.IRecipeInput;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class GTJeiInteractionWrapper implements IRecipeWrapper {

	private MultiRecipe multiRecipe;

	public GTJeiInteractionWrapper(MultiRecipe multiRecipe) {
		this.multiRecipe = multiRecipe;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void getIngredients(IIngredients ingredients) {
		ArrayList<ItemStack> inputs = new ArrayList<>();
		for (IRecipeInput input : multiRecipe.getInputs()) {
			inputs.addAll(input.getInputs());
		}
		ingredients.setInputs(ItemStack.class, inputs);
		ingredients.setOutputs(ItemStack.class, multiRecipe.getOutputs().getAllOutputs());
	}

	public MultiRecipe getMultiRecipe() {
		return multiRecipe;
	}
}
