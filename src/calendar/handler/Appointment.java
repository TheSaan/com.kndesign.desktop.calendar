package calendar.handler;

import com.kndesign.common.*;
import java.sql.Date;
import java.sql.Time;
import java.util.Vector;
import javax.swing.*;

public class Appointment {

    DateHandler date;
    public String titel, description;
    //to share the appointment send it per mail
    Vector<String> mails;
    public int fromday, frommonth, fromyear, fromhours, fromminutes;
    public int today, tomonth, toyear, tohours, tominutes;
    java.sql.Date toDate, fromDate;
    java.sql.Time toTime, fromTime;

    public Appointment(String titel, String description, Vector<String> mails, java.sql.Date fromDate, java.sql.Time fromTime, java.sql.Date toDate, java.sql.Time toTime) {
        this.titel = titel;

        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.toDate = toDate;
        this.toTime = toTime;

        this.description = description;
        //TODO implementing regular expression for mails 
        //@URL http://openbook.galileocomputing.de/javainsel/javainsel_04_008.html#dodtpb7c12b47-3234-4d45-967e-0e365a0e12e6
        this.mails = mails;

        date = new DateHandler();
    }

    /*Inner class of a JPanel object
	 * calculates and sends graphical timelines 
	 * to the JPanel Calender Object to draw it
	 * 
	 * In this way every timeline shows automatically the
	 * correct data of the correct appointment
	 * */
    protected class AppointmentTimeline extends JPanel {

    }
    //SETTERS

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String titel) {
        this.titel = titel;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Time getFromTime() {
        return fromTime;
    }

    public Date getToDate() {
        return toDate;
    }

    public Time getToTime() {
        return toTime;
    }

    public String getDescription() {
        return description;
    }

    //TODO Create setters and a update method 
    public String getTitle() {
        return titel;
    }

    public Vector<String> getMails() {
        return mails;
    }
    
    

}
