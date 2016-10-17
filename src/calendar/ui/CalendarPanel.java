package calendar.ui;

import calendar.database.CalendarSqLiteDB;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import com.kndesign.common.*;
import calendar.graphics.Dimensions;
import calendar.handler.Account;
import java.awt.Font;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;

public class CalendarPanel extends JPanel {

    DateHandler date = new DateHandler();
    Dimensions dims = new Dimensions();
    JMenu menu;
    JMenuBar fileMB, editMB, settingsMB, helpMB;
    JPanel p;
    JLabel labelCURRENT_MONTH, labelCURRENT_YEAR, labelAPPOINTMENT_OVERVIEW;

    boolean isUpdate = false;

    final String[] monthNames_de = {"Januar", "Februar", "M�rz", "April",
        "Mai", "Juni", "Juli", "August", "September", "Oktober",
        "November", "Dezember"};

    int selectedYear, selectedMonth;

    int currentAmountOfDays = date.getDaysOfMonth(selectedMonth, selectedYear);
    // amount of day elements in calendar
    final byte AMOUNT_DAY_ELEMENTS = 35;

    Vector<CalendarDayElement> dayElements = new Vector<CalendarDayElement>();

    JLabel meetingsList = null;

    Calendar calendar;

    // Calendar data
    // Object[][] calData =
    CalendarPanel(Calendar c) {
        calendar = c;

        init();
        setLayout(new GridLayout(0, 7));
//        setPreferredSize(dims.DIM_CALENDAR_PANEL_SIZE);

        setBackground(Color.BLUE);

    }

    // TODO make init to boolean to check if everything has loaded correctly
    private void init() {
        addListeners();
        buildMonthView();
    }

    private void addListeners() {

    }

    private void buildMonthView() {
        int fields = 1;

        int m = 0;

        int column;
        int row;
        for (int i = 0; i < 6; i++) {

            for (int j = 0; j < 7; j++) {

                column = j + 1;
                row = i + 1;
                dayElements.addElement(new CalendarDayElement(column, row,
                        fields));

                this.add(dayElements.elementAt(j + m));
                fields++;
            }
            m += 7;
        }
        addDateContent();
    }

