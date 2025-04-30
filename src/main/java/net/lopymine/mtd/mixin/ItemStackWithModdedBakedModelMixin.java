package net.lopymine.mtd.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.lopymine.mtd.utils.mixin.ItemStackWithModdedBakedModel;

@Mixin(ItemStack.class)
public class ItemStackWithModdedBakedModelMixin implements ItemStackWithModdedBakedModel {

	@Unique
	private boolean modded = false;

	@Override
	public void myTotemDoll$setModdedModel(boolean modded) {
		this.modded = modded;
	}

	@Override
	public boolean myTotemDoll$isModdedModel() {
		return modded;
	}

	@Inject(at = @At("RETURN"), method = /*? if <=1.21.4 {*/ /*"copy" *//*?} else {*/ "copy()Lnet/minecraft/item/ItemStack;" /*?}*/)
	private void generated(CallbackInfoReturnable<ItemStack> cir) {
		((ItemStackWithModdedBakedModel)cir.getReturnValue()).myTotemDoll$setModdedModel(this.myTotemDoll$isModdedModel());
	}
}
