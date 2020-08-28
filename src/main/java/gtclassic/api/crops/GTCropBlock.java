package gtclassic.api.crops;

import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialFlag;
import gtclassic.api.material.GTMaterialGen;
import ic2.api.crops.CropProperties;
import ic2.api.crops.ICropTile;
import ic2.core.block.crop.crops.CropCardBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTCropBlock extends CropCardBase {

	GTCropType entry;

	public GTCropBlock(GTCropType entry) {
		super(new CropProperties(entry.getTier(), 2, 0, 0, 2, 0));
		this.entry = entry;
	}

	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getTexture(int state) {
		return this.getSprite(this.entry.getSpriteSheet())[this.entry.getId() + (state - 1)];
	}

	@Override
	public String getDiscoveredBy() {
		return this.entry.getDiscoverer();
	}

	public String getId() {
		return this.entry.getName();
	}

	public int getMaxSize() {
		return 4;
	}

	public ItemStack getGain(ICropTile crop) {
		return GTMaterialGen.getDust(entry.getMaterial(), 1).copy();
	}

	@Override
	public double dropGainChance() {
		return super.dropGainChance() / 2.0D;
	}

	@Override
	public String[] getAttributes() {
		return entry.getAttributes();
	}

	@Override
	public int getGrowthDuration(ICropTile cropTile) {
		return cropTile.getCurrentSize() == 3 ? 2200 : 1000;
	}

	@Override
	public boolean canGrow(ICropTile cropTile) {
		GTMaterial mat = this.entry.getMaterial();
		String name = mat.getDisplayName();
		boolean size = cropTile.getCurrentSize() < 4;
		boolean below = cropTile.isBlockBelow("ore" + name) || cropTile.isBlockBelow("block" + name);
		if (GTMaterial.isGem(mat) || mat.hasFlag(GTMaterialFlag.INGOT)) {
			return size && below;
		}
		return size;
	}

	@Override
	public int getOptimalHarvestSize(ICropTile cropTile) {
		return 4;
	}

	@Override
	public boolean canBeHarvested(ICropTile cropTile) {
		return cropTile.getCurrentSize() == 4;
	}

	@Override
	public int getSizeAfterHarvest(ICropTile cropTile) {
		return 2;
	}
}
