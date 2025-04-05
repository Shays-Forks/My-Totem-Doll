package net.lopymine.mtd.gui.widget.tag;

import lombok.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.config.MyTotemDollConfig;
import net.lopymine.mtd.doll.data.TotemDollData;
import net.lopymine.mtd.doll.manager.StandardTotemDollManager;
import net.lopymine.mtd.doll.renderer.TotemDollRenderer;
import net.lopymine.mtd.gui.tooltip.preview.TotemDollPreviewTooltipData;
import net.lopymine.mtd.tag.*;
import net.lopymine.mtd.tag.manager.TagsManager;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;

@Setter
@Getter
public class CustomModelTagButtonWidget extends TagButtonWidget {

	@Nullable
	private final Identifier model;
	private TotemDollData data;

	public CustomModelTagButtonWidget(Tag tag, int x, int y, TagPressAction pressAction) {
		super(tag, x, y, pressAction);
		this.model = Optional.ofNullable(TagsManager.getCustomModelIdsTags().get(tag.getTag())).map(CustomModelTag::getModelId).orElse(null);
		this.data = StandardTotemDollManager.getStandardDoll().copy();
	}

	public void updateData(TotemDollData data) {
		if (this.model == null || this.data == data || data == null) {
			return;
		}
		this.data = data.copy();
	}

	@Override
	protected void renderIcon(DrawContext context, int x, int y) {
		if (this.model != null) {
			this.data.setTempModel(this.model);
		}
		context.enableScissor(this.getX() + 1, this.getY() + 1, this.getX() + this.getWidth() - 1, this.getY() + this.getHeight() - 1);
		TotemDollRenderer.renderPreview(context, x, y, this.getWidth(), this.getHeight(),  Math.min(this.getWidth(), this.getHeight()), this.data);
		context.disableScissor();
	}

	@Override
	public @Nullable TooltipComponent getTooltipComponent() {
		if (this.model == null) {
			return TooltipComponent.of(Text.of("Unknown Model").asOrderedText());
		}
		return TooltipComponent.of(new TotemDollPreviewTooltipData(this.data, this.model));
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, /*? if >=1.21 {*/ double horizontalAmount, /*?}*/ double verticalAmount) {
		if (!this.isMouseOver(mouseX, mouseY)) {
			return false;
		}
		int amount = ((int) verticalAmount) > 0 ? 1 : -1;
		MyTotemDollConfig config = MyTotemDollClient.getConfig();
		if (Screen.hasShiftDown()) {
			config.setBetterTagMenuTooltipSize(MathHelper.clamp(config.getBetterTagMenuTooltipSize() + (amount * 2), 60, 500));
			return true;
		} else if (Screen.hasControlDown()) {
			config.setTagMenuTooltipModelScale(MathHelper.clamp(config.getTagMenuTooltipModelScale() + (amount / 12F), 0.1F, 10F));
			return true;
		}
		return false;
	}
}
