package net.lopymine.mtd.tag.manager;

import net.minecraft.text.Text;
import net.minecraft.util.*;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.doll.data.TotemDollData;
import net.lopymine.mtd.doll.model.TotemDollModel;
import net.lopymine.mtd.model.base.MModel;
import net.lopymine.mtd.pack.TotemDollModelFinder;
import net.lopymine.mtd.tag.*;

import java.util.*;
import java.util.stream.*;
import org.jetbrains.annotations.*;

public class TagsManager {

	private static final Map<Character, CustomModelTag> CUSTOM_MODEL_IDS_TAGS = new LinkedHashMap<>();
	private static final Map<Character, Tag> PREPROCESSOR_TAGS = new LinkedHashMap<>();
	private static final Map<Character, Tag> POSTPROCESSOR_TAGS = new LinkedHashMap<>();

	public static Map<Character, Tag> getTags() {
		Map<Character, Tag> tags = new LinkedHashMap<>(PREPROCESSOR_TAGS);
		tags.putAll(POSTPROCESSOR_TAGS);
		return tags;
	}

	public static Map<Character, CustomModelTag> getCustomModelIdsTags() {
		return CUSTOM_MODEL_IDS_TAGS;
	}

	public static void register() {
		// We need pre- and post-tags to avoid unnecessary overriding values in a model

		registerPreprocessorTag(
				Tag.startBuilder('0')
						.setAction((data) -> data.getModel().setSlim(true))
						.build()
		);

		registerPreprocessorTag(
				Tag.startBuilder('1')
						.setAction((data) -> data.getModel().setSlim(false))
						.build()
		);

		registerPostprocessorTag(
				Tag.startBuilder('2')
						.setAction((data) -> {
							TotemDollModel.disableIfPresent(data.getModel().getCape());
						})
						.build()
		);

		registerPostprocessorTag(
				Tag.startBuilder('3')
						.setAction((data) -> {
							TotemDollModel.disableIfPresent(data.getModel().getCape());
							TotemDollModel.enableIfPresent(data.getModel().getElytra());
						})
						.build()
		);

	}

	public static void reloadCustomModelIdsTags() {
		Collection<Set<Identifier>> values = TotemDollModelFinder.getFoundedTotemModels().values();
		Set<Character> characters = getTags().keySet();
		TagsGenerator generator = new TagsGenerator();

		CUSTOM_MODEL_IDS_TAGS.clear();
		registerBuiltinCustomModels();
		for (Set<Identifier> value : values) {
			for (Identifier id : value) {

				Character next = null;
				while (generator.hasNext()) {
					Character character = generator.next();
					if (characters.contains(character)) {
						continue;
					}
					next = character;
					break;
				}

				if (next == null) {
					return;
				}

				CUSTOM_MODEL_IDS_TAGS.put(next,
						CustomModelTag.startBuilder(next, id)
								.setAction((data) -> data.setTempModel(id))
								.build()
				);
			}
		}
	}

	private static void registerBuiltinCustomModels() {
		registerBuiltinCustomModel('j', "2d_doll");
		registerBuiltinCustomModel('k', "3d_doll");
		registerBuiltinCustomModel('l', "3d_funko");
		registerBuiltinCustomModel('m', "3d_doll_with_party_hat_3");
		registerBuiltinCustomModel('n', "hat");
	}

	private static void registerBuiltinCustomModel(char ch, String modelName) {
		Identifier modelId = MyTotemDoll.getDollModelId(modelName);
		CustomModelTag tag = CustomModelTag.startBuilder(ch, modelId)
				.setAction((data) -> data.setTempModel(modelId))
				.build();
		CUSTOM_MODEL_IDS_TAGS.put(ch, tag);
	}

	public static void registerPostprocessorTag(Tag tag) {
		POSTPROCESSOR_TAGS.put(tag.getTag(), tag);
	}

	public static void registerPreprocessorTag(Tag tag) {
		PREPROCESSOR_TAGS.put(tag.getTag(), tag);
	}

	public static String getNicknameOrSkinProviderFromName(String name) {
		return getDataFromString(name)[0];
	}

	@Nullable
	public static String getTagsFromName(String name) {
		return getDataFromString(name)[1];
	}

	public static String[] getDataFromString(String name) {
		String[] split = name.split("\\|");
		String o = split[0].trim();

		if (TagsSkinProviders.isProvider(o) && split.length >= 2) {
			String value = split[1].trim();
			String tags = split.length >= 3 ? split[2].trim() : null;
			return new String[]{joinData(o, value), tags};
		}

		String tags = split.length >= 2 ? split[1].trim() : null;
		return new String[]{o, tags};
	}

	public static void processTags(String tags, @NotNull TotemDollData data) {
		processCustomModelIdsTags(tags, data);
		processPreTags(tags, data);
		processPostTags(tags, data);
	}

	public static void processCustomModelIdsTags(String tags, TotemDollData data) {
		processTags(tags, data, CUSTOM_MODEL_IDS_TAGS);
	}

	public static void processPreTags(String tags, @NotNull TotemDollData data) {
		processTags(tags, data, PREPROCESSOR_TAGS);
	}

	public static void processPostTags(String tags, @NotNull TotemDollData data) {
		processTags(tags, data, POSTPROCESSOR_TAGS);
	}

	public static <E extends Tag> void processTags(String tags, @NotNull TotemDollData data, Map<Character, E> map) {
		getTagsStream(tags).forEach((character) -> {
			Tag tag = map.get(character);
			if (tag == null) {
				return;
			}
			tag.process(data);
		});
	}

	public static Stream<Character> getTagsStream(String tags) {
		return tags.trim().chars().mapToObj(i -> (char) i).distinct();
	}

	public static String addTag(String string, Character tag) {
		String[] data = getDataFromString(string);
		if (data.length < 2) {
			return string;
		}

		String tags = data[1];
		String unsortedTags = tags == null ? String.valueOf(tag) : tags + tag;
		data[1] = sortTags(unsortedTags);
		return joinData(data);
	}

	private static String sortTags(String unsortedTags) {
		return getTagsStream(unsortedTags).sorted().map(String::valueOf).collect(Collectors.joining());
	}

	private static String joinData(String... data) {
		return String.join(" | ", data);
	}

	public static String removeTag(String name, Character tag) {
		String[] data = getDataFromString(name);
		if (data.length < 2) {
			return name;
		}
		String tags = data[1];
		data[1] = tags == null ? "" : tags.replace(String.valueOf(tag), "");

		if (data[1].isEmpty()) {
			return data[0].trim();
		}
		return joinData(data);
	}

	public static Identifier getTagIcon(Character character) {
		if (hasTag(CUSTOM_MODEL_IDS_TAGS, character)) {
			return MyTotemDoll.id("textures/gui/tags/unknown.png");
		}
		return MyTotemDoll.id("textures/gui/tags/%s.png".formatted(character));
	}

	public static Text getTagDescription(Character character) {
		return MyTotemDoll.text("tags.%s".formatted(character));
	}

	public static Text getAppliedTagDescription(Character character) {
		return MyTotemDoll.text("tags.%s.applied".formatted(character));
	}

	public static boolean hasTag(Character character) {
		return hasTag(getTags(), character);
	}

	public static <E extends Tag> boolean hasTag(Map<Character, E> tags, Character character) {
		return tags.containsKey(character);
	}

}
