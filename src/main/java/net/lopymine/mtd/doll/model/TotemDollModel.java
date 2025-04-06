package net.lopymine.mtd.doll.model;

import lombok.*;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.ArrayUtils;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.doll.data.TotemDollTextures;
import net.lopymine.mtd.model.base.*;
import net.lopymine.mtd.model.bb.manager.BlockBenchModelManager;
import java.util.*;
import java.util.function.*;
import org.jetbrains.annotations.*;

public class TotemDollModel extends Model {

	public static final Identifier TWO_D_MODEL_ID = MyTotemDoll.id("dolls/2d_doll.bbmodel");
	public static final Identifier THREE_D_MODEL_id = MyTotemDoll.id("dolls/3d_doll.bbmodel");

	@Getter
	private final MModel main;

	@Getter
	private final MModelCollection
			head,
			body,
			leftArmSlim,
			rightArmSlim,
			leftArmWide,
			rightArmWide,
			leftLeg,
			rightLeg;

	@Getter
	private final MModelCollection
			cape,
			elytra,
			ears;

	@Getter
	@Setter
	private boolean slim;

	@Getter(AccessLevel.PRIVATE)
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

	public void draw(MatrixStack matrices, int light, int overlay, /*? if >=1.21 {*/int color/*?} else {*//*float red, float green, float blue, float alpha *//*?}*/) {
		this.getDrawer().checkInitialization();

		MModelCollection leftArm = this.getLeftArm();
		MModelCollection rightArm = this.getRightArm();

		enableIfPresent(leftArm);
		enableIfPresent(rightArm);

		this.getMain().setDrawer(this.getDrawer());
		super.render(matrices, this.drawer.getMainVertexConsumer(), light, overlay, color);
		this.getMain().setDrawer(null);

		disableIfPresent(leftArm);
		disableIfPresent(rightArm);

		this.drawer.clear();
	}

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

	public Drawer getPreparedForRenderDrawer(VertexConsumerProvider provider, Identifier mainTexture) {
		if (this.drawer == null) {
			this.drawer = new Drawer();
		}
		this.drawer.setProvider(provider);
		this.drawer.setLayerFunction(this::getLayer);
		this.drawer.setCurrentMainVertexConsumer(() -> this.drawer.getProvider().getBuffer(this.drawer.getLayerFunction().apply(mainTexture)));
		return this.drawer;
	}

	public static class Drawer {

		@Getter
		private final Map<String, VertexConsumerGetter> requestedPartsWithTextureConsumer = new HashMap<>();
		@Setter
		@Nullable
		public VertexConsumerProvider provider;
		@Setter
		@Nullable
		private Function<Identifier, RenderLayer> layerFunction;
		private VertexConsumerGetter[] mainVertexConsumerGetters = new VertexConsumerGetter[0];

		public void requestPartWithTexture(String part, Identifier texture) {
			if (this.provider == null || this.layerFunction == null) {
				throw new IllegalArgumentException("MyTotemDoll Model Drawer was not initialized before rendering!");
			}
			this.requestedPartsWithTextureConsumer.put(part, () -> this.provider.getBuffer(this.layerFunction.apply(texture)));
		}

		public void requestPartWithTexture(String part, Supplier<Identifier> texture) {
			if (this.provider == null || this.layerFunction == null) {
				throw new IllegalArgumentException("MyTotemDoll Model Drawer was not initialized before rendering!");
			}
			this.requestedPartsWithTextureConsumer.put(part, () -> this.provider.getBuffer(this.layerFunction.apply(texture.get())));
		}

		public void clear() {
			this.provider = null;
			this.layerFunction = null;
			this.mainVertexConsumerGetters = new VertexConsumerGetter[0];
			this.requestedPartsWithTextureConsumer.clear();
		}

		public void checkInitialization() {
			if (this.provider == null || this.layerFunction == null) {
				throw new IllegalArgumentException("MyTotemDoll Model Drawer was not initialized before rendering!");
			}
		}

		public @NotNull VertexConsumerProvider getProvider() {
			if (this.provider == null) {
				throw new IllegalArgumentException("MyTotemDoll Model Drawer was not initialized before rendering!");
			}
			return this.provider;
		}

		public @NotNull Function<Identifier, RenderLayer> getLayerFunction() {
			if (this.layerFunction == null) {
				throw new IllegalArgumentException("MyTotemDoll Model Drawer was not initialized before rendering!");
			}
			return this.layerFunction;
		}

		public void setCurrentMainVertexConsumer(VertexConsumerGetter consumer) {
			this.mainVertexConsumerGetters = ArrayUtils.add(this.mainVertexConsumerGetters, consumer);
		}

		public void clearCurrentMainVertexConsumer() {
			if (this.mainVertexConsumerGetters.length == 1) {
				return;
			}
			this.mainVertexConsumerGetters = ArrayUtils.remove(this.mainVertexConsumerGetters, this.mainVertexConsumerGetters.length-1);
		}

		public VertexConsumer getMainVertexConsumer() {
			if (this.mainVertexConsumerGetters.length == 0) {
				throw new IllegalArgumentException("mainVertexConsumerGetters is empty for MyTotemDoll model!");
			}
			return this.mainVertexConsumerGetters[this.mainVertexConsumerGetters.length-1].get();
		}
	}

	public interface VertexConsumerGetter {
		VertexConsumer get();
	}
}