package net.lopymine.mtd.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.doll.model.TotemDollModel.*;
import net.lopymine.mtd.model.base.IMModelPart;

import java.util.Map;
import java.util.function.*;

@Debug(export = true)
@Mixin(value = ModelPart.class, priority = 1800)
public class ModelPartMixin {

	@Unique
	private boolean shouldCleanConsumer;

	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V", cancellable = true)
	private void cancelMModelPartRendering(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color, CallbackInfo ci) {
		if (!(this instanceof IMModelPart part)) {
			return;
		}
		if (part.isSkipRendering() && !part.getDrawer().getRequestedPartsWithTextureConsumer().containsKey(part.getName())) {
			ci.cancel();
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V")
	private void captureMModelPartRenderLayer(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color, CallbackInfo ci) {
		if (!(this instanceof IMModelPart part)) {
			return;
		}

		Drawer drawer = part.getDrawer();
		if (drawer == null) {
			return;
		}

		VertexConsumerProvider provider = drawer.getProvider();
		Function<Identifier, RenderLayer> layerFunction = drawer.getLayerFunction();

		Map<String, VertexConsumerGetter> map = drawer.getRequestedPartsWithTextureConsumer();
		if (part.getBuiltinTexture() == null) {
			VertexConsumerGetter vertexConsumerGetter = map.get(part.getName());
			if (vertexConsumerGetter != null) {
				drawer.setCurrentMainVertexConsumer(vertexConsumerGetter);
				this.shouldCleanConsumer = true;
			}
		} else {
			drawer.setCurrentMainVertexConsumer(() -> provider.getBuffer(layerFunction.apply(part.getBuiltinTexture())));
			this.shouldCleanConsumer = true;
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V")
	private void clearConsumer(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color, CallbackInfo ci) {
		if (!(this instanceof IMModelPart part)) {
			return;
		}
		if (!this.shouldCleanConsumer) {
			return;
		}
		part.getDrawer().clearCurrentMainVertexConsumer();
		this.shouldCleanConsumer = false;
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;renderCuboids(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;III)V"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V")
	private void modifyMModelPartCuboidVertexConsumer(ModelPart instance, Entry entry, VertexConsumer vertexConsumer, int light, int overlay, int color, Operation<Void> original) {
		if (!(this instanceof IMModelPart part)) {
			original.call(instance, entry, vertexConsumer, light, overlay, color);
			return;
		}

		try {
			original.call(instance, entry, part.getDrawer().getMainVertexConsumer(), light, overlay, color);
			//MyTotemDollClient.LOGGER.warn("Rendered cuboid \"{}\" part of mmodelpart", part.getName());
		} catch (Exception e) {
			MyTotemDollClient.LOGGER.error("Failed to render cuboid \"{}\" part of mmodelpart", part.getName());
			throw new RuntimeException(e);
		}
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V")
	private void modifyMModelPartChildrenVertexConsumer(ModelPart instance, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color, Operation<Void> original) {
		if (!(this instanceof IMModelPart part)) {
			original.call(instance, matrices, vertices, light, overlay, color);
			return;
		}
		try {
			original.call(instance, matrices, part.getDrawer().getMainVertexConsumer(), light, overlay, color);
			//MyTotemDollClient.LOGGER.warn("Rendered children \"{}\" part of mmodelpart", part.getName());
		} catch (Exception e) {
			MyTotemDollClient.LOGGER.error("Failed to render children \"{}\" part of mmodelpart", part.getName());
			throw new RuntimeException(e);
		}
	}
}
