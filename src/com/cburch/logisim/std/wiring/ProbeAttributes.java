/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/. */

package com.cburch.logisim.std.wiring;

import java.awt.Font;
import java.util.Arrays;
import java.util.List;

import com.cburch.logisim.circuit.RadixOption;
import com.cburch.logisim.data.AbstractAttributeSet;
import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.instance.StdAttr;

class ProbeAttributes extends AbstractAttributeSet {
	public static ProbeAttributes instance = new ProbeAttributes();

	private static final List<Attribute<?>> ATTRIBUTES
		= Arrays.asList(new Attribute<?>[] {
			StdAttr.FACING, RadixOption.ATTRIBUTE,
			StdAttr.LABEL, Pin.ATTR_LABEL_LOC, StdAttr.LABEL_FONT,
		});

	Direction facing = Direction.EAST;
	String label = "";
	Direction labelloc = Direction.WEST;
	Font labelfont = StdAttr.DEFAULT_LABEL_FONT;
	RadixOption radix = RadixOption.RADIX_2;
	BitWidth width = BitWidth.ONE;

	public ProbeAttributes() { }

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
	public <E> E getValue(Attribute<E> attr) {
		if (attr == StdAttr.FACING) return (E) facing;
		if (attr == StdAttr.LABEL) return (E) label;
		if (attr == Pin.ATTR_LABEL_LOC) return (E) labelloc;
		if (attr == StdAttr.LABEL_FONT) return (E) labelfont;
		if (attr == RadixOption.ATTRIBUTE) return (E) radix;
		return null;
	}

	@Override
	public <V> void setValue(Attribute<V> attr, V value) {
		Object oldValue = null;
		if (attr == StdAttr.FACING) {
			oldValue = facing;
			facing = (Direction) value;
		} else if (attr == StdAttr.LABEL) {
			oldValue = label;
			label = (String) value;
		} else if (attr == Pin.ATTR_LABEL_LOC) {
			oldValue = labelloc;
			labelloc = (Direction) value;
		} else if (attr == StdAttr.LABEL_FONT) {
			oldValue = labelfont;
			labelfont = (Font) value;
		} else if (attr == RadixOption.ATTRIBUTE) {
			oldValue = radix;
			radix = (RadixOption) value;
		} else {
			throw new IllegalArgumentException("unknown attribute");
		}
		fireAttributeValueChanged(attr, value, (V) oldValue);
	}
}


