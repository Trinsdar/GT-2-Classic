package gtmodern;

import java.util.ArrayList;
import java.util.List;

import gtmodern.block.GTBlockBloom;
import gtmodern.block.GTBlockCasing;
import gtmodern.block.GTBlockCustom;
import gtmodern.block.GTBlockDrum;
import gtmodern.block.GTBlockFalling;
import gtmodern.block.GTBlockGlass;
import gtmodern.block.GTBlockMortar;
import gtmodern.block.GTBlockSluice;
import gtmodern.block.GTBlockSluiceBoxExt;
import gtmodern.block.GTBlockStone;
import gtmodern.block.GTBlockTileBasic;
import gtmodern.block.GTBlockTileCustom;
import gtmodern.block.GTBlockTileStorage;
import gtmodern.color.GTColorBlockInterface;
import gtmodern.color.GTColorItemBlock;
import gtmodern.fluid.GTFluidBlockDryable;
import gtmodern.itemblock.GTBlockBattery;
import gtmodern.itemblock.GTBlockDuctTape;
import gtmodern.itemblock.GTItemBlockInterface;
import gtmodern.itemblock.GTItemBlockRare;
import gtmodern.material.GTMaterial;
import gtmodern.material.GTMaterialFlag;
import gtmodern.material.GTMaterialGen;
import gtmodern.ore.GTOreFalling;
import gtmodern.ore.GTOreFlag;
import gtmodern.ore.GTOreRegistry;
import gtmodern.ore.GTOreStone;
import gtmodern.tile.GTTileBasicEnergyStorage;
import gtmodern.tile.GTTileBath;
import gtmodern.tile.GTTileBlockCustom;
import gtmodern.tile.GTTileBookshelf;
import gtmodern.tile.GTTileCentrifuge;
import gtmodern.tile.GTTileComputerCube;
import gtmodern.tile.GTTileDigitalChest;
import gtmodern.tile.GTTileDigitalTransformer;
import gtmodern.tile.GTTileDrum;
import gtmodern.tile.GTTileElectrolyzer;
import gtmodern.tile.GTTileFacing;
import gtmodern.tile.GTTileHeatingElement;
import gtmodern.tile.GTTileLargeChest;
import gtmodern.tile.GTTilePlayerDetector;
import gtmodern.tile.GTTileQuantumEnergyStorage;
import gtmodern.tile.GTTileRoaster;
import gtmodern.tile.GTTileShredder;
import gtmodern.tile.GTTileSluice;
import gtmodern.tile.GTTileSmallChest;
import gtmodern.tile.GTTileSmelter;
import gtmodern.tile.GTTileSolarPanel;
import gtmodern.tile.GTTileSuperConductorHigh;
import gtmodern.tile.GTTileSuperConductorLow;
import gtmodern.tile.GTTileWorkbench;
import gtmodern.tile.multi.GTTileMultiBlastFurnace;
import gtmodern.tile.multi.GTTileMultiBloomery;
import gtmodern.tile.multi.GTTileMultiCharcoalPit;
import gtmodern.tile.multi.GTTileMultiChemicalReactor;
import gtmodern.tile.multi.GTTileMultiCryogenicSeparator;
import gtmodern.tile.multi.GTTileMultiFusion;
import gtmodern.tile.multi.GTTileMultiLeadChamber;
import gtmodern.tile.multi.GTTileMultiLightningRod;
import gtmodern.tile.multi.GTTileMultiRefractory;
import ic2.core.IC2;
import ic2.core.item.block.ItemBlockRare;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GTBlocks {

	private GTBlocks() {
		throw new IllegalStateException("Utility class");
	}

	static final List<Block> toRegister = new ArrayList<>();
	public static final GTBlockBloom bloomIron = registerBlock(new GTBlockBloom(GTMaterial.RefinedIron, 3)),
			bloomBronze = registerBlock(new GTBlockBloom(GTMaterial.Bronze, 4)),
			bloomRedAlloy = registerBlock(new GTBlockBloom(GTMaterial.RedAlloy, 1)),
			bloomInvar = registerBlock(new GTBlockBloom(GTMaterial.Invar, 3)),
			bloomBrass = registerBlock(new GTBlockBloom(GTMaterial.Brass, 4)),
			bloomElectrum = registerBlock(new GTBlockBloom(GTMaterial.Electrum, 2)),
			bloomConstantan = registerBlock(new GTBlockBloom(GTMaterial.Constantan, 2)),
			bloomBismuthBronze = registerBlock(new GTBlockBloom(GTMaterial.BismuthBronze, 4));
	public static final GTBlockStone stoneMagnesia = registerBlock(new GTBlockStone("Magnesia", 16, 20.0F, 2));
	public static final GTBlockFalling sandSlag = registerBlock(new GTBlockFalling("Slag", 17)),
			sandSlagrete = registerBlock(new GTBlockFalling("Slagcrete", 18)),
			sandCharcoalAsh = registerBlock(new GTBlockFalling("Charcoal", 20));
	public static final GTBlockGlass glassSlag = registerBlock(new GTBlockGlass("Slag", 19));
	public static final GTBlockCasing casingSuperConductor = registerBlock(new GTBlockCasing("Superconductor", 0)),
			casingFusion = registerBlock(new GTBlockCasing("Fusion", 1)),
			casingFission = registerBlock(new GTBlockCasing("Fission", 2)),
			casingPlastic1x = registerBlock(new GTBlockCasing("Plastic1", 3)),
			casingPlastic4x = registerBlock(new GTBlockCasing("Plastic4", 4)),
			casingPlastic16x = registerBlock(new GTBlockCasing("Plastic16", 5)),
			casingLightning = registerBlock(new GTBlockCasing("Lightning", 8));
	public static final GTBlockSluice tileSluice = registerBlock(new GTBlockSluice());
	public static final GTBlockSluiceBoxExt tileSluiceExt = registerBlock(new GTBlockSluiceBoxExt());
	public static final GTBlockTileBasic tileHeating = registerBlock(new GTBlockTileBasic("machine_heatingelement")),
			tileBloomery = registerBlock(new GTBlockTileBasic("machine_bloomery", 5)),
			tileCharcoalPit = registerBlock(new GTBlockTileBasic("machine_charcoalpit", 4)),
			tileBlastFurnace = registerBlock(new GTBlockTileBasic("machine_blastfurnace_lv", 3)),
			tileLeadChamber = registerBlock(new GTBlockTileBasic("machine_leadchamber_lv")),
			tileChargeOmat = registerBlock(new GTBlockTileBasic("machine_chargeomat_ev")),
			tileComputer = registerBlock(new GTBlockTileBasic("machine_computercube_ev")),
			tileCentrifuge = registerBlock(new GTBlockTileBasic("machine_industrialcentrifuge_lv")),
			tileSmelter = registerBlock(new GTBlockTileBasic("machine_electricsmelter_lv")),
			tileFluidSmelter = registerBlock(new GTBlockTileBasic("machine_fluidsmelter_lv")),
			tileRoaster = registerBlock(new GTBlockTileBasic("machine_roaster_lv")),
			tileShredder = registerBlock(new GTBlockTileBasic("machine_shredder_mv")),
			tileBath = registerBlock(new GTBlockTileBasic("machine_bath")),
			tileElectrolyzer = registerBlock(new GTBlockTileBasic("machine_industrialelectrolyzer_hv", 1)),
			tileCryogenicSeparator = registerBlock(new GTBlockTileBasic("machine_cryogenicseparator_mv")),
			tileConstructor = registerBlock(new GTBlockTileBasic("machine_constructor_mv")),
			tileCompiler = registerBlock(new GTBlockTileBasic("machine_compiler_hv")),
			tileChemicalReactor = registerBlock(new GTBlockTileBasic("machine_chemicalreactor_hv")),
			tileRefractory = registerBlock(new GTBlockTileBasic("machine_refractory_hv")),
			tileFabricator = registerBlock(new GTBlockTileBasic("machine_matterfabricator_ev")),
			tileReplicator = registerBlock(new GTBlockTileBasic("machine_matterreplicator_ev")),
			tileDigitalChest = registerBlock(new GTBlockTileBasic("tile_digitalchest")),
			tilePlayerDetector = registerBlock(new GTBlockTileBasic("machine_playerdetector_lv", 1)),
			tileFusion = registerBlock(new GTBlockTileBasic("machine_fusioncomputer_ev")),
			tileLightningRod = registerBlock(new GTBlockTileBasic("machine_lightningrod_iv")),
			tileQuantumEnergy = registerBlock(new GTBlockTileBasic("machine_quantumenergystorage_ev")),
			tileBasicEnergy = registerBlock(new GTBlockTileBasic("machine_basicenergystorage_ev")),
			tileDigitalTransformer = registerBlock(new GTBlockTileBasic("machine_digitaltransformer_luv")),
			tileCableEnergium = registerBlock(new GTBlockTileBasic("cable_energium_luv", 1)),
			tileCableLapotron = registerBlock(new GTBlockTileBasic("cable_lapotron_zpm"));
	public static final GTBlockDrum drum = registerBlock(new GTBlockDrum(GTMaterial.StainlessSteel));
	public static final GTBlockCustom driedResin = registerBlock(new GTBlockCustom("Resin", 0, 12, 1)),
			driedTailings = registerBlock(new GTBlockCustom("Tailings", 21, 16, 2)),
			driedBrine = registerBlock(new GTBlockCustom("Brine", 22, 16, 2)),
			driedLithium = registerBlock(new GTBlockCustom("Lithium", 23, 16, 2));
	public static final GTBlockMortar mortar = registerBlock(new GTBlockMortar());
	public static final GTBlockDuctTape tape = registerBlock(new GTBlockDuctTape("block_ducttape", 10, 4, false));
	public static final GTBlockTileCustom solarPanel = registerBlock(new GTBlockTileCustom("solar_panel", 16, 2, false)),
			coolantHeliumSmall = registerBlock(new GTBlockTileCustom("coolant_helium_small", 5, 13, false)),
			coolantHeliumMed = registerBlock(new GTBlockTileCustom("coolant_helium_med", 13, 5, false)),
			coolantHeliumLarge = registerBlock(new GTBlockTileCustom("coolant_helium_large", 13, 5, false)),
			rodThoriumSmall = registerBlock(new GTBlockTileCustom("rod_thorium_small", 3, 10, true)),
			rodThoriumMed = registerBlock(new GTBlockTileCustom("rod_thorium_med", 4, 10, true)),
			rodThoriumLarge = registerBlock(new GTBlockTileCustom("rod_thorium_large", 5, 10, true)),
			rodPlutoniumSmall = registerBlock(new GTBlockTileCustom("rod_plutonium_small", 3, 10, true)),
			rodPlutoniumMed = registerBlock(new GTBlockTileCustom("rod_plutonium_med", 4, 10, true)),
			rodPlutoniumLarge = registerBlock(new GTBlockTileCustom("rod_plutonium_large", 5, 10, true));
	public static final GTBlockBattery batteryLithiumSmall = registerBlock(new GTBlockBattery("battery_lithium_small", 6, 11, false, 100000, 128, 1)),
			batteryLithiumMed = registerBlock(new GTBlockBattery("battery_lithium_med", 8, 11, false, 200000, 256, 2)),
			batteryLithiumLarge = registerBlock(new GTBlockBattery("battery_lithium_large", 10, 11, false, 400000, 512, 3)),
			batteryEnergiumTiny = registerBlock(new GTBlockBattery("battery_energium_tiny", 6, 6, true, 100000, 256, 2)),
			batteryEnergiumSmall = registerBlock(new GTBlockBattery("battery_energium_small", 8, 8, true, 1000000, 512, 3)),
			batteryEnergiumMed = registerBlock(new GTBlockBattery("battery_energium_med", 10, 10, true, 10000000, 1024, 4)),
			batteryEnergiumLarge = registerBlock(new GTBlockBattery("battery_energium_large", 12, 12, true, 100000000, 4096, 5)),
			batteryEnergiumHuge = registerBlock(new GTBlockBattery("battery_energium_huge", 14, 14, true, 1000000000, 8192, 6)),
			batteryLapotronTiny = registerBlock(new GTBlockBattery("battery_lapotron_tiny", 6, 6, true, 1000000, 1024, 3)),
			batteryLapotronSmall = registerBlock(new GTBlockBattery("battery_lapotron_small", 8, 8, true, 10000000, 4096, 4)),
			batteryLapotronMed = registerBlock(new GTBlockBattery("battery_lapotron_med", 10, 10, true, 100000000, 8192, 5)),
			batteryLapotronLarge = registerBlock(new GTBlockBattery("battery_lapotron_large", 12, 12, true, 1000000000, 16384, 6)),
			batteryLapotronHuge = registerBlock(new GTBlockBattery("battery_lapotron_huge", 14, 14, true, Integer.MAX_VALUE, 32768, 7));
	public static final GTFluidBlockDryable fluidTailings = registerBlock(new GTFluidBlockDryable(GTMaterial.BauxiteTailings, GTBlocks.driedTailings)),
			fluidBrine = registerBlock(new GTFluidBlockDryable(GTMaterial.Brine, GTBlocks.driedBrine)),
			fluidLithium = registerBlock(new GTFluidBlockDryable(GTMaterial.LithiumCarbonate, GTBlocks.driedLithium));
	protected static final String[] textureTileBasic = { "machine_heatingelement", "machine_bloomery",
			"machine_charcoalpit", "machine_blastfurnace_lv", "machine_leadchamber_lv", "machine_bath",
			"machine_chemicalreactor_hv", "machine_refractory_hv", "machine_roaster_lv", "machine_constructor_mv",
			"machine_compiler_hv", "machine_cryogenicseparator_mv", "machine_chargeomat_ev", "machine_computercube_ev",
			"machine_industrialcentrifuge_lv", "machine_industrialelectrolyzer_hv", "machine_shredder_mv",
			"machine_electricsmelter_lv", "machine_fluidsmelter_lv", "machine_matterfabricator_ev",
			"machine_matterreplicator_ev", "machine_playerdetector_lv", "machine_fusioncomputer_ev",
			"machine_lightningrod_iv", "machine_quantumenergystorage_ev", "machine_basicenergystorage_ev",
			"machine_digitaltransformer_luv", "cable_energium_luv", "cable_lapotron_zpm", "tile_digitalchest",
			"tile_smallchest", "tile_largechest", "tile_bookshelf", "tile_workbench", "tile_drum" };
	protected static final String[] textureTileCustom = { "block_mortar", "block_ducttape", "solar_panel",
			"coolant_helium_small", "coolant_helium_med", "coolant_helium_large", "rod_thorium_small",
			"rod_thorium_med", "rod_thorium_large", "rod_plutonium_small", "rod_plutonium_med", "rod_plutonium_large",
			"battery_lithium_small", "battery_lithium_med", "battery_lithium_large", "battery_lapotron_tiny",
			"battery_lapotron_small", "battery_lapotron_med", "battery_lapotron_large", "battery_lapotron_huge",
			"battery_energium_tiny", "battery_energium_small", "battery_energium_med", "battery_energium_large",
			"battery_energium_huge", };

	public static void registerBlocks() {
		for (Block block : GTMaterialGen.blockMap.values()) {
			createBlock(block);
		}
		registerStorageBlocks();
		registerOreBlocks();
		for (Block block : toRegister) {
			createBlock(block);
		}
	}

	static <T extends Block> T registerBlock(T block) {
		toRegister.add(block);
		return block;
	}

	public static void createBlock(Block block) {
		IC2.getInstance().createBlock(block, getItemBlock(block));
	}

	static Class<? extends ItemBlockRare> getItemBlock(Block block) {
		if (block instanceof GTItemBlockInterface) {
			return ((GTItemBlockInterface) block).getCustomItemBlock();
		}
		if (block instanceof GTColorBlockInterface) {
			return GTColorItemBlock.class;
		}
		return GTItemBlockRare.class;
	}

	public static void registerOreBlocks() {
		for (GTOreRegistry ore : GTOreRegistry.values()) {
			createBlock(new GTOreStone(ore, GTOreFlag.STONE));
		}
		for (GTOreRegistry ore : GTOreRegistry.values()) {
			createBlock(new GTOreStone(ore, GTOreFlag.NETHER));
		}
		for (GTOreRegistry ore : GTOreRegistry.values()) {
			createBlock(new GTOreStone(ore, GTOreFlag.END));
		}
		for (GTOreRegistry ore : GTOreRegistry.values()) {
			createBlock(new GTOreFalling(ore, GTOreFlag.SAND));
		}
		for (GTOreRegistry ore : GTOreRegistry.values()) {
			createBlock(new GTOreFalling(ore, GTOreFlag.GRAVEL));
		}
		for (GTOreRegistry ore : GTOreRegistry.values()) {
			createBlock(new GTOreStone(ore, GTOreFlag.BEDROCK));
		}
	}

	public static void registerStorageBlocks() {
		for (GTMaterial mat : GTMaterial.values()) {
			if (mat.hasFlag(GTMaterialFlag.CASING) && !mat.equals(GTMaterial.Copper) && !mat.equals(GTMaterial.Tin)
					&& !mat.equals(GTMaterial.Zinc)) {
				createBlock(new GTBlockTileStorage(mat, 0));
				createBlock(new GTBlockTileStorage(mat, 1));
				createBlock(new GTBlockTileStorage(mat, 2));
				createBlock(new GTBlockTileStorage(mat, 3));
			}
		}
	}

	public static void registerTiles() {
		registerUtil(GTTileBlockCustom.class, "CustomBlock");
		registerUtil(GTTileSluice.class, "Sluice");
		registerUtil(GTTileBath.class, "Bath");
		registerUtil(GTTileMultiLeadChamber.class, "LeadChamber");
		registerUtil(GTTileHeatingElement.class, "HeatingElement");
		registerUtil(GTTileMultiBloomery.class, "Bloomery");
		registerUtil(GTTileMultiCharcoalPit.class, "CharcoalPit");
		registerUtil(GTTileCentrifuge.class, "IndustrialCentrifuge");
		registerUtil(GTTileRoaster.class, "Roaster");
		registerUtil(GTTileShredder.class, "Shredder");
		registerUtil(GTTileElectrolyzer.class, "IndustrialElectrolyzer");
		registerUtil(GTTileSmelter.class, "ElectricSmelter");
		registerUtil(GTTileMultiCryogenicSeparator.class, "CyrogenicSeparator");
		registerUtil(GTTilePlayerDetector.class, "PlayerDetector");
		registerUtil(GTTileMultiChemicalReactor.class, "ChemicalReactor");
		registerUtil(GTTileMultiRefractory.class, "ArcFurnace");
		registerUtil(GTTileMultiBlastFurnace.class, "BlastFurnace");
		registerUtil(GTTileComputerCube.class, "ComputerCube");
		registerUtil(GTTileDigitalTransformer.class, "DigitalTransformer");
		registerUtil(GTTileBasicEnergyStorage.class, "BasicEnergy");
		registerUtil(GTTileQuantumEnergyStorage.class, "QuantumEnergy");
		registerUtil(GTTileMultiLightningRod.class, "LightningRod");
		registerUtil(GTTileMultiFusion.class, "FusionComputer");
		registerUtil(GTTileSuperConductorLow.class, "SuperConductorLow");
		registerUtil(GTTileSuperConductorHigh.class, "SuperConductorHigh");
		registerUtil(GTTileSmallChest.class, "SmallChest");
		registerUtil(GTTileLargeChest.class, "LargeChest");
		registerUtil(GTTileDigitalChest.class, "QuantumChest");
		registerUtil(GTTileBookshelf.class, "Bookshelf");
		registerUtil(GTTileWorkbench.class, "Workbench");
		registerUtil(GTTileFacing.class, "Facing");
		registerUtil(GTTileSolarPanel.class, "SolarPanel");
		registerUtil(GTTileDrum.class, "Tank");
	}

	// TODO fix the generic type
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void registerUtil(Class tile, String name) {
		GameRegistry.registerTileEntity(tile, new ResourceLocation(GTMod.MODID, "tileEntity" + name));
	}
}