/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/. */

package com.cburch.logisim.std.wiring;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;

import com.cburch.logisim.comp.TextField;
import com.cburch.logisim.data.AbstractAttributeSet;
import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.std.io.Io;

public class TunnelAttributes extends AbstractAttributeSet {
	private static final List<Attribute<?>> ATTRIBUTES
		= Arrays.asList(new Attribute<?>[] {
			StdAttr.FACING, StdAttr.WIDTH, StdAttr.LABEL, StdAttr.LABEL_FONT, Io.ATTR_COLOR
		});

	private Direction facing;
	private BitWidth width;
	private String label;
	private Font labelFont;
	private Bounds offsetBounds;
	private boolean isLonely;
	private Color color;
	private int labelX;
	private int labelY;
	private int labelHAlign;
	private int labelVAlign;

	public TunnelAttributes() {
		facing = Direction.WEST;
		width = BitWidth.ONE;
		label = "";
		labelFont = StdAttr.DEFAULT_LABEL_FONT;
		offsetBounds = null;
		isLonely = true;
		color = Color.WHITE;
		configureLabel();
	}

	Direction getFacing() {
		return facing;
	}

	String getLabel() {
		return label;
	}

	Font getFont() {
		return labelFont;
	}

	Bounds getOffsetBounds() {
		return offsetBounds;
	}

	public boolean isLonely() {
		return isLonely;
	}

	public void setLonely(boolean b) {
		isLonely = b;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color c) {
		color = c;
	}

	int getLabelX() { return labelX; }
	int getLabelY() { return labelY; }
	int getLabelHAlign() { return labelHAlign; }
	int getLabelVAlign() { return labelVAlign; }

	boolean setOffsetBounds(Bounds value) {
		Bounds old = offsetBounds;
		boolean same = old == null ? value == null : old.equals(value);
		if (!same) {
			offsetBounds = value;
		}
		return !same;
	}

	@Override
	protected void copyInto(AbstractAttributeSet destObj) {
		; // nothing to do
	}

	@Override
	public List<Attribute<?>> getAttributes() {
		return ATTRIBUTES;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <V> V getValue(Attribute<V> attr) {
		if (attr == StdAttr.FACING) return (V) facing;
		if (attr == StdAttr.WIDTH) return (V) width;
		if (attr == StdAttr.LABEL) return (V) label;
		if (attr == StdAttr.LABEL_FONT) return (V) labelFont;
		if (attr == Io.ATTR_COLOR) return (V) color;
		return null;
	}

	@Override
	public <V> void setValue(Attribute<V> attr, V value) {
		Object oldValue = null;
		if (attr == StdAttr.FACING) {
			oldValue = facing;
			facing = (Direction) value;
			configureLabel();
		} else if (attr == StdAttr.WIDTH) {
			oldValue = width;
			width = (BitWidth) value;
		} else if (attr == StdAttr.LABEL) {
			oldValue = label;
			label = (String) value;
		} else if (attr == StdAttr.LABEL_FONT) {
			oldValue = labelFont;
			labelFont = (Font) value;
		} else if (attr == Io.ATTR_COLOR) {
			oldValue = color;
			color = (Color) value;
		} else {
			throw new IllegalArgumentException("unknown attribute");
		}
		offsetBounds = null;
		fireAttributeValueChanged(attr, value, (V) oldValue);
	}

	private void configureLabel() {
		Direction facing = this.facing;
		if (facing == Direction.NORTH) {
			labelX      = 0;
			labelY      = Tunnel.ARROW_MARGIN;
			labelHAlign = TextField.H_CENTER;
			labelVAlign = TextField.V_TOP;
		} else if (facing == Direction.SOUTH) {
			labelX      = 0;
			labelY      = -Tunnel.ARROW_MARGIN;
			labelHAlign = TextField.H_CENTER;
			labelVAlign = TextField.V_BOTTOM;
		} else if (facing == Direction.EAST) {
			labelX      = -Tunnel.ARROW_MARGIN;
			labelY      = 0;
			labelHAlign = TextField.H_RIGHT;
			labelVAlign = TextField.V_CENTER_OVERALL;
		} else {
			labelX      = Tunnel.ARROW_MARGIN;
			labelY      = 0;
			labelHAlign = TextField.H_LEFT;
			labelVAlign = TextField.V_CENTER_OVERALL;
		}
	}
}
