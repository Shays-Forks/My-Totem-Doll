package net.lopymine.mtd.gui.tooltip.preview;

import lombok.experimental.ExtensionMethod;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;

import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.config.MyTotemDollConfig;
import net.lopymine.mtd.doll.data.TotemDollData;
import net.lopymine.mtd.doll.renderer.TotemDollRenderer;
import net.lopymine.mtd.extension.IdentifierExtension;

@ExtensionMethod(IdentifierExtension.class)
public class TotemDollPreviewTooltipComponent implements TooltipComponent {

	private final TotemDollData data;
	private final Identifier model;

	public TotemDollPreviewTooltipComponent(TotemDollData data, Identifier model) {
		this.data = data;
		this.model = model;
	}

	@Override
	public int getHeight(/*? >=1.21.2 {*/TextRenderer textRenderer/*?}*/) {
		return MyTotemDollClient.getConfig().getBetterTagMenuTooltipSize() + 10;
	}

	@Override
	public int getWidth(TextRenderer textRenderer) {
		return MyTotemDollClient.getConfig().getBetterTagMenuTooltipSize();
	}

	@Override
	public void drawItems(TextRenderer textRenderer, int x, int y,/*? >=1.21.2 {*/int w, int h,/*?}*/ DrawContext context) {
		int width = this.getWidth(textRenderer);
		MyTotemDollConfig config = MyTotemDollClient.getConfig();
		float sizeOriginal = config.getBetterTagMenuTooltipSize();
		float size = (sizeOriginal / 1.25F) * config.getTagMenuTooltipModelScale();
		Text text = Text.of(this.model.getFileName());
		int textWidth = textRenderer.getWidth(text);

		int height = this.getHeight(/*? >=1.21.2 {*/textRenderer/*?}*/);
		context.enableScissor(x, y + 10 + 4 + 2, x + width, y + height - 2);
		this.data.setTempModel(this.model);
		TotemDollRenderer.renderPreview(context, x, y + 10, width, height - 10, size, this.data);
		context.disableScissor();

		context.enableScissor(x, y, x + width, y + height);
		if (textWidth > width) {
			ClickableWidget.drawScrollableText(context, textRenderer, text, x, y, x + width, y + 10, -1);
		} else {
			context.drawText(textRenderer, text, x, y + 1, -1, true);
		}
		context.fill(x, y + 10 + 3, x + Math.min((textWidth - 5), width), y + 10 + 4, -1);
		context.disableScissor();
	}
}
