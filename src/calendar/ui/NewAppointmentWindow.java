package calendar.ui;

import calendar.database.CalendarDB;
import calendar.database.CalendarSqLiteDB;
import javax.swing.*;
import javax.swing.border.Border;

import calendar.handler.Appointment;
import com.kndesign.common.*;
import calendar.graphics.Dimensions;
import calendar.handler.Account;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;

public class NewAppointmentWindow extends JFrame {

    /*
	 * SETTING
	 * 
	 * changing CALENDAR_MAX_FUTURE_RANGE defines, how far into future
	 * appointments can be added
     */
    public final int CALENDAR_MAX_FUTURE_RANGE = 75;

    /*
	 * Variables get testet before saving appointment
	 * 
	 * If the user input like "days" is higher than 31 or 31/30 and the month
	 * only has 28/29 days than inform the user about this
     */
    int amountOfDaysInSelection; // day selected
    int amountOfValidDays; // saves the amount days of the selected month
    DateHandler datecalc;

    private Vector<String> mails;

    Dimensions dims;

    private static final long serialVersionUID = 1L;
    // Variables
    boolean dateBoxesAreSelected = false;
    // Layouts
    JTextField jtfTITLE, jtfMAILS;
    JTextField jtfDateFrom, jtfTimeFrom, jtfDateTo, jtfTimeTo;

    JTextArea jtaDESCRIPTION;
    JComboBox<String> comboBoxEMAILS;
    JButton buttonADD_EMAIL, buttonSAVE, buttonABORT;
    LayoutPanel jpanelDATE, jpanelTITLE, jpanelDESCRIPTION, jpanelEMAILS,
            jpanelSAVEorABORT;

    JComboBox<String> accountList;
    Vector<Account> accounts;
    Account account;

    JLabel labelDate;
    JLabel invalidMonthErrorLabel;

    Border blackline;
    Pattern pattern;
    Matcher matcher;
    CalendarDB database;
    Calendar calendar;

