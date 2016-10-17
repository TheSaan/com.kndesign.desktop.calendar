package calendar.ui;

import calendar.database.CalendarDB;
import calendar.database.CalendarSqLiteDB;
import calendar.database.DBConnect;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import calendar.graphics.Colors;
import calendar.handler.Account;
import com.kndesign.common.*;
import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

//import lib.graphics.Dimensions;
public class Calendar extends JFrame {

    private static final long serialVersionUID = 1L;

    JMenuBar menubar;
    JMenu file;
    JMenu edit;

    JMenuItem file_newAccount;

    //sets the current account
    private Account account;
    public Account currentAccount;

    private boolean calAvailable = false;

    DateHandler date = new DateHandler();
    CalendarPanel cpanel;
    CalendarHeaderPanel hpanel;
    DayNameHeader dpanel;
    AccountMenu accountMenu;
    Colors c;
    JPanel p;

    JButton buttonNEW_APPOINTMENT;
    JButton buttonLEFT_ARROW, buttonRIGHT_ARROW;
    JButton buttonSHOW_TODAY;
    JButton buttonShowMonthView, buttonShowYearView, buttonShowWeekView;

    JLabel currentMonthLabel;
    /*
	 * if the selected month is Janurary(1) and you click to december, means the
	 * year before, the yearRubber turns to -1 to count the selected year. The
	 * same to future (Right) clicks to +1
     */
    int yearRubber = 0;
    // Arrow Listener items
    boolean wasArrowClicked = false;
    int newMonthSelection;
    int newYearSelection;
    int past, future;

    //derby db
//    static String host = "jdbc:derby://localhost:1527/CalendarDB;create=true";
    //sqllite db
    static String host = "jdbc:sqlite:/home/mknoefler/NetBeansProjects/Calendar/src/calendar/database/calendar.sqlite";
    static String user = "mknoefler";
    static String pwd = "latinrce44";

    CalendarSqLiteDB db;

    //represents the different calendars
    Vector<Account> accounts;

    //
    public Calendar(String titel, DBConnect dbc) {
        super(titel);
        setDatabase(dbc);
        setResizable(true);
        
        setAutoRequestFocus(true);
        //on start set the first account as standard
        //NOTE: default account gets set by database
//        account = new Account(db, getFirstAccount(), true);
//        currentAccount = account;
        accounts = new Vector<Account>();
//        accounts.add(account);

        // Dimensions dims = new Dimensions();
        // header panel
        refresh(true);
    }

    private Vector<Account> updateAccounts() {
        accounts = db.getAccounts();
        return accounts;
    }

    private String getFirstAccount() {
        try {
            ResultSet rs = db.query("SELECT * FROM calendars");
            if (rs.next()) {
                return rs.getString("id");
            } else {
                return "";
            }
        } catch (SQLException ex) {
            Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }

    }

    public void refresh(boolean calledFirstTime) {
        if (!calledFirstTime) {
            removeAll();
        }

        if (!calendarSet()) {
            new NewAccountWindow(db, this);
        }

        if (accounts != null && accounts.size() > 0) {
            for (Account a : accounts) {
                if (a.isDefault()) {
                    currentAccount = a;
                }
            }
        }

        initUI();
        buildUI();

    }

    private void initUI() {
        // Buttons
        buttonShowMonthView = new JButton("Monat");
        buttonShowWeekView = new JButton("Woche");
        buttonShowYearView = new JButton("Jahr");

        buttonNEW_APPOINTMENT = new JButton("Neuer Termin");

        // TODO Add arrow icons
        buttonLEFT_ARROW = new JButton("<--|");
        buttonRIGHT_ARROW = new JButton("|-->");
        buttonSHOW_TODAY = new JButton("Heute");
        // header panel end
        // /////////////////////////////////////////////////
        currentMonthLabel = new JLabel("");

        accounts = updateAccounts();
        if (accounts.size() > 0) {
            calAvailable = true;
        }
        accountMenu = new AccountMenu("Accounts", accounts, db);

        dpanel = new DayNameHeader();
        cpanel = new CalendarPanel(this);
        hpanel = new CalendarHeaderPanel();

        //Menubar
        menubar = new JMenuBar();

        file = new JMenu("Datei");
        file_newAccount = new JMenuItem("Neuer Kalender");

        file.add(file_newAccount);
        edit = new JMenu("Bearbeiten");

        menubar.add(file);
        menubar.add(edit);
        menubar.add(accountMenu);

        setJMenuBar(menubar);
        setLayout(new BorderLayout());
//        setSize(1920, 1080);
// Set the JFrame to maximize by default on opening
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        setResizable(true);
        setBackground(Color.DARK_GRAY);

        addListeners();

    }

