/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/. */

package com.cburch.logisim.data;

public class AttributeEvent {
	private AttributeSet source;
	private Attribute<?> attr;
	private Object value;
	private Object oldValue;

	public AttributeEvent(AttributeSet source, Attribute<?> attr,
			Object value) {
		this.source = source;
		this.attr = attr;
		this.value = value;
		this.oldValue = value;
	}

	public AttributeEvent(AttributeSet source, Attribute<?> attr,
			Object value, Object oldValue) {
		this.source = source;
		this.attr = attr;
		this.value = value;
		this.oldValue = oldValue;
	}

	public AttributeEvent(AttributeSet source) {
		this(source, null, null);
	}

	public Attribute<?> getAttribute() { return attr; }

	public AttributeSet getSource() { return source; }

	public Object getValue() { return value; }

	public Object getOldValue() { return oldValue; }
}
