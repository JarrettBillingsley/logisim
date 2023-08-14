package com.jfbillingsley.compiler;

import java.awt.Graphics;
import java.util.Arrays;

import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.util.GraphicsUtil;

class CompilerState implements InstanceData, Cloneable {
	// Graphical constants
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int PADDING = 10;
	public static final int CODE_W = 220;
	public static final int CODE_H = 200;
	public static final int HEX_W = 40;
	public static final int HEX_H = CODE_H;
	public static final int ADDR_W = 40;
	public static final int ADDR_H = CODE_H;
	public static final int CODE_X = WIDTH - PADDING - CODE_W;
	public static final int CODE_Y = HEIGHT - PADDING - CODE_H;
	public static final int HEX_X = CODE_X - HEX_W;
	public static final int HEX_Y = CODE_Y;
	public static final int ADDR_X = HEX_X - ADDR_W;
	public static final int ADDR_Y = CODE_Y;
	public static final int COLUMN_LABEL_PADDING = 2;

	public static Bounds createOffsetBounds() {
		return Bounds.create(-WIDTH, -HEIGHT/2, WIDTH, HEIGHT);
	}

	public static CompilerState get(InstanceState state, CompilerCore core) {
		CompilerState ret = (CompilerState) state.getData();
		if(ret == null) {
			ret = new CompilerState(core);
			state.setData(ret);
		}
		return ret;
	}

	// -----------------------------------------------------------------------------------------

	private CompilerCore core;
	private int[] compiled;
	private String[] lines;
	private int curAddr = 0;

	public CompilerState(CompilerCore core) {
		this.core = core;
		this.compiled = new int[1 << core.getAddressBits()];
		this.compiled[0] = 10;
		this.compiled[1] = 20;
		this.compiled[2] = 30;
		this.lines = new String[] { "ld r0, x", "inc r0", "st x, r0" };
	}

	@Override
	public Object clone() {
		try {
			CompilerState ret = (CompilerState) super.clone();
			ret.compiled = Arrays.copyOf(this.compiled, this.compiled.length);
			return ret;
		}
		catch(CloneNotSupportedException e) {
			return null;
		}
	}

	public int getCurrent() {
		return curAddr;
	}

	public void setCurrent(int addr) {
		if(addr >= 0 && addr < compiled.length)
			curAddr = addr;
	}

	public void scrollToShow(int addr) {

	}

	public int getInstruction(int addr) {
		return (addr >= 0 && addr < compiled.length) ? compiled[addr] : 0;
	}

	// --------------------------------------------------------------------------------------------
	// Graphics

	private static void drawColumnLabel(Graphics g, String text, int x, int y) {
		GraphicsUtil.drawText(g, text, x, y - COLUMN_LABEL_PADDING,
			GraphicsUtil.H_LEFT, GraphicsUtil.V_BOTTOM);
	}

	public void paint(Graphics g, Bounds bds) {
		int x = bds.getX();
		int y = bds.getY();

		g.drawRect(x + CODE_X, y + CODE_Y, CODE_W, CODE_H);
		g.drawRect(x + HEX_X,  y + HEX_Y,  HEX_W,  HEX_H );
		drawColumnLabel(g, "Code", x + CODE_X, y + CODE_Y);
		drawColumnLabel(g, "Hex",  x + HEX_X,  y + HEX_Y);
		drawColumnLabel(g, "Addr", x + ADDR_X, y + ADDR_Y);
	}
}

