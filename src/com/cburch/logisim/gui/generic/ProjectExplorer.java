/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/. */

package com.cburch.logisim.gui.generic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.circuit.SubcircuitFactory;
import com.cburch.logisim.comp.ComponentFactory;
import com.cburch.logisim.comp.ComponentDrawContext;
import com.cburch.logisim.gui.main.Canvas;
import com.cburch.logisim.prefs.AppPreferences;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.proj.ProjectEvent;
import com.cburch.logisim.proj.ProjectListener;
import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.tools.Library;
import com.cburch.logisim.tools.Tool;
import com.cburch.logisim.util.LocaleListener;
import com.cburch.logisim.util.LocaleManager;

public class ProjectExplorer extends JTree implements LocaleListener {
	private static final String DIRTY_MARKER = "*";
	
	public static final Color MAGNIFYING_INTERIOR = new Color(200, 200, 255, 64);

	private class ToolIcon implements Icon {
		Tool tool;
		Circuit circ = null;

		ToolIcon(Tool tool) {
			this.tool = tool;
			if (tool instanceof AddTool) {
				ComponentFactory fact = ((AddTool) tool).getFactory(false);
				if (fact instanceof SubcircuitFactory) {
					circ = ((SubcircuitFactory) fact).getSubcircuit();
				}
			}
		}

		public int getIconHeight() {
			return 20;
		}

		public int getIconWidth() {
			return 20;
		}

		public void paintIcon(java.awt.Component c, Graphics g,
				int x, int y) {
			// draw halo if appropriate
			if (tool == haloedTool && AppPreferences.ATTRIBUTE_HALO.getBoolean()) {
				g.setColor(Canvas.HALO_COLOR);
				g.fillRoundRect(x, y, 20, 20, 10, 10);
				g.setColor(Color.BLACK);
			}

			// draw tool icon
			Graphics gIcon = g.create();
			ComponentDrawContext context = new ComponentDrawContext(ProjectExplorer.this, null, null, g, gIcon);
			tool.paintIcon(context, x, y);
			gIcon.dispose();

			// draw magnifying glass if appropriate
			if (circ == proj.getCurrentCircuit()) {
				int tx = x + 13;
				int ty = y + 13;
				int[] xp = { tx - 1, x + 18, x + 20, tx + 1 };
				int[] yp = { ty + 1, y + 20, y + 18, ty - 1 };
				g.setColor(MAGNIFYING_INTERIOR);
				g.fillOval(x + 5, y + 5, 10, 10);
				g.setColor(Color.BLACK);
				g.drawOval(x + 5, y + 5, 10, 10);
				g.fillPolygon(xp, yp, xp.length);
			}
		}
	}

	private class MyCellRenderer extends DefaultTreeCellRenderer {
		@Override
		public java.awt.Component getTreeCellRendererComponent(
				JTree tree, Object value, boolean selected,
				boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			java.awt.Component ret;
			ret = super.getTreeCellRendererComponent(tree, value,
				selected, expanded, leaf, row, hasFocus);

			if (ret instanceof JComponent) {
				JComponent comp = (JComponent) ret;
				comp.setToolTipText(null);
			}
			if (value instanceof ProjectExplorerToolNode) {
				ProjectExplorerToolNode toolNode = (ProjectExplorerToolNode) value;
				Tool tool = toolNode.getValue();
				if (ret instanceof JLabel) {
					((JLabel) ret).setText(tool.getDisplayName());
					((JLabel) ret).setIcon(new ToolIcon(tool));
					((JLabel) ret).setToolTipText(tool.getDescription());
				}
			} else if (value instanceof ProjectExplorerLibraryNode) {
				ProjectExplorerLibraryNode libNode = (ProjectExplorerLibraryNode) value;
				Library lib = libNode.getValue();
				if (ret instanceof JLabel) {
					String text = lib.getDisplayName();
					if (lib.isDirty()) text += DIRTY_MARKER;
					((JLabel) ret).setText(text);
				}
			}
			return ret;
		}
	}

	private class MySelectionModel extends DefaultTreeSelectionModel {
		@Override
		public void addSelectionPath(TreePath path) {
			if (isPathValid(path)) super.addSelectionPath(path);
		}

		@Override
		public void setSelectionPath(TreePath path) {
			if (isPathValid(path)) super.setSelectionPath(path);
		}

		@Override
		public void addSelectionPaths(TreePath[] paths) {
			paths = getValidPaths(paths);
			if (paths != null) super.addSelectionPaths(paths);
		}

		@Override
		public void setSelectionPaths(TreePath[] paths) {
			paths = getValidPaths(paths);
			if (paths != null) super.setSelectionPaths(paths);
		}