    // Constructor
    public NewAppointmentWindow(String titel, CalendarSqLiteDB db, Calendar cal) {
        super(titel);
        calendar = cal;
        database = db;
        blackline = BorderFactory.createLineBorder(Color.BLACK);
        invalidMonthErrorLabel = new JLabel();
        datecalc = new DateHandler();
        dims = new Dimensions();

        fillAccountList();

        setUndecorated(false);
        setVisible(true);
        setBackground(Color.WHITE);
        setPreferredSize(dims.DIM_MAIN_WINDOW_SIZE);
        setMinimumSize(dims.DIM_MAIN_WINDOW_SIZE);
        setMaximumSize(dims.DIM_MAIN_WINDOW_MAX_SIZE);

        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        buttonABORT = new JButton("Abbrechen");
        buttonADD_EMAIL = new JButton("Empfaenger hinzufuegen");
        buttonSAVE = new JButton("Speichern");

        labelDate = new JLabel("bis");

        jpanelTITLE = new LayoutPanel("Titel");
        jpanelTITLE.setPreferredSize(dims.DIM_NEW_APPOINTMENT_TITLE_MIN_SIZE);
        jpanelDATE = new LayoutPanel("Datum");
        jpanelDATE.setPreferredSize(dims.DIM_NEW_APPOINTMENT_TITLE_MIN_SIZE);
        jpanelDESCRIPTION = new LayoutPanel("Beschreibung");
        jpanelDATE.setMaximumSize(dims.DIM_NEW_APPOINTMENT_DATE_MAX_SIZE);
        jpanelEMAILS = new LayoutPanel("Senden an (Email Empfaenger)");
        jpanelEMAILS
                .setPreferredSize(dims.DIM_NEW_APPOINTMENT_EMAILS_PANEL_SIZE);
        jpanelSAVEorABORT = new LayoutPanel("");
        jpanelSAVEorABORT
                .setPreferredSize(dims.DIM_NEW_APPOINTMENT_JPANEL_ELEMENT_SAVE_ABORT);

        jtaDESCRIPTION = new JTextArea();
        jtaDESCRIPTION.setEditable(true);
        jtaDESCRIPTION
                .setPreferredSize(dims.DIM_NEW_APPOINTMENT_DESCRIPTION_MIN_SIZE);
        jtaDESCRIPTION.setBorder(blackline);
        jtfTITLE = new JTextField();
        jtfTITLE.setEditable(true);
        jtfTITLE.setPreferredSize(dims.DIM_NEW_APPOINTMENT_TEXTFIELD_SIZE);
        jpanelTITLE.setMaximumSize(dims.DIM_NEW_APPOINMENT_TITLE_MAX_SIZE);

        jtfDateFrom = new JTextField();
        jtfDateTo = new JTextField();
        jtfTimeFrom = new JTextField();
        jtfTimeTo = new JTextField();

        // creates the comboBox Objects
        setTodayTimeAndDate();
        // /////////////////////////////
        jpanelDATE.add(jtfDateFrom);
        jpanelDATE.add(jtfTimeFrom);
        jpanelDATE.add(labelDate);
        jpanelDATE.add(jtfDateTo);
        jpanelDATE.add(jtfTimeTo);
        jpanelTITLE.add(jtfTITLE);
        jpanelTITLE.add(accountList);
        jpanelDESCRIPTION.add(jtaDESCRIPTION);

        jpanelSAVEorABORT.add(buttonABORT);
        jpanelSAVEorABORT.add(buttonSAVE);

        jtfMAILS = new JTextField();
        comboBoxEMAILS = new JComboBox<String>();

        comboBoxEMAILS.setVisible(true);
        jtfMAILS.setVisible(true);

        jtfMAILS.setPreferredSize(dims.DIM_NEW_APPOINTMENT_TEXTFIELD_SIZE);
        comboBoxEMAILS
                .setPreferredSize(dims.DIM_NEW_APPOINTMENT_EMAILS_JCOMBOBOX_SIZE);

        jpanelEMAILS.add(jtfMAILS);
        jpanelEMAILS.add(buttonADD_EMAIL);
        jpanelEMAILS.add(comboBoxEMAILS);

        jpanelSAVEorABORT.add(invalidMonthErrorLabel);

        /*
		 * if the month got selected run getAmountOfDaysInList() method to get
		 * automatically the correct amount of days in this month (also
		 * calculates leap years)
         */
        addActionListeners();

        add(jpanelTITLE);
        add(jpanelDATE);
        add(jpanelDESCRIPTION);
        add(jpanelEMAILS);
        add(jpanelSAVEorABORT);

    }

