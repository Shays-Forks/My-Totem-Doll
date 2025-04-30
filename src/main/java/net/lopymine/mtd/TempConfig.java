package net.lopymine.mtd;

//? if =1.21.4 {
import com.google.gson.*;
import lombok.*;
import net.minecraft.util.Identifier;
import org.slf4j.*;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;

import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.config.other.vector.Vec2i;
import net.lopymine.mtd.config.rendering.RenderingConfig;
import net.lopymine.mtd.config.totem.*;
import net.lopymine.mtd.doll.model.TotemDollModel;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

import static net.lopymine.mtd.utils.CodecUtils.option;
import static net.lopymine.mtd.utils.CodecUtils.optional;

@Getter
@Setter
@AllArgsConstructor
public class TempConfig {

	public static final Codec<TempConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("title", Codec.BOOL, TempConfig::isTitle),
			option("b1", Codec.BOOL, TempConfig::isB1),
			option("b2", Codec.BOOL, TempConfig::isB2),
			option("b3", Codec.BOOL, TempConfig::isB3)
	).apply(instance, TempConfig::new));

	private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(MyTotemDoll.MOD_ID + "-temp.json5").toFile();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Logger LOGGER = LoggerFactory.getLogger(MyTotemDoll.MOD_NAME + "/Config");

	private boolean title;
	private boolean b1;
	private boolean b2;
	private boolean b3;

	public TempConfig() {
		this.title = false;
		this.b1    = false;
		this.b2    = false;
		this.b3    = false;
	}

	public static TempConfig getInstance() {
		return TempConfig.read();
	}

	private static @NotNull TempConfig create() {
		TempConfig config = new TempConfig();
		try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
			String json = GSON.toJson(CODEC.encode(config, JsonOps.INSTANCE, JsonOps.INSTANCE.empty())/*? if >=1.20.5 {*/.getOrThrow());/*?} else*//*.getOrThrow(false, LOGGER::error));*/
			writer.write(json);
		} catch (Exception e) {
			LOGGER.error("Failed to create config", e);
		}
		return config;
	}

	private static TempConfig read() {
		if (!CONFIG_FILE.exists()) {
			return TempConfig.create();
		}

		try (FileReader reader = new FileReader(CONFIG_FILE, StandardCharsets.UTF_8)) {
			return CODEC.decode(JsonOps.INSTANCE, JsonParser.parseReader(reader))/*? if >=1.20.5 {*/.getOrThrow()/*?} else {*//*.getOrThrow(false, LOGGER::error)*//*?}*/.getFirst();
		} catch (Exception e) {
			LOGGER.error("Failed to read config", e);
		}
		return TempConfig.create();
	}

	public void save() {
		MyTotemDollClient.setTempConfig(this);
		CompletableFuture.runAsync(() -> {
			try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
				String json = GSON.toJson(CODEC.encode(this, JsonOps.INSTANCE, JsonOps.INSTANCE.empty())/*? if >=1.20.5 {*/.getOrThrow());/*?} else*//*.getOrThrow(false, LOGGER::error));*/
				writer.write(json);
			} catch (Exception e) {
				LOGGER.error("Failed to save config", e);
			}
		});
	}
}

//?}