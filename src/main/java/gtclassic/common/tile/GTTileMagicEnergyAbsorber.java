package gtclassic.common.tile;

import java.util.List;
import java.util.Random;

import gtclassic.api.helpers.GTHelperStack;
import gtclassic.api.helpers.GTUtility;
import gtclassic.api.helpers.PlayerXP;
import gtclassic.api.helpers.int3;
import gtclassic.api.interfaces.IGTDisplayTickTile;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.common.GTConfig;
import gtclassic.common.GTLang;
import gtclassic.common.container.GTContainerMagicEnergyAbsorber;
import ic2.api.classic.audio.PositionSpec;
import ic2.api.classic.energy.tile.IEnergySourceInfo;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.tile.machine.IEUStorage;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.IC2;
import ic2.core.RotationList;
import ic2.core.audio.AudioSource;
import ic2.core.block.base.tile.TileEntityMachine;
import ic2.core.block.base.util.info.misc.IEmitterTile;
import ic2.core.block.wiring.misc.EntityChargePadAuraFX;
import ic2.core.inventory.base.IHasGui;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.gui.GuiComponentContainer;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Sounds;
import ic2.core.util.misc.StackUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTTileMagicEnergyAbsorber extends TileEntityMachine implements ITickable, IHasGui, IEUStorage,
		IEmitterTile, IEnergySourceInfo, INetworkClientTileEntityEventListener, IGTDisplayTickTile {

	@NetworkField(index = 4)
	public int storage = 0;
	public int maxStorage = 1000000;
	public int production = 128;
	int slotInput = 0;
	int slotOutput = 1;
	boolean enet = false;
	public boolean potionMode = false;
	public boolean xpMode = false;
	@NetworkField(index = 5)
	public boolean portalMode = false;
	public boolean isAbovePortal = false;
	public AudioSource audioSource = null;
	private static final String NBT_ENERGYSTORED = "storage";
	private static final String NBT_POTIONMODE = "potionMode";
	private static final String NBT_XPMODE = "xpMode";
	private static final String NBT_PORTALMODE = "portalMode";
	private static final String NBT_ABOVEPORTAL = "isAbovePortal";

	public GTTileMagicEnergyAbsorber() {
		super(2);
		this.addNetworkFields(NBT_PORTALMODE);
		this.addGuiFields(NBT_POTIONMODE, NBT_XPMODE, NBT_ABOVEPORTAL);
	}

	@Override
	protected void addSlots(InventoryHandler handler) {
		handler.registerDefaultSideAccess(AccessRule.Both, RotationList.UP.invert());
		handler.registerDefaultSlotAccess(AccessRule.Import, 0);
		handler.registerDefaultSlotAccess(AccessRule.Export, 1);
		handler.registerDefaultSlotsForSide(RotationList.DOWN.invert(), 0);
		handler.registerDefaultSlotsForSide(RotationList.UP.invert(), 1);
		handler.registerSlotType(SlotType.Input, 0);
		handler.registerSlotType(SlotType.Output, 1);
	}

	@Override
	public LocaleComp getBlockName() {
		return GTLang.MAGIC_ENERGY_ABSORBER;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.storage = nbt.getInteger(NBT_ENERGYSTORED);
		this.potionMode = nbt.getBoolean(NBT_POTIONMODE);
		this.xpMode = nbt.getBoolean(NBT_XPMODE);
		this.portalMode = nbt.getBoolean(NBT_PORTALMODE);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger(NBT_ENERGYSTORED, this.storage);
		nbt.setBoolean(NBT_POTIONMODE, this.potionMode);
		nbt.setBoolean(NBT_XPMODE, this.xpMode);
		nbt.setBoolean(NBT_PORTALMODE, this.portalMode);
		return nbt;
	}

	@Override
	public void onLoaded() {
		super.onLoaded();
		if (this.isSimulating() && !this.enet) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			this.enet = true;
		}
	}

	@Override
	public void onUnloaded() {
		if (this.isSimulating() && this.enet) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			this.enet = false;
		}
		if (this.isRendering() && this.audioSource != null) {
			IC2.audioManager.removeSources(this);
			this.audioSource = null;
		}
		super.onUnloaded();
	}

	@Override
	public boolean canSetFacing(EntityPlayer player, EnumFacing facing) {
		return false;
	}

	@Override
	public boolean canRemoveBlock(EntityPlayer player) {
		return true;
	}

	@Override
	public boolean canInteractWith(EntityPlayer paramEntityPlayer) {
		return !this.isInvalid();
	}

	@Override
	public boolean hasGui(EntityPlayer paramEntityPlayer) {
		return true;
	}

	@Override
	public Class<? extends GuiScreen> getGuiClass(EntityPlayer var1) {
		return GuiComponentContainer.class;
	}

	@Override
	public ContainerIC2 getGuiContainer(EntityPlayer player) {
		return new GTContainerMagicEnergyAbsorber(player.inventory, this);
	}

	@Override
	public void onGuiClosed(EntityPlayer var1) {
	}

	@Override
	public void update() {
		checkForEndPortal();
		if (isConvertingToolItem() || isConvertingBookItem()) {
			return;
		}
		if (xpMode && isConvertingXP()) {
			this.setActive(true);
			return;
		}
		if (potionMode && isConvertingPotion()) {
			this.setActive(true);
			return;
		}
		if (portalMode && isDrawingEnergyFromADarkPlace()) {
			this.setActive(true);
			return;
		}
		this.setActive(false);
	}

	public boolean isConvertingToolItem() {
		ItemStack inputStack = this.getStackInSlot(slotInput);
		int level = GTHelperStack.getItemStackEnchantmentLevel(inputStack);
		if (level > 0 && !this.isFull()) {
			int generate = world.rand.nextInt(20000 * level);
			ItemStack blankTool = inputStack.copy();
			blankTool.getTagCompound().removeTag("ench");
			if (!GTHelperStack.canMerge(blankTool, this.getStackInSlot(slotOutput))) {
				return false;
			}
			this.addEnergy(generate);
			this.setStackInSlot(slotOutput, StackUtil.copyWithSize(blankTool, this.getStackInSlot(slotOutput).getCount()
					+ 1));
			inputStack.shrink(1);
			world.playSound((EntityPlayer) null, this.pos, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, 0.75F
					+ world.rand.nextFloat());
			return true;
		}
		return false;
	}

	public boolean isConvertingBookItem() {
		ItemStack inputStack = this.getStackInSlot(slotInput);
		int level = GTHelperStack.getBookEnchantmentLevel(inputStack);
		if (level > 0 && !this.isFull()) {
			int generate = world.rand.nextInt(20000 * level);
			ItemStack blankBook = GTMaterialGen.get(Items.BOOK);
			if (!GTHelperStack.canMerge(blankBook, this.getStackInSlot(slotOutput))) {
				return false;
			}
			this.addEnergy(generate);
			this.setStackInSlot(slotOutput, StackUtil.copyWithSize(blankBook, this.getStackInSlot(slotOutput).getCount()
					+ 1));
			inputStack.shrink(1);
			world.playSound((EntityPlayer) null, this.pos, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, 0.75F
					+ world.rand.nextFloat());
			return true;
		}
		return false;
	}

	public boolean isConvertingXP() {
		AxisAlignedBB area = new AxisAlignedBB(new int3(pos, getFacing()).up(1).asBlockPos());
		List<EntityPlayer> players = (world.getEntitiesWithinAABB(EntityPlayer.class, area));
		if (!players.isEmpty() && !this.isFull()) {
			EntityPlayer activePlayer = players.get(0);
			int playerXP = PlayerXP.getPlayerXP(activePlayer);
			if (playerXP <= 0) {
				return false;
			}
			if (!this.isFull()) {
				this.addEnergy(128);
				this.setActive(true);
				PlayerXP.addPlayerXP(activePlayer, -1);
				if (world.getTotalWorldTime() % 4 == 0) {
					world.playSound((EntityPlayer) null, this.pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.1F, 0.5F
							+ world.rand.nextFloat());
				}
				return true;
			}
		}
		return false;
	}

	public boolean isConvertingPotion() {
		AxisAlignedBB area = new AxisAlignedBB(new int3(pos, getFacing()).up(1).asBlockPos());
		List<EntityAreaEffectCloud> list = world.<EntityAreaEffectCloud>getEntitiesWithinAABB(EntityAreaEffectCloud.class, area);
		if (!list.isEmpty() && !this.isFull()) {
			this.addEnergy(128);
			return true;
		}
		return false;
	}

	private boolean isDrawingEnergyFromADarkPlace() {
		if (this.isAbovePortal && !this.isFull()) {
			this.addEnergy(128);
			return true;
		}
		return false;
	}

	private void checkForEndPortal() {
		if (world.getTotalWorldTime() % 100 == 0) {
			this.isAbovePortal = world.getBlockState(this.pos.offset(EnumFacing.DOWN)).getBlock() == Blocks.END_PORTAL;
			this.updateGui();
			if (this.portalMode && this.isAbovePortal) {
				if (world.rand.nextInt(512) == 0 && GTUtility.tryResetStrongholdPortal(world, pos)) {
					this.isAbovePortal = false;
					this.updateGui();
					return;
				}
				if (GTConfig.general.oneMagicAbsorberPerEndPortal
						&& GTUtility.tryExplodeOtherAbsorbers(this.world, this.pos)) {
					// TODO something here
				}
			}
		}
	}

	public boolean isFull() {
		return this.storage >= this.maxStorage;
	}

	public void addEnergy(int added) {
		if (added < 1) {
			return;
		}
		int newAmount = this.storage + added < this.maxStorage ? this.storage + added : this.maxStorage;
		this.storage = newAmount;
	}

	@Override
	public int getStoredEU() {
		return this.storage;
	}

	@Override
	public int getMaxEU() {
		return this.maxStorage;
	}

	@Override
	public int getOutput() {
		return this.production;
	}

	@Override
	public void drawEnergy(double amount) {
		this.storage = (int) ((double) this.storage - amount);
	}

	@Override
	public double getOfferedEnergy() {
		return (double) Math.min(this.storage, this.production);
	}

	@Override
	public int getSourceTier() {
		return 2;
	}

	public int getMaxSendingEnergy() {
		return this.production + 1;
	}

	@Override
	public boolean emitsEnergyTo(IEnergyAcceptor var1, EnumFacing facing) {
		return true;
	}

	public void updateGui() {
		this.getNetwork().updateTileGuiField(this, NBT_POTIONMODE);
		this.getNetwork().updateTileGuiField(this, NBT_XPMODE);
		this.getNetwork().updateTileEntityField(this, NBT_PORTALMODE);
		this.getNetwork().updateTileGuiField(this, NBT_ABOVEPORTAL);
	}

	@Override
	public void onNetworkUpdate(String field) {
		if (field.equals("isActive") && this.isActiveChanged()) {
			if (this.audioSource != null && this.audioSource.isRemoved()) {
				this.audioSource = null;
			}
			if (this.audioSource == null && this.getOperationSoundFile() != null) {
				this.audioSource = IC2.audioManager.createSource(this, PositionSpec.Center, this.getOperationSoundFile(), true, false, IC2.audioManager.defaultVolume);
			}
			if (this.getActive()) {
				if (this.audioSource != null) {
					this.audioSource.play();
				}
			} else if (this.audioSource != null) {
				this.audioSource.stop();
			}
		}
		super.onNetworkUpdate(field);
	}

	public ResourceLocation getOperationSoundFile() {
		return Ic2Sounds.generatorLoop;
	}

	@Override
	public void onNetworkEvent(EntityPlayer player, int event) {
		if (event == 1) {
			this.xpMode = !this.xpMode;
			this.setActive(false);
			this.updateGui();
		}
		if (event == 2) {
			this.potionMode = !this.potionMode;
			this.setActive(false);
			this.updateGui();
		}
		if (event == 3) {
			this.portalMode = !this.portalMode;
			this.setActive(false);
			this.updateGui();
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomTickDisplay(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (this.isActive && this.portalMode
				&& worldIn.getBlockState(this.pos.offset(EnumFacing.DOWN)).getBlock() == Blocks.END_PORTAL) {
			for (EnumFacing facing : EnumFacing.HORIZONTALS) {
				BlockPos sidePos = pos.offset(facing);
				if (world.getBlockState(sidePos).isFullBlock()) {
					continue;
				}
				for (int k = 3; k > 0; --k) {
					ParticleManager er = Minecraft.getMinecraft().effectRenderer;
					float multPos = (float) (.1 * 2) + 0.9F;
					double x = (double) ((float) sidePos.getX() + 0.05F + rand.nextFloat() * multPos);
					double y = (double) ((float) sidePos.getY() + 0.0F + rand.nextFloat() * 0.5F);
					double z = (double) ((float) sidePos.getZ() + 0.05F + rand.nextFloat() * multPos);
					double[] velocity = new double[] { 0.0D, 7.6D, 0.0D };
					if (k < 4) {
						velocity[2] *= 0.55D;
					}
					float foo = rand.nextFloat() * .25F;
					float[] colour = new float[] { 0.0F, foo, foo };
					er.addEffect(new EntityChargePadAuraFX(this.world, x, y, z, 8, velocity, colour, false));
				}
			}
		}
	}
}
