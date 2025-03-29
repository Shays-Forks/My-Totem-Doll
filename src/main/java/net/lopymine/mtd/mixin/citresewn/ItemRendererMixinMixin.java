package net.lopymine.mtd.mixin.citresewn;

//? if <=1.21.4 {
import com.bawnorton.mixinsquared.TargetHandler;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
//?}

import lombok.experimental.ExtensionMethod;
import net.minecraft.client.render.item.ItemRenderer;
import org.spongepowered.asm.mixin.*;
import net.lopymine.mtd.extension.ItemStackExtension;

@Pseudo
@Mixin(value = ItemRenderer.class, priority = 1500)
@ExtensionMethod(ItemStackExtension.class)
public abstract class ItemRendererMixinMixin {

	//? if <=1.21.4 {
	@Dynamic
	@TargetHandler(
			mixin = "shcm.shsupercm.fabric.citresewn.defaults.mixin.types.item.ItemRendererMixin",
			name = "citresewn$getItemModel",
			prefix = "handler"
	)
	@Inject(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfoReturnable;setReturnValue(Ljava/lang/Object;)V", remap = false))
	private void flagCITResewnModel(ItemStack stack, World world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir, CallbackInfo ci) {
		stack.setModdedModel(true);
	}
	//?}
}
