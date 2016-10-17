package calendar.ui;

import java.awt.Color;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import calendar.handler.Appointment;
import com.kndesign.common.*;
import calendar.graphics.Bounds;
import calendar.graphics.Dimensions;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;

public class CalendarDayElement extends JPanel {

    Bounds b;
    DateHandler date;
    Dimensions dims;
    Border greyline;
    private static final long serialVersionUID = 1L;

    int day, month, year, hour, minute;
    //pixel position
    public int posX, posY;
    //table (but not table layout!) position
    public int row, column;
    //access id (just to call one peace easier by id value
    int id;
    String dayName = "";
    /*
	 * TODO create class which calulates all holydays*/
//	String[] holidays = date.getCurrentHoliday();

    //Layout inside the field
    JLabel currentDayLabel;
    JLabel moreMeetingsLabel;
    ///meetings shown on the dayelement field
    Vector<JLabel> meetings;
    //Container for the appointment list
    JPanel appointmentListContainerPanel;

    //Actual position of the whole panel element
    Vector<Integer> coordinates;

    final static int MAX_MEETINGS_ON_FIELD = 2;
    
    //Constructor
    public CalendarDayElement(int column, int row, int id) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        greyline = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
        b = new Bounds();
        date = new DateHandler();
        dims = new Dimensions();
        coordinates = new Vector<Integer>();
        appointmentListContainerPanel = new JPanel();
        currentDayLabel = new JLabel("", JLabel.LEFT);
        this.row = row;
        this.column = column;
        this.id = id;
        setMinimumSize(dims.DIM_CALENDAR_FIELD_MIN_SIZE);
        setMaximumSize(dims.DIM_CALENDAR_FIELD_MAX_SIZE);
        setPreferredSize(dims.DIM_CALENDAR_FIELD_MIN_SIZE);

        setPosition(column, row);
        setBorder(greyline);
        setBackground(Color.WHITE);
//        setToolTipText("Element Pos:" + column + "/" + row + "\t ID: " + id);

        add(currentDayLabel, BorderLayout.PAGE_START);
    }
    //Setters
    //////////////////////////
    public void clear(){
        if(meetings != null){
            for (JLabel meeting : meetings) {
                meeting.setText("");
            }
        }
        if(moreMeetingsLabel != null)
            moreMeetingsLabel.setText("");
    }
    
    public void setList(Vector<JLabel> meetings){
        if(meetings != null && meetings.size() > 0){
            
            this.meetings = meetings;
            for (int i = 0; i < MAX_MEETINGS_ON_FIELD && i < meetings.size(); i++) {
                add(meetings.elementAt(i));
            }
            int more = meetings.size() - MAX_MEETINGS_ON_FIELD;
            
            String moreText = "";
            
            if(more > 0){
                moreText = "+" + more + " mehr...";
            }else{
                moreText = "";
            }
            moreMeetingsLabel = new JLabel(moreText);
            add(moreMeetingsLabel);
        }
    }
    public void setPosition(int column, int row) {
        coordinates = b.CalendarDayElementSize;
        //+1 for the borders, otherwise they will cross eachother
        //bad effect
        this.posX = coordinates.elementAt(0) * column;
        this.posY = coordinates.elementAt(1) * row;
    }

    public void setDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public void setTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public void addAppointment(Appointment a) {

    }

    private void setupContent() {

        String currentDateView = day + "\t\t\n"; 	//later +holiday
        currentDayLabel.setText(currentDateView);

        this.add(currentDayLabel);
        this.add(appointmentListContainerPanel);
    }


    /**
     *
     * @return
     */
    public int[] getDate() {
        return new int[]{day, month, year};
    }

    /*
	 pre implementation
	 
	  setDayOfWeekName(date.getDayOfWeekAt(this.day,this.month,this.year,"de");

	 
	 * */
    public void setDayOfWeekName() {
        dayName = date.getDayOfWeekAt(day, month, year, "de");
    }

}
