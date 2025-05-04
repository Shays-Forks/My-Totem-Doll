package net.lopymine.mtd.doll.model;

import lombok.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.doll.data.TotemDollTextures;
import net.lopymine.mtd.model.base.*;
import net.lopymine.mtd.model.bb.manager.BlockBenchModelManager;
import java.util.*;
import java.util.function.*;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class TotemDollModel extends Model {

	public static final Identifier TWO_D_MODEL_ID = MyTotemDoll.id("dolls/2d_doll.bbmodel");
	public static final Identifier THREE_D_MODEL_id = MyTotemDoll.id("dolls/3d_doll.bbmodel");

	private final MModel main;

	private final MModelCollection
			head,
			body,
			leftArmSlim,
			rightArmSlim,
			leftArmWide,
			rightArmWide,
			leftLeg,
			rightLeg;

	private final MModelCollection
			cape,
			elytra,
			ears;

	private boolean slim;

	private Drawer drawer;

	public TotemDollModel(MModel root, boolean slim) {
		super(/*? >=1.21.2 {*/ root, /*?}*/RenderLayer::getEntityTranslucent);

		this.head         = root.findModels("head");
		this.body         = root.findModels("body");
		this.leftArmSlim  = root.findModels("left_arm_slim");
		this.rightArmSlim = root.findModels("right_arm_slim");
		this.leftArmWide  = root.findModels("left_arm_wide");
		this.rightArmWide = root.findModels("right_arm_wide");
		this.leftLeg      = root.findModels("left_leg");
		this.rightLeg     = root.findModels("right_leg");

		this.cape   = root.findModels("cape");
		this.elytra = root.findModels("elytra");
		this.ears   = root.findModels("ears");

		this.main = root;
		this.slim = slim;

		disableIfPresent(this.leftArmSlim);
		disableIfPresent(this.rightArmSlim);
		disableIfPresent(this.leftArmWide);
		disableIfPresent(this.rightArmWide);

		this.resetPartsVisibility();
	}

	public static MModel createDollModel() {
		MModel model = BlockBenchModelManager.getModel(MyTotemDollClient.getConfig().getStandardTotemDollModelValue());
		MModel mmodel = model == null ? BlockBenchModelManager.getModel(THREE_D_MODEL_id) : model;
		if (mmodel == null) {
			throw new IllegalArgumentException("Failed to find standard doll model! [TotemDollModel.class]");
		}
		return mmodel;
	}

	public static void enableIfPresent(MModelCollection collection) {
		collection.setVisible(true);
	}

	public static void disableIfPresent(MModelCollection collection) {
		collection.setVisible(false);
	}

	public static void enableSkipRenderingIfPresent(MModelCollection collection) {
		collection.setSkipRendering(true);
	}

	public static void disableSkipRenderingIfPresent(MModelCollection collection) {
		collection.setSkipRendering(false);
	}

	public void resetPartsVisibility() {
		enableSkipRenderingIfPresent(this.cape);
		enableIfPresent(this.cape);

		enableSkipRenderingIfPresent(this.ears);
		enableIfPresent(this.ears);

		enableSkipRenderingIfPresent(this.elytra);
		disableIfPresent(this.elytra);
	}

	//? <=1.21.1 {

	/*@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, /^? if >=1.21 {^/ /^int color ^//^?} else {^/ float r, float g, float b, float a/^?}^/) {
		// NO-OP
	}

	*///?}

	public void apply(TotemDollTextures textures) {
		this.slim = textures.getArmsType().isSlim();
		this.resetPartsVisibility();
	}

	public MModelCollection getLeftArm() {
		return this.slim ? this.leftArmSlim : this.leftArmWide;
	}

	public MModelCollection getRightArm() {
		return this.slim ? this.rightArmSlim : this.rightArmWide;
	}

	public Drawer getDrawer() {
		if (this.drawer == null) {
			this.drawer = new Drawer(this);
		}
		this.drawer.prepareForRender();
		return this.drawer;
	}

	public static class Drawer {

		private final Map<String, Supplier<Identifier>> textures = new HashMap<>();
		private final Set<String> requestedParts = new HashSet<>();

		private final Function<Identifier, RenderLayer> layerFunction;
		private final TotemDollModel model;

		public Drawer(TotemDollModel model) {
			this.model         = model;
			this.layerFunction = model::getLayer;
		}

		public Drawer texture(String part, Identifier texture) {
			this.textures.put(part, () -> texture);
			return this;
		}

		public Drawer texture(String part, Supplier<Identifier> texture) {
			this.textures.put(part, texture);
			return this;
		}

		public Drawer requestDrawingPart(String part) {
			this.requestedParts.add(part);
			return this;
		}

		public void draw(MatrixStack matrices, VertexConsumerProvider provider, Identifier mainTexture, int light, int overlay, /*? if >=1.21 {*/int color/*?} else {*//*float red, float green, float blue, float alpha *//*?}*/) {
			MModelCollection leftArm = this.model.getLeftArm();
			MModelCollection rightArm = this.model.getRightArm();

			enableIfPresent(leftArm);
			enableIfPresent(rightArm);

			this.model.getMain().draw(matrices, provider, this.layerFunction, mainTexture, this.textures, this.requestedParts, light, overlay, /*? if >=1.21 {*/color/*?} else {*/ /*red, green, blue, alpha*//*?}*/);

			disableIfPresent(leftArm);
			disableIfPresent(rightArm);

			this.textures.clear();
		}

		public void prepareForRender() {
			this.textures.clear();
			this.requestedParts.clear();
		}
	}
}