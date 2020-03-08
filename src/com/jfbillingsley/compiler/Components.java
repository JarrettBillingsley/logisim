package com.jfbillingsley.compiler;

import java.util.Arrays;
import java.util.List;

import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.tools.Library;

public class Components extends Library {
	private List<AddTool> tools;

	public Components() {
		tools = Arrays.asList(new AddTool[] {
			new AddTool(new Compiler()),
		});
	}

	@Override
	public String getDisplayName() {
		return "CS0447 Compiler";
	}

	@Override
	public List<AddTool> getTools() {
		return tools;
	}
}

