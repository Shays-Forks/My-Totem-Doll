package net.lopymine.mtd.doll.data;

import lombok.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.client.MyTotemDollClient;
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
					MModel value = response.value();
					this.tempModels.put(id, value);
					this.currentTempMModel = value;
				}
			});
			return;
		}
		this.currentTempMModel = model;
	}

	public TotemDollModel getModel() {
		if (this.currentTempMModel != null) {
			if (this.currentTempModel == null || !this.currentTempModel.getMain().equals(this.currentTempMModel)) {
				this.currentTempModel = new TotemDollModel(this.currentTempMModel, this.textures.getArmsType().isSlim());
			}
			return this.currentTempModel;
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
		this.currentTempMModel = null;
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
		TotemDollModel model = this.getModel();
		model.apply(textures);
		return this;
	}
}
