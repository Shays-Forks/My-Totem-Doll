package net.lopymine.mtd.mixin;

//? if <=1.21.3 {

/*import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.texture.*;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;


import net.lopymine.mtd.exception.SuppressedMyTotemDollException;
import net.lopymine.mtd.utils.mixin.MTDPlayerSkinTexture;
import net.lopymine.mtd.utils.texture.*;

import org.jetbrains.annotations.Nullable;

@Mixin(PlayerSkinTexture.class)
public class PlayerSkinTextureMixin implements MTDPlayerSkinTexture {

	@Unique
	@Nullable
	private SuccessAction onSuccessLoading;

	@Unique
	@Nullable
	private FailedAction onFailedLoading;
	@Unique
	private boolean cape;

	@WrapOperation(at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Throwable;)V", remap = false), method = "loadTexture")
	private void onFailedLoading1(Logger instance, String string, Throwable throwable, Operation<Void> original) {
		if (this.onFailedLoading != null) {
			if (!this.onFailedLoading.onFailed(string, throwable)) {
				original.call(instance, string, throwable);
			}
		}
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Throwable;)V", remap = false), method = "method_22801")
	private void onFailedLoading2(Logger instance, String string, Throwable throwable, Operation<Void> original) {
		if (this.onFailedLoading != null) {
			if (!this.onFailedLoading.onFailed(string, throwable)) {
				original.call(instance, string, throwable);
			}
		}
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false), method = "method_22799")
	private void onFailedLoading3(Logger instance, String string, Object a, Object o, Operation<Void> original) {
		if (this.onFailedLoading != null) {
			if (!this.onFailedLoading.onFailed(string, o instanceof Throwable throwable ? throwable : null, a)) {
				original.call(instance, string, a, o);
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "remapTexture", cancellable = true)
	private void remapCape(NativeImage image, CallbackInfoReturnable<NativeImage> cir) {
		if (!this.cape) {
			return;
		}
		NativeImage nativeImage = new NativeImage(64, 64, true);
		nativeImage.copyFrom(image);
		image.close();
		cir.setReturnValue(nativeImage);
	}

	@Inject(at = @At(value = "TAIL"), method = "uploadTexture")
	private void onSuccessLoading(NativeImage image, CallbackInfo ci) {
		if (this.onSuccessLoading != null) {
			this.onSuccessLoading.onSuccess();
		}
	}

	@Override
	public void myTotemDoll$setOnSuccessAction(@Nullable SuccessAction runnable) {
		this.onSuccessLoading = runnable;
	}

	@Override
	public void myTotemDoll$setOnFailedAction(@Nullable FailedAction runnable) {
		this.onFailedLoading = runnable;
	}

	@Override
	public void myTotemDoll$markAsCape() {
		this.cape = true;
	}
}

*///?}
