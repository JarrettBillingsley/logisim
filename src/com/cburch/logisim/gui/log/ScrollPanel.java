/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/. */

package com.cburch.logisim.gui.log;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JScrollPane;

import com.cburch.logisim.data.Value;

class ScrollPanel extends LogPanel {
	private class MyListener implements ActionListener, ModelListener {
		public void actionPerformed(ActionEvent event) {
			Object src = event.getSource();
			if (src == clear) {
				getModel().clearLog();
			}
		}

		public void selectionChanged(ModelEvent event) {}
		public void entryAdded(ModelEvent event, Value[] values) {}
		public void filePropertyChanged(ModelEvent event) {
			clear.setEnabled(!getModel().isFileEnabled());
		}
		public void logCleared(ModelEvent event) {}
	}

	private TablePanel table;
	private MyListener myListener = new MyListener();
	private JButton clear = new JButton();

	public ScrollPanel(LogFrame frame) {
		super(frame);
		this.table = new TablePanel(frame);
		JScrollPane pane = new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.setVerticalScrollBar(table.getVerticalScrollBar());
		setLayout(new BorderLayout());
		add(pane, BorderLayout.CENTER);
		add(clear, BorderLayout.SOUTH);

		clear.addActionListener(myListener);
	}

	@Override
	public String getTitle() {
		return table.getTitle();
	}

	@Override
	public String getHelpText() {
		return table.getHelpText();
	}

	@Override
	public void localeChanged() {
		table.localeChanged();
		clear.setText(Strings.get("clearButton"));
	}

	@Override
	public void modelChanged(Model oldModel, Model newModel) {
		table.modelChanged(oldModel, newModel);
	}
}
