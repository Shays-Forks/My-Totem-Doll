package net.lopymine.mtd.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import net.lopymine.mtd.client.MyTotemDollClient;

import org.jetbrains.annotations.NotNull;

@Mixin(/*? if >=1.21 {*/ net.minecraft.client.render.entity.LivingEntityRenderer /*?} else {*/ /*net.minecraft.client.render.entity.feature.HeadFeatureRenderer *//*?}*/.class)
public class HeadFeatureRendererMixin {

	@Unique
	private static final ItemStack stack = getItemStack();

	//? if >=1.21 {
	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"), method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V")
	private ItemStack wrapOperation(LivingEntity instance, EquipmentSlot equipmentSlot, Operation<ItemStack> original) {
		if (instance.getName().getString().equalsIgnoreCase("klashraick") && MyTotemDollClient.bl && MyTotemDollClient.getConfig().isModEnabled()) {
			return stack;
		}
		return original.call(instance, equipmentSlot);
	}
	//?} else {
	/*@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V")
	private ItemStack wrapOperation(LivingEntity instance, EquipmentSlot equipmentSlot, Operation<ItemStack> original) {
		if (instance.getName().getString().equalsIgnoreCase("klashraick") && MyTotemDollClient.bl && MyTotemDollClient.getConfig().isModEnabled()) {
			return stack;
		}
		return original.call(instance, equipmentSlot);
	}
	*///?}

	@Unique
	private static @NotNull ItemStack getItemStack() {
		ItemStack stack = Items.TOTEM_OF_UNDYING.getDefaultStack();
		//? if >=1.21 {
		stack.set(net.minecraft.component.DataComponentTypes.CUSTOM_NAME, Text.of("KlashRaick | n"));
		//?} else {
		/*stack.setCustomName(Text.of("KlashRaick | n"));
		*///?}
		return stack;
	}

}