		private TreePath[] getValidPaths(TreePath[] paths) {
			int count = 0;
			for (int i = 0; i < paths.length; i++) {
				if (isPathValid(paths[i])) ++count;
			}
			if (count == 0) {
				return null;
			} else if (count == paths.length) {
				return paths;
			} else {
				TreePath[] ret = new TreePath[count];
				int j = 0;
				for (int i = 0; i < paths.length; i++) {
					if (isPathValid(paths[i])) ret[j++] = paths[i];
				}
				return ret;
			}
		}

		private boolean isPathValid(TreePath path) {
			if (path == null || path.getPathCount() > 3) return false;
			Object last = path.getLastPathComponent();
			return last instanceof ProjectExplorerToolNode;
		}
	}
	
	private class DeleteAction extends AbstractAction {
		public void actionPerformed(ActionEvent event) {
			TreePath path = getSelectionPath();
			if (listener != null && path != null && path.getPathCount() == 2) {
				listener.deleteRequested(new ProjectExplorerEvent(path));
			}
			ProjectExplorer.this.requestFocus();
		}
	}

	private class MyListener
			implements MouseListener, TreeSelectionListener,
				ProjectListener, PropertyChangeListener {
		//
		// MouseListener methods
		//
		public void mouseEntered(MouseEvent e) { }
		public void mouseExited(MouseEvent e) { }
		public void mousePressed(MouseEvent e) {
			ProjectExplorer.this.requestFocus();
			checkForPopup(e);
		}
		public void mouseReleased(MouseEvent e) {
			checkForPopup(e);
		}
		private void checkForPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				TreePath path = getPathForLocation(e.getX(), e.getY());
				if (path != null && listener != null) {
					JPopupMenu menu = listener.menuRequested(new ProjectExplorerEvent(path));
					if (menu != null) {
						menu.show(ProjectExplorer.this, e.getX(), e.getY());
					}
				}
			}
		}
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				TreePath path = getPathForLocation(e.getX(), e.getY());
				if (path != null && listener != null) {
					listener.doubleClicked(new ProjectExplorerEvent(path));
				}
			}
		}

		//
		// TreeSelectionListener methods
		//
		public void valueChanged(TreeSelectionEvent e) {
			TreePath path = e.getNewLeadSelectionPath();
			if (listener != null) {
				listener.selectionChanged(new ProjectExplorerEvent(path));
			}
		}
		
		//
		// project/library file/circuit listener methods
		//
		public void projectChanged(ProjectEvent event) {
			int act = event.getAction();
			if (act == ProjectEvent.ACTION_SET_TOOL) {
				TreePath path = getSelectionPath();
				if (path != null && path.getLastPathComponent() != event.getTool()) {
					clearSelection();
				}
			} else if (act == ProjectEvent.ACTION_SET_CURRENT) {
				ProjectExplorer.this.repaint();
			}
		}
		
		//
		// PropertyChangeListener methods
		//
		public void propertyChange(PropertyChangeEvent event) {
			if (AppPreferences.GATE_SHAPE.isSource(event)) {
				repaint();
			}
		}
	}

	private Project proj;
	private MyListener myListener = new MyListener();
	private MyCellRenderer renderer = new MyCellRenderer();
	private DeleteAction deleteAction = new DeleteAction();
	private ProjectExplorerListener listener = null;
	private Tool haloedTool = null;

	public ProjectExplorer(Project proj) {
		super();
		this.proj = proj;

		setModel(new ProjectExplorerModel(proj));
		setRootVisible(true);
		addMouseListener(myListener);
		ToolTipManager.sharedInstance().registerComponent(this);

		MySelectionModel selector = new MySelectionModel();
		selector.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setSelectionModel(selector);
		setCellRenderer(renderer);
		addTreeSelectionListener(myListener);
		
		InputMap imap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), deleteAction);
		ActionMap amap = getActionMap();
		amap.put(deleteAction, deleteAction);

		proj.addProjectListener(myListener);
		AppPreferences.GATE_SHAPE.addPropertyChangeListener(myListener);
		LocaleManager.addLocaleListener(this);
	}
	
	public Tool getSelectedTool() {
		TreePath path = getSelectionPath();
		if (path == null) return null;
		Object last = path.getLastPathComponent();
		if (last instanceof ProjectExplorerToolNode) {
			return ((ProjectExplorerToolNode) last).getValue();
		} else {
			return null;
		}
	}
	
	public void setListener(ProjectExplorerListener value) {
		listener = value;
	}

	public void setHaloedTool(Tool t) {
		if (haloedTool == t) return;
		haloedTool = t;
		repaint();
	}

	public void localeChanged() {
		// repaint() would work, except that names that get longer will be
		// abbreviated with an ellipsis, even when they fit into the window.
		ProjectExplorerModel model = (ProjectExplorerModel) getModel();
		model.fireStructureChanged();
	}
}
