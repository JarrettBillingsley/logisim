/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/. */

package com.cburch.logisim.gui.menu;

public class LogisimMenuItem {
	private String name;
	private boolean isCheck;

	LogisimMenuItem(String name) {
		this.name = name;
		this.isCheck = false;
	}

	LogisimMenuItem(String name, boolean isCheck) {
		this.name = name;
		this.isCheck = isCheck;
	}

	@Override
	public String toString() {
		return name;
	}

	public boolean isCheck() {
		return this.isCheck;
	}
}
