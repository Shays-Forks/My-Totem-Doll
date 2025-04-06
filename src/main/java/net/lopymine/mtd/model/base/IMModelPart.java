package net.lopymine.mtd.model.base;

import net.minecraft.util.Identifier;

import net.lopymine.mtd.doll.model.TotemDollModel.Drawer;

public interface IMModelPart {

	Drawer getDrawer();

	boolean isSkipRendering();

	String getName();

	Identifier getBuiltinTexture();

}
