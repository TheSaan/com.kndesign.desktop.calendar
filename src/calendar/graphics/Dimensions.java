package calendar.graphics;

import java.awt.Dimension;

public class Dimensions {

	//NEW APPOINTMENT WINDOW
	//PREFERED SIZE
	public Dimension
		DIM_NEW_APPOINTMENT_JPANEL_ELEMENT,
		DIM_NEW_APPOINTMENT_JPANEL_ELEMENT_SAVE_ABORT,
		DIM_NEW_APPOINTMENT_DESCRIPTION_MIN_SIZE,
		DIM_NEW_APPOINTMENT_TITLE_MIN_SIZE,
		DIM_NEW_APPOINTMENT_TEXTFIELD_SIZE,
		DIM_NEW_APPOINTMENT_HEADER_SIZE,
		DIM_NEW_APPOINTMENT_DATE_JCOMBOBOX_SIZE,
		DIM_NEW_APPOINTMENT_EMAILS_PANEL_SIZE,
		DIM_NEW_APPOINTMENT_EMAILS_JCOMBOBOX_SIZE,
		DIM_MAIN_WINDOW_SIZE,
		DIM_NEW_APPOINTMENT_CONFIRMATION_DIALOG;
	//NEW APPOINTMENT WINDOW
	//MAXIMUM SIZES
	public Dimension
		DIM_NEW_APPOINMENT_TITLE_MAX_SIZE,
		DIM_NEW_APPOINTMENT_DATE_MAX_SIZE,
		DIM_NEW_APPOINTMENT_EMAILS_MAX_SIZE,
		DIM_MAIN_WINDOW_MAX_SIZE;
			
	//CALENDAR
	
	//CALENDAR.FIELDS
	public Dimension
		DIM_CALENDAR_FIELD_MIN_SIZE,
		DIM_CALENDAR_FIELD_MAX_SIZE,
		DIM_CALENDAR_FIELD_SIZE,
		DIM_CALENDAR_PANEL_SIZE,
		DIM_CALENDAR_PANEL_HEADER_SIZE,
		DIM_CALENDAR_PANEL_DAYNAMES_SIZE;
	//MAIN WINDOW
	public Dimension DIM_MAIN_WINDOW_LABEL;
	
	
	//ERROR LABELS
	public Dimension DIM_NEW_APPOINTMENT_EMAIL_NOT_VALID_ERROR;
	//Constructor
	public Dimensions() {
		//NEW APPOINTMENT WINDOW
		//PREFERED SIZES
		DIM_NEW_APPOINTMENT_JPANEL_ELEMENT = new Dimension(300, 100);
		DIM_NEW_APPOINTMENT_JPANEL_ELEMENT_SAVE_ABORT = new Dimension(300,30);
		DIM_NEW_APPOINTMENT_DESCRIPTION_MIN_SIZE = new Dimension(460, 130);
		DIM_NEW_APPOINTMENT_TITLE_MIN_SIZE = new Dimension(460, 70);
		DIM_NEW_APPOINTMENT_DATE_JCOMBOBOX_SIZE = new Dimension(55,20);
		DIM_NEW_APPOINTMENT_EMAILS_PANEL_SIZE = new Dimension(200,80);
		DIM_NEW_APPOINTMENT_EMAILS_JCOMBOBOX_SIZE = new Dimension(470,20);
		DIM_NEW_APPOINTMENT_CONFIRMATION_DIALOG = new Dimension(470,300);
		DIM_NEW_APPOINTMENT_HEADER_SIZE = new Dimension(250,20);
		DIM_NEW_APPOINTMENT_TEXTFIELD_SIZE = new Dimension(460,20);
		//MAX SIZES
		DIM_NEW_APPOINMENT_TITLE_MAX_SIZE = new Dimension(480,20);
		DIM_NEW_APPOINTMENT_DATE_MAX_SIZE = new Dimension(480,20);
		DIM_NEW_APPOINTMENT_EMAILS_MAX_SIZE = new Dimension(480,40);
		//////////////////////////////////
		//MAIN WINDOW
		//PREFERED SIZES
		DIM_MAIN_WINDOW_LABEL = new Dimension(250,20);
		DIM_MAIN_WINDOW_SIZE = new Dimension(500,600);
		//MAX SIZES
		DIM_MAIN_WINDOW_MAX_SIZE = new Dimension(500,600);
		//////////////////////////////////
		//CALENDAR
		DIM_CALENDAR_PANEL_HEADER_SIZE = new Dimension(800,40);
		DIM_CALENDAR_PANEL_DAYNAMES_SIZE = new Dimension(800,36);
		DIM_CALENDAR_PANEL_SIZE = new Dimension(800,400);
		DIM_CALENDAR_FIELD_MIN_SIZE = new Dimension(72,72);
		DIM_CALENDAR_FIELD_MAX_SIZE = new Dimension(108,108);//+50% of MIN
		DIM_CALENDAR_FIELD_SIZE = new Dimension(72,72);
		//////////////////////////////////
		//ERROR LABELS
		DIM_NEW_APPOINTMENT_EMAIL_NOT_VALID_ERROR = new Dimension(250,100);
	}
}
