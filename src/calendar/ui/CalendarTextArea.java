package calendar.ui;
import javax.swing.JTextArea;
import javax.swing.JToolTip;

import calendar.handler.Appointment;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CalendarTextArea extends JTextArea{
		
	boolean isSelected = false;
	
	//Tooltip show all appointments on ths day if the mouse hovers
	JToolTip tooltip;
	//constants
	final int fieldSizeX = 144;
	final int fieldSizeY = 72;
	final int fieldUnitMultiplcatorX = 10;
	final int fieldUnitMultiplicatorY = 20;
	//Dimensions
	Dimension fieldSize = new Dimension(144,72);
	
	/*Listeners for 
	 * 
	 * - selection
	 * - interaction with the stored data
	 * */
	private class selectionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(!isSelected){
				setBackground(Color.decode("#CECEF6"));
				isSelected = true;
			}else{
				setBackground(Color.GRAY);
				isSelected = false;
			}			
		}
			
	}
	/*
	 * Constructor
	 * */
	CalendarTextArea(){
		setSize(fieldSize);
		setBackground(Color.GRAY);
		
		
	}
	
	public void addAppointment(Appointment app){
			
	}
		
}
