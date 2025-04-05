package net.lopymine.mtd.mixin.sodium;

import com.bawnorton.mixinsquared.TargetHandler;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.lopymine.mtd.model.base.*;

@Pseudo
@Mixin(ModelPart.class)
public class ModelPartMixinMixin {

	@Dynamic
	@TargetHandler(
			mixin = "me.jellysquid.mods.sodium.mixin.features.render.entity.ModelPartMixin",
			name = "onRender",
			prefix = "handler"
	)
	@Inject(at = @At("HEAD"), method = "@MixinSquared:Handler", cancellable = true, remap = false, require = 0)
	private void helloSodium(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, /*? if =1.20.1 {*/ /*float red, float green, float blue, float alpha, *//*?} else {*/ int color, /*?}*/ CallbackInfo a, CallbackInfo b) {
		ModelPart modelPart = (ModelPart) (Object) this;
		if (!(modelPart instanceof MModel)) {
			return;
		}
		b.cancel();
	}

}