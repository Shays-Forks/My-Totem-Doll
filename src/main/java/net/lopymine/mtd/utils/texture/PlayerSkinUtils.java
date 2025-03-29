package net.lopymine.mtd.utils.texture;

import lombok.experimental.ExtensionMethod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.*;
import net.minecraft.resource.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.extension.PlayerSkinTextureExtension;

import java.io.*;
import java.nio.file.*;
import java.util.Optional;
import java.util.function.Supplier;
import org.jetbrains.annotations.*;

//? <=1.21.3 {
/*import net.minecraft.client.util.DefaultSkinHelper;
*///?}

@ExtensionMethod(PlayerSkinTextureExtension.class)
public class PlayerSkinUtils {

	public static void downloadSkin(@NotNull String textureUrl, @NotNull Identifier textureId, @Nullable SuccessAction onSuccessRegistration, @Nullable FailedAction onFailedRegistration, boolean cape, Path cachedTexturePath) {
		try {
			// TODO check with completable future
			Supplier<AbstractTexture> supplier = PlayerSkinUtils.download(cachedTexturePath, textureUrl, cape, textureId); // DO NOT CLOSE

			MinecraftClient.getInstance().send(() -> {
				AbstractTexture abstractTexture = supplier.get();

				//? <=1.21.3 {
				/*if (supplier instanceof PlayerSkinTexture playerSkinTexture) {
					playerSkinTexture.setOnSuccessAction(onSuccessRegistration);
					playerSkinTexture.setOnFailedAction(onFailedRegistration);
					if (cape) {
						playerSkinTexture.markAsCape();
					}
				}
				*///?}

				MinecraftClient.getInstance().getTextureManager().registerTexture(textureId, abstractTexture);
				//? >=1.21.4 {
				if (onSuccessRegistration != null) {
					onSuccessRegistration.onSuccess();
				}
				//?}
			});
		} catch (Exception e) {
			MyTotemDollClient.LOGGER.error("Failed to download skin texture with id \"%s\": ".formatted(textureId), e.getMessage());
			if (onFailedRegistration != null) {
				onFailedRegistration.onFailed(e.getMessage(), e);
			}
		}
	}

	private static Supplier<AbstractTexture> download(Path path, String uri, boolean cape, Identifier id) throws IOException {
		//? >=1.21.4 {
		NativeImage download = PlayerSkinTextureDownloader.download(path, uri);
		if (cape) {
			download = remapTextureToStandardSize(download, true);
		} else {
			download = PlayerSkinTextureDownloader.remapTexture(download, uri);
		}
		NativeImage image = download;
		return () -> new NativeImageBackedTexture(/*? if >=1.21.5 {*/ /*id::toString, *//*?}*/ image);
		//?} else {
		/*return () -> new PlayerSkinTexture(path.toFile(), uri, DefaultSkinHelper.getTexture(), true, () -> {
			try {
				if (path.toFile().exists()) {
					Files.delete(path);
				}
			} catch (FileSystemException ignored) {
			} catch (FileNotFoundException e) {
				if (MyTotemDollClient.getConfig().isDebugLogEnabled()) {
					MyTotemDollClient.LOGGER.warn("Failed to find temp texture file at {} to delete it", path);
				}
			} catch (Exception e) {
				if (MyTotemDollClient.getConfig().isDebugLogEnabled()) {
					MyTotemDollClient.LOGGER.error("Failed to delete temp texture file: ", e);
				}
			}
		});
		*///?}
	}

	@Nullable
	public static Identifier remapTextureIfRequired(@Nullable Identifier id) {
		if (id == null) {
			return null;
		}
		TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
		Identifier identifier = MyTotemDoll.id("remapped_textures/%s.png".formatted(MathHelper.abs(id.toString().hashCode())));
		AbstractTexture remappedTexture = textureManager.getTexture(identifier);
		if (remappedTexture instanceof NativeImageBackedTexture) {
			return identifier;
		}
		AbstractTexture texture = textureManager.getTexture(id);
		NativeImage image = null;
		if (texture instanceof NativeImageBackedTexture backedTexture) {
			image = backedTexture.getImage();
		}
		if (texture instanceof ResourceTexture) {
			ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
			try {
				InputStream open = resourceManager.open(id);
				image = NativeImage.read(open);
			} catch (Exception e) {
				MyTotemDollClient.LOGGER.error("Failed to read resource texture with id \"%s\"".formatted(id.toString()), e);
				return id;
			}
		}
		if (image == null || (image.getWidth() == 64 && image.getHeight() == 64)) {
			return id;
		}
		NativeImage remapped = remapTextureToStandardSize(image, false);
		textureManager.registerTexture(identifier, new NativeImageBackedTexture(/*? if >=1.21.5 {*/ /*identifier::toString, *//*?}*/ remapped));
		return identifier;
	}

	public static @NotNull NativeImage remapTextureToStandardSize(NativeImage image, boolean close) {
		NativeImage nativeImage = new NativeImage(64, 64, true);
		nativeImage.copyFrom(image);
		if (close) {
			image.close();
		}
		return nativeImage;
	}
}
