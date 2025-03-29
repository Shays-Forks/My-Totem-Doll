package net.lopymine.mtd.mixin;

//? >=1.21.4 {
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.extension.ItemStackExtension;
import net.lopymine.mtd.utils.mixin.ItemRenderStateWithStack;
import net.lopymine.mtd.utils.plugin.TotemDollPlugin;

import java.util.function.Supplier;

@ExtensionMethod(ItemStackExtension.class)
@Mixin(ItemModelManager.class)
public class ItemModelManagerMixin {

	//? if >=1.21.5 {
	/*@Inject(at = @At("HEAD"), method = "update")
	private void capture(ItemRenderState renderState, ItemStack stack, ItemDisplayContext displayContext, World world, LivingEntity entity, int seed, CallbackInfo ci) {
		this.captureEntity(stack, entity, renderState);
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;get(Lnet/minecraft/component/ComponentType;)Ljava/lang/Object;"), method = "update")
	private Object wrap(ItemStack stack, ComponentType<?> componentType, Operation<?> original) {
		return this.changeModel(stack, () -> original.call(stack, componentType));
	}
	*///?} else {
	@Inject(at = @At("HEAD"), method = "update(Lnet/minecraft/client/render/item/ItemRenderState;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V")
	private void capture(ItemRenderState renderState, ItemStack stack, ModelTransformationMode transformationMode, World world, LivingEntity entity, int seed, CallbackInfo ci) {
		this.captureEntity(stack, entity, renderState);
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;get(Lnet/minecraft/component/ComponentType;)Ljava/lang/Object;"), method = "update(Lnet/minecraft/client/render/item/ItemRenderState;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V")
	private Object wrap(ItemStack stack, ComponentType<?> componentType, Operation<?> original) {
		return this.changeModel(stack, () -> original.call(stack, componentType));
	}
	//?}

	@Unique
	private Object changeModel(ItemStack stack, Supplier<Object> supplier) {
		if (!MyTotemDollClient.getConfig().isModEnabled() || !stack.isOf(Items.TOTEM_OF_UNDYING)) { // TODO you know what I mean
			return supplier.get();
		}

		String string = stack.getName().getString();
		if (TotemDollPlugin.work(string) || (!MyTotemDollClient.getConfig().isUseVanillaTotemModel() && stack.getRealCustomName() == null && TotemDollPlugin.work(MyTotemDollClient.getConfig().getStandardTotemDollSkinValue()))) {
			stack.setModdedModel(true);
			return TotemDollPlugin.ID;
		}

		return supplier.get();
	}

	@Unique
	private void captureEntity(ItemStack stack, LivingEntity entity, ItemRenderState renderState) {
		stack.setPlayerEntity(null);
		if (entity instanceof AbstractClientPlayerEntity player) {
			stack.setPlayerEntity(player);
		}
		((ItemRenderStateWithStack) renderState).myTotemDoll$setStack(stack);
	}

}

//?}
