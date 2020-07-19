package gtclassic.api.material;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;

public class GTMaterial {

	static GTMaterialFlag fluid = GTMaterialFlag.FLUID;
	static GTMaterialFlag gas = GTMaterialFlag.GAS;
	static GTMaterialFlag dust = GTMaterialFlag.DUST;
	static GTMaterialFlag gemRuby = GTMaterialFlag.RUBY;
	static GTMaterialFlag gemSapphire = GTMaterialFlag.SAPPHIRE;
	static GTMaterialFlag ingot = GTMaterialFlag.INGOT;
	static GTMaterialFlag blockMetal = GTMaterialFlag.BLOCKMETAL;
	static GTMaterialFlag blockGem = GTMaterialFlag.BLOCKGEM;
	static GTMaterialFlag empty = GTMaterialFlag.NULL;
	static GTMaterialFlag[] rubyAll = { dust, gemRuby, blockGem };
	static GTMaterialFlag[] sapphireAll = { dust, gemSapphire, blockGem };
	static GTMaterialFlag[] metalAll = { dust, ingot, blockMetal };
	/** Master Material Map **/
	private static HashMap<String, GTMaterial> generatedMap = new HashMap<>();
	/** Material Instances **/
	public static final GTMaterial Aluminium = new GTMaterial(13, "Aluminium", 2, 128, 200, 240, metalAll);
	public static final GTMaterial Argon = new GTMaterial(18, "Argon", 255, 100, 255, gas);
	public static final GTMaterial Bauxite = new GTMaterial("Bauxite", 200, 100, 0, dust);
	public static final GTMaterial Basalt = new GTMaterial("Basalt", 30, 20, 20, dust);
	public static final GTMaterial Beryllium = new GTMaterial(4, "Beryllium", 30, 80, 50, fluid);
	public static final GTMaterial BrownDye = new GTMaterial("BrownDye", 150, 75, 0, dust);
	public static final GTMaterial Calcite = new GTMaterial("Calcite", 250, 230, 220, dust);
	public static final GTMaterial Calcium = new GTMaterial(20, "Calcium", 155, 96, 80, fluid);
	public static final GTMaterial Carbon = new GTMaterial(6, "Carbon", 0, 0, 0, dust);
	public static final GTMaterial Chlorine = new GTMaterial(17, "Chlorine", 50, 150, 150, fluid);
	public static final GTMaterial Chrome = new GTMaterial(24, "Chrome", 3, 240, 210, 230, metalAll);
	public static final GTMaterial Deuterium = new GTMaterial("Deuterium", 255, 255, 0, gas);
	public static final GTMaterial Electrum = new GTMaterial("Electrum", 2, 255, 255, 100, metalAll);
	public static final GTMaterial Emerald = new GTMaterial("Emerald", 70, 232, 100, dust);
	public static final GTMaterial EnderEye = new GTMaterial("EnderEye", 160, 250, 230, dust);
	public static final GTMaterial EnderPearl = new GTMaterial("EnderPearl", 108, 220, 200, dust);
	public static final GTMaterial Flint = new GTMaterial("Flint", 1, 0, 32, 64, dust);
	public static final GTMaterial Fuel = new GTMaterial("Fuel", 255, 255, 0, fluid);
	public static final GTMaterial Helium = new GTMaterial(2, "Helium", 255, 255, 0, gas);
	public static final GTMaterial Helium3 = new GTMaterial("Helium3", 255, 255, 0, gas);
	public static final GTMaterial Hydrogen = new GTMaterial(1, "Hydrogen", 0, 38, 255, gas);
	public static final GTMaterial Invar = new GTMaterial("Invar", 2, 220, 220, 150, metalAll);
	public static final GTMaterial Iridium = new GTMaterial(77, "Iridium", 4, 255, 255, 255, metalAll);
	public static final GTMaterial Lazurite = new GTMaterial("Lazurite", 100, 120, 255, dust);
	public static final GTMaterial Lithium = new GTMaterial(3, "Lithium", 87, 150, 204, dust);
	public static final GTMaterial Lubricant = new GTMaterial("Lubricant", 255, 196, 0, fluid);
	public static final GTMaterial MagicDye = new GTMaterial("MagicDye", 255, 255, 255, GTMaterialFlag.MAGICDYE);
	public static final GTMaterial Mercury = new GTMaterial(88, "Mercury", 250, 250, 250, fluid);
	public static final GTMaterial Methane = new GTMaterial("Methane", 255, 50, 130, gas);
	public static final GTMaterial Neon = new GTMaterial(10, "Neon", 255, 100, 100, gas);
	public static final GTMaterial Nitrogen = new GTMaterial(7, "Nitrogen", 0, 190, 190, gas);
	public static final GTMaterial Nickel = new GTMaterial(28, "Nickel", 2, 250, 250, 200, metalAll);
	public static final GTMaterial Oil = new GTMaterial("Oil", 0, 0, 0, fluid);
	public static final GTMaterial Oxygen = new GTMaterial(8, "Oxygen", 100, 160, 220, gas);
	public static final GTMaterial Phosphorus = new GTMaterial(15, "Phosphorus", 190, 0, 0, dust);
	public static final GTMaterial Potassium = new GTMaterial(19, "Potassium", 250, 250, 250, fluid);
	public static final GTMaterial Platinum = new GTMaterial(78, "Platinum", 3, 100, 180, 250, metalAll);
	public static final GTMaterial Plutonium = new GTMaterial(94, "Plutonium", 2, 240, 50, 50, false, metalAll);
	public static final GTMaterial Pyrite = new GTMaterial("Pyrite", 150, 120, 40, dust);
	public static final GTMaterial Ruby = new GTMaterial("Ruby", 2, 255, 75, 75, rubyAll);
	public static final GTMaterial Sapphire = new GTMaterial("Sapphire", 2, 75, 75, 200, sapphireAll);
	public static final GTMaterial Silicon = new GTMaterial(14, "Silicon", 60, 60, 80, dust, ingot);
	public static final GTMaterial Sodalite = new GTMaterial("Sodalite", 20, 20, 255, dust);
	public static final GTMaterial Sodium = new GTMaterial(11, "Sodium", 0, 38, 255, fluid);
	public static final GTMaterial Sulfur = new GTMaterial(16, "Sulfur", 200, 200, 0, dust);
	public static final GTMaterial Thorium = new GTMaterial(90, "Thorium", 2, 0, 30, 0, false, metalAll);
	public static final GTMaterial Technetium = new GTMaterial(43, "Technetium", 200, 200, 200, metalAll);
	public static final GTMaterial Titanium = new GTMaterial(22, "Titanium", 3, 170, 143, 222, metalAll);
	public static final GTMaterial Tritium = new GTMaterial("Tritium", 255, 0, 0, gas);
	public static final GTMaterial Tungsten = new GTMaterial(74, "Tungsten", 3, 50, 50, 50, metalAll);
	public static final GTMaterial Uranium = new GTMaterial("Uranium", 2, 50, 240, 50, dust);
	public static final GTMaterial Wood = new GTMaterial("Wood", 137, 103, 39, dust);
	/** Instance Members **/
	private int element;
	private String name, displayName;
	private int tier;
	private long mask;
	private Color color;
	private boolean smeltable;

