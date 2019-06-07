package gtmodern.util;

import java.awt.Color;

import gtmodern.material.GTMaterial;
import ic2.core.IC2;
import ic2.core.platform.lang.components.base.LangComponentHolder.LocaleBlockComp;
import ic2.core.platform.lang.components.base.LangComponentHolder.LocaleJEIInfoComp;
import ic2.core.platform.lang.components.base.LocaleComp;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;

public class GTValues {

	/*
	 * This is place to hold global values temporarily, it will all be refactored
	 * into better places in time
	 */
	// boolean that renders anything labeled as WIP uncraftable
	public static boolean debugMode = false;
	// colors
	public static int white = 16777215;
	public static int grey = 4210752;
	public static int red = 15599112;
	public static int green = 9567352;
	// lang
	public static LocaleComp centrifugeEU = new LocaleJEIInfoComp("jei.centrifugeu.name");
	public static LocaleComp sluiceBox = new LocaleBlockComp("tile.gtmodern.sluicebox");
	public static LocaleComp sluiceBoxExtension = new LocaleBlockComp("tile.gtmodern.sluiceboxextension");
	// recipe stuff
	public static FluidStack water = new FluidStack(FluidRegistry.WATER, 1000);
	public static FluidStack lava = new FluidStack(FluidRegistry.LAVA, 1000);

	public static String getIC2Ingot() {
		return IC2.getRefinedIron();
	}

	public static String getIC2Plate() {
		if (IC2.getRefinedIron().equals("ingotSteel")) {
			return "plateSteel";
		} else {
			return "plateRefinedIron";
		}
	}

	public static Color getToolColor(int tier) {
		if (tier == 1) {
			return GTMaterial.Steel.getColor();
		}
		if (tier == 2) {
			return GTMaterial.Aluminium.getColor();
		}
		if (tier == 3) {
			return GTMaterial.StainlessSteel.getColor();
		}
		return Color.white;
	}

	public static Color getMachineColor(int tier) {
		if (tier == 1) {
			return GTMaterial.Steel.getColor();
		}
		if (tier == 2) {
			return GTMaterial.Aluminium.getColor();
		}
		if (tier == 3) {
			return GTMaterial.StainlessSteel.getColor();
		}
		if (tier == 4) {
			return GTMaterial.Titanium.getColor();
		}
		if (tier == 5) {
			return GTMaterial.Tungsten.getColor();
		}
		if (tier == 6) {
			return GTMaterial.Chrome.getColor();
		}
		if (tier == 7) {
			return GTMaterial.Iridium.getColor();
		}
		if (tier == 8) {
			return GTMaterial.Osmium.getColor();
		} else {
			return Color.white;
		}
	}

	public static String getTierString(int tier) {
		if (tier == 0) {
			return "N/A";
		}
		if (tier == 1) {
			return "LV";
		}
		if (tier == 2) {
			return "MV";
		}
		if (tier == 3) {
			return "HV";
		}
		if (tier == 4) {
			return "EV";
		}
		if (tier == 5) {
			return "IV";
		}
		if (tier == 6) {
			return "LuV";
		}
		if (tier == 7) {
			return "ZPM";
		}
		if (tier == 8) {
			return "UV";
		}
		if (tier == 9) {
			return "MAX";
		} else {
			return "null";
		}
	}

	public static boolean isBCShard(ItemStack stack) {
		if (Loader.isModLoaded("buildcraftcore")) {
			return stack.isItemEqual(new ItemStack(Item.getByNameOrId("buildcraftcore:fragile_fluid_shard")));
		}
		return false;
	}
}
