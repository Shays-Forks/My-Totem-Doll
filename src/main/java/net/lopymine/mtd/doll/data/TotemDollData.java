package net.lopymine.mtd.doll.data;

import lombok.*;
import net.minecraft.util.Identifier;

import net.lopymine.mtd.doll.model.TotemDollModel;
import net.lopymine.mtd.model.base.MModel;
import net.lopymine.mtd.model.bb.manager.BlockBenchModelManager;

import java.util.*;
import org.jetbrains.annotations.*;

@Getter
@Setter
public class TotemDollData {

	private final Map<Identifier, MModel> tempModels = new HashMap<>();
	@Nullable
	private String nickname;
	@Nullable
	private TotemDollModel model;
	@NotNull
	private TotemDollTextures textures;
	@Nullable
	private TotemDollTextures currentTempTextures;

	private boolean shouldRecreateModel;
	@Nullable
	private MModel currentTempMModel;
	@Nullable
	private TotemDollModel currentTempModel;

	public TotemDollData(@Nullable String nickname, @NotNull TotemDollTextures textures) {
		this.nickname = nickname;
		this.textures = textures;
	}

	public static TotemDollData create(@Nullable String nickname) {
		return new TotemDollData(nickname, TotemDollTextures.create());
	}

	public void setCustomModel(MModel model) {
		this.model = new TotemDollModel(model, this.textures.getArmsType().isSlim());
	}

	public void setTempModel(Identifier id) {
		MModel model = this.tempModels.get(id);
		if (model == null) {
			BlockBenchModelManager.getModelAsyncAsResponse(id, (response) -> {
				if (!response.isEmpty()) {
					MModel tempMModel = response.value();
					this.tempModels.put(id, tempMModel);
					this.setCurrentTempMModel(tempMModel);
				}
			});
			return;
		}
		this.setCurrentTempMModel(model);
	}

	public void setCurrentTempMModel(@Nullable MModel currentTempMModel) {
		this.currentTempMModel = currentTempMModel;
		TotemDollModel tempModel = this.getOrCreateOrUpdateTempModelWithTempMModel();
		if (tempModel != null && this.model != null) {
			tempModel.setSlim(this.model.isSlim());
		}
	}

	@Nullable
	private TotemDollModel getOrCreateOrUpdateTempModelWithTempMModel() {
		if (this.currentTempMModel != null) {
			if (this.currentTempModel == null || !this.currentTempModel.getMain().equals(this.currentTempMModel)) {
				return this.currentTempModel = new TotemDollModel(this.currentTempMModel, this.textures.getArmsType().isSlim());
			}
			return this.currentTempModel;
		}
		return null;
	}

	public TotemDollModel getModel() {
		TotemDollModel tempModel = this.getOrCreateOrUpdateTempModelWithTempMModel();
		if (tempModel != null) {
			return tempModel;
		}

		if (this.model != null && !this.shouldRecreateModel) {
			return this.model;
		}

		MModel dollModel = TotemDollModel.createDollModel();
		this.model = new TotemDollModel(dollModel, this.textures.getArmsType().isSlim());

		if (this.shouldRecreateModel) {
			this.shouldRecreateModel = false;
		}

		return this.model;
	}

	public TotemDollTextures getRenderTextures() {
		return this.currentTempTextures == null ? this.textures : this.currentTempTextures;
	}

	public TotemDollData copy() {
		return new TotemDollData(this.nickname, this.textures.copy());
	}

	public void clearCurrentTempModel() {
		if (this.currentTempModel != null) {
			this.currentTempModel.resetPartsVisibility();
		}
		this.setCurrentTempMModel(null);
	}

	public void clearAllTempModels() {
		this.clearCurrentTempModel();
		this.currentTempModel = null;
		this.tempModels.clear();
	}

	public void clearCurrentTempTextures() {
		this.currentTempTextures = null;
	}

	public TotemDollData refreshBeforeRendering() {
		this.clearCurrentTempTextures();
		this.clearCurrentTempModel();
		TotemDollTextures textures = this.getTextures();
		textures.refreshBeforeRendering();
		TotemDollModel model = this.getModel();
		model.apply(textures);
		return this;
	}
}
