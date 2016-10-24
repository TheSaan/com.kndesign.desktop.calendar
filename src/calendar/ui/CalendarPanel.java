package calendar.ui;

import calendar.database.CalendarSqLiteDB;
import calendar.graphics.Colors;
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

    Vector<DayInMonthPanel> dayElements = new Vector<DayInMonthPanel>();

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

    /**
     *
     */
    private void init() {
        addListeners();
        buildMonthView();
    }

    /**
     *
     */
    private void addListeners() {

    }

    /**
     *
     */
    private void buildMonthView() {
        int fields = 1;

        int m = 0;

        int column;
        int row;
        for (int i = 0; i < 6; i++) {

            for (int j = 0; j < 7; j++) {

                column = j + 1;
                row = i + 1;
                dayElements.addElement(new DayInMonthPanel(column, row,
                        fields));

                this.add(dayElements.elementAt(j + m));
                fields++;
            }
            m += 7;
        }
        addDateContent();
    }

    /**
     *
     */
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
            DayInMonthPanel d = dayElements.elementAt(i);
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
            DayInMonthPanel d = dayElements.elementAt(j);

            //add date for db usage
            d.setDate(daysOfLastMonth, month - 1, year);

            d.currentDayLabel.setText(""
                    + daysOfLastMonth);
            d.currentDayLabel.setForeground(Color
                    .decode("#A4A4A4"));

            d.setBackground(Color.decode(Colors.HEX_OF_BRIGHT_GREY));

            daysOfLastMonth--;
        }

        /*
		 * fill fields as long the month has days left
         */
        elementTracker = dayInCalendar;// count from first day

        int dayFontSize = 20;
        for (int i = dayInCalendar; i < daysOfThisMonth + elementTracker; i++) {
            DayInMonthPanel d = dayElements.elementAt(i);
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

            if (i == DateHandler.currDay && month == DateHandler.currMonth) {
                dayElements.elementAt(i + elementTracker - 1).setBackground(Color.decode(Colors.HEX_OF_TODAY_BLUE));
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

    /**
     *
     */
    public void updateContent() {
        addDateContent();
    }

    /**
     *
     * @param d
     */
    private void setMeetingsList(DayInMonthPanel d) {

        //add number of meetings for this day as big sized number
        CalendarSqLiteDB db = calendar.getDatabase();
        db.setCalendar(calendar);
        Vector<Account> accounts = calendar.getAccounts();

        Vector<EntryLabel> meetings = new Vector<EntryLabel>();

        Vector<String> titles = new Vector<>();
        int numAccounts = accounts.size();

        if (numAccounts > 0) {
            //put all meetings of this day into one list (for all accounts)
            int tmp = 0;
            for (Account a : accounts) {
                meetings.addAll(db.getMeetingsList(d.getDate(), a.getId(), 0));
            }

            if (meetings.size() > 0) {
//                
                meetings = sorts(meetings);

                d.setList(meetings);
            }
        }

    }

    /**
     *
     * @param list
     * @return
     */
    private Vector<EntryLabel> sorts(Vector<EntryLabel> list) {
        try {
            SimpleDateFormat parser = new SimpleDateFormat("HH:mm");

            Date first, second;

            int loopCounts = 1;
            for (int i = 0; i < list.size() - 1; i++) {
                first = parser.parse(list.elementAt(i).getText());
                second = parser.parse(list.elementAt(i + 1).getText());
                EntryLabel firstElement = list.elementAt(i);
                EntryLabel secondElement = list.elementAt(i + 1);

                //swith them
                if (first.after(second)) {
                    list.add(i, secondElement);
                    //remove second
                    list.remove(i + 2);
                    i = -1;
                }
            }

//            System.out.println("New sort method output:\n");
//            for (EntryLabel label : list) {
//                System.out.println(label.getText());
//            }
        } catch (ParseException e) {

        }
        return list;
    }

    /**
     *
     */
    public void update() {
        addDateContent();
    }
}