	public GTMaterial(int element, String displayName, int r, int g, int b, GTMaterialFlag... flags) {
		this(element, displayName, 0, r, g, b, true, flags);
	}

	public GTMaterial(int element, String displayName, int tier, int r, int g, int b, GTMaterialFlag... flags) {
		this(element, displayName, tier, r, g, b, true, flags);
	}

	public GTMaterial(String displayName, int r, int g, int b, GTMaterialFlag... flags) {
		this(-1, displayName, 0, r, g, b, true, flags);
	}

	public GTMaterial(String displayName, int tier, int r, int g, int b, GTMaterialFlag... flags) {
		this(-1, displayName, tier, r, g, b, true, flags);
	}

	public GTMaterial(String displayName, int r, int g, int b, boolean smeltable, GTMaterialFlag... flags) {
		this(-1, displayName, 0, r, g, b, smeltable, flags);
	}

	/**
	 * @param element     - element number
	 * @param displayName - The name of the material
	 * @param r           - red value 0-255
	 * @param g           - green value 0-255
	 * @param b           - blue value 0-255
	 * @param smeltable   - Can it be smelted
	 * @param flags       - Types of items and blocks to generate from material
	 */
	public GTMaterial(int element, String displayName, int tier, int r, int g, int b, boolean smeltable,
			GTMaterialFlag... flags) {
		this.element = element;
		this.displayName = displayName;
		this.name = displayName.toLowerCase().replaceAll("-", "_").replaceAll(" ", "_");
		this.tier = tier;
		this.color = new Color(r, g, b, 255);
		this.smeltable = smeltable;
		for (GTMaterialFlag flag : flags) {
			mask |= flag.getMask();
		}
		generatedMap.put(name, this);
	}

	public static void removeMapEntries(String name) {
		generatedMap.remove(name);
	}

	public boolean hasFlag(GTMaterialFlag flag) {
		return (mask & flag.getMask()) != 0;
	}

	public GTMaterial addFlags(GTMaterialFlag... flags) {
		for (GTMaterialFlag flag : flags) {
			if (!this.hasFlag(flag)) {
				mask |= flag.getMask();
			}
		}
		return this;
	}

	public GTMaterial setElement(int element) {
		this.element = element;
		return this;
	}

	public GTMaterial setTier(int tier) {
		this.tier = tier;
		return this;
	}

	public GTMaterial setSmeltable(boolean smeltable) {
		this.smeltable = smeltable;
		return this;
	}

	/** Returns original name with upper case and special characters **/
	public String getDisplayName() {
		return displayName;
	}

	/** Returns lower case and formatted version of mat name **/
	public String getName() {
		return name;
	}

	public boolean getSmeltable() {
		return smeltable;
	}

	public Color getColor() {
		return color;
	}

	public int getElementNumber() {
		return element;
	}

	public int getTier() {
		return tier;
	}

	public static boolean isGem(GTMaterial mat) {
		return mat.hasFlag(GTMaterialFlag.RUBY) || mat.hasFlag(GTMaterialFlag.SAPPHIRE)
				|| mat.hasFlag(GTMaterialFlag.BLOCKGEM);
	}

	/** Map Get **/
	public static GTMaterial get(String name) {
		// This is where you could do error handling if you want
		return generatedMap.get(name);
	}

	/** Helper for looping **/
	public static Collection<GTMaterial> values() {
		return generatedMap.values();
	}

	public static boolean hasMaterial(String name) {
		return generatedMap.containsKey(name);
	}
}
