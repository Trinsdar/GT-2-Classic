package gtmodern.tool;

import com.google.common.collect.ImmutableMap;

import gtmodern.material.GTMaterial;
import gtmodern.material.GTMaterialGen;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class GTToolMaterial {

	/*
	 * This where materials for tools specifically - are handled, although it seems
	 * weird to have a second smaller materials list, this ensures integration with
	 * vanilla and other mods using the tool classes and more control over tool
	 * values. So quit bitchin.
	 */
	static GTMaterial m;
	// TODO add flint tools
	public static final ToolMaterial Flint = createToolMaterial(GTMaterial.Flint);
	public static final ToolMaterial Bronze = createToolMaterial(GTMaterial.Bronze);
	public static final ToolMaterial BismuthBronze = createToolMaterial(GTMaterial.BismuthBronze);
	public static final ToolMaterial Gold = createToolMaterial(GTMaterial.Gold);
	public static final ToolMaterial Iron = createToolMaterial(GTMaterial.Iron);
	public static final ToolMaterial Diamond = createToolMaterial(GTMaterial.Diamond);
	public static final ToolMaterial Tungsten = createToolMaterial(GTMaterial.Tungsten);
	public static final ToolMaterial Invar = createToolMaterial(GTMaterial.Invar);
	public static final ToolMaterial Silver = createToolMaterial(GTMaterial.Silver);
	public static final ToolMaterial Steel = createToolMaterial(GTMaterial.Steel);
	public static final ToolMaterial Electrum = createToolMaterial(GTMaterial.Electrum);
	public static final ToolMaterial Constantan = createToolMaterial(GTMaterial.Constantan);
	public static final ToolMaterial Platinum = createToolMaterial(GTMaterial.Platinum);
	public static final ToolMaterial Iridium = createToolMaterial(GTMaterial.Iridium);
	public static final ToolMaterial Chrome = createToolMaterial(GTMaterial.Chrome);
	public static final ToolMaterial Brass = createToolMaterial(GTMaterial.Brass);
	public static final ToolMaterial Plutonium = createToolMaterial(GTMaterial.Plutonium);
	public static final ToolMaterial RefinedIron = createToolMaterial(GTMaterial.RefinedIron);
	public static final ToolMaterial Tungstensteel = createToolMaterial(GTMaterial.TungstenSteel);
	public static final ToolMaterial Titanium = createToolMaterial(GTMaterial.Titanium);
	public static final ToolMaterial Nickel = createToolMaterial(GTMaterial.Nickel);
	public static final ToolMaterial Nichrome = createToolMaterial(GTMaterial.Nichrome);
	public static final ToolMaterial Osmium = createToolMaterial(GTMaterial.Osmium);
	public static final ToolMaterial Tantalum = createToolMaterial(GTMaterial.Tantalum);
	public static final ToolMaterial Cobalt = createToolMaterial(GTMaterial.Cobalt);
	public static final ToolMaterial Ultimet = createToolMaterial(GTMaterial.Ultimet);
	public static final ToolMaterial StainlessSteel = createToolMaterial(GTMaterial.StainlessSteel);
	/*
	 * This is the array that you can iterate to make new tools
	 */
	public static final ToolMaterial[] toolMaterial = { Flint, Bronze, Iron, Gold, Diamond, Tungsten, Invar, Silver,
			Steel, Electrum, Constantan, Platinum, Iridium, Chrome, Brass, Plutonium, RefinedIron, Tungstensteel,
			Titanium, Nickel, Nichrome, Osmium, Tantalum, BismuthBronze, Cobalt, Ultimet, StainlessSteel };
	/*
	 * This is map to allow access to a GTMaterial equivalent of the Tool Material
	 */
	public static final ImmutableMap<ToolMaterial, GTMaterial> MAT_REG = ImmutableMap.<ToolMaterial, GTMaterial>builder().put(Tungsten, GTMaterial.Tungsten).put(Invar, GTMaterial.Invar).put(Silver, GTMaterial.Silver).put(Steel, GTMaterial.Steel).put(Electrum, GTMaterial.Electrum).put(Constantan, GTMaterial.Constantan).put(Platinum, GTMaterial.Platinum).put(Iridium, GTMaterial.Iridium).put(Chrome, GTMaterial.Chrome).put(Brass, GTMaterial.Brass).put(Plutonium, GTMaterial.Plutonium).put(RefinedIron, GTMaterial.RefinedIron).put(Tungstensteel, GTMaterial.TungstenSteel).put(Titanium, GTMaterial.Titanium).put(Nickel, GTMaterial.Nickel).put(Nichrome, GTMaterial.Nichrome).put(Osmium, GTMaterial.Osmium).put(Tantalum, GTMaterial.Tantalum).put(Flint, GTMaterial.Flint).put(BismuthBronze, GTMaterial.BismuthBronze).put(Cobalt, GTMaterial.Cobalt).put(Ultimet, GTMaterial.Ultimet).put(StainlessSteel, GTMaterial.StainlessSteel).put(Bronze, GTMaterial.Bronze).put(Gold, GTMaterial.Gold).put(Iron, GTMaterial.Iron).put(Diamond, GTMaterial.Diamond).build();

	public static GTMaterial getGTMaterial(ToolMaterial tmat) {
		return MAT_REG.get(tmat);
	}

	public static String n(GTMaterial mat) {
		// creates the display name for the tool material enum
		return mat.getDisplayName();
	}

	public static int l(GTMaterial mat) {
		// gets the level from the gt mat
		return mat.getLevel();
	}

	public static int d(GTMaterial mat) {
		// gets durability from the gt mat
		return mat.getDurability();
	}

	public static float s(GTMaterial mat) {
		// gets speed from the gt mat
		return mat.getSpeed();
	}

	public static float a(GTMaterial mat) {
		// creates attack damage from speed
		return (mat.getSpeed() / 2.0F) + (mat.getDurability() / 1000);
	}

	public static int e(GTMaterial mat) {
		// creates enchantment level from tool level
		return mat.getLevel() * 7;
	}

	public static ToolMaterial createToolMaterial(GTMaterial mat) {
		// takes everything above and creates the tool material enum entry
		return EnumHelper.addToolMaterial(n(mat), l(mat), d(mat), s(mat), a(mat), e(mat)).setRepairItem(GTMaterialGen.getIngot(mat, 1));
	}
}
