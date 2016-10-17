package calendar.ui;

import java.awt.Color;
import javax.swing.JPanel;
import calendar.graphics.Dimensions;

public class CalendarHeaderPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public CalendarHeaderPanel() {
//		DateCalculator date = new DateCalculator();
		
		Dimensions dims = new Dimensions();
		setPreferredSize(dims.DIM_CALENDAR_PANEL_HEADER_SIZE);
		setVisible(true);
		setBackground(Color.WHITE);
		setToolTipText("Calender Header JPanel");
	}	
}
