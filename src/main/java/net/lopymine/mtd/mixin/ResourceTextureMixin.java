package net.lopymine.mtd.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.resource.*;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.exception.SuppressedMyTotemDollException;

@Mixin(/*? if <=1.21.3 {*//* net.minecraft.client.texture.ResourceTexture.TextureData.class *//*?} else {*/ net.minecraft.client.texture.TextureContents.class /*?}*/)
public class ResourceTextureMixin {

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceManager;getResourceOrThrow(Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/Resource;"), method = "load")
	private static Resource suppressMyTotemDollCodeWarning(ResourceManager instance, Identifier identifier, Operation<Resource> original) throws SuppressedMyTotemDollException {
		if (!identifier.toString().startsWith(MyTotemDoll.MOD_ID + ":remapped_textures/")) {
			return original.call(instance, identifier);
		}
		try {
			return original.call(instance, identifier);
		} catch (Exception e) {
			throw new SuppressedMyTotemDollException();
		}
	}

}
