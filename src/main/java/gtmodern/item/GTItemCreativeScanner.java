package gtmodern.item;

import gtmodern.GTMod;
import gtmodern.tile.GTTileBaseMachine;
import gtmodern.tile.GTTileBlockCustom;
import gtmodern.tile.GTTileDigitalChest;
import gtmodern.tile.GTTileDrum;
import gtmodern.tile.multi.GTTileMultiBaseMachine;
import gtmodern.tile.multi.GTTileMultiBloomery;
import gtmodern.tile.multi.GTTileMultiCharcoalPit;
import gtmodern.tile.multi.GTTileMultiLightningRod;
import ic2.api.classic.item.IEUReader;
import ic2.api.energy.EnergyNet;
import ic2.api.item.ElectricItem;
import ic2.api.reactor.IReactor;
import ic2.api.tile.IEnergyStorage;
import ic2.core.IC2;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.block.base.tile.TileEntityElecMachine;
import ic2.core.block.base.tile.TileEntityElectricBlock;
import ic2.core.block.base.tile.TileEntityGeneratorBase;
import ic2.core.block.base.tile.TileEntityTransformer;
import ic2.core.block.crop.TileEntityCrop;
import ic2.core.block.personal.base.misc.IPersonalBlock;
import ic2.core.item.base.ItemBatteryBase;
import ic2.core.platform.registry.Ic2Sounds;
import ic2.core.platform.textures.Ic2Icons;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTItemCreativeScanner extends ItemBatteryBase implements IEUReader {

	public GTItemCreativeScanner() {
		super(0);
		this.setRightClick();
		setRegistryName("debug_scanner");
		setUnlocalizedName(GTMod.MODID + ".debug_scanner");
		setCreativeTab(GTMod.creativeTabGT);
		this.maxCharge = Integer.MAX_VALUE;
		this.transferLimit = Integer.MAX_VALUE;
		this.tier = 1;
		this.provider = true;
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}

	@Override
	public boolean isDamaged(ItemStack stack) {
		return true;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}

	@Override
	public boolean wantsToPlay(ItemStack stack) {
		return true;
	}

	@Override
	public ResourceLocation createSound(ItemStack stack) {
		return Ic2Sounds.batteryUse;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			ItemStack full = new ItemStack(this, 1, 0);
			ElectricItem.manager.charge(full, 2.147483647E9D, Integer.MAX_VALUE, true, false);
			items.add(full);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getTexture(ItemStack item) {
		return Ic2Icons.getTextures(GTMod.MODID + "_items")[8];
	}

	@Override
	public EnumRarity getRarity(ItemStack thisItem) {
		return EnumRarity.UNCOMMON;
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ, EnumHand hand) {
		return scanBlock(player, world, pos, side, hitX, hitY, hitZ, hand);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		return ActionResult.newResult(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
	}

	@Override
	public boolean isEUReader(ItemStack var1) {
		return true;
	}

	/*
	 * The logic for both the creative and survival scanners.
	 */
	@SuppressWarnings("deprecation")
	public static EnumActionResult scanBlock(EntityPlayer player, World world, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ, EnumHand hand) {
		TileEntity tileEntity = world.getTileEntity(pos);
		IBlockState state = world.getBlockState(pos);
		if (player.isSneaking() || !IC2.platform.isSimulating()) {
			return EnumActionResult.PASS;
		} else {
			IC2.platform.messagePlayer(player, "-----X: " + pos.getX() + " Y: " + pos.getY() + " Z: " + pos.getZ()
					+ " -----");
			IC2.platform.messagePlayer(player, "" + state.getBlock().getLocalizedName());
			IC2.platform.messagePlayer(player, "" + state.getBlock().getUnlocalizedName());
			IC2.platform.messagePlayer(player, "Hardness: " + state.getBlock().getBlockHardness(state, world, pos));
			IC2.platform.messagePlayer(player, "Blast Resistance: "
					+ state.getBlock().getExplosionResistance(null) * 5.0F);
			IC2.audioManager.playOnce(player, Ic2Sounds.scannerUse);
			if (tileEntity instanceof TileEntityBlock) {
				TileEntityBlock te = (TileEntityBlock) tileEntity;
				IC2.platform.messagePlayer(player, "Active: " + te.getActive());
				IC2.platform.messagePlayer(player, "Facing: " + te.getFacing());
			}
			if (tileEntity instanceof TileEntityGeneratorBase) {
				TileEntityGeneratorBase te2 = (TileEntityGeneratorBase) tileEntity;
				IC2.platform.messagePlayer(player, "Fuel: " + te2.fuel);
				IC2.platform.messagePlayer(player, "Storage: " + te2.storage + " EU");
			}
			if (tileEntity instanceof TileEntityElecMachine) {
				TileEntityElecMachine te3 = (TileEntityElecMachine) tileEntity;
				IC2.platform.messagePlayer(player, "Tier: " + te3.getTier());
				IC2.platform.messagePlayer(player, "Energy: " + te3.energy + " EU");
			}
			if (tileEntity instanceof IEnergyStorage) {
				IEnergyStorage te4 = (IEnergyStorage) tileEntity;
				IC2.platform.messagePlayer(player, "Stored: " + te4.getStored() + " EU");
			}
			if (tileEntity instanceof IReactor) {
				IReactor te5 = (IReactor) tileEntity;
				IC2.platform.messagePlayer(player, "Reactor Heat: " + te5.getHeat());
				IC2.platform.messagePlayer(player, "Max Heat: " + te5.getMaxHeat());
				IC2.platform.messagePlayer(player, "HEM: " + te5.getHeatEffectModifier());
				IC2.platform.messagePlayer(player, "Output: " + te5.getReactorEnergyOutput() + " EU");
			}
			if (tileEntity instanceof IPersonalBlock) {
				IPersonalBlock te6 = (IPersonalBlock) tileEntity;
				IC2.platform.messagePlayer(player, "Can Access: " + te6.canAccess(player.getUniqueID()));
			}
			if (tileEntity instanceof TileEntityCrop) {
				TileEntityCrop te7 = (TileEntityCrop) tileEntity;
				IC2.platform.messagePlayer(player, "Crop=" + te7.getCrop() + " Size=" + te7.getCurrentSize()
						+ " Growth=" + te7.getStatGrowth() + " Gain=" + te7.getStatGain() + " Resistance="
						+ te7.getStatResistance() + " Nutrients=" + te7.getTerrainNutrients() + " Water="
						+ te7.getTerrainHumidity() + " GrowthPoints=" + te7.getGrowthPoints());
			}
			if (tileEntity instanceof GTTileBaseMachine) {
				GTTileBaseMachine machine = (GTTileBaseMachine) tileEntity;
				IC2.platform.messagePlayer(player, "Progress: "
						+ (Math.round((machine.getProgress() / machine.getMaxProgress()) * 100)) + "%");
				if (!machine.isPassive) {
					IC2.platform.messagePlayer(player, "Default Input: " + machine.defaultEnergyConsume + " EU");
					IC2.platform.messagePlayer(player, "Max Input: " + machine.defaultMaxInput + " EU");
				}
			}
			if (tileEntity instanceof GTTileMultiBaseMachine) {
				GTTileMultiBaseMachine multi = (GTTileMultiBaseMachine) tileEntity;
				IC2.platform.messagePlayer(player, "Correct Strucuture: " + multi.checkStructure());
			}
			if (tileEntity instanceof GTTileMultiLightningRod) {
				GTTileMultiLightningRod rod = (GTTileMultiLightningRod) tileEntity;
				IC2.platform.messagePlayer(player, "Correct Strucuture: " + rod.checkStructure());
				IC2.platform.messagePlayer(player, "Casing Block Amount: "
						+ (rod.casingheight - (rod.getPos().getY() + 1)));
				IC2.platform.messagePlayer(player, "Casing Block Level: " + rod.casingheight);
				IC2.platform.messagePlayer(player, "Weather Height: " + world.getPrecipitationHeight(pos).getY());
				IC2.platform.messagePlayer(player, "Block Up Level: " + (rod.getPos().getY() + 1));
				IC2.platform.messagePlayer(player, "Storm Strength: " + ((int) (world.thunderingStrength) * 100) + "%");
				IC2.platform.messagePlayer(player, "1 out of " + rod.chance
						+ " chance to strike based on fence height");
			}
			if (tileEntity instanceof GTTileMultiBloomery) {
				GTTileMultiBloomery bloom = (GTTileMultiBloomery) tileEntity;
				IC2.platform.messagePlayer(player, "Correct Strucuture: " + bloom.checkStructure());
				IC2.platform.messagePlayer(player, "Progress: "
						+ (Math.round((bloom.getProgress() / bloom.getMaxProgress()) * 100)) + "%");
				IC2.platform.messagePlayer(player, "Recipe State: " + bloom.getActiveRecipe());
			}
			if (tileEntity instanceof GTTileMultiCharcoalPit) {
				GTTileMultiCharcoalPit pit = (GTTileMultiCharcoalPit) tileEntity;
				IC2.platform.messagePlayer(player, "Correct Strucuture: " + pit.checkStructure());
				IC2.platform.messagePlayer(player, "Progress: "
						+ (Math.round((pit.getProgress() / pit.getMaxProgress()) * 100)) + "%");
			}
			if (tileEntity instanceof GTTileDigitalChest) {
				GTTileDigitalChest chest = (GTTileDigitalChest) tileEntity;
				IC2.platform.messagePlayer(player, "Internal Count: " + chest.getQuantumCount());
			}
			if (tileEntity instanceof TileEntityElectricBlock) {
				TileEntityElectricBlock eu = (TileEntityElectricBlock) tileEntity;
				IC2.platform.messagePlayer(player, "Tier: " + eu.getTier());
				IC2.platform.messagePlayer(player, "Capacity: " + eu.getMaxEU() + " EU");
				IC2.platform.messagePlayer(player, "Output: " + eu.getOutput() + " EU");
			}
			if (tileEntity instanceof TileEntityTransformer) {
				TileEntityTransformer transformer = (TileEntityTransformer) tileEntity;
				IC2.platform.messagePlayer(player, "Low: " + transformer.lowOutput + " EU");
				IC2.platform.messagePlayer(player, "Low Tier: "
						+ EnergyNet.instance.getTierFromPower((double) transformer.lowOutput));
				IC2.platform.messagePlayer(player, "High: " + transformer.highOutput + " EU");
				IC2.platform.messagePlayer(player, "High Tier: "
						+ EnergyNet.instance.getTierFromPower((double) transformer.highOutput));
			}
			if (tileEntity instanceof GTTileBlockCustom) {
				GTTileBlockCustom blockcustom = (GTTileBlockCustom) tileEntity;
				IC2.platform.messagePlayer(player, "Stack Stored: " + blockcustom.getItem().getUnlocalizedName());
			}
			if (tileEntity instanceof GTTileDrum) {
				GTTileDrum tank = (GTTileDrum) tileEntity;
				if (!tank.isEmpty()) {
					IC2.platform.messagePlayer(player, tank.getFluidAmount() + "mB of " + tank.getFluidName());
				} else {
					IC2.platform.messagePlayer(player, "Drum is empty");
				}
				IC2.platform.messagePlayer(player, "Auto Output: " + tank.getExport());
			}
			return EnumActionResult.SUCCESS;
		}
	}
}
