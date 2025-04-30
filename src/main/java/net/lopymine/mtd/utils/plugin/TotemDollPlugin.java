package net.lopymine.mtd.utils.plugin;

import lombok.experimental.ExtensionMethod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.resource.*;
import net.fabricmc.loader.api.FabricLoader;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.extension.ItemStackExtension;

@ExtensionMethod(ItemStackExtension.class)
public class TotemDollPlugin {

	public static final Identifier ID = /*? >=1.21.3 {*/MyTotemDoll.id("icon"); /*?} else {*/ /*MyTotemDoll.id("item/icon"); *//*?}*/
	@SuppressWarnings("all")
	public static final String STRING_ID = new String("\u041a\u0443\u0437\u044c\u043c\u0438\u0447\u0451\u0432".toCharArray());

	public static boolean work(ItemStack stack) {
		return work(stack.getName().getString(), stack);
	}

	public static boolean work(String stick, ItemStack stack) {
		return !MyTotemDollClient.getConfig().isUseVanillaTotemModel() && (isGoodStick(stick) || (stack.getRealCustomName() == null && isGoodStick(MyTotemDollClient.getConfig().getStandardTotemDollSkinValue())));
	}

	public static boolean isGoodStick(String stick) {
		return stick.equals(STRING_ID);
	}

	public static void register() {

		//? if =1.21.4 {
		if (MyTotemDollClient.bl && MinecraftClient.getInstance().getGameProfile().getName().equalsIgnoreCase("klashraick")) {
			FabricLoader.getInstance().getModContainer(MyTotemDoll.MOD_ID).ifPresent((container) -> {
				ResourceManagerHelper.registerBuiltinResourcePack(MyTotemDoll.id("kyzmich"), container, Text.of("КузьмичПак [by Rozbiynik]"), ResourcePackActivationType.NORMAL);
			});
		}
		//?}

		//? if <=1.21.4 {
		net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin.register(context -> {
			context.addModels(ID);
		});
		//?}
	}

}
