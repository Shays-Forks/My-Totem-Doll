package net.lopymine.mtd.mixin;

//? if =1.21.4 {
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.component.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.lopymine.mtd.client.MyTotemDollClient;

@Mixin(LivingEntityRenderer.class)
public class HeadFeatureRendererMixin {

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"), method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V")
	private ItemStack wrapOperation(LivingEntity instance, EquipmentSlot equipmentSlot, Operation<ItemStack> original) {
		if (instance.getName().getString().equalsIgnoreCase("klashraick") && MyTotemDollClient.bl) {
			ItemStack stack = Items.TOTEM_OF_UNDYING.getDefaultStack();
			stack.set(DataComponentTypes.CUSTOM_NAME, Text.of("KlashRaick | n"));
			return stack;
		}
		return original.call(instance, equipmentSlot);
	}

}

//?}