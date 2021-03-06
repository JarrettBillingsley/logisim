/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/. */

package com.cburch.logisim.std.wiring;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import com.cburch.logisim.comp.TextField;
import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Location;
import com.cburch.logisim.instance.Instance;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.tools.key.BitWidthConfigurator;
import com.cburch.logisim.util.GraphicsUtil;

public class Tunnel extends InstanceFactory {
	public static final Tunnel FACTORY = new Tunnel();

	static final int MARGIN = 3;            // space between text and outer border
	static final int ARROW_MARGIN = 5;      // space from pin to label text
	static final int ARROW_DEPTH = 4;       // space from pin to beginning of box part
	static final int ARROW_MIN_WIDTH = 16;  // basically, minimum height of E/W facing label
	static final int ARROW_MAX_WIDTH = 20;  // widest the arrow part of a big label will be drawn
	static final int MIN_DIM = ARROW_MIN_WIDTH - 2 * MARGIN;

	public Tunnel() {
		super("Tunnel", Strings.getter("tunnelComponent"));
		setIconName("tunnel.gif");
		setFacingAttribute(StdAttr.FACING);
		setKeyConfigurator(new BitWidthConfigurator(StdAttr.WIDTH));
	}

	@Override
	public AttributeSet createAttributeSet() {
		return new TunnelAttributes();
	}

	@Override
	public Bounds getOffsetBounds(AttributeSet attrsBase) {
		TunnelAttributes attrs = (TunnelAttributes) attrsBase;
		Bounds bds = attrs.getOffsetBounds();
		if (bds != null) {
			return bds;
		} else {
			// my guess is that it does this because it has to return *something*
			// but doesn't know what the *real* bounds are until the first time it's
			// been drawn. so, this is just an estimate?
			int ht = attrs.getFont().getSize();
			int wd = ht * attrs.getLabel().length() / 2;
			bds = computeBounds(attrs, wd, ht);
			attrs.setOffsetBounds(bds);
			return bds;
		}
	}

	//
	// graphics methods
	//
	@Override
	public void paintGhost(InstancePainter painter) {
		TunnelAttributes attrs = (TunnelAttributes) painter.getAttributeSet();
		Direction facing = attrs.getFacing();
		String label = attrs.getLabel();

		Graphics g = painter.getGraphics();
		g.setFont(attrs.getFont());
		FontMetrics fm = g.getFontMetrics();
		Bounds expanded = computeBounds(
			attrs,
			fm.stringWidth(label),
			fm.getAscent() + fm.getDescent());
		if (attrs.setOffsetBounds(expanded)) {
			Instance instance = painter.getInstance();
			if (instance != null) instance.recomputeBounds();
		}

		int x0 = expanded.getX();
		int y0 = expanded.getY();
		int x1 = x0 + expanded.getWidth();
		int y1 = y0 + expanded.getHeight();
		int mw = ARROW_MAX_WIDTH / 2;
		int[] xp;
		int[] yp;
		if (facing == Direction.NORTH) {
			int yb = y0 + ARROW_DEPTH;
			if (x1 - x0 <= ARROW_MAX_WIDTH) {
				xp = new int[] { x0, 0,  x1, x1, x0 };
				yp = new int[] { yb, y0, yb, y1, y1 };
			} else {
				xp = new int[] { x0, -mw, 0,  mw, x1, x1, x0 };
				yp = new int[] { yb, yb,  y0, yb, yb, y1, y1 };
			}
		} else if (facing == Direction.SOUTH) {
			int yb = y1 - ARROW_DEPTH;
			if (x1 - x0 <= ARROW_MAX_WIDTH) {
				xp = new int[] { x0, x1, x1, 0,  x0 };
				yp = new int[] { y0, y0, yb, y1, yb };
			} else {
				xp = new int[] { x0, x1, x1, mw, 0,  -mw, x0 };
				yp = new int[] { y0, y0, yb, yb, y1, yb,  yb };
			}
		} else if (facing == Direction.EAST) {
			int xb = x1 - ARROW_DEPTH;
			if (y1 - y0 <= ARROW_MAX_WIDTH) {
				xp = new int[] { x0, xb, x1, xb, x0 };
				yp = new int[] { y0, y0, 0,  y1, y1 };
			} else {
				xp = new int[] { x0, xb, xb,  x1, xb, xb, x0 };
				yp = new int[] { y0, y0, -mw, 0,  mw,  y1, y1 };
			}
		} else {
			int xb = x0 + ARROW_DEPTH;
			if (y1 - y0 <= ARROW_MAX_WIDTH) {
				xp = new int[] { xb, x1, x1, xb, x0 };
				yp = new int[] { y0, y0, y1, y1, 0  };
			} else {
				xp = new int[] { xb, x1, x1, xb, xb, x0, xb  };
				yp = new int[] { y0, y0, y1, y1, mw, 0,  -mw };
			}
		}

		Color saveColor = g.getColor();

		if (attrs.isLonely()) {
			g.setColor(attrs.getColor().brighter());
		} else {
			g.setColor(attrs.getColor());
		}

		g.fillPolygon(xp, yp, xp.length);

		g.setColor(saveColor);
		GraphicsUtil.switchToWidth(g, 2);
		g.drawPolygon(xp, yp, xp.length);
		GraphicsUtil.drawText(g, label,
			attrs.getLabelX(), attrs.getLabelY(),
			attrs.getLabelHAlign(), attrs.getLabelVAlign());
	}

	@Override
	public void paintInstance(InstancePainter painter) {
		Location loc = painter.getLocation();
		int x = loc.getX();
		int y = loc.getY();
		Graphics g = painter.getGraphics();
		g.translate(x, y);

		TunnelAttributes attrs = (TunnelAttributes) painter.getAttributeSet();

		if (attrs.isLonely()) {
			g.setColor(Color.GRAY);
		} else {
			g.setColor(Color.BLACK);
		}

		paintGhost(painter);
		g.translate(-x, -y);
		painter.drawPorts();
	}

	//
	// methods for instances
	//
	@Override
	protected void configureNewInstance(Instance instance) {
		instance.addAttributeListener();
		instance.setPorts(new Port[] {
				new Port(0, 0, Port.INOUT, StdAttr.WIDTH)
			});
		configureLabel(instance);
	}

	@Override
	protected void instanceAttributeChanged(Instance instance, Attribute<?> attr) {
		if (attr == StdAttr.FACING) {
			configureLabel(instance);
			instance.recomputeBounds();
		} else if (attr == StdAttr.LABEL || attr == StdAttr.LABEL_FONT) {
			instance.recomputeBounds();
		}
	}

	@Override
	public void propagate(InstanceState state) {
		; // nothing to do - handled by circuit
	}

	//
	// private methods
	//
	private void configureLabel(Instance instance) {
		TunnelAttributes attrs = (TunnelAttributes) instance.getAttributeSet();
		Location loc = instance.getLocation();
		instance.setTextField(StdAttr.LABEL, StdAttr.LABEL_FONT,
				loc.getX() + attrs.getLabelX(), loc.getY() + attrs.getLabelY(),
				attrs.getLabelHAlign(), attrs.getLabelVAlign());
	}

	private Bounds computeBounds(TunnelAttributes attrs, int textWidth, int textHeight) {
		int bw;
		int bh;
		int bx;
		int by;
		switch (attrs.getLabelHAlign()) {
			case TextField.H_LEFT:
				bw = ARROW_MARGIN + Math.max(MIN_DIM, textWidth) + MARGIN;
				bx = 0;
				break;
			case TextField.H_RIGHT:
				bw = ARROW_MARGIN + Math.max(MIN_DIM, textWidth) + MARGIN;
				bx = -bw;
				break;
			default:
				bw = Math.max(MIN_DIM, textWidth) + 2 * MARGIN;
				bx = -(bw / 2);
		}
		switch (attrs.getLabelVAlign()) {
			case TextField.V_TOP:
				bh = ARROW_MARGIN + Math.max(MIN_DIM, textHeight) + MARGIN;
				by = 0;
				break;
			case TextField.V_BOTTOM:
				bh = ARROW_MARGIN + Math.max(MIN_DIM, textHeight) + MARGIN;
				by = -bh;
				break;
			default:
				bh = Math.max(MIN_DIM, textHeight) + 2 * MARGIN;
				by = -(bh / 2);
		}

		return Bounds.create(bx, by, bw, bh);
	}
}