    protected void addDateContent() {

        /*
		 * DESCRIPTION
         */
        DayNameHeader dnh = new DayNameHeader();

        String[] daysInWeek = dnh.columnHeader_de;
        boolean isFirstDayInMonth = false;
        String dayOfWeek;

        int month, year;
        int[] currDate = date.getDate(); // month == [1]
        if (!isUpdate) {
            month = currDate[1];
            year = currDate[2];
        } else {
            month = selectedMonth;
            year = selectedYear;
        }
        int dayInCalendar = 0;
        int elementTracker = 0;
        int firstDay = 1;
        int nextDay = 2;

        //clear all elements 
        for (int i = 0; i < dayElements.size(); i++) {
            CalendarDayElement d = dayElements.elementAt(i);
            d.clear();
        }

        // get the position aquivalent to the day labels
        while (!isFirstDayInMonth) {

            if (date.isLeapYear(year)) {
                dayOfWeek = date.getDayOfWeekAt(firstDay, month, year, "de");

                for (int l = 0; l < daysInWeek.length; l++) {
                    if (daysInWeek[l].equals(dayOfWeek)) {
                        dayInCalendar = l;
                        isFirstDayInMonth = true;
                    }
                }
            } else {
                dayOfWeek = date.getDayOfWeekAt(firstDay, month, year, "de");

                for (int l = 0; l < daysInWeek.length; l++) {
                    if (daysInWeek[l].equals(dayOfWeek)) {
                        dayInCalendar = l;
                        isFirstDayInMonth = true;
                    }
                }
            }

        }

        int daysOfLastMonth;
        if (month > 1) {
            daysOfLastMonth = date.getDaysOfMonth(month - 1, year);
        } else {
            daysOfLastMonth = 31; // if month is January, return 31 for December
        }
        int daysOfThisMonth = date.getDaysOfMonth(month, year);
        // reset fields
        for (int t = 0; t < dayElements.size(); t++) {
            dayElements.elementAt(t).setBackground(Color.decode("#F2F2F2"));
            dayElements.elementAt(t).currentDayLabel.setText("");
            dayElements.elementAt(t).currentDayLabel.setForeground(Color.BLACK);
        }

        /*
		 * get amount of days of the last month
		 * 
		 * fill first fields which belong to the last month
         */
        for (int i = dayInCalendar; i > 0; i--) {
            int j = i - 1;
            CalendarDayElement d = dayElements.elementAt(j);

            //add date for db usage
            d.setDate(daysOfLastMonth, month - 1, year);

            d.currentDayLabel.setText(""
                    + daysOfLastMonth);
            d.currentDayLabel.setForeground(Color
                    .decode("#A4A4A4"));

            d.setBackground(Color.decode("#D8D8D8"));

            daysOfLastMonth--;
        }

        /*
		 * fill fields as long the month has days left
         */
        elementTracker = dayInCalendar;// count from first day

        int dayFontSize = 20;
        for (int i = dayInCalendar; i < daysOfThisMonth + elementTracker; i++) {
            CalendarDayElement d = dayElements.elementAt(i);
            JLabel label = d.currentDayLabel;
            d.setDate(i, month, year);

            Font labelFont = label.getFont();
            label.setFont(new Font(labelFont.getName(), Font.PLAIN, dayFontSize));

            if (firstDay == 1) {
                label.setText("" + firstDay);
                d.setDate(firstDay, month, year);
                firstDay++;
            } else {

                label.setText("" + nextDay);
                d.setDate(nextDay, month, year);
                nextDay++;

                // if end of month start next month counting
            }

            dayInCalendar++;
        }

        /*
		 * fill the last fields which belong to the next month
         */
        int daysOfNextMonth = 1;
        for (int i = dayInCalendar; i < dayElements.size(); i++) {
            if (daysOfNextMonth == 1) {
                if (month + 1 == 13) {
                    dayElements.elementAt(i).currentDayLabel.setText(""
                            + daysOfNextMonth + ". "
                            + date.getMonthName(1, "de"));
                } else {
                    dayElements.elementAt(i).currentDayLabel.setText(""
                            + daysOfNextMonth + ". "
                            + date.getMonthName(month + 1, "de"));
                }
            } else {
                dayElements.elementAt(i).currentDayLabel.setText(""
                        + daysOfNextMonth);
            }
            dayElements.elementAt(i).currentDayLabel.setForeground(Color
                    .decode("#BDBDBD"));
            dayElements.elementAt(i).setBackground(Color.decode("#E6E6E6"));

            daysOfNextMonth++;
        }

        for (int o = 0; o < dayElements.size(); o++) {
            if (dayElements.elementAt(o).currentDayLabel.getText() == "") {
                dayElements.elementAt(o).setBackground(Color.decode("#58ACFA"));
            }
        }

        boolean calSet = calendar.isCalendarAvailable();
        if (calSet) {
            //fill with meetings
            for (int i = 0; i < dayElements.size(); i++) {
                setMeetingsList(dayElements.elementAt(i));
            }

        }
        isFirstDayInMonth = false;
    }

    private void setMeetingsList(CalendarDayElement d) {

        //add number of meetings for this day as big sized number
        CalendarSqLiteDB db = calendar.getDatabase();

        Vector<Account> accounts = calendar.getAccounts();

        Vector<EntryLabel> meetings = new Vector<EntryLabel>();

        Vector<String> titles = new Vector<>();
        int numAccounts = accounts.size();

        if (numAccounts > 0) {
            //put all meetings of this day into one list (for all accounts)
            for (Account a : accounts) {
                //get a vektor of all meeting titles
                titles.addAll(db.getMeetingsList(d.getDate(), a.getId()));
            }

            if (titles.size() > 0) {
                System.out.println("Show meetings unsorted:");
                for (String str : titles) {
                    System.out.println(str);
                }
                //sort by time
                titles = sortAfterTime(titles);
                System.out.println("Show meetings sorted:");

                for (String str : titles) {
                    System.out.println(str);
                }

                for (String s : titles) {
                    meetings.add(new EntryLabel(s,db));
                }

                int meetingsFontSize = 10;

                for (JLabel meeting : meetings) {
                    Font labelFont = meeting.getFont();
                    meeting.setFont(new Font(labelFont.getName(), Font.PLAIN, meetingsFontSize));
                }

//            String labelText = "";
//            
//            if(numMeetings > 0){
//                if(numMeetings > 1)
//                    labelText = titles.elementAt(0)+"\n+"+(numMeetings-1)+" more...";            
//                else
//                    labelText = titles.elementAt(0);
//            }
                d.setList(meetings);
            }
        }

    }