    private boolean calendarSet() {
        if (db.getAccountsCount(CalendarDB.TABLE_ACCOUNTS) > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void buildUI() {

        JPanel headers = new JPanel();
        headers.setLayout(new BoxLayout(headers, BoxLayout.Y_AXIS));

        JPanel contents = new JPanel();
        contents.setLayout(new BoxLayout(contents, BoxLayout.X_AXIS));

        hpanel.add(buttonSHOW_TODAY);
        hpanel.add(buttonLEFT_ARROW);
        hpanel.add(buttonRIGHT_ARROW);
        hpanel.add(buttonNEW_APPOINTMENT);
        hpanel.add(buttonShowWeekView);
        hpanel.add(buttonShowMonthView);
        hpanel.add(buttonShowYearView);

        addCurrentMonth(date, hpanel);
        addCurrentTime(date, hpanel);

        hpanel.setSize(this.getSize().width, this.getSize().height);
        headers.add(hpanel);
        headers.add(dpanel);
        contents.add(cpanel);

        add(headers, BorderLayout.NORTH);
        add(contents, BorderLayout.CENTER);
    }

    private void selectToday(CalendarPanel cp) {
        int[] d = date.getDate();
        // add 1 to get the next month
        int day = d[0];
        //change month name
        addCurrentMonth(date, hpanel);
        //change month view to current month
        updateMonth(cpanel, d[1], d[2]);
        //mark current day with different color		
        for (int i = 0; i < cp.dayElements.size(); i++) {
            System.out.println("i: " + i);
            if (cp.dayElements.elementAt(i).currentDayLabel.getText().equals(Integer.toString(day))) {

                cp.dayElements.elementAt(i).setBackground(
                        Color.decode("#CEF6D8"));
            }
        }
    }

    public void updateMonth(CalendarPanel cp, int month, int year) {
        cp.isUpdate = true;
        cp.selectedMonth = month;
        cp.selectedYear = year;
        cp.addDateContent();
    }

    private void addCurrentMonth(DateHandler date, JPanel p) {

        int[] d = date.getDate();
        int month = d[1];
        int year = d[2];
        //clear label		
        currentMonthLabel.setText("" + date.getMonthName(month, "de")
                + "  " + year);
        p.add(currentMonthLabel);
    }

    private void addCurrentTime(DateHandler date, JPanel p) {

        int[] d = date.getTime();
        int h = d[0];
        int m = d[1];
        //clear label		
        String time = h + ":" + m;

        p.add(new JLabel(time));
    }

    private void addListeners() {
        Calendar c = this;
        buttonSHOW_TODAY.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {

                selectToday(cpanel);

            }
        });
        buttonNEW_APPOINTMENT.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                NewAppointmentWindow naw = new NewAppointmentWindow(
                        "Neuen Termin erstellen", db,c);

            }
        });
        buttonRIGHT_ARROW.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int[] d = date.getDate();
                // add 1 to get the next month
                int month;
                int year;

                month = newMonthSelection;
                year = newYearSelection;

                if (!wasArrowClicked) {
                    month = d[1];
                    year = d[2];
                    wasArrowClicked = true;
                }
                if (month <= 12) {
                    month += 1;
                }
                // if December(12) than its 1 for January
                if (month > 12) {
                    month = 1;

                    yearRubber = 1;
                    newYearSelection += yearRubber;
                    year = newYearSelection;

                }
                currentMonthLabel.setText("" + date.getMonthName(month, "de")
                        + "  " + year);

                newYearSelection = year;
                newMonthSelection = month;
                updateMonth(cpanel, month, year);

            }
        });

        buttonLEFT_ARROW.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int[] d = date.getDate();
                int month;
                int year;

                month = newMonthSelection;
                year = newYearSelection;
                if (!wasArrowClicked) {
                    month = d[1];
                    year = d[2];
                    wasArrowClicked = true;

                    // add 1 to get the next month
                }
                if (month >= 1) {
                    month -= 1;
                }
                // if December(12) than its 1 for January
                System.out.println("month:\t" + month);
                if (month < 1) {
                    month = 12;
                    yearRubber = 1;
                    newYearSelection -= yearRubber;
                    year = newYearSelection;

                }
                currentMonthLabel.setText("" + date.getMonthName(month, "de")
                        + "  " + year);

                newYearSelection = year;
                newMonthSelection = month;
                updateMonth(cpanel, month, year);

            }
        });

        file_newAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewAccountWindow naw = new NewAccountWindow(db, c);
            }
        });

    }

    private void setDatabase(DBConnect dbc) {
        db = new CalendarSqLiteDB(dbc);
    }

    protected CalendarSqLiteDB getDatabase() {
        return db;
    }

    public void calendarsAvailable(boolean av) {
        calAvailable = av;
    }

    public boolean isCalendarAvailable() {
        return calAvailable;
    }

    public Vector<Account> getAccounts() {
        return accounts;
    }

    public static void main(String[] args) {

        DBConnect dbcon = new DBConnect(host, user, pwd);

        Calendar c = new Calendar("Calendar", dbcon);

    }
}
