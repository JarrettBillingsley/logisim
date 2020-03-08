package com.jfbillingsley.compiler;

import java.util.Arrays;

import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceState;

class CompilerState implements InstanceData, Cloneable {
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
}

