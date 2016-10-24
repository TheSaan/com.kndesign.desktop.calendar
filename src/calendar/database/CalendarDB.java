/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendar.database;

import calendar.handler.Account;
import calendar.handler.Appointment;
import calendar.ui.Calendar;
import java.awt.Color;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Vector;

/**
 * This class returns explicit results from database for the {@link Calendar}
 *
 * @author mknoefler
 */
public class CalendarDB {

    private int numberOfAccounts = 0;
    private Connection connection;
    DBConnect dbConnect;
    Calendar calendar;
    public final static String TABLE_ACCOUNTS = "accounts";

    public CalendarDB(DBConnect dbc) {
        if (dbc != null && dbc.isConnected()) {
            dbConnect = dbc;
            connection = dbc.getConnection();
        }
    }

    /**
     *
     * @param date
     * @param calendar
     * @return
     * @throws SQLException
     */
    public int getNumberOfMeetingsForDay(int[] date, String calendar) throws SQLException {
        String d2 = date[0] + "-" + date[1] + "-" + date[2];
        java.sql.Date formatDate = new Date(date[2] - 1900, date[1] - 1, date[0]);

        String d = formatDate.toString();

        String q = "SELECT * FROM " + calendar + " WHERE startdate ='" + d + "'";

        int c = count(query(q));

        if (date[0] == 12) {
            System.out.println("Entries on " + d2 + " -> " + c);
        }

        return c;
    }

    /**
     *
     * @param date
     * @param account
     * @return
     */
    public Vector<String> getMeetingsList(Date date, String account) {

        Vector<String> list = new Vector<String>();

        try {
            String d = date.toString();
            String q = "SELECT * FROM " + account + " WHERE startdate ='" + d + "'";

            ResultSet rs = query(q);

            while (rs.next()) {
                //get time and remove seconds
//                Time time = rs.getDate("starttime");
                String time = rs.getString("starttime");
                String[] times = time.split(":");
                time = times[0] + ":" + times[1];

                String s = time + " " + rs.getString("name");
//                System.out.println(s);
                list.add(s);
            }

            return list;
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
            return list;
        }
    }

    /**
     *
     * @return
     */
    public Vector<Account> getAccounts() {
        try {
            int i = 0;

            Vector<Account> vec = new Vector<Account>();
            ResultSet rs = query("SELECT * FROM  " + TABLE_ACCOUNTS);

            while (rs.next()) {
                String s = rs.getString("id");
                vec.add(new Account(this, s));
                System.out.println((i++ + 1) + " Record(s) Exist: " + s);

            }

            numberOfAccounts = vec.size();

            return vec;
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     *
     * @param query
     * @return
     */
    public ResultSet query(String query) {
        try {
            Statement stmt;

            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
//            stmt.close();
            return rs;

        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    /**
     *
     * @param query
     */
    public void update(String query) {
        try {
//            reconnect();
            Statement stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.executeUpdate(query);

        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param query
     * @return
     */
    public ResultSet first(String query) {

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setMaxRows(1);

            return stmt.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Insert Record from Appointment data
     *
     * @param app
     */
    public void insertAppointment(Appointment app, Account account) {
        String toDateStr = "";
        String toTimeStr = "";

        if (app.getToDate() != null) {
            toDateStr = app.getToDate().toString();
        }
        if (app.getToTime() != null) {
            toTimeStr = app.getToTime().toString();
        }

        // save mails to array with Semikolon separated
        String guests = "";
        // mails is null as long as no mails gonna get set
        if (app.getMails() != null) {
            //final string to store in db
            //TODO have to read for "functional operations(Streams)" in JAVA 8 API for this
            guests = app.getMails().stream().map((mail) -> mail + ";").reduce(guests, String::concat);
        }
        String q
                = "INSERT INTO '" + account.getId() + "' ("
                + "name, "
                + "description, "
                + "startdate, "
                + "starttime, "
                + "enddate, "
                + "endtime, "
                + "guests"
                + ") VALUES ("
                + "'" + app.getTitle()
                + "','" + app.getDescription()
                + "','" + app.getFromDate() + "','"
                + app.getFromTime() + "','"
                + toDateStr + "','"
                + toTimeStr + "','"
                + guests + "'"
                + ") ";
        update(q);
    }

    /**
     * Simply loops via next() and counts (quiet unefficient).
     *
     * @param rs
     * @return Amount of rows.
     */
    public int count(ResultSet rs) {
        try {
            int i = 0;
            while (rs.next()) {
                i++;
            }
            return i;
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    /**
     *
     * @return
     */
    public Account getStandardAccount() {
        try {
            ResultSet rs = query("SELECT * FROM " + TABLE_ACCOUNTS + " WHERE default_account=true");
            rs.next();
            return new Account(this, rs.getString("id"));
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     *
     */
    public void setStandardAccount() {

    }

    /**
     *
     * @param comparedID
     * @return
     */
    public boolean isIdAvailable(String comparedID) {
        try {
            ResultSet rs = query("SELECT * FROM " + TABLE_ACCOUNTS + " WHERE id='" + comparedID + "'");
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Maybe Record available, but ERROR thrown [CalendarDB.isIdAvailable(" + comparedID + ")");
            return false;
        }
    }

    /**
     *
     * @param accountTable
     * @return
     */
    public int getAccountsCount(String accountTable) {

        try {
            PreparedStatement p = connection.prepareStatement("SELECT * FROM " + accountTable);

            //Store it also in the variable 
            numberOfAccounts = count(p.executeQuery());

            return numberOfAccounts;
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    /**
     * Uses an Exception when the table was not found it will return false.
     *
     * @return
     */
    public boolean accountsTableCreated() {
        try {
            ResultSet rs = query("SELECT * FROM " + TABLE_ACCOUNTS);
            rs.next();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     *
     * @param cal
     */
    public void setCalendar(Calendar cal) {
        calendar = cal;
    }

    /**
     *
     * @return
     */
    public Calendar getCalendar() {
        return calendar;
    }

    /**
     *
     * @param account
     * @param id
     * @return
     */
    public ResultSet getAppointment(String account, int id) {

        try {
            PreparedStatement p = connection.prepareStatement("SELECT * FROM " + account + " WHERE id=?");
            p.setInt(1, id);
            return p.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     *
     * @param account
     * @param id
     */
    public void deleteAppointment(String account, int id) {

        try {
            PreparedStatement p = connection.prepareStatement("DELETE FROM " + account + " WHERE id=?");
            p.setInt(1, id);
            p.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param accountID
     * @return
     */
    public String getAccountName(String accountID) {
        try {
            return query("SELECT name FROM " + TABLE_ACCOUNTS + " WHERE id='" + accountID + "'").getString("name");
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
            return "kein Name gefunden";
        }
    }

    /**
     *
     * @param account
     * @return
     */
    public Color getAccountForegroundColor(String account) {
        try {
            return Color.decode(query("SELECT color FROM " + TABLE_ACCOUNTS + " WHERE id='" + account + "'").getString("color"));
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
            return Color.BLACK;
        }
    }

    private void reconnect() {
        connection = dbConnect.getConnection();
    }
}
