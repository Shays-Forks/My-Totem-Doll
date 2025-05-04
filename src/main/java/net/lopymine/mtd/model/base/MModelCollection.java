package net.lopymine.mtd.model.base;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class MModelCollection {

	private final List<MModel> models;
	private int skipRendering = -1;
	private int visible = -1;

	public MModelCollection(List<MModel> models) {
		this.models = models;
	}

	public void setVisible(boolean visible) {
		int state = visible ? 1 : 0;
		if (this.visible == state) {
			return;
		}
		this.visible = state;
		for (MModel model : this.models) {
			model.visible = visible;
		}
	}

	public void setSkipRendering(boolean skipRendering) {
		int state = skipRendering ? 1 : 0;
		if (this.skipRendering == state) {
			return;
		}
		this.skipRendering = state;
		for (MModel model : this.models) {
			model.setSkipRendering(skipRendering);
		}
	}
}
