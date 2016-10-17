package calendar.ui;

import calendar.graphics.Dimensions;
import calendar.graphics.Colors;
import java.awt.Color;
import java.awt.Component;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;


public class LayoutPanel extends JPanel {
	
	String title = "Titel";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Own object collections
	Colors colors;
	Dimensions dims;
	Border bevelborder, loweredetched;

	public LayoutPanel(String borderTitle) {
		colors = new Colors();
		dims = new Dimensions();

		loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		bevelborder = BorderFactory.createTitledBorder(borderTitle);
		
		setAlignmentX(Component.CENTER_ALIGNMENT);
		
		setPreferredSize(dims.DIM_NEW_APPOINTMENT_JPANEL_ELEMENT);
		setVisible(true);
		setBackground(Color.decode("#F2F2F2"));
		setBorder(BorderFactory.createTitledBorder(loweredetched,borderTitle));
		
	}
}
