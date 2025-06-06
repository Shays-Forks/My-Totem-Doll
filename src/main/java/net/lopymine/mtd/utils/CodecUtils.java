package net.lopymine.mtd.utils;

import com.google.gson.*;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;
import java.util.function.*;

import static net.lopymine.mtd.client.MyTotemDollClient.LOGGER;

public final class CodecUtils {

	public static <A> Codec<A> recursive(String name, Function<Codec<A>, Codec<A>> wrapped) {
		//? if >=1.21 {
		return Codec.recursive(name, wrapped);
		//?} else {
		/*return new RecursiveCodec<>(name, wrapped);
		*///?}
	}

	public static <A, B> RecordCodecBuilder<A, B> optional(String optionId, B defValue, Codec<B> codec, Function<A, B> getter) {
		return codec.optionalFieldOf(optionId).xmap((o) -> o.orElse(defValue), Optional::ofNullable).forGetter(getter);
	}

	public static <A, B> RecordCodecBuilder<A, B> option(String optionId, Codec<B> codec, Function<A, B> getter) {
		return codec.fieldOf(optionId).forGetter(getter);
	}

	public static <T> void decode(Codec<T> codec, JsonElement o, Consumer<T> consumer) {
		try {
			T value = codec.decode(JsonOps.INSTANCE, o)/*? if >=1.20.5 {*/.getOrThrow()/*?} else {*//*.getOrThrow(false, LOGGER::error)*//*?}*/.getFirst();
			consumer.accept(value);
		} catch (Exception e) {
			LOGGER.warn("Failed to decode JsonElement:", e);
		}
	}

	public static <T> T decode(String id, Codec<T> codec, JsonObject o) {
		if (o.has(id)) {
			try {
				return codec.decode(JsonOps.INSTANCE, o.get(id))/*? if >=1.20.5 {*/.getOrThrow()/*?} else {*//*.getOrThrow(false, LOGGER::error)*//*?}*/.getFirst();
			} catch (Exception e) {
				LOGGER.warn("Failed to decode \"%s\" from JsonObject:".formatted(id), e);
			}
		}
		return null;
	}

	public static <T> T decode(String id, T fallback, Codec<T> codec, JsonObject o) {
		if (o.has(id)) {
			try {
				return codec.decode(JsonOps.INSTANCE, o.get(id))/*? if >=1.20.5 {*/.getOrThrow()/*?} else {*//*.getOrThrow(false, LOGGER::error)*//*?}*/.getFirst();
			} catch (Exception e) {
				LOGGER.warn("Failed to decode \"%s\" from JsonObject:".formatted(id), e);
			}
		}
		return fallback;
	}

}
