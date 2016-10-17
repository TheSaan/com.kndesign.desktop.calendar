package calendar.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

import calendar.graphics.Dimensions;

public class DayNameHeader extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final String[] columnHeader_de = { "Montag", "Dienstag", "Mittwoch",
			"Donnerstag", "Freitag", "Samstag", "Sonntag" };
	Vector<JLabel> labelsOfDays = new Vector<JLabel>();

	public DayNameHeader() {
		
		Dimensions dims = new Dimensions();
		setPreferredSize(dims.DIM_CALENDAR_PANEL_DAYNAMES_SIZE);
		setLayout(new GridLayout(0, 7));
		setBackground(Color.LIGHT_GRAY);
		setToolTipText("Day name JPanel");
		createDayHeaders();

	}

	private void createDayHeaders() {
		for (byte i = 0; i < columnHeader_de.length; i++) {
			labelsOfDays.addElement(new JLabel(columnHeader_de[i],JLabel.CENTER));
			
		}
		// add headers to this
		for (int i = 0; i < 7; i++) {
//			labelsOfDays.elementAt(i).setForeground(Color.WHITE);
			this.add(labelsOfDays.elementAt(i));
		}
		this.setVisible(true);
	}
}