    /**
     * Sorts the account meetings after their times
     *
     * @param list
     * @return
     */
    private Vector<String> sortAfterTime(Vector<String> list) {
        //regex to cut out the wanted time format
        String regex = "[0-2]{1}[0-9]{1}+[:]+[0-5]{1}[0-9]{1}";

        //only stores the times
        Vector<java.util.Date> times = new Vector<>();

        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        Vector<String> names = new Vector<String>();
        //parse string list to Date list
        for (String s : list) {
            try {
                times.add(parser.parse(s));
                names.add(s.split(regex)[1]);
//                System.out.println("Test:\t" + s);
            } catch (ParseException ex) {
                Logger.getLogger(CalendarPanel.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

        Vector<java.util.Date> sortedDates = new Vector<java.util.Date>();

        //will get sorted beside the dates
        Vector<String> sortedNames = new Vector<String>();

        //1. split out the times

        /*
                Always searching for the most recent and add it to a new  List
         */
        int numTimes = times.size();

        Date first, second, smallest;

        while (!times.isEmpty()) {
            second = null;
            smallest = null;

            int indexToRemove = 0;

            if (numTimes == 1) {//in case only one item is left
                //cut last object
                sortedDates.add(times.elementAt(0));
                sortedNames.add(names.elementAt(0));
                names.remove(0);
                times.remove(0);
                numTimes = times.size();
            } else if (numTimes == 2) {//in case two items left
                int i = 0;
                first = times.elementAt(0);
                second = times.elementAt(1);
                if (first.after(second)) {
                    smallest = second;
                    i = 1;
                } else {
                    smallest = first;
                }

                sortedDates.add(smallest);
                sortedNames.add(names.elementAt(i));
                names.remove(i);
                times.remove(i);
                numTimes = times.size();
            } else if (numTimes > 2) {//handles more than 2 objects

                for (int m = 0; m < numTimes - 1; m++) {
                    first = times.elementAt(m);
                    //prevent IndexOutOfBoundsException
//                    if (m < numTimes) {
                    second = times.elementAt(m + 1);
//                    }
                    //look for the smalles time and cut it into the new list

                    //when the current is later then one of the next
                    //set the l-cursor to this further time value and
                    //start from there
                    if (first.after(second)) {
                        //set the next smallest value
                        if (smallest != null) {
                            if (smallest.after(second)) {
                                smallest = second;
                                indexToRemove++;
                            }
                        } else {
                            smallest = second;
                        }
                        indexToRemove++;
                    } else {
                        if (smallest != null) {
                            if (smallest.after(first)) {
                                smallest = first;
                            }
                        } else {
                            smallest = first;
                        }
                    }

                }

                sortedDates.add(smallest);
                sortedNames.add(names.elementAt(indexToRemove));
                names.remove(indexToRemove);
                times.remove(indexToRemove);
                indexToRemove = 0;
                numTimes = times.size();

            }
        }

        list.removeAllElements();

        
        for (int i = 0; i < sortedDates.size();i++) {
            Date da = sortedDates.elementAt(i);
            String n = sortedNames.elementAt(i);

            String sp1 = da.toString().split(regex)[0];
//            System.out.println(sp1);
            String sp2 = da.toString().split(regex)[1];
//            System.out.println(sp2);
            String s = da.toString().split(sp1)[1];
            s = s.split(sp2)[0];

            list.add(s +" "+n);
        }

        //add the names in the correct order
        //parse string list to Date list
        for (String s : list) {
            try {
                times.add(parser.parse(s));

            } catch (ParseException ex) {
                Logger.getLogger(CalendarPanel.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list;
    }

    public void update() {
        addDateContent();
    }
}
