package net.lopymine.mtd.mixin;

//? if =1.21.4 {
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.ButtonWidget.Builder;
import net.minecraft.client.toast.*;
import net.minecraft.client.toast.SystemToast.Type;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import net.lopymine.mtd.*;
import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.gui.*;
import net.lopymine.mtd.utils.DrawUtils;

import java.time.LocalDate;
import org.jetbrains.annotations.Nullable;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

	@Unique
	private int animationX = 0;
	@Unique
	private int animationY = 0;

	@Unique
	private int progress = 2;
	@Unique
	private boolean started = false;
	@Unique
	private boolean ended = false;

	@Unique
	private int progressAlpha = 0;

	@Unique
	private float splashAlpha = 1.0F;
	@Unique
	private float titleAlpha = 1.0F;

	@Unique
	@Nullable
	private StartWidget startWidget;
	@Unique
	@Nullable
	private GiftWidget giftWidget1;
	@Unique
	@Nullable
	private GiftWidget giftWidget2;
	@Unique
	@Nullable
	private GiftWidget giftWidget3;

	@Unique
	private boolean cakeStarted = false;
	@Unique
	private boolean cakeEnded = false;
	@Unique
	private int cakeProgress = 3;

	protected TitleScreenMixin(Text title) {
		super(title);
	}

	@Inject(at = @At("HEAD"), method = "setWidgetAlpha")
	private void inject123(float alpha, CallbackInfo ci) {
		if (this.startWidget != null) {
			this.startWidget.setAlpha(alpha);
		}
	}

	@Inject(at = @At("HEAD"), method = "removed")
	private void inject455(CallbackInfo ci) {
		MyTotemDollClient.getTempConfig().save();
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/LogoDrawer;draw(Lnet/minecraft/client/gui/DrawContext;IF)V"), method = "render")
	private void wrapAlpha(LogoDrawer instance, DrawContext context, int screenWidth, float alpha, Operation<Void> original) {
		if (!MyTotemDollClient.getTempConfig().isTitle()) {
			original.call(instance, context, screenWidth, alpha == 1.0F ? this.titleAlpha : alpha);
		}
		DrawUtils.drawTexture(context, MyTotemDoll.id("textures/gui/frames/1/minecraft_title.png"), (screenWidth / 2) - (320 / 2), 25, 0, 0, 320, 40, 320, 40, ColorHelper.getWhite(1F - this.titleAlpha));
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/SplashTextRenderer;render(Lnet/minecraft/client/gui/DrawContext;ILnet/minecraft/client/font/TextRenderer;I)V"), method = "render")
	private void wrapAlpha(SplashTextRenderer instance, DrawContext context, int screenWidth, TextRenderer textRenderer, int color, Operation<Void> original) {
		boolean bl = MinecraftClient.getInstance().getGameProfile().getName().equalsIgnoreCase("klashraick") && MyTotemDollClient.bl;
		if (!bl) {
			int alpha = ColorHelper.getAlpha(color);
			int red = ColorHelper.getRed(color);
			int green = ColorHelper.getGreen(color);
			int blue = ColorHelper.getBlue(color);
			original.call(instance, context, screenWidth, textRenderer, alpha == 255 ? ColorHelper.getArgb((int) (this.splashAlpha * 255), red, green, blue) : color);
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;isRealmsNotificationsGuiDisplayed()Z"), method = "mouseClicked")
	private void invokeStart(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
//		this.progress = 2;
//		this.progressAlpha = 2;
//		this.cakeProgress = 3;
//		this.titleAlpha = 1.0F;
//		this.splashAlpha = 1.0F;
//		this.started = false;
//		this.ended = false;
//		this.cakeStarted = false;
//		this.cakeEnded = false;
//		if (this.startWidget != null) {
//			this.startWidget.reset();
//		}
//		if (this.giftWidget1 != null) {
//			this.giftWidget1.visible = false;
//			this.giftWidget1.active = false;
//			this.giftWidget1.stop(false);
//			this.giftWidget1.opened = false;
//		}
//		if (this.giftWidget2 != null) {
//			this.giftWidget2.visible = false;
//			this.giftWidget2.active = false;
//			this.giftWidget2.stop(false);
//			this.giftWidget2.opened = false;
//		}
//		if (this.giftWidget3 != null) {
//			this.giftWidget3.visible = false;
//			this.giftWidget3.active = false;
//			this.giftWidget3.stop(false);
//			this.giftWidget3.opened = false;
//		}
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void tickAnimation(CallbackInfo ci) {
		if (this.startWidget != null) {
			this.startWidget.tick();
		}

		if (this.ended) {
			if (this.cakeStarted && !this.cakeEnded) {
				this.cakeProgress++;
				if (this.cakeProgress >= 55 * 3) {
					this.cakeEnded = true;
					MyTotemDollClient.getTempConfig().setB3(true);
					if (this.giftWidget3 != null) {
						this.giftWidget3.active  = true;
						this.giftWidget3.visible = true;
					}
					MinecraftClient.getInstance().getToastManager().add(new SystemToast(Type.NARRATOR_TOGGLE, Text.of("Шляпа-торт!"), Text.of("Кажется, чуть-чуть упала... Но выглядит сладко!")));
				}
			}
			if (this.progressAlpha < 10) {
				this.progressAlpha++;
			}
			this.titleAlpha  = 1F - (this.progressAlpha / 10F);
			this.splashAlpha = 1F - (this.progressAlpha / 10F);
			return;
		}
		if (!this.started) {
			return;
		}
		this.progress++;
		if (this.progress >= 104 * 2) {
			this.ended = true;
			MyTotemDollClient.getTempConfig().setTitle(true);
			if (this.giftWidget1 != null) {
				this.giftWidget1.visible = true;
				this.giftWidget1.active  = true;
			}
			if (this.giftWidget2 != null) {
				this.giftWidget2.visible = true;
				this.giftWidget2.active  = true;
			}
			if (this.giftWidget3 != null) {
				this.giftWidget3.visible = true;
				this.giftWidget3.active  = true;
			}
		}
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;", ordinal = 0), method = "addNormalWidgets")
	private Builder capturePos(Builder instance, int x, int y, int width, int height, Operation<Builder> original) {
		if (!MinecraftClient.getInstance().getGameProfile().getName().equalsIgnoreCase("klashraick")) {
			return original.call(instance, x, y, width, height);
		}

		if (!MyTotemDollClient.bl) {
			return original.call(instance, x, y, width, height);
		}

		TempConfig tempConfig = MyTotemDollClient.getTempConfig();
		if (tempConfig.isTitle()) {
			this.started       = true;
			this.ended         = true;
			this.progress      = 104 * 2;
			this.progressAlpha = 10;
			this.titleAlpha    = 0.0F;
			this.splashAlpha   = 0.0F;
		}

		if (!this.started) {
			this.startWidget         = this.addSelectableChild(new StartWidget(x + width - 55, y - 12 - 7, 13, 7, (button) -> {
				this.started   = true;
				button.visible = false;
				button.active  = false;
			}));
			this.startWidget.visible = !this.ended && !tempConfig.isTitle();
			this.startWidget.active  = !this.ended && !tempConfig.isTitle();
		}
		this.animationX          = x + 33;
		this.animationY          = y - 128 - 12;
		this.giftWidget1         = this.addSelectableChild(new GiftWidget(1, this.animationX + 12, this.animationY + 88, (a) -> {
			ResourcePackManager resourcePackManager = MinecraftClient.getInstance().getResourcePackManager();
			resourcePackManager.enable("my-totem-doll:kyzmich");
			MinecraftClient.getInstance().reloadResources();
			tempConfig.setB1(true);
		}));
		this.giftWidget1.visible = this.ended || tempConfig.isB1();
		this.giftWidget1.active  = this.ended || tempConfig.isB1();
		if (tempConfig.isB1()) {
			this.giftWidget1.opened = true;
		}
		this.giftWidget2         = this.addSelectableChild(new GiftWidget(2, this.animationX + 12 + 19, this.animationY + 88, (a) -> {
			if (this.giftWidget1 != null && this.giftWidget1.opened) {
				tempConfig.setB2(true);
				Util.getOperatingSystem().open("https://t.me/pretzalien");
			} else {
				if (this.giftWidget2 != null) {
					this.giftWidget2.opened = false;
				}
				MinecraftClient.getInstance().getToastManager().add(new SystemToast(Type.NARRATOR_TOGGLE, Text.of("А-та-та!"), Text.of("Самое вкусное в самом конце :3")));
			}
		}));
		this.giftWidget2.visible = this.ended || tempConfig.isB2();
		this.giftWidget2.active  = this.ended || tempConfig.isB2();
		if (tempConfig.isB2()) {
			this.giftWidget2.opened = true;
		}
		this.giftWidget3         = this.addSelectableChild(new GiftWidget(3, this.animationX + 12 + 38, this.animationY + 88, (a) -> {
			a.visible        = false;
			a.active         = false;
			this.cakeStarted = true;
		}));
		this.giftWidget3.visible = this.ended || tempConfig.isB3();
		this.giftWidget3.active  = this.ended || tempConfig.isB3();
		if (tempConfig.isB3()) {
			this.giftWidget3.opened = true;
			this.cakeProgress       = 55 * 3;
			this.cakeStarted        = true;
			this.cakeEnded          = true;
		}
		return original.call(instance, x, y, width, height);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)I"), method = "render")
	private void renderAnimation(DrawContext context, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci) {
		if (this.startWidget != null) {
			this.startWidget.render(context, mouseX, mouseY, deltaTicks);
		}
		if (this.giftWidget1 != null) {
			this.giftWidget1.stop(this.cakeStarted && !this.cakeEnded);
			this.giftWidget1.render(context, mouseX, mouseY, deltaTicks);
		}
		if (this.giftWidget2 != null) {
			this.giftWidget2.stop(this.cakeStarted && !this.cakeEnded);
			this.giftWidget2.render(context, mouseX, mouseY, deltaTicks);
		}
		if (this.giftWidget3 != null) {
			this.giftWidget3.stop(this.cakeStarted && !this.cakeEnded);
			this.giftWidget3.render(context, mouseX, mouseY, deltaTicks);
		}

		if (!this.started) {
			return;
		}

		DrawUtils.drawTexture(context, MyTotemDoll.id("textures/gui/frames/frame_" + ((int) (this.progress / 2)) + ".png"), this.animationX, this.animationY, 0, 0, 133, 128, 133, 128);

		if (this.cakeStarted && !this.cakeEnded) {
			DrawUtils.drawTexture(context, MyTotemDoll.id("textures/gui/frames/1/bruh_2.png"), this.animationX + 116, this.animationY + 88, 0, 0, 17, 40, 17, 40);
		} else if (this.cakeEnded) {
			DrawUtils.drawTexture(context, MyTotemDoll.id("textures/gui/frames/1/cake/cake_ready.png"), this.animationX + 116, this.animationY + 88, 0, 0, 17, 40, 17, 40);
		} else if (this.ended) {
			DrawUtils.drawTexture(context, MyTotemDoll.id("textures/gui/frames/1/bruh.png"), this.animationX + 116, this.animationY + 88, 0, 0, 17, 40, 17, 40);
		}

		if (this.cakeStarted && !this.cakeEnded) {
			DrawUtils.drawTexture(context, MyTotemDoll.id("textures/gui/frames/1/cake/cake_" + ((int) (this.cakeProgress / 3)) + ".png"), this.animationX, this.animationY, 0, 0, 148, 128, 148, 128);
		}
	}
}
//?}
