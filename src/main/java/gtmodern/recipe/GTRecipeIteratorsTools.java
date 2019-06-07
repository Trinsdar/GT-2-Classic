package gtmodern.recipe;

import gtmodern.GTBlocks;
import gtmodern.GTItems;
import gtmodern.material.GTMaterial;
import gtmodern.material.GTMaterialFlag;
import gtmodern.material.GTMaterialGen;
import gtmodern.tool.GTToolAxe;
import gtmodern.tool.GTToolChainsaw;
import gtmodern.tool.GTToolElectricWrench;
import gtmodern.tool.GTToolFile;
import gtmodern.tool.GTToolHammer;
import gtmodern.tool.GTToolKnife;
import gtmodern.tool.GTToolMiningDrill;
import gtmodern.tool.GTToolPickaxe;
import gtmodern.tool.GTToolShovel;
import gtmodern.tool.GTToolSword;
import gtmodern.tool.GTToolWrench;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.crafting.ICraftingRecipeList;
import ic2.core.item.recipe.upgrades.EnchantmentModifier;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GTRecipeIteratorsTools {

	public static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;

	public static void recipeIteratorsTools() {
		for (Item item : Item.REGISTRY) {
			createFileRecipe(item);
			createHammerRecipe(item);
			createWrenchRecipe(item);
			createKnifeRecipe(item);
			createSwordRecipe(item);
			createShovelRecipe(item);
			createAxeRecipe(item);
			createPickaxeRecipe(item);
			createDrillRecipe(item);
			createChainsawRecipe(item);
			createElectricWrenchRecipe(item);
		}
	}

	public static void createFileRecipe(Item item) {
		if (item instanceof GTToolFile) {
			GTToolFile file = (GTToolFile) item;
			if (file.getMaterial().equals(GTMaterial.Diamond) || file.getMaterial().hasFlag(GTMaterialFlag.GEM)) {
				String input = "plate" + file.getMaterial().getDisplayName();
				recipes.addRecipe(new ItemStack(item), new Object[] { "P ", "P ", "SF", 'P', input, 'S', "stickWood",
						'F', "craftingToolFile" });
			} else {
				String input = "plate" + file.getMaterial().getDisplayName();
				recipes.addRecipe(new ItemStack(item), new Object[] { "P", "P", "S", 'P', input, 'S', "stickWood" });
			}
		}
	}

	public static void createHammerRecipe(Item item) {
		if (item instanceof GTToolHammer) {
			GTToolHammer hammer = (GTToolHammer) item;
			if (hammer.getMaterial().equals(GTMaterial.Diamond) || hammer.getMaterial().hasFlag(GTMaterialFlag.GEM)) {
				String input = "gem" + hammer.getMaterial().getDisplayName();
				recipes.addRecipe(new ItemStack(item), new Object[] { "II ", "IIS", "IIF", 'I', input, 'S', "stickWood",
						'F', "craftingToolFile" });
			} else {
				String input = "ingot" + hammer.getMaterial().getDisplayName();
				recipes.addRecipe(new ItemStack(item), new Object[] { "II ", "IIS", "II ", 'I', input, 'S',
						"stickWood" });
			}
		}
	}

	public static void createWrenchRecipe(Item item) {
		if (item instanceof GTToolWrench) {
			GTToolWrench wrench = (GTToolWrench) item;
			if (wrench.getMaterial().equals(GTMaterial.Diamond) || wrench.getMaterial().hasFlag(GTMaterialFlag.GEM)) {
				String input = "gem" + wrench.getMaterial().getDisplayName();
				recipes.addRecipe(new ItemStack(item), new Object[] { "IFI", "III", " I ", 'I', input, 'F',
						"craftingToolFile" });
			} else {
				String input = "ingot" + wrench.getMaterial().getDisplayName();
				recipes.addRecipe(new ItemStack(item), new Object[] { "I I", "III", " I ", 'I', input });
			}
		}
	}

	public static void createKnifeRecipe(Item item) {
		if (item instanceof GTToolKnife) {
			GTToolKnife knife = (GTToolKnife) item;
			if (knife.getMaterial().equals(GTMaterial.Diamond) || knife.getMaterial().hasFlag(GTMaterialFlag.GEM)) {
				String input = "gem" + knife.getMaterial().getDisplayName();
				recipes.addRecipe(GTMaterialGen.get(item, 1), new Object[] { "PPS", 'P', input, 'S', "stickWood" });
			} else {
				String mat = knife.getMaterial().getDisplayName();
				String input = "plate" + mat;
				recipes.addRecipe(GTMaterialGen.get(item, 1), new Object[] { "PPS", 'P', input, 'S', "stickWood" });
			}
		}
	}

	public static void createSwordRecipe(Item item) {
		if (item instanceof GTToolSword) {
			GTToolSword sword = (GTToolSword) item;
			if (sword.getMaterial().equals(GTMaterial.Flint)) {
				recipes.addRecipe(new ItemStack(item), new Object[] { "P", "P", "S", 'P',
						new EnchantmentModifier(GTMaterialGen.get(item), Enchantments.FIRE_ASPECT).setUsesInput(), 'P',
						Items.FLINT, 'S', "stickWood" });
				recipes.addRecipe(new ItemStack(item), new Object[] { "PPS", 'P',
						new EnchantmentModifier(GTMaterialGen.get(item), Enchantments.FIRE_ASPECT).setUsesInput(), 'P',
						Items.FLINT, 'S', "stickWood" });
			} else {
				String input = "plate" + sword.getMaterial().getDisplayName();
				recipes.addRecipe(new ItemStack(item), new Object[] { "PF", "P ", "S ", 'P', input, 'S', "stickWood",
						'F', "craftingToolFile" });
			}
		}
	}

	public static void createShovelRecipe(Item item) {
		if (item instanceof GTToolShovel) {
			GTToolShovel shovel = (GTToolShovel) item;
			if (shovel.getMaterial().equals(GTMaterial.Flint)) {
				recipes.addRecipe(new ItemStack(item), new Object[] { "P", "S", 'P',
						new EnchantmentModifier(GTMaterialGen.get(item), Enchantments.FIRE_ASPECT).setUsesInput(), 'P',
						Items.FLINT, 'S', "stickWood" });
			} else {
				String input = "plate" + shovel.getMaterial().getDisplayName();
				recipes.addRecipe(new ItemStack(item), new Object[] { "P", "S", "S", 'P', input, 'S', "stickWood", });
			}
		}
	}

	public static void createAxeRecipe(Item item) {
		if (item instanceof GTToolAxe) {
			GTToolAxe axe = (GTToolAxe) item;
			if (axe.getMaterial().equals(GTMaterial.Flint)) {
				recipes.addRecipe(new ItemStack(item), new Object[] { "PP", "PS", 'P',
						new EnchantmentModifier(GTMaterialGen.get(item), Enchantments.FIRE_ASPECT).setUsesInput(), 'P',
						Items.FLINT, 'S', "stickWood" });
			} else {
				String plate = "plate" + axe.getMaterial().getDisplayName();
				String ingot = "ingot" + axe.getMaterial().getDisplayName();
				recipes.addRecipe(new ItemStack(item), new Object[] { "PI", "PS", " S", 'P', plate, 'I', ingot, 'S',
						"stickWood" });
			}
		}
	}

	public static void createPickaxeRecipe(Item item) {
		if (item instanceof GTToolPickaxe) {
			GTToolPickaxe pickaxe = (GTToolPickaxe) item;
			if (pickaxe.getMaterial().equals(GTMaterial.Flint)) {
				recipes.addRecipe(new ItemStack(item), new Object[] { "PPP", " S ", 'P',
						new EnchantmentModifier(GTMaterialGen.get(item), Enchantments.FIRE_ASPECT).setUsesInput(), 'P',
						Items.FLINT, 'S', "stickWood" });
			} else {
				String plate = "plate" + pickaxe.getMaterial().getDisplayName();
				String ingot = "ingot" + pickaxe.getMaterial().getDisplayName();
				recipes.addRecipe(new ItemStack(item), new Object[] { "PII", " S ", " S ", 'P', plate, 'I', ingot, 'S',
						"stickWood" });
			}
		}
	}

	public static void createDrillRecipe(Item item) {
		if (item instanceof GTToolMiningDrill) {
			GTToolMiningDrill drill = (GTToolMiningDrill) item;
			if (drill.getTier(new ItemStack(item)) == 1) {
				String plate = "plate" + drill.getMaterial().getDisplayName();
				recipes.addRecipe(new ItemStack(item), new Object[] { "CTT", "PPT", "PPB", 'T', plate, 'P',
						"plateSteel", 'C', GTItems.motorLV, 'B', GTBlocks.batteryLithiumSmall });
			}
			if (drill.getTier(new ItemStack(item)) == 2) {
				if (!drill.getMaterial().equals(GTMaterial.Diamond)) {
					String plate = "plate" + drill.getMaterial().getDisplayName();
					recipes.addRecipe(new ItemStack(item), new Object[] { "CTT", "PPT", "PPB", 'T', plate, 'P',
							"plateAluminium", 'C', GTItems.motorMV, 'B', GTBlocks.batteryLithiumMed });
				}
			}
		}
	}

	public static void createChainsawRecipe(Item item) {
		if (item instanceof GTToolChainsaw) {
			GTToolChainsaw chainsaw = (GTToolChainsaw) item;
			if (chainsaw.getTier(new ItemStack(item)) == 1) {
				String plate = "plate" + chainsaw.getMaterial().getDisplayName();
				recipes.addRecipe(new ItemStack(item), new Object[] { "PPT", "BCT", "PPT", 'T', plate, 'P',
						"plateSteel", 'C', GTItems.motorLV, 'B', GTBlocks.batteryLithiumSmall });
			}
		}
	}

	public static void createElectricWrenchRecipe(Item item) {
		if (item instanceof GTToolElectricWrench) {
			GTToolElectricWrench wrench = (GTToolElectricWrench) item;
			if (wrench.getTier(new ItemStack(item)) == 1) {
				String ingot = "ingot" + wrench.getMaterial().getDisplayName();
				recipes.addRecipe(new ItemStack(item), new Object[] { "TTT", "PCP", "PBP", 'T', ingot, 'P',
						"plateSteel", 'C', GTItems.motorLV, 'B', GTBlocks.batteryLithiumSmall });
			}
		}
	}
}
