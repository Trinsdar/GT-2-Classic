package gtmodern.container;

import gtmodern.GTMod;
import ic2.core.inventory.container.ContainerItemComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GTItemContainerCraftingTablet extends ContainerItemComponent<GTItemInventoryCraftingTablet> {

	public static ResourceLocation TEXTURE = new ResourceLocation(GTMod.MODID, "textures/gui/craftingtablet.png");
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public InventoryCraftResult craftResult = new InventoryCraftResult();
	private final World world;
	private final EntityPlayer player;

	public GTItemContainerCraftingTablet(GTItemInventoryCraftingTablet inv, int id, InventoryPlayer player) {
		super(inv, id);
		this.world = player.player.getEntityWorld();
		this.player = player.player;
		this.addSlotToContainer(new SlotCrafting(this.player, this.craftMatrix, this.craftResult, 0, 124, 35));
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
			}
		}
		this.addPlayerInventory(player, 0, 0);
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn) {
		this.slotChangedCraftingGrid(this.world, this.player, this.craftMatrix, this.craftResult);
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
		if (!this.world.isRemote) {
			this.clearContainer(playerIn, this.world, this.craftMatrix);
		}
	}

	@Override
	public ResourceLocation getTexture() {
		return TEXTURE;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return getGuiHolder().canInteractWith(player);
	}
}
