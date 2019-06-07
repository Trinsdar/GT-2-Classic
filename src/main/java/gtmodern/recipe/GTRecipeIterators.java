package gtmodern.recipe;

import gtmodern.GTConfig;
import gtmodern.GTItems;
import gtmodern.block.GTBlockBloom;
import gtmodern.block.GTBlockTileStorage;
import gtmodern.material.GTMaterial;
import gtmodern.material.GTMaterialFlag;
import gtmodern.material.GTMaterialGen;
import gtmodern.tile.GTTileBath;
import gtmodern.tile.GTTileSmelter;
import gtmodern.tile.multi.GTTileMultiBlastFurnace;
import gtmodern.tile.multi.GTTileMultiRefractory;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.crafting.ICraftingRecipeList;
import ic2.api.recipe.IRecipeInput;
import ic2.core.block.machine.low.TileEntityCompressor;
import ic2.core.block.machine.low.TileEntityMacerator;
import ic2.core.item.recipe.entry.RecipeInputItemStack;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GTRecipeIterators {

	public static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;

	public static void recipeIterators1() {
		/*
		 * The statements below iterate through the material registry to create recipes
		 * for the correct corresponding items and blocks.
		 */
		for (GTMaterial mat : GTMaterial.values()) {
			createDustRecipe(mat);
			createIngotRecipe(mat);
			createGemRecipe(mat);
			createNuggetRecipe(mat);
			createPlateRecipe(mat);
			createSmallPlateRecipe(mat);
			createGearRecipe(mat);
			createStickRecipe(mat);
			createMagneticStickRecipe(mat);
			createWireRecipe(mat);
			createBlockRecipe(mat);
			createCasingRecipe(mat);
			createCoilRecipe(mat);
			createFoilRecipe(mat);
			createSmelterRecipes(mat);
		}
	}

	public static void createDustRecipe(GTMaterial mat) {
		String smalldust = "dustSmall" + mat.getDisplayName();
		String dust = "dust" + mat.getDisplayName();
		if (mat.hasFlag(GTMaterialFlag.SMALLDUST)) {
			// Regular dust to small dust,
			recipes.addShapelessRecipe(GTMaterialGen.getSmallDust(mat, 4), new Object[] { dust });
			if (mat.hasFlag(GTMaterialFlag.DUST)) {
				// Small dust to regular dust
				recipes.addShapelessRecipe(GTMaterialGen.getDust(mat, 1), new Object[] { smalldust, smalldust,
						smalldust, smalldust });
				TileEntityCompressor.addRecipe(smalldust, 4, GTMaterialGen.getDust(mat, 1), 0.0F);
			}
		}
	}

	public static void createIngotRecipe(GTMaterial mat) {
		String dust = "dust" + mat.getDisplayName();
		String nugget = "nugget" + mat.getDisplayName();
		int k = mat.getTemp();
		if (!mat.equals(GTMaterial.Plastic)) {
			if (mat.hasFlag(GTMaterialFlag.INGOT)) {
				// Ingot crafting recipe
				recipes.addRecipe(GTMaterialGen.getIngot(mat, 1), new Object[] { "XXX", "XXX", "XXX", 'X', nugget });
				if (mat.hasFlag(GTMaterialFlag.DUST) || mat.equals(GTMaterial.AnnealedCopper)) {
					if (k < 2000) {
						GameRegistry.addSmelting(GTMaterialGen.getDust(mat, 1), (GTMaterialGen.getIngot(mat, 1)), 0.1F);
					}
					if (k >= 2000 && k < 2700) {
						GTTileMultiBlastFurnace.addRecipe(new IRecipeInput[] {
								new RecipeInputOreDict(dust, 1) }, 8000, GTMaterialGen.getIngot(mat, 1));
					}
					if (k >= 2700 && k < 3000) {
						GTTileMultiBlastFurnace.addRecipe(new IRecipeInput[] {
								new RecipeInputOreDict(dust, 1) }, 16000, GTMaterialGen.getHotIngot(mat, 1));
						GTTileBath.addRecipe(new IRecipeInput[] {
								new RecipeInputItemStack(GTMaterialGen.getHotIngot(mat, 1)),
								new RecipeInputItemStack(GTMaterialGen.getWater(1)) }, GTTileBath.totalTicks(300), GTMaterialGen.getIngot(mat, 1), GTMaterialGen.get(GTItems.testTube, 1));
						GTRecipeCauldron.addFakeQuenchingRecipe(mat);
					}
					if (k >= 3000) {
						if (!mat.equals(GTMaterial.AnnealedCopper)) {
							GTTileMultiRefractory.addRecipe(new IRecipeInput[] {
									new RecipeInputOreDict(dust, 1) }, GTTileMultiRefractory.totalEu(32000), GTMaterialGen.getHotIngot(mat, 1));
						}
						GTTileBath.addRecipe(new IRecipeInput[] {
								new RecipeInputItemStack(GTMaterialGen.getHotIngot(mat, 1)),
								new RecipeInputItemStack(GTMaterialGen.getWater(1)) }, GTTileBath.totalTicks(300), GTMaterialGen.getIngot(mat, 1), GTMaterialGen.get(GTItems.testTube, 1));
						GTRecipeCauldron.addFakeQuenchingRecipe(mat);
					}
				}
			}
		}
	}

	public static void createGemRecipe(GTMaterial mat) {
		String dust = "dust" + mat.getDisplayName();
		String gem = "gem" + mat.getDisplayName();
		String block = "block" + mat.getDisplayName();
		if (mat.hasFlag(GTMaterialFlag.GEM)) {
			// Dust to gem
			TileEntityCompressor.addRecipe(dust, 1, GTMaterialGen.getGem(mat, 1), 0.0F);
			// Inverse
			TileEntityMacerator.addRecipe(gem, 1, GTMaterialGen.getDust(mat, 1), 0.0F);
			if (mat.hasFlag(GTMaterialFlag.BLOCK)) {
				// Block and gem related logic
				recipes.addShapelessRecipe(GTMaterialGen.getGem(mat, 9), new Object[] { block });
				TileEntityCompressor.addRecipe(gem, 9, GTMaterialGen.getMaterialBlock(mat, 1), 0.0F);
				TileEntityMacerator.addRecipe(block, 1, GTMaterialGen.getDust(mat, 9), 0.0F);
				recipes.addRecipe(GTMaterialGen.getMaterialBlock(mat, 1), new Object[] { "XXX", "XXX", "XXX", 'X',
						gem });
			}
		}
	}

	public static void createNuggetRecipe(GTMaterial mat) {
		String ingot = "ingot" + mat.getDisplayName();
		if (mat.hasFlag(GTMaterialFlag.NUGGET)) {
			recipes.addShapelessRecipe(GTMaterialGen.getNugget(mat, 9), new Object[] { ingot });
		}
	}

	public static void createPlateRecipe(GTMaterial mat) {
		String ingot = "ingot" + mat.getDisplayName();
		String plate = "plate" + mat.getDisplayName();
		if (mat.hasFlag(GTMaterialFlag.PLATE) && mat != GTMaterial.Silicon && mat != GTMaterial.Plastic) {
			// Plate crafting recipe
			if (GTConfig.harderPlates) {
				recipes.addRecipe(GTMaterialGen.getPlate(mat, 1), new Object[] { "H", "X", "X", 'H',
						"craftingToolForgeHammer", 'X', ingot });
			} else {
				recipes.addRecipe(GTMaterialGen.getPlate(mat, 1), new Object[] { "H", "X", 'H',
						"craftingToolForgeHammer", 'X', ingot });
			}
			// If a dust is present create a maceration recipe
			if (mat.hasFlag(GTMaterialFlag.DUST)) {
				TileEntityMacerator.addRecipe(plate, 1, GTMaterialGen.getDust(mat, 1), 0.0F);
			}
		}
	}

	public static void createSmallPlateRecipe(GTMaterial mat) {
		String plate = "plate" + mat.getDisplayName();
		if (mat.hasFlag(GTMaterialFlag.SMALLPLATE)) {
			recipes.addShapelessRecipe(GTMaterialGen.getSmallPlate(mat, 4), new Object[] { "craftingToolKnife",
					plate });
		}
	}

	public static void createGearRecipe(GTMaterial mat) {
		String plate = "plate" + mat.getDisplayName();
		String stick = "stick" + mat.getDisplayName();
		if (mat.hasFlag(GTMaterialFlag.GEAR)) {
			if (GTConfig.harderGears) {
				recipes.addRecipe(GTMaterialGen.getGear(mat, 1), new Object[] { "IXI", "XWX", "IXI", 'I', stick, 'X',
						plate, 'W', "craftingToolWrench" });
			} else {
				recipes.addRecipe(GTMaterialGen.getGear(mat, 1), new Object[] { " X ", "XWX", " X ", 'X', plate, 'W',
						"craftingToolWrench" });
			}
		}
	}

	public static void createStickRecipe(GTMaterial mat) {
		String ingot = "ingot" + mat.getDisplayName();
		String stick = "stick" + mat.getDisplayName();
		if (mat.hasFlag(GTMaterialFlag.STICK)) {
			// Stick crafting recipe
			if (GTConfig.harderRods) {
				recipes.addShapelessRecipe(GTMaterialGen.getStick(mat, 1), new Object[] { "craftingToolFile", ingot });
			} else {
				recipes.addShapelessRecipe(GTMaterialGen.getStick(mat, 2), new Object[] { "craftingToolFile", ingot });
			}
			// If a dust is present create a maceration recipe
			if (mat.hasFlag(GTMaterialFlag.DUST)) {
				TileEntityMacerator.addRecipe(stick, 1, GTMaterialGen.getSmallDust(mat, 2), 0.0F);
			}
		}
	}

	public static void createMagneticStickRecipe(GTMaterial mat) {
		String stick = "stick" + mat.getDisplayName();
		if (mat.hasFlag(GTMaterialFlag.MAGNETICSTICK)) {
			// Magnetic Stick crafting recipe
			recipes.addShapelessRecipe(GTMaterialGen.getMagneticStick(mat, 1), new Object[] { stick, "dustRedstone",
					"dustRedstone", "dustRedstone", "dustRedstone" });
		}
	}

	public static void createWireRecipe(GTMaterial mat) {
		String stick = "stick" + mat.getDisplayName();
		if (mat.hasFlag(GTMaterialFlag.WIRE)) {
			recipes.addShapelessRecipe(GTMaterialGen.getWire(mat, 4), new Object[] { "craftingToolKnife",
					"craftingToolForgeHammer", stick });
		}
	}

	public static void createBlockRecipe(GTMaterial mat) {
		String ingot = "ingot" + mat.getDisplayName();
		String block = "block" + mat.getDisplayName();
		if (mat.hasFlag(GTMaterialFlag.BLOCK)) {
			if (mat.hasFlag(GTMaterialFlag.INGOT)) {
				// Block crafting recipe
				recipes.addRecipe(GTMaterialGen.getMaterialBlock(mat, 1), new Object[] { "XXX", "XXX", "XXX", 'X',
						ingot });
				TileEntityCompressor.addRecipe(ingot, 9, GTMaterialGen.getMaterialBlock(mat, 1), 0.0F);
				// Inverse
				recipes.addShapelessRecipe(GTMaterialGen.getIngot(mat, 9), new Object[] { block });
				TileEntityMacerator.addRecipe(block, 1, GTMaterialGen.getDust(mat, 9), 0.0F);
			}
		}
	}

	public static void createCasingRecipe(GTMaterial mat) {
		String stick = "stick" + mat.getDisplayName();
		String plate = "plate" + mat.getDisplayName();
		String casing = "casingMachine" + mat.getDisplayName();
		if (mat.hasFlag(GTMaterialFlag.CASING)) {
			// Casing crafting recipe
			recipes.addRecipe(GTMaterialGen.getCasing(mat, 1), new Object[] { "SXX", "XWX", "XXS", 'X', plate, 'S',
					stick, 'W', "craftingToolWrench" });
			TileEntityMacerator.addRecipe(casing, 1, GTMaterialGen.getDust(mat, 7), 0.0F);
		}
	}

	public static void createCoilRecipe(GTMaterial mat) {
		String stick = "stick" + mat.getDisplayName();
		if (mat.hasFlag(GTMaterialFlag.COIL)) {
			recipes.addRecipe(GTMaterialGen.getCoil(mat, 1), new Object[] { "XXX", "XXX", "XXX", 'X', stick });
		}
	}

	public static void createFoilRecipe(GTMaterial mat) {
		String plate = "plate" + mat.getDisplayName();
		if (mat.hasFlag(GTMaterialFlag.FOIL)) {
			recipes.addShapelessRecipe(GTMaterialGen.getFoil(mat, 4), new Object[] { "craftingToolKnife",
					"craftingToolForgeHammer", plate });
		}
	}

	public static void createSmelterRecipes(GTMaterial mat) {
		String dust = "dust" + mat.getDisplayName();
		String ingot = "ingot" + mat.getDisplayName();
		String nugget = "nugget" + mat.getDisplayName();
		if (GTMaterial.isLowHeat(mat) && !mat.equals(GTMaterial.RefinedIron) && !mat.hasFlag(GTMaterialFlag.GEM)) {
			if (mat.hasFlag(GTMaterialFlag.PLATE)) {
				if (!mat.equals(GTMaterial.Plastic)) {
					GTTileSmelter.addRecipe(ingot, 1, GTMaterialGen.get(GTItems.moldPlate), GTMaterialGen.getPlate(mat, 1));
					GTTileSmelter.addRecipe(nugget, 9, GTMaterialGen.get(GTItems.moldPlate), GTMaterialGen.getPlate(mat, 1));
				}
				GTTileSmelter.addRecipe(dust, 1, GTMaterialGen.get(GTItems.moldPlate), GTMaterialGen.getPlate(mat, 1));
			}
			if (mat.hasFlag(GTMaterialFlag.GEAR)) {
				GTTileSmelter.addRecipe(ingot, 4, GTMaterialGen.get(GTItems.moldGear), GTMaterialGen.getGear(mat, 1));
				GTTileSmelter.addRecipe(nugget, 36, GTMaterialGen.get(GTItems.moldGear), GTMaterialGen.getGear(mat, 1));
				GTTileSmelter.addRecipe(dust, 4, GTMaterialGen.get(GTItems.moldGear), GTMaterialGen.getGear(mat, 1));
			}
			if (mat.hasFlag(GTMaterialFlag.STICK)) {
				GTTileSmelter.addRecipe(ingot, 1, GTMaterialGen.get(GTItems.moldStick), GTMaterialGen.getStick(mat, 2));
				GTTileSmelter.addRecipe(nugget, 9, GTMaterialGen.get(GTItems.moldStick), GTMaterialGen.getStick(mat, 2));
				GTTileSmelter.addRecipe(dust, 1, GTMaterialGen.get(GTItems.moldStick), GTMaterialGen.getStick(mat, 2));
			}
			if (mat.hasFlag(GTMaterialFlag.BLOCK)) {
				GTTileSmelter.addRecipe(ingot, 9, GTMaterialGen.get(GTItems.moldBlock), GTMaterialGen.getMaterialBlock(mat, 1));
				GTTileSmelter.addRecipe(dust, 9, GTMaterialGen.get(GTItems.moldBlock), GTMaterialGen.getMaterialBlock(mat, 1));
			}
			if (mat.hasFlag(GTMaterialFlag.NUGGET)) {
				GTTileSmelter.addRecipe(ingot, 1, GTMaterialGen.get(GTItems.moldNugget), GTMaterialGen.getNugget(mat, 9));
				GTTileSmelter.addRecipe(dust, 1, GTMaterialGen.get(GTItems.moldNugget), GTMaterialGen.getNugget(mat, 9));
				if (mat != GTMaterial.Bronze && mat != GTMaterial.Silver && mat != GTMaterial.Copper
						&& mat != GTMaterial.Tin) {
					GTTileSmelter.addRecipe(nugget, 9, GTMaterialGen.get(GTItems.moldIngot), GTMaterialGen.getIngot(mat, 1));
					GTTileSmelter.addRecipe(dust, 1, GTMaterialGen.get(GTItems.moldIngot), GTMaterialGen.getIngot(mat, 1));
				}
			}
		}
	}

	public static void recipeIterators2() {
		/*
		 * This is where I will store recipes that are part of the material registry but
		 * are tied to other mods/vanilla so they cannot be created through iteration.
		 */
		final ItemStack dustGlowstone = new ItemStack(Items.GLOWSTONE_DUST);
		final ItemStack dustGunpowder = new ItemStack(Items.GUNPOWDER);
		final ItemStack dustRedstone = new ItemStack(Items.REDSTONE);
		dustUtil(dustGlowstone, GTMaterial.Glowstone);
		dustUtil(dustGunpowder, GTMaterial.Gunpowder);
		dustUtil(Ic2Items.tinDust, GTMaterial.Tin);
		dustUtil(Ic2Items.obsidianDust, GTMaterial.Obsidian);
		dustUtil(Ic2Items.bronzeDust, GTMaterial.Bronze);
		dustUtil(Ic2Items.coalDust, GTMaterial.Coal);
		dustUtil(Ic2Items.silverDust, GTMaterial.Silver);
		dustUtil(dustRedstone, GTMaterial.Redstone);
		dustUtil(Ic2Items.clayDust, GTMaterial.Clay);
		dustUtil(Ic2Items.goldDust, GTMaterial.Gold);
		dustUtil(Ic2Items.copperDust, GTMaterial.Copper);
		dustUtil(Ic2Items.netherrackDust, GTMaterial.Netherrack);
		dustUtil(Ic2Items.ironDust, GTMaterial.Iron);
		dustUtil(Ic2Items.charcoalDust, GTMaterial.Charcoal);
		ingotUtil(Ic2Items.copperIngot, GTMaterial.Copper);
		ingotUtil(Ic2Items.tinIngot, GTMaterial.Tin);
		ingotUtil(Ic2Items.bronzeIngot, GTMaterial.Bronze);
		ingotUtil(Ic2Items.silverIngot, GTMaterial.Silver);
	}

	public static void recipeIterators3() {
		for (Block block : Block.REGISTRY) {
			createBloomJEIRecipe(block);
			if (block instanceof GTBlockTileStorage) {
				GTBlockTileStorage tile = (GTBlockTileStorage) block;
				GTMaterial material = tile.getMaterial();
				String cabinet = "chest" + material.getDisplayName();
				String plate = "plate" + material.getDisplayName();
				String stick = "stick" + material.getDisplayName();
				String wrench = "craftingToolWrench";
				String hammer = "craftingToolForgeHammer";
				if (tile.getType() == 0) { // cabinet
					recipes.addRecipe(new ItemStack(block), new Object[] { "HPW", "P P", "PPP", 'P', plate, 'S', stick,
							'H', hammer, 'W', wrench });
				}
				if (tile.getType() == 1) {// large cabinet
					recipes.addRecipe(new ItemStack(block), new Object[] { "SPS", "CWC", "SPS", 'P', plate, 'S', stick,
							'C', cabinet, 'W', wrench });
				}
				if (tile.getType() == 2) {// bookshelf
					recipes.addRecipe(new ItemStack(block), new Object[] { "PPP", "H W", "PPP", 'P', plate, 'S', stick,
							'H', hammer, 'W', wrench });
				}
				if (tile.getType() == 3) {// workbench
					recipes.addRecipe(new ItemStack(block), new Object[] { "PHP", "SWS", "PCP", 'P', plate, 'S', stick,
							'H', hammer, 'W', "workbench", 'C', "chestWood" });
				}
			}
		}
	}

	public static void createBloomJEIRecipe(Block block) {
		if (block instanceof GTBlockBloom) {
			GTBlockBloom bloom = (GTBlockBloom) block;
			GTRecipeProcessing.addByproduct(GTMaterialGen.get(block), bloom.getOutput(), GTMaterialGen.getDust(GTMaterial.Slag, 1));
		}
	}

	public static void dustUtil(ItemStack stack, GTMaterial material) {
		String smalldust = "dustSmall" + material.getDisplayName();
		recipes.addShapelessRecipe(stack, new Object[] { smalldust, smalldust, smalldust, smalldust });
		TileEntityCompressor.addRecipe(smalldust, 4, GTMaterialGen.getIc2(stack, 1), 0.0F);
	}

	public static void ingotUtil(ItemStack stack, GTMaterial material) {
		String nugget = "nugget" + material.getDisplayName();
		recipes.addRecipe(stack, new Object[] { "XXX", "XXX", "XXX", 'X', nugget });
	}
}
