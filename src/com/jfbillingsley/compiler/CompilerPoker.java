package com.jfbillingsley.compiler;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstancePoker;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.StdAttr;

public class CompilerPoker extends InstancePoker {
	public CompilerPoker() { }

	@Override
	public boolean init(InstanceState state, MouseEvent e) {
		return state.getInstance().getBounds().contains(e.getX(), e.getY());
	}

	@Override
	public void paint(InstancePainter painter) {
		Bounds bds = painter.getBounds();
		// BitWidth width = painter.getAttributeValue(StdAttr.WIDTH);
		// int len = (width.getWidth() + 3) / 4;

		// Graphics g = painter.getGraphics();
		// g.setColor(Color.RED);
		// int wid = 7 * len + 2; // width of caret rectangle
		// int ht = 16; // height of caret rectangle
		// g.drawRect(bds.getX() + (bds.getWidth() - wid) / 2,
		// 		bds.getY() + (bds.getHeight() - ht) / 2, wid, ht);
		// g.setColor(Color.BLACK);

		Graphics g = painter.getGraphics();
		g.setColor(Color.RED);
		g.drawRect(bds.getX() + 2, bds.getY() + 2, 10, 10);
	}

	@Override
	public void keyTyped(InstanceState state, KeyEvent e) {
		/*int val = Character.digit(e.getKeyChar(), 16);
		BitWidth width = state.getAttributeValue(StdAttr.WIDTH);
		if (val < 0 || (val & width.getMask()) != val) return;

		CompilerState cur = CompilerState.get(state, width);
		int newVal = (cur.getValue().toIntValue() * 16 + val) & width.getMask();
		Value newValue = Value.createKnown(width, newVal);
		cur.setValue(newValue);
		state.fireInvalidated();*/
	}
}
