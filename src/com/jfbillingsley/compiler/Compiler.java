package com.jfbillingsley.compiler;

import java.net.URL;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Graphics;

import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.Instance;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.util.GraphicsUtil;
import com.cburch.logisim.util.StringUtil;

class Compiler extends InstanceFactory {
	// Port constants
	public static final int DATA = 0;
	public static final int ADDR = 1;
	public static final int NUM_PORTS = 2;

	public static final int PORT_DELAY = 10;

	// Compiler-related constants
	public static final CompilerCore core = new CompilerCore2204();

	private final int ADDRESS_WIDTH;
	private final int INSTRUCTION_WIDTH;
	private final BitWidth inBitWidth;
	private final BitWidth outBitWidth;

	public Compiler() {
		super(String.format("Compiler (%s)", core.getName()));

		ADDRESS_WIDTH = core.getAddressBits();
		INSTRUCTION_WIDTH = core.getInstructionBits();
		inBitWidth = BitWidth.create(ADDRESS_WIDTH);
		outBitWidth = BitWidth.create(INSTRUCTION_WIDTH);

		Bounds bds = CompilerState.createOffsetBounds();
		setOffsetBounds(bds);

		Port[] ps = new Port[NUM_PORTS];
		ps[ADDR] = new Port(-bds.getWidth(), 0, Port.INPUT,  ADDRESS_WIDTH);
		ps[DATA] = new Port(              0, 0, Port.OUTPUT, INSTRUCTION_WIDTH);
		setPorts(ps);

		setInstancePoker(CompilerPoker.class);

		// URL url = getClass().getClassLoader().getResource("com/cburch/gray/counter.gif");
		// if(url != null) setIcon(new ImageIcon(url));

		/*
		MemMenu is the special popup menu for memory components. It's bound to an instance.
		MemPoker is the poker. It has two subclasses, DataPoker and AddrPoker.
		MemState is the instance state; it has a MemContents.
		MemContents abstracts the pages; MemContentsSub abstracts a single page using an
			appropriately sized array based on the memory width.
		Mem is the abstract base class instance factory for both RAM and ROM.
		Rom extends Mem and returns RomAttributes.
		RomAttributes is the ROM attribute set, and also manages popup windows and change listeners.
		RomContentsListener is used to listen for changes by the hex editor.

		BRAIN COREDUMP:
			CompilerState holds the state for one instance of a Compiler component.
			CompilerCore is an interface that can be used for several different compilers.
			Adding a new compiler would mean implementing that interface, and using that as the
				"core" variable above.
			The rest of the component just needs to:
				- Display the code
				- Display the compiled version of it next to it (with proper line info)
				- Allow the user to load programs, give them to the compiler core, and get an error
				message if it failed
			Yee

			ALSO remove the "just for testing" stuff in Builtin.java

		Then, to make the library:
			Make a jar file with META-INF/MANIFEST.MF:
				Manifest-Version: 1.0
				Library-Class: com.jfbillingsley.compiler.Components

			(don't forget newline after last line)
			and the class files
			and that's it.
		*/
	}

	@Override
	protected void configureNewInstance(Instance instance) {

	}

	@Override
	public void propagate(InstanceState state) {
		CompilerState myState = CompilerState.get(state, core);
		Value addrValue = state.getPort(ADDR);
		int addr = addrValue.toIntValue();

		if(!addrValue.isFullyDefined() || addr < 0)
			return;
		if(addr != myState.getCurrent()) {
			myState.setCurrent(addr);
			myState.scrollToShow(addr);
		}

		int val = myState.getInstruction(addr);
		state.setPort(DATA, Value.createKnown(outBitWidth, val), PORT_DELAY);
	}

	@Override
	public void paintInstance(InstancePainter painter) {
		Graphics g = painter.getGraphics();
		painter.drawBounds();
		painter.drawPort(DATA);
		painter.drawPort(ADDR);

		if(painter.getShowState()) {
			CompilerState state = CompilerState.get(painter, core);
			state.paint(painter.getGraphics(), painter.getBounds());
		} else {
			Bounds bds = painter.getBounds();
			GraphicsUtil.drawCenteredText(g, "Haha who prints circuits?",
				bds.getCenterX(), bds.getCenterY());
		}
	}
}
