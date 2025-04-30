package net.lopymine.mtd.gui;

//? if =1.21.4 {

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Unique;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.utils.DrawUtils;

public class StartWidget extends ButtonWidget {

	@Unique
	private int progress = 2;
	@Unique
	private boolean started = false;
	@Unique
	private boolean ended = false;

	public StartWidget(int x, int y, int width, int height, PressAction onPress) {
		super(x, y, width, height, Text.of(""), onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
	}

	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		DrawUtils.drawTexture(context, MyTotemDoll.id("textures/gui/frames/1/start_" + ((int) (Math.min(this.progress, 6) / 2)) + ".png"), this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
	}

	@Override
	public void playDownSound(SoundManager soundManager) {
		if (this.ended) {
			return;
		}
		super.playDownSound(soundManager);
	}

	@Override
	public void onPress() {
		if (this.ended) {
			return;
		}
		this.start();
	}

	public void start() {
		this.progress = 2;
		this.started  = true;
		this.ended    = false;
	}

	public void reset() {
		this.active   = true;
		this.visible  = true;
		this.progress = 2;
		this.started  = false;
		this.ended    = false;
	}

	public void tick() {
		if (!this.started || this.ended) {
			return;
		}
		this.progress++;
		if (this.progress >= 20) {
			this.ended = true;
			super.onPress();
		}
	}
}
//?}
