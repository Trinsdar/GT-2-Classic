package gtclassic.tileentity;

import gtclassic.GTClassic;
import gtclassic.container.GTContainerIndustrialCentrifuge;
import gtclassic.util.GTItems;
import ic2.api.classic.audio.PositionSpec;
import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.recipe.INullableRecipeInput;
import ic2.api.classic.recipe.machine.IMachineRecipeList;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.classic.tile.IMachine;
import ic2.api.classic.tile.machine.IProgressMachine;
import ic2.api.energy.EnergyNet;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.recipe.IRecipeInput;
import ic2.core.IC2;
import ic2.core.RotationList;
import ic2.core.audio.AudioSource;
import ic2.core.block.base.tile.TileEntityElecMachine;
import ic2.core.block.base.util.comparator.ComparatorManager;
import ic2.core.block.base.util.comparator.comparators.ComparatorProgress;
import ic2.core.block.base.util.info.EnergyUsageInfo;
import ic2.core.block.base.util.info.ProgressInfo;
import ic2.core.block.base.util.info.misc.IEnergyUser;
import ic2.core.block.machine.recipes.managers.BasicMachineRecipeList;
import ic2.core.inventory.base.IHasGui;
import ic2.core.inventory.base.IHasInventory;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.*;
import ic2.core.inventory.gui.GuiComponentContainer;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.inventory.transport.wrapper.RangedInventoryWrapper;
import ic2.core.item.recipe.entry.RecipeInputItemStack;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.platform.lang.components.base.LangComponentHolder;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.registry.Ic2Sounds;
import ic2.core.util.helpers.FilteredList;
import ic2.core.util.misc.StackUtil;
import ic2.core.util.obj.IOutputMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class GTTileEntityIndustrialCentrifuge extends TileEntityElecMachine implements ITickable, IProgressMachine, IMachine, IOutputMachine, IHasGui, INetworkTileEntityEventListener, IEnergyUser {
    @NetworkField(index = 7)
    public float progress = 0.0F;

    public int defaultEnergyConsume;
    public int defaultOperationLength;
    public int defaultMaxInput;
    public int defaultEnergyStorage;
    public int energyConsume;
    public int operationLength;
    public float progressPerTick;

    @NetworkField(index = 8)
    public float soundLevel = 1.0F;

    public IMachineRecipeList.RecipeEntry lastRecipe;

    @NetworkField(index = 9)
    public int recipeOperation;

    @NetworkField(index = 10)
    public int recipeEnergy;

    @NetworkField(index = 11)
    public boolean redstoneInverted;

    @NetworkField(index = 12)
    public boolean redstoneSensitive;

    public boolean defaultSensitive;
    public List<ItemStack> results = new ArrayList();
    public AudioSource audioSource;
    public IFilter filter;

    public static final int slotInput = 0;
    public static final int slotCell = 1;
    public static final int slotFuel = 2;
    public static final int slotOutput = 3;
    public static final int slotOutput2 = 4;
    public static final int slotOutput3 = 5;
    public static final int slotOutput4 = 6;

    public GTTileEntityIndustrialCentrifuge() {
        this(11, 1, 98, 32);
    }

    public GTTileEntityIndustrialCentrifuge(int slots, int energyPerTick, int maxProgress, int maxInput)
    {
        super(slots, maxInput);
        this.setFuelSlot(slotFuel);
        this.setCustomName("tileIndustrialCentrifuge");
        this.energyConsume = energyPerTick;
        this.defaultEnergyConsume = energyPerTick;
        this.operationLength = maxProgress;
        this.defaultOperationLength = maxProgress;
        this.defaultMaxInput = this.maxInput;
        this.defaultEnergyStorage = energyPerTick * maxProgress;
        this.defaultSensitive = false;
        this.addNetworkFields("soundLevel", "redstoneInverted", "redstoneSensitive");
        this.addGuiFields("recipeOperation", "recipeEnergy", "progress");
        this.addInfos(new EnergyUsageInfo(this), new ProgressInfo(this));
    }

    public ResourceLocation getGuiTexture() {
        return new ResourceLocation(GTClassic.MODID, "textures/gui/industrialcentrifuge.png");
    }

    @Override
    public LocaleComp getBlockName() {
        return new LangComponentHolder.LocaleBlockComp("tile.industrialCentrifuge");
    }
    public static IMachineRecipeList industrialCentrifuge = new BasicMachineRecipeList("industrialCentrifuge");

    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GuiComponentContainer.class;
    }

    public IMachineRecipeList.RecipeEntry getOutputFor(ItemStack input) {
        return industrialCentrifuge.getRecipeInAndOutput(input, false);
    }

    @Override
    protected void addComparators(ComparatorManager manager)
    {
        super.addComparators(manager);
        manager.addComparatorMode(new ComparatorProgress(this));
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        this.filter = new MachineFilter(this);
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
        handler.registerDefaultSlotAccess(AccessRule.Both, slotFuel);
        handler.registerDefaultSlotAccess(AccessRule.Import, slotInput, slotCell);
        handler.registerDefaultSlotAccess(AccessRule.Export, slotOutput, slotOutput2, slotOutput3, slotOutput4);
        handler.registerDefaultSlotsForSide(RotationList.UP, slotInput);
        handler.registerDefaultSlotsForSide(RotationList.DOWN, slotFuel);
        handler.registerDefaultSlotsForSide(RotationList.HORIZONTAL, slotCell);
        handler.registerDefaultSlotsForSide(RotationList.HORIZONTAL, slotOutput, slotOutput2, slotOutput3, slotOutput4);
        handler.registerInputFilter(new ArrayFilter(CommonFilters.DischargeEU, new BasicItemFilter(Items.REDSTONE), new BasicItemFilter(Ic2Items.suBattery)), slotFuel);
        handler.registerOutputFilter(CommonFilters.NotDischargeEU, slotFuel);
        handler.registerSlotType(SlotType.Fuel, slotFuel);
        handler.registerSlotType(SlotType.Input, slotInput);
        handler.registerSlotType(SlotType.SecondInput, slotCell);
        handler.registerSlotType(SlotType.Output, slotOutput, slotOutput2, slotOutput3, slotOutput4);
    }

    @Override
    public int getEnergyUsage()
    {
        return this.recipeEnergy;
    }

    @Override
    public float getProgress()
    {
        return this.progress;
    }

    @Override
    public float getMaxProgress()
    {
        return (float) this.recipeOperation;
    }

    public ResourceLocation getStartSoundFile() {
        return Ic2Sounds.electricFurnaceLoop;
    }

    public ResourceLocation getInterruptSoundFile() {
        return Ic2Sounds.electricFurnaceStop;
    }

    @Override
    public double getWrenchDropRate() {
        return 0.8500000238418579D;
    }

    public boolean isValidInput(ItemStack par1) {
        return false;
    }

    @Override
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return new LinkedHashSet(Arrays.asList(IMachineUpgradeItem.UpgradeType.values()));
    }

    public boolean canWorkWithoutItems()
    {
        return false;
    }

    @Override
    public boolean supportsNotify() {
        return true;
    }

    @Override
    public boolean needsInitialRedstoneUpdate()
    {
        return true;
    }

    @Override
    public String getName() {
        return "Industrial Centrifuge";
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    public IMachineRecipeList getRecipeList() {
        return industrialCentrifuge;
    }

    @Override
    public void update()
    {
        this.handleChargeSlot(500);
        this.updateNeighbors();

        boolean noRoom = this.addToInventory();
        IMachineRecipeList.RecipeEntry entry = this.getRecipe();
        boolean canWork = this.canWork() && !noRoom;
        boolean operate = canWork && entry != null;
        if (operate)
        {
            this.handleChargeSlot(this.maxEnergy);
        }

        if (operate && this.energy >= this.energyConsume)
        {
            if (!this.getActive())
            {
                this.getNetwork().initiateTileEntityEvent(this, 0, false);
            }

            this.setActive(true);
            this.progress += this.progressPerTick;
            this.useEnergy(this.recipeEnergy);
            if (this.progress >= (float) this.recipeOperation)
            {
                this.operate(entry);
                this.progress = 0.0F;
                this.notifyNeighbors();
            }

            this.getNetwork().updateTileGuiField(this, "progress");
        }
        else
        {
            if (this.getActive())
            {
                if (this.progress != 0.0F)
                {
                    this.getNetwork().initiateTileEntityEvent(this, 1, false);
                }
                else
                {
                    this.getNetwork().initiateTileEntityEvent(this, 2, false);
                }
            }

            if (entry == null && this.progress != 0.0F)
            {
                this.progress = 0.0F;
                this.getNetwork().updateTileGuiField(this, "progress");
            }

            this.setActive(false);
        }

        for (int i = 0; i < 4; ++i)
        {
            ItemStack item = this.inventory.get(i + this.inventory.size() - 4);
            if (item.getItem() instanceof IMachineUpgradeItem)
            {
                ((IMachineUpgradeItem) item.getItem()).onTick(item, this);
            }
        }

        this.updateComparators();
    }

    public void handleModifiers(IMachineRecipeList.RecipeEntry entry)
    {
        if (entry != null && entry.getOutput().getMetadata() != null)
        {
            NBTTagCompound nbt = entry.getOutput().getMetadata();
            double energyMod = nbt.hasKey("RecipeEnergyModifier") ? nbt.getDouble("RecipeEnergyModifier") : 1.0D;
            int newEnergy = applyModifier(this.energyConsume, nbt.getInteger("RecipeEnergy"), energyMod);
            if (newEnergy != this.recipeEnergy)
            {
                this.recipeEnergy = newEnergy;
                if (this.recipeEnergy < 1)
                {
                    this.recipeEnergy = 1;
                }

                this.getNetwork().updateTileGuiField(this, "recipeEnergy");
            }

            double progMod = nbt.hasKey("RecipeTimeModifier") ? nbt.getDouble("RecipeTimeModifier") : 1.0D;
            int newProgress = applyModifier(this.operationLength, nbt.getInteger("RecipeTime"), progMod);
            if (newProgress != this.recipeOperation)
            {
                this.recipeOperation = newProgress;
                if (this.recipeOperation < 1)
                {
                    this.recipeOperation = 1;
                }

                this.getNetwork().updateTileGuiField(this, "recipeOperation");
            }

        }
        else
        {
            if (this.recipeEnergy != this.energyConsume)
            {
                this.recipeEnergy = this.energyConsume;
                if (this.recipeEnergy < 1)
                {
                    this.recipeEnergy = 1;
                }

                this.getNetwork().updateTileGuiField(this, "recipeEnergy");
            }

            if (this.recipeOperation != this.operationLength)
            {
                this.recipeOperation = this.operationLength;
                if (this.recipeOperation < 1)
                {
                    this.recipeOperation = 1;
                }

                this.getNetwork().updateTileGuiField(this, "recipeOperation");
            }

        }
    }

    public void operate(IMachineRecipeList.RecipeEntry entry)
    {
        IRecipeInput input = entry.getInput();
        MachineOutput output = entry.getOutput().copy();

        for (int i = 0; i < 4; ++i)
        {
            ItemStack itemStack = this.inventory.get(i + this.inventory.size() - 4);
            if (itemStack.getItem() instanceof IMachineUpgradeItem)
            {
                IMachineUpgradeItem item = (IMachineUpgradeItem) itemStack.getItem();
                item.onProcessEndPre(itemStack, this, output);
            }
        }

        List<ItemStack> list = new FilteredList();
        this.operateOnce(input, output, list);

        for (int i = 0; i < 4; ++i)
        {
            ItemStack itemStack = this.inventory.get(i + this.inventory.size() - 4);
            if (itemStack.getItem() instanceof IMachineUpgradeItem)
            {
                IMachineUpgradeItem item = (IMachineUpgradeItem) itemStack.getItem();
                item.onProcessEndPost(itemStack, this, input, output, list);
            }
        }

        if (!list.isEmpty())
        {
            this.results.addAll(list);
            this.addToInventory();
        }

    }

    public void operateOnce(IRecipeInput input, MachineOutput output, List<ItemStack> list)
    {
        list.addAll(output.getRecipeOutput(this.getMachineWorld().rand, getTileData()));
        if (!(input instanceof INullableRecipeInput) || !this.inventory.get(slotInput).isEmpty())
        {
            if (this.inventory.get(slotInput).getItem().hasContainerItem(this.inventory.get(slotInput)))
            {
                this.inventory.set(slotInput, this.inventory.get(slotInput).getItem().getContainerItem(this.inventory.get(slotInput)));
            }
            else
            {
                this.inventory.get(slotInput).shrink(input.getAmount());
            }

        }
    }

    public boolean addToInventory()
    {
        if (this.results.isEmpty())
        {
            return false;
        }
        else
        {
            for (int i = 0; i < this.results.size(); ++i)
            {
                ItemStack item = this.results.get(i);
                if (item.isEmpty())
                {
                    this.results.remove(i--);
                }
                else if (this.inventory.get(slotOutput).isEmpty())
                {
                    this.inventory.set(slotOutput, item.copy());
                    this.results.remove(i--);
                }
                else if (StackUtil.isStackEqual(this.inventory.get(slotOutput), item, false, false))
                {
                    int left = this.inventory.get(slotOutput).getMaxStackSize() - this.inventory.get(slotOutput).getCount();
                    if (left <= 0)
                    {
                        break;
                    }

                    if (left < item.getCount())
                    {
                        int itemLeft = item.getCount() - left;
                        item.setCount(itemLeft);
                        this.inventory.get(slotOutput).setCount(this.inventory.get(slotOutput).getMaxStackSize());
                        break;
                    }

                    this.inventory.get(slotOutput).grow(item.getCount());
                    this.results.remove(i--);
                }
                else if (this.inventory.get(slotOutput2).isEmpty())
                {
                    this.inventory.set(slotOutput2, item.copy());
                    this.results.remove(i--);
                }
                else if (StackUtil.isStackEqual(this.inventory.get(slotOutput2), item, false, false))
                {
                    int left = this.inventory.get(slotOutput2).getMaxStackSize() - this.inventory.get(slotOutput2).getCount();
                    if (left <= 0)
                    {
                        break;
                    }

                    if (left < item.getCount())
                    {
                        int itemLeft = item.getCount() - left;
                        item.setCount(itemLeft);
                        this.inventory.get(slotOutput2).setCount(this.inventory.get(slotOutput2).getMaxStackSize());
                        break;
                    }

                    this.inventory.get(slotOutput2).grow(item.getCount());
                    this.results.remove(i--);
                }
                else if (this.inventory.get(slotOutput3).isEmpty())
                {
                    this.inventory.set(slotOutput3, item.copy());
                    this.results.remove(i--);
                }
                else if (StackUtil.isStackEqual(this.inventory.get(slotOutput3), item, false, false))
                {
                    int left = this.inventory.get(slotOutput3).getMaxStackSize() - this.inventory.get(slotOutput3).getCount();
                    if (left <= 0)
                    {
                        break;
                    }

                    if (left < item.getCount())
                    {
                        int itemLeft = item.getCount() - left;
                        item.setCount(itemLeft);
                        this.inventory.get(slotOutput3).setCount(this.inventory.get(slotOutput3).getMaxStackSize());
                        break;
                    }

                    this.inventory.get(slotOutput3).grow(item.getCount());
                    this.results.remove(i--);
                }
                else if (this.inventory.get(slotOutput4).isEmpty())
                {
                    this.inventory.set(slotOutput4, item.copy());
                    this.results.remove(i--);
                }
                else if (StackUtil.isStackEqual(this.inventory.get(slotOutput4), item, false, false))
                {
                    int left = this.inventory.get(slotOutput4).getMaxStackSize() - this.inventory.get(slotOutput4).getCount();
                    if (left <= 0)
                    {
                        break;
                    }

                    if (left < item.getCount())
                    {
                        int itemLeft = item.getCount() - left;
                        item.setCount(itemLeft);
                        this.inventory.get(slotOutput4).setCount(this.inventory.get(slotOutput4).getMaxStackSize());
                        break;
                    }

                    this.inventory.get(slotOutput4).grow(item.getCount());
                    this.results.remove(i--);
                }
            }

            return !this.results.isEmpty();
        }
    }

    private IMachineRecipeList.RecipeEntry getRecipe()
    {
        if (this.inventory.get(slotInput).isEmpty() && !this.canWorkWithoutItems())
        {
            return null;
        }
        else
        {
            if (this.lastRecipe != null)
            {
                IRecipeInput recipe = this.lastRecipe.getInput();
                if (recipe instanceof INullableRecipeInput)
                {
                    if (!recipe.matches(this.inventory.get(slotInput)))
                    {
                        this.lastRecipe = null;
                    }
                }
                else if (!this.inventory.get(slotInput).isEmpty() && recipe.matches(this.inventory.get(0)))
                {
                    if (recipe.getAmount() > this.inventory.get(slotInput).getCount())
                    {
                        return null;
                    }
                }
                else
                {
                    this.lastRecipe = null;
                }
            }

            if (this.lastRecipe == null)
            {
                IMachineRecipeList.RecipeEntry out = this.getOutputFor(this.inventory.get(slotInput).copy());
                if (out == null)
                {
                    return null;
                }

                this.lastRecipe = out;

                //this.handleModifiers(out);
            }

            if (this.lastRecipe == null)
            {
                return null;
            }
            else if (this.inventory.get(slotOutput).getCount() >= this.inventory.get(slotOutput).getMaxStackSize())
            {
                return null;
            }
            else if (this.inventory.get(slotOutput2).getCount() >= this.inventory.get(slotOutput2).getMaxStackSize())
            {
                return null;
            }
            else if (this.inventory.get(slotOutput3).getCount() >= this.inventory.get(slotOutput3).getMaxStackSize())
            {
                return null;
            }
            else if (this.inventory.get(slotOutput4).getCount() >= this.inventory.get(slotOutput4).getMaxStackSize())
            {
                return null;
            }
            else if (this.inventory.get(slotOutput).isEmpty())
            {
                return this.lastRecipe;
            }
            else
            {
                Iterator var4 = this.lastRecipe.getOutput().getAllOutputs().iterator();

                ItemStack output;
                do
                {
                    if (!var4.hasNext())
                    {
                        return null;
                    }

                    output = (ItemStack) var4.next();
                }
                while (!StackUtil.isStackEqual(this.inventory.get(slotOutput), output, false, true));

                return this.lastRecipe;
            }
        }
    }

    public boolean canWork()
    {
        return !this.redstoneSensitive || this.isRedstonePowered();
    }

    @Override
    public boolean isRedstonePowered()
    {
        if (this.redstoneInverted)
        {
            return !super.isRedstonePowered();
        }
        else
        {
            return super.isRedstonePowered();
        }
    }

    @Override
    public void handleRedstone()
    {
        if (this.redstoneSensitive)
        {
            super.handleRedstone();
        }

    }

    public double getEnergy()
    {
        return (double) this.energy;
    }

    public boolean useEnergy(double amount, boolean simulate)
    {
        if ((double) this.energy < amount)
        {
            return false;
        }
        else
        {
            if (!simulate)
            {
                this.useEnergy((int) amount);
            }

            return true;
        }
    }

    public void setRedstoneSensitive(boolean active)
    {
        if (this.redstoneSensitive != active)
        {
            this.redstoneSensitive = active;
        }

    }

    public boolean isRedstoneSensitive()
    {
        return this.redstoneSensitive;
    }

    public boolean isProcessing()
    {
        return this.getActive();
    }

    public float getRecipeProgress()
    {
        float ret = this.progress / (float) this.recipeOperation;
        if (ret > 1.0F)
        {
            ret = 1.0F;
        }

        return ret;
    }

    public void setOverclockRates()
    {
        this.lastRecipe = null;
        int extraProcessSpeed = 0;
        double processingSpeedMultiplier = 1.0D;
        int extraProcessTime = 0;
        double processTimeMultiplier = 1.0D;
        int extraEnergyDemand = 0;
        double energyDemandMultiplier = 1.0D;
        int extraEnergyStorage = 0;
        double energyStorageMultiplier = 1.0D;
        int extraTier = 0;
        float soundModfier = 1.0F;
        boolean redstonePowered = false;
        this.redstoneSensitive = this.defaultSensitive;

        for (int i = 0; i < 4; ++i)
        {
            ItemStack item = this.inventory.get(i + this.inventory.size() - 4);
            if (item.getItem() instanceof IMachineUpgradeItem)
            {
                IMachineUpgradeItem upgrade = (IMachineUpgradeItem) item.getItem();
                upgrade.onInstalling(item, this);
                extraProcessSpeed += upgrade.getExtraProcessSpeed(item, this) * item.getCount();
                processingSpeedMultiplier *= Math.pow(upgrade.getProcessSpeedMultiplier(item, this), (double) item.getCount());
                extraProcessTime += upgrade.getExtraProcessTime(item, this) * item.getCount();
                processTimeMultiplier *= Math.pow(upgrade.getProcessTimeMultiplier(item, this), (double) item.getCount());
                extraEnergyDemand += upgrade.getExtraEnergyDemand(item, this) * item.getCount();
                energyDemandMultiplier *= Math.pow(upgrade.getEnergyDemandMultiplier(item, this), (double) item.getCount());
                extraEnergyStorage += upgrade.getExtraEnergyStorage(item, this) * item.getCount();
                energyStorageMultiplier *= Math.pow(upgrade.getEnergyStorageMultiplier(item, this), (double) item.getCount());
                soundModfier = (float) ((double) soundModfier * Math.pow((double) upgrade.getSoundVolumeMultiplier(item, this), (double) item.getCount()));
                extraTier += upgrade.getExtraTier(item, this) * item.getCount();
                if (upgrade.useRedstoneInverter(item, this))
                {
                    redstonePowered = true;
                }
            }
        }

        this.redstoneInverted = redstonePowered;
        this.progressPerTick = applyFloatModifier(1, extraProcessSpeed, processingSpeedMultiplier);
        this.energyConsume = applyModifier(this.defaultEnergyConsume, extraEnergyDemand, energyDemandMultiplier);
        this.operationLength = applyModifier(this.defaultOperationLength, extraProcessTime, processTimeMultiplier);
        this.setMaxEnergy(applyModifier(this.defaultEnergyStorage, extraEnergyStorage, energyStorageMultiplier));
        this.tier = this.baseTier + extraTier;
        if (this.tier > 13)
        {
            this.tier = 13;
        }

        this.maxInput = (int) EnergyNet.instance.getPowerFromTier(this.tier);
        if (this.energy > this.maxEnergy)
        {
            this.energy = this.maxEnergy;
        }

        this.soundLevel = soundModfier;
        if (this.progressPerTick < 0.01F)
        {
            this.progressPerTick = 0.01F;
        }

        if (this.operationLength < 1)
        {
            this.operationLength = 1;
        }

        if (this.energyConsume < 1)
        {
            this.energyConsume = 1;
        }

        this.handleModifiers(this.lastRecipe);
        this.getNetwork().updateTileEntityField(this, "redstoneInverted");
        this.getNetwork().updateTileEntityField(this, "redstoneSensitive");
        this.getNetwork().updateTileEntityField(this, "soundLevel");
        this.getNetwork().updateTileGuiField(this, "maxInput");
        this.getNetwork().updateTileGuiField(this, "energy");
    }

    static int applyModifier(int base, int extra, double multiplier)
    {
        long ret = Math.round((double) (base + extra) * multiplier);
        return ret > 2147483647L ? 2147483647 : (int) ret;
    }

    static float applyFloatModifier(int base, int extra, double multiplier)
    {
        double ret = (double) Math.round((double) (base + extra) * multiplier);
        return ret > 2.147483648E9D ? 2.14748365E9F : (float) ret;
    }

    @Override
    public void onLoaded()
    {
        super.onLoaded();
        if (this.isSimulating())
        {
            this.setOverclockRates();
        }

    }

    @Override
    public void onUnloaded()
    {
        if (this.isRendering() && this.audioSource != null)
        {
            IC2.audioManager.removeSources(this);
            this.audioSource.remove();
            this.audioSource = null;
        }

        super.onUnloaded();
    }

    public void onNetworkEvent(int event)
    {
        if (this.audioSource != null && this.audioSource.isRemoved())
        {
            this.audioSource = null;
        }

        if (this.audioSource == null && this.getStartSoundFile() != null)
        {
            this.audioSource = IC2.audioManager.createSource(this, PositionSpec.Center, this.getStartSoundFile(), true, false, IC2.audioManager.defaultVolume * this.soundLevel);
        }

        if (event == 0)
        {
            if (this.audioSource != null)
            {
                this.audioSource.play();
            }
        }
        else if (event == 1)
        {
            if (this.audioSource != null)
            {
                this.audioSource.stop();
                if (this.getInterruptSoundFile() != null)
                {
                    IC2.audioManager.playOnce(this, PositionSpec.Center, this.getInterruptSoundFile(), false, IC2.audioManager.defaultVolume * this.soundLevel);
                }
            }
        }
        else if (event == 2 && this.audioSource != null)
        {
            this.audioSource.stop();
        }

    }

    @Override
    public void onNetworkUpdate(String field)
    {
        if (field.equals("isActive") && this.getActive())
        {
            this.onNetworkEvent(0);
        }

        super.onNetworkUpdate(field);
        if (field.equals("soundLevel") && this.audioSource != null)
        {
            this.audioSource.setVolume(IC2.audioManager.defaultVolume * this.soundLevel);
        }

    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.progress = nbt.getFloat("progress");
        this.results.clear();
        NBTTagList list = nbt.getTagList("Results", 10);

        for (int i = 0; i < list.tagCount(); ++i)
        {
            NBTTagCompound data = list.getCompoundTagAt(i);
            this.results.add(new ItemStack(data));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setFloat("progress", this.progress);
        NBTTagList list = new NBTTagList();
        Iterator var3 = this.results.iterator();

        while (var3.hasNext())
        {
            ItemStack item = (ItemStack) var3.next();
            NBTTagCompound data = new NBTTagCompound();
            item.writeToNBT(data);
            list.appendTag(data);
        }

        nbt.setTag("Results", list);
        return nbt;
    }

    @Override
    public World getMachineWorld()
    {
        return this.getWorld();
    }

    @Override
    public BlockPos getMachinePos()
    {
        return this.getPos();
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return !this.isInvalid();
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player)
    {
        return new GTContainerIndustrialCentrifuge(player.inventory, this);
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer)
    {
        //needed for construction
    }

    @Override
    public boolean hasGui(EntityPlayer player)
    {
        return true;
    }

    @Override
    public IHasInventory getOutputInventory()
    {
        return new RangedInventoryWrapper(this, slotOutput, slotOutput2, slotOutput3, slotOutput4);
    }

    @Override
    public IHasInventory getInputInventory()
    {
        return new RangedInventoryWrapper(this, slotInput, slotCell);
    }


    //not sure if we need this
//    public static void postInit() {
//        Set<String> oreBlacklist = new HashSet();
//        oreBlacklist.addAll(Arrays.asList("oreIron", "oreGold", "oreSilver", "oreCopper", "oreTin", "oreRedstone", "oreUranium"));
//        Set<String> ingotBlackList = new HashSet();
//        ingotBlackList.addAll(Arrays.asList("ingotIron", "ingotGold", "ingotSilver", "ingotCopper", "ingotTin", "ingotBronze"));
//        String[] var2 = OreDictionary.getOreNames();
//        int var3 = var2.length;
//
//        for(int var4 = 0; var4 < var3; ++var4) {
//            String id = var2[var4];
//            String dust;
//            NonNullList list;
//            if (id.startsWith("ore")) {
//                if (!oreBlacklist.contains(id)) {
//                    dust = "dust" + id.substring(3);
//                    if (OreDictionary.doesOreNameExist(dust)) {
//                        list = OreDictionary.getOres(dust, false);
//                        if (!list.isEmpty()) {
//                            addRecipe((String)id, 1, StackUtil.copyWithSize((ItemStack)list.get(0), 2));
//                        }
//                    }
//                }
//            } else if (id.startsWith("ingot") && !ingotBlackList.contains(id)) {
//                dust = "dust" + id.substring(3);
//                if (OreDictionary.doesOreNameExist(dust)) {
//                    list = OreDictionary.getOres(dust, false);
//                    if (!list.isEmpty()) {
//                        addRecipe((String)id, 1, ((ItemStack)list.get(0)).func_77946_l());
//                    }
//                }
//            }
//        }
//
//    }

    public static void init(){

    	//recipes that dont require cells and work properly right now 
    	addRecipe((new RecipeInputOreDict("dustGlowstone", 16)),  new MachineOutput(null, Arrays.asList(new ItemStack[]{new ItemStack(Items.REDSTONE, 8, 0), StackUtil.copyWithSize(Ic2Items.goldDust, 8)})));
    	addRecipe((new RecipeInputOreDict("dustEnderEye", 16)),  new MachineOutput(null, Arrays.asList(new ItemStack[]{new ItemStack(GTItems.dustEnderpearl, 8, 0), new ItemStack(Items.BLAZE_POWDER, 8, 0)})));
    	addRecipe((new RecipeInputOreDict("gemLapis", 64)),  new MachineOutput(null, Arrays.asList(new ItemStack[]{new ItemStack(GTItems.dustLazurite, 48, 0), new ItemStack(GTItems.dustSodalite, 8, 0), new ItemStack(GTItems.dustPyrite, 4, 0),  new ItemStack(GTItems.dustCalcite, 4, 0)})));
    	addRecipe((new RecipeInputItemStack(StackUtil.copyWithSize(Ic2Items.netherrackDust, 64))),  new MachineOutput(null, Arrays.asList(new ItemStack[]{new ItemStack(Items.GOLD_NUGGET, 4, 0), new ItemStack(Items.REDSTONE, 4, 0), new ItemStack(Items.GUNPOWDER, 8, 0),  StackUtil.copyWithSize(Ic2Items.coalDust, 4)})));
    	addRecipe((new RecipeInputOreDict("dustPyrite", 3)),  new MachineOutput(null, Arrays.asList(new ItemStack[]{StackUtil.copyWithSize(Ic2Items.ironDust, 1)}))); 
    	
    	//experimental recipes that need the cell input added
    	addRecipe((new RecipeInputOreDict("dustBauxite", 24)),  new MachineOutput(null, Arrays.asList(new ItemStack[]{new ItemStack(GTItems.dustAluminum, 16, 0), new ItemStack(GTItems.dustTitanium, 1, 0), StackUtil.copyWithSize(Ic2Items.airCell, 6), new ItemStack(GTItems.cellH, 10)})));
        addRecipe((new RecipeInputOreDict("dustCoal", 4)),  new MachineOutput(null, Arrays.asList(new ItemStack[]{new ItemStack(GTItems.cellC, 8)})));
        addRecipe((new RecipeInputItemStack(StackUtil.copyWithSize(Ic2Items.electrolyzedWaterCell, 6))),  new MachineOutput(null, Arrays.asList(new ItemStack[]{new ItemStack(GTItems.cellH, 4), (Ic2Items.emptyCell), (Ic2Items.airCell)})));
        addRecipe((new RecipeInputItemStack(new ItemStack(GTItems.cellH, 4))),  new MachineOutput(null, Arrays.asList(new ItemStack[]{new ItemStack(GTItems.cellD, 1), StackUtil.copyWithSize(Ic2Items.emptyCell, 3)})));
        addRecipe((new RecipeInputItemStack(new ItemStack(GTItems.cellD, 4))),  new MachineOutput(null, Arrays.asList(new ItemStack[]{new ItemStack(GTItems.cellT, 1), StackUtil.copyWithSize(Ic2Items.emptyCell, 3)})));
        addRecipe((new RecipeInputItemStack(new ItemStack(GTItems.cellHE, 16))),  new MachineOutput(null, Arrays.asList(new ItemStack[]{new ItemStack(GTItems.cellHE3, 1), StackUtil.copyWithSize(Ic2Items.emptyCell, 15)})));
    }

    public static void addRecipe(IRecipeInput input, MachineOutput output)
    {
        industrialCentrifuge.addRecipe(input, output, output.toString());
    }

    private static void addRecipe(ItemStack itemStack, MachineOutput machineOutput) {
    }

    public static void addRecipe(ItemStack input, ItemStack output) {
        addRecipe((new RecipeInputItemStack(input)), output);
    }

    public static void addRecipe(ItemStack input, int stacksize, ItemStack output) {
        addRecipe((new RecipeInputItemStack(input, stacksize)), output);
    }

    public static void addRecipe(String input, int stacksize, ItemStack output) {
        addRecipe((new RecipeInputOreDict(input, stacksize)), output);
    }

    public static void addRecipe(ItemStack input, ItemStack output, float exp) {
        addRecipe((new RecipeInputItemStack(input)), output, exp);
    }

    public static void addRecipe(ItemStack input, int stacksize, ItemStack output, float exp) {
        addRecipe((new RecipeInputItemStack(input, stacksize)), output, exp);
    }

    public static void addRecipe(String input, int stacksize, ItemStack output, float exp) {
        addRecipe((new RecipeInputOreDict(input, stacksize)), output, exp);
    }

    public static void addRecipe(IRecipeInput input, ItemStack output) {
        addRecipe(input, output, 0.0F);
    }

    public static void addRecipe(IRecipeInput input, ItemStack output, float exp) {
        industrialCentrifuge.addRecipe(input, output, exp, makeString(output));
    }

    private static String makeString(ItemStack stack) {
        return stack.getDisplayName();
    }
}