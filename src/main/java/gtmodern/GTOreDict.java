package gtmodern;

import gtmodern.block.GTBlockTileStorage;
import gtmodern.material.GTMaterial;
import gtmodern.material.GTMaterialGen;
import gtmodern.ore.GTOreFalling;
import gtmodern.ore.GTOreFlag;
import gtmodern.ore.GTOreStone;
import gtmodern.tool.GTToolElectricWrench;
import gtmodern.tool.GTToolFile;
import gtmodern.tool.GTToolHammer;
import gtmodern.tool.GTToolKnife;
import gtmodern.tool.GTToolWrench;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class GTOreDict {

	public static void init() {
		for (Item item : Item.REGISTRY) {
			if (item instanceof GTToolFile) {
				registerFile(item);
			}
			if (item instanceof GTToolHammer) {
				registerHammer(item);
			}
			if (item instanceof GTToolWrench) {
				registerWrench(item);
			}
			if (item instanceof GTToolElectricWrench) {
				registerWrench(item);
			}
			if (item instanceof GTToolKnife) {
				registerKnife(item);
			}
		}
		for (Block block : Block.REGISTRY) {
			if (block instanceof GTBlockTileStorage) {
				GTBlockTileStorage tile = (GTBlockTileStorage) block;
				if (tile.getType() == 0) {
					String name = "chest" + tile.getMaterial().getDisplayName();
					OreDictionary.registerOre(name, new ItemStack(block));
				}
			}
			// Register ores... with ore dict
			if (block instanceof GTOreStone) {
				GTOreStone ore = (GTOreStone) block;
				String name = "ore" + ore.getOreEntry().getMaterial().getDisplayName();
				if (!ore.getOreFlag().equals(GTOreFlag.BEDROCK)) {
					OreDictionary.registerOre(name, new ItemStack(block));
				}
			}
			if (block instanceof GTOreFalling) {
				GTOreFalling sand = (GTOreFalling) block;
				String name = "ore" + sand.getOreEntry().getMaterial().getDisplayName();
				OreDictionary.registerOre(name, new ItemStack(block));
			}
		}
		// Just doing these to make iteration & unification easier in some cases
		OreDictionary.registerOre("dustGunpowder", Items.GUNPOWDER);
		OreDictionary.registerOre("bookshelf", Blocks.BOOKSHELF);
		// Register some missing Ic2c stuff cause Speiger is a bad doge
		OreDictionary.registerOre("dustNetherrack", Ic2Items.netherrackDust);
		OreDictionary.registerOre("dustObsidian", Ic2Items.obsidianDust);
		OreDictionary.registerOre("plateIridium", Ic2Items.iridiumPlate);
		OreDictionary.registerOre("blockCharcoal", Ic2Items.charcoalBlock);
		OreDictionary.registerOre("logRubber", Ic2Items.rubberWood);
		OreDictionary.registerOre("ingotWroughtIron", Ic2Items.refinedIronIngot);
		OreDictionary.registerOre("dustRareEarth", Ic2Items.rareEarthDust);
		// Registering Aluminium for idiots
		GTMaterial aluminium = GTMaterial.Aluminium;
		OreDictionary.registerOre("casingMachineAluminum", (GTMaterialGen.getCasing(aluminium, 1)));
		OreDictionary.registerOre("blockAluminum", (GTMaterialGen.getMaterialBlock(aluminium, 1)));
		OreDictionary.registerOre("dustSmallAluminum", (GTMaterialGen.getSmallDust(aluminium, 1)));
		OreDictionary.registerOre("dustAluminum", (GTMaterialGen.getDust(aluminium, 1)));
		OreDictionary.registerOre("ingotAluminum", (GTMaterialGen.getIngot(aluminium, 1)));
		OreDictionary.registerOre("nuggetAluminum", (GTMaterialGen.getNugget(aluminium, 1)));
		OreDictionary.registerOre("plateAluminum", (GTMaterialGen.getPlate(aluminium, 1)));
		OreDictionary.registerOre("stickAluminum", (GTMaterialGen.getStick(aluminium, 1)));
		OreDictionary.registerOre("rodAluminum", (GTMaterialGen.getStick(aluminium, 1)));
		// Stuff to fit with how other mods have done it
		OreDictionary.registerOre("dustAsh", (GTMaterialGen.getDust(GTMaterial.Ashes, 1)));
		OreDictionary.registerOre("dustEnderEye", (GTMaterialGen.getDust(GTMaterial.EnderEye, 1)));
		OreDictionary.registerOre("itemSilicon", (GTMaterialGen.getIngot(GTMaterial.Silicon, 1)));
		OreDictionary.registerOre("dyeBlue", (GTMaterialGen.getDust(GTMaterial.Lazurite, 1)));
		OreDictionary.registerOre("blockGlass", (GTMaterialGen.get(GTBlocks.glassSlag)));
		OreDictionary.registerOre("itemSlag", (GTMaterialGen.getDust(GTMaterial.Slag, 1)));
		OreDictionary.registerOre("plateWroughtIron", (GTMaterialGen.getPlate(GTMaterial.RefinedIron, 1)));
		OreDictionary.registerOre("stickWroughtIron", (GTMaterialGen.getStick(GTMaterial.RefinedIron, 1)));
		OreDictionary.registerOre("pcbAny", (GTMaterialGen.get(GTItems.resinPCB, 1)));
		OreDictionary.registerOre("pcbBasic", (GTMaterialGen.get(GTItems.resinPCB, 1)));
		OreDictionary.registerOre("pcbAny", (GTMaterialGen.get(GTItems.plasticPCB, 1)));
		OreDictionary.registerOre("pcbAdvanced", (GTMaterialGen.get(GTItems.plasticPCB, 1)));
	}

	public static void registerFile(Item tool) {
		OreDictionary.registerOre("craftingToolFile", new ItemStack(tool, 1, OreDictionary.WILDCARD_VALUE));
	}

	public static void registerHammer(Item tool) {
		OreDictionary.registerOre("craftingToolForgeHammer", new ItemStack(tool, 1, OreDictionary.WILDCARD_VALUE));
	}

	public static void registerWrench(Item tool) {
		OreDictionary.registerOre("craftingToolWrench", new ItemStack(tool, 1, OreDictionary.WILDCARD_VALUE));
	}

	public static void registerKnife(Item tool) {
		OreDictionary.registerOre("craftingToolKnife", new ItemStack(tool, 1, OreDictionary.WILDCARD_VALUE));
	}
}