    /**
     *
     */
    private void addActionListeners() {

        ActionListener emailListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                /**
                 * Regular Expressions Tutorial URL:
                 * http://ocpsoft.org/opensource
                 * /guide-to-regular-expressions-in-java-part-1/
                 */
                checkMails();
            }

        };
        buttonSAVE.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                save();

            }
        });
        buttonABORT.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                dispose();

            }
        });
        buttonADD_EMAIL.addActionListener(emailListener);

    }

    /**
     *
     */
    public void save() {
        // date as integer container
        java.sql.Date fromD, toD = null;
        java.sql.Time fromT, toT = null;

        fromD = getDateFromInput(jtfDateFrom);
        fromT = getTimeFromInput(jtfTimeFrom);

        if (!jtfDateTo.getText().isEmpty()) {
            toD = getDateFromInput(jtfDateTo);
        }
        if (!jtfTimeTo.getText().isEmpty()) {
            toT = getTimeFromInput(jtfTimeTo);
        }

        if (jtfTITLE.getText() != "") {
            Appointment app = new Appointment(jtfTITLE.getText(),
                    jtaDESCRIPTION.getText(), mails, fromD, fromT, toD, toT);
            database.insertAppointment(app, getSelectedAccount());
        }

        try {
            if (calendar != null) {
                calendar.setVisible(true);
                calendar.update();
            }
            this.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(NewAppointmentWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Gets the date which from input in a format which can the database handle.
     * Input format: DD.MM.YYYY
     *
     * No need of writing the year if its the current.
     *
     * @param tf
     * @return
     */
    private java.sql.Date getDateFromInput(JTextField tf) {
        int year, month, day;

        String text = tf.getText();
        String[] values = text.split("\\.");

        int l = values.length;

        if (l > 0 && l < 4) {
            /*
                If the year is missing, add the current year to it
             */
            if (l == 2) {
                year = DateHandler.currYear - 1900;
            } else {

                year = Integer.decode(values[2]) - 1900;
            }
            month = Integer.decode(values[1]) - 1;
            day = Integer.decode(values[0]);

            return new java.sql.Date(year, month, day);
        } else {
            return new java.sql.Date(
                    DateHandler.currYear - 1900,
                    DateHandler.currMonth,
                    DateHandler.currDay);
        }

    }

    /**
     * Gets the time which from input in a format which can the database handle.
     * Input format: HH:MM
     *
     * No need of writing the year if its the current.
     *
     * @param tf
     * @return
     */
    private java.sql.Time getTimeFromInput(JTextField tf) {
        int hour, minute;
        String text = tf.getText();
        String[] values = text.split(":");
        int l = values.length;
        if (l > 0 && l < 4) {
            hour = DateHandler.getTimeDecoded(values[0]);
            minute = DateHandler.getTimeDecoded(values[1]);

            return new java.sql.Time(hour, minute, DateHandler.currSecond);
        } else {
            return new java.sql.Time(
                    DateHandler.currHour,
                    DateHandler.currMinute,
                    DateHandler.currSecond
            );
        }
    }

    /**
     *
     */
    private void checkMails() {
        pattern = Pattern
                .compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,7}$");
        String mail = jtfMAILS.getText();
        matcher = pattern.matcher(mail);

        if (matcher.matches()) {
            buttonADD_EMAIL.setForeground(Color.BLACK);
            comboBoxEMAILS.addItem(mail);
            jtfMAILS.setText("");

            if (mails == null) {
                mails = new Vector<String>();
            }
            //add to mails vector for storing into database in save()
            mails.add(mail);
        } else {
            buttonADD_EMAIL.setForeground(Color.RED);

            JLabel errorLabel = new JLabel("Emailadresse ist ungueltig!");
            errorLabel.setVisible(true);
            JDialog d = new JDialog();
            d.add(errorLabel);
            d.setVisible(true);
            d.setMinimumSize(dims.DIM_NEW_APPOINTMENT_EMAIL_NOT_VALID_ERROR);
            d.setPreferredSize(dims.DIM_NEW_APPOINTMENT_EMAIL_NOT_VALID_ERROR);

        }

    }

    /**
     *
     */
    private void setTodayTimeAndDate() {
        int y = DateHandler.currYear;
        int m = DateHandler.currMonth;
        int d = DateHandler.currDay;
        int h = DateHandler.currHour;
        int min = 0;

        //set one hour as timerange as default
        int h2 = h + 1;

        //add "0" when number is less than 10
        String sm = m + "", sh = h + "", sd = d + "", smin = min + "", sh2 = h2 + "";
        if (m < 10) {
            sm = "0" + m;
        }
        if (h < 10) {
            sh = "0" + h;
        }
        if (d < 10) {
            sd = "0" + d;
        }
        if (min < 10) {
            smin = "0" + min;
        }
        if (h2 < 10) {
            sh2 = "0" + h2;
        }

        jtfDateFrom.setText(sd + "." + sm + "." + y);
        jtfTimeFrom.setText(sh + ":" + smin);

        jtfDateTo.setText(sd + "." + sm + "." + y);
        jtfTimeTo.setText(sh2 + ":" + smin);
    }

    /**
     *
     */
    private void fillAccountList() {
        try {
            ResultSet rs = database.query("SELECT * FROM " + CalendarDB.TABLE_ACCOUNTS);
            accountList = new JComboBox<>();
            accounts = new Vector<Account>();

            while (rs.next()) {
                accountList.addItem(rs.getString("name"));

//                boolean def = rs.getBoolean("default_account");
//                String name = rs.getString("name");
                String id = rs.getString("id");
                accounts.add(new Account(database, id));

            }
        } catch (SQLException ex) {
            Logger.getLogger(NewAppointmentWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @return
     */
    private Account getSelectedAccount() {

        return accounts.elementAt(accountList.getSelectedIndex());

    }

}
