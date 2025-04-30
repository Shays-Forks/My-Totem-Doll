package net.lopymine.mtd.extension;

import net.minecraft.client.model.ModelTransform;

public class ModelTransformExtension {

	public static ModelTransform subtract(ModelTransform root, ModelTransform parent) {
		return ModelTransform.of(
				getPivotX(root) - getPivotX(parent),
				getPivotY(root) - getPivotY(parent),
				getPivotZ(root) - getPivotZ(parent),
				getPitch(root),
				getYaw(root),
				getRoll(root)
		);
	}
	
	public static float getPivotX(ModelTransform transform) {
		return /*? if >=1.21.5 {*/ /*transform.x(); *//*?} elif >=1.21.2 {*/ transform.pivotX(); /*?} else {*/ /*transform.pivotX; *//*?}*/
	}

	public static float getPivotY(ModelTransform transform) {
		return /*? if >=1.21.5 {*/ /*transform.y(); *//*?} elif >=1.21.2 {*/ transform.pivotY(); /*?} else {*/ /*transform.pivotY; *//*?}*/
	}

	public static float getPivotZ(ModelTransform transform) {
		return /*? if >=1.21.5 {*/ /*transform.z(); *//*?} elif >=1.21.2 {*/ transform.pivotZ(); /*?} else {*/ /*transform.pivotZ; *//*?}*/
	}

	public static float getPitch(ModelTransform transform) {
		return /*? >=1.21.2 {*/ transform.pitch(); /*?} else {*/ /*transform.pitch; *//*?}*/
	}

	public static float getYaw(ModelTransform transform) {
		return /*? >=1.21.2 {*/ transform.yaw(); /*?} else {*/ /*transform.yaw; *//*?}*/
	}

	public static float getRoll(ModelTransform transform) {
		return /*? >=1.21.2 {*/ transform.roll(); /*?} else {*/ /*transform.roll; *//*?}*/
	}

	public static ModelTransform getBlockBenchedModelTransform(ModelTransform transform) {
		return ModelTransform.of(-getPivotX(transform), -getPivotY(transform), getPivotZ(transform), getPitch(transform), getYaw(transform), getRoll(transform));
	}

	public static String asString(ModelTransform transform) {
		return "%s %s %s | %s %s %s".formatted(getPivotX(transform), getPivotY(transform), getPivotZ(transform), getPitch(transform), getYaw(transform), getRoll(transform));
	}

}
