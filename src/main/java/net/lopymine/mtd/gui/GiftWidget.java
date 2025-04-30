package net.lopymine.mtd.gui;

//? if =1.21.4 {
/*import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.utils.DrawUtils;

public class GiftWidget extends ButtonWidget {

	private final int id;
	public boolean opened;
	private boolean stop;

	public GiftWidget(int id, int x, int y, PressAction onPress) {
		super(x, y, 15, 17, Text.of(""), onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
		this.id = id;
	}

	@Override
	public void onPress() {
		if (this.opened) {
			return;
		}
		this.opened = true;
		super.onPress();
	}

	@Override
	public void playDownSound(SoundManager soundManager) {
		if (this.opened) {
			return;
		}
		super.playDownSound(soundManager);
	}

	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		MatrixStack matrices = context.getMatrices();
		boolean bl = Util.getMeasuringTimeMs() / 800L % 2L == 0L;
		int offset = ((this.id == 2) != bl) && !this.stop && !this.isHovered() && !this.opened ? -1 : 0;

		matrices.push();
		matrices.translate(0, offset, 0);
		DrawUtils.drawTexture(context, MyTotemDoll.id("textures/gui/frames/1/gifts/gift_%s_%s_%s.png".formatted(this.id, this.isHovered() && !this.opened, this.opened)), this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
		matrices.pop();
	}

	public void stop(boolean b) {
		this.stop = b;
	}
}

*///?}