package gtclassic.api.material;

import gtclassic.GTMod;

public class GTMaterialFlag {

	public static GTMaterialFlag DUST = new GTMaterialFlag("_dust", 0, false);
	public static GTMaterialFlag INGOT = new GTMaterialFlag("_ingot", 1, false);
	public static GTMaterialFlag INGOTHOT = new GTMaterialFlag("_ingothot", 1, true);
	public static GTMaterialFlag RUBY = new GTMaterialFlag("_gem", 3, false);
	public static GTMaterialFlag SAPPHIRE = new GTMaterialFlag("_gem", 4, false);
	public static GTMaterialFlag FLUID = new GTMaterialFlag("_fluid", -1, true);
	public static GTMaterialFlag GAS = new GTMaterialFlag("_gas", -1, true);
	public static GTMaterialFlag BLOCKMETAL = new GTMaterialFlag("_block", 14, false);
	public static GTMaterialFlag BLOCKGEM = new GTMaterialFlag("_block", 15, false);
	public static GTMaterialFlag NULL = new GTMaterialFlag("", -1, false);
	public static GTMaterialFlag MAGICDYE = new GTMaterialFlag("_magicdye", -1, true);
	private static int LAST_INTERNAL_ID;
	private long mask;
	private String suffix;
	private String texture;
	private int id;
	private boolean layered;
	private String modid;

	public GTMaterialFlag(String suffix, int id, boolean layered) {
		this(suffix, GTMod.MODID + "_materials", id, layered, GTMod.MODID);
	}

	public GTMaterialFlag(String suffix, String texture, int id, boolean layered, String modid) {
		if (LAST_INTERNAL_ID >= 63) {
			throw new IllegalArgumentException("GregTech Classic material flags overloaded! Limit is 64");
		}
		this.mask = 1L << LAST_INTERNAL_ID++;
		this.suffix = suffix;
		this.texture = texture;
		this.id = id;
		this.layered = layered;
		this.modid = modid;
	}

	public String getTexture() {
		return texture;
	}

	public long getMask() {
		return mask;
	}

	public String getSuffix() {
		return suffix;
	}

	public String getPrefix() {
		return suffix.replace("_", "");
	}

	public int getTextureID() {
		return id;
	}

	public boolean isLayered() {
		return layered;
	}

	public String getModID() {
		return this.modid;
	}
}
