package es.ucm.fdi.tp.view;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;


import es.ucm.fdi.tp.view.ColorChooser.ColorInterface;

@SuppressWarnings("serial")
public class ColorPanel extends JPanel {

	private MyTableModel tModel;

	private Map<Integer, Color> colors; // Line -> Color
	private ColorChooser colorChooser;
	private ColorInterface CController;

	public ColorPanel(ColorInterface cControler) {
		super();
		this.CController = cControler;
		initGUI();
	}

	private void initGUI() {

		setLayout(new BorderLayout());
		setMaximumSize(new Dimension(300,200));
		colors = new HashMap<>();
		colorChooser = new ColorChooser(new JFrame(), "Choose Line Color", Color.BLACK);

		// names table
		tModel = new MyTableModel();
		tModel.getRowCount();
		final JTable table = new JTable(tModel) {
			private static final long serialVersionUID = 1L;

			// THIS IS HOW WE CHANGE THE COLOR OF EACH ROW
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component comp = super.prepareRenderer(renderer, row, col);

				// the color of row 'row' is taken from the colors table, if
				// 'null' setBackground will use the parent component color.
				if (col == 1)
					for(int i = 0; i < tModel.getRowCount(); i++)
					{
						if(row == i)
							comp.setBackground(CController.PaintColor(i));
					}
				else if (col == 2 && row == CController.getPlayerID())
							comp = new JLabel(CController.getPlayerType());
					
				else
					comp.setBackground(Color.WHITE);
				comp.setForeground(Color.BLACK);
				return comp;
			}
		};

		table.setToolTipText("Click on a row to change the color of a player");
		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = table.rowAtPoint(evt.getPoint());
				int col = table.columnAtPoint(evt.getPoint());
				if (row >= 0 && col >= 0) {
					changeColor(row);
					CController.changePlayerColor(row, colors.put(row, colorChooser.getColor()));
				}
			}

		});

		add(new JScrollPane(table), BorderLayout.CENTER);
		JPanel ctrlPabel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		add(ctrlPabel, BorderLayout.PAGE_START);

	}

	private void changeColor(int row) {
		colorChooser.setSelectedColorDialog(colors.get(row));
		colorChooser.openDialog();
		if (colorChooser.getColor() != null) {
			colors.put(row, colorChooser.getColor());
			repaint();
		}
	}
	
	
	public void changePlayer(int PlayerID)
	{
		tModel.setValueAt(CController.getPlayerType(),PlayerID,2);
	}
}
