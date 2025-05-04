package net.lopymine.mtd.doll.data;

import lombok.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.EntityModels;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

import net.lopymine.mtd.config.totem.TotemDollArmsType;
import net.lopymine.mtd.utils.texture.PlayerSkinUtils;

import org.jetbrains.annotations.*;

@Getter
@Setter
public class TotemDollTextures {

	public static final Identifier STEVE_SKIN = Identifier.of("minecraft", "textures/entity/player/wide/steve.png");
	//? if >=1.21.2 {
	public static final Identifier ELYTRA_TEXTURE = Identifier.of("textures/entity/equipment/wings/elytra.png");
	//?} else {
	/*public static final Identifier ELYTRA_TEXTURE = Identifier.of("minecraft","textures/entity/elytra.png");
	*///?}

	@NotNull
	private LoadingState state = LoadingState.NOT_DOWNLOADED;

	@Nullable
	private Identifier skinTexture;
	@Nullable
	private Identifier capeTexture;
	@Nullable
	private Identifier elytraTexture;

	private TotemDollArmsType standardArmsType;
	private TotemDollArmsType armsType;

	public TotemDollTextures(@Nullable Identifier skinTexture, @Nullable Identifier capeTexture, @Nullable Identifier elytraTexture, TotemDollArmsType armsType) {
		this.skinTexture   = skinTexture;
		this.capeTexture   = capeTexture;
		this.elytraTexture = elytraTexture;
		this.armsType      = armsType;
	}

	public static TotemDollTextures create() {
		return new TotemDollTextures(null, null, null, TotemDollArmsType.WIDE);
	}

	public static TotemDollTextures of(AbstractClientPlayerEntity player) {
		//? if >=1.21 {
		net.minecraft.client.util.SkinTextures skinTextures = player.getSkinTextures();
		Identifier capeTexture = PlayerSkinUtils.remapTextureIfRequired(skinTextures.capeTexture());
		TotemDollTextures totemDollTextures = new TotemDollTextures(skinTextures.texture(), capeTexture, skinTextures.elytraTexture(), TotemDollArmsType.of(skinTextures.model().getName()));
		//?} else {
		/*Identifier capeTexture = PlayerSkinUtils.remapTextureIfRequired(player.getCapeTexture());
		TotemDollTextures totemDollTextures = new TotemDollTextures(player.getSkinTexture(), capeTexture, player.getElytraTexture(), TotemDollArmsType.of(player.getModel()));
		*///?}

		totemDollTextures.setState(LoadingState.DOWNLOADED);
		return totemDollTextures;
	}

	public void setStandardArmsType(TotemDollArmsType standardArmsType) {
		this.armsType = standardArmsType;
		this.standardArmsType = standardArmsType;
	}

	public TotemDollArmsType getArmsType() {
		return this.armsType == null ?
				this.standardArmsType == null ?
						TotemDollArmsType.WIDE
						:
						this.standardArmsType
				:
				this.armsType;
	}

	public @NotNull Identifier getSkinTexture() {
		return this.skinTexture == null || this.state != LoadingState.DOWNLOADED ? STEVE_SKIN : this.skinTexture;
	}

	public @NotNull Identifier getElytraTexture() {
		Identifier capeTexture = this.getCapeTexture();
		if (capeTexture != null) {
			return capeTexture;
		}
		Identifier elytraTexture = this.elytraTexture;
		if (elytraTexture != null) {
			return elytraTexture;
		}
		Identifier identifier = PlayerSkinUtils.remapTextureIfRequired(ELYTRA_TEXTURE);
		return identifier == null ? ELYTRA_TEXTURE : identifier;
	}

	public void destroy() {
		this.setState(LoadingState.DESTROYED);

		Identifier skinTexture = this.skinTexture;
		Identifier capeTexture = this.capeTexture;
		Identifier elytraTexture = this.elytraTexture;

		this.skinTexture = null;
		this.capeTexture = null;
		this.elytraTexture = null;

		MinecraftClient.getInstance().send(() -> {
			TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();

			if (skinTexture != null) {
				textureManager.destroyTexture(skinTexture);
			}

			if (capeTexture != null) {
				textureManager.destroyTexture(capeTexture);
			}

			if (elytraTexture != null) {
				textureManager.destroyTexture(elytraTexture);
			}
		});
	}

	public boolean canStartDownloading() {
		return this.state == LoadingState.ERROR || this.state == LoadingState.NOT_DOWNLOADED;
	}

	public TotemDollTextures copy() {
		TotemDollTextures totemDollTextures = new TotemDollTextures(this.skinTexture, this.capeTexture, this.elytraTexture, this.armsType);
		totemDollTextures.setState(this.state);
		return totemDollTextures;
	}

	public void refreshBeforeRendering() {
		this.armsType = this.standardArmsType;
	}
}
