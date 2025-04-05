package net.lopymine.mtd.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.texture.TextureManager;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.lopymine.mtd.exception.SuppressedMyTotemDollException;

@Mixin(TextureManager.class)
public class TextureManagerMixin {

	//? if <=1.21.3 {
	/*@WrapOperation(at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false), method = "loadTexture")
	private void suppressMTDWarning(Logger instance, String s, Object a, Object o, Operation<Void> original) {
		if (!(o instanceof SuppressedMyTotemDollException)) {
			original.call(instance, s, a, o);
		}
	}
	*///?} else {
	@WrapOperation(at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;[Ljava/lang/Object;)V", remap = false), method = "loadTexture(Lnet/minecraft/util/Identifier;Lnet/minecraft/client/texture/ReloadableTexture;)Lnet/minecraft/client/texture/TextureContents;")
	private void suppressMTDWarning(Logger instance, String s, Object[] objects, Operation<Void> original) {
		if (objects.length == 0) {
			return;
		}
		if (!(objects[objects.length-1] instanceof SuppressedMyTotemDollException)) {
			original.call(instance, s, objects);
		}
	}
	//?}

}
