/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendar.database;

import calendar.handler.Account;
import calendar.handler.Appointment;
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
 * This class returns explicit results from database for the calendar
 *
 * @author mknoefler
 */
public class CalendarDB {
    private int numberOfAccounts = 0;
    private Connection connection;

    public CalendarDB(DBConnect dbc) {
        if (dbc != null && dbc.isConnected()) {
            connection = dbc.getConnection();
        }
    }

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

    public Vector<String> getMeetingsList(int[] date, String calendar) {

        Vector<String> list = new Vector<String>();

        try {
            java.sql.Date formatDate = new Date(date[2] - 1900, date[1] - 1, date[0]);

            String d = formatDate.toString();

            String q = "SELECT * FROM " + calendar + " WHERE startdate ='" + d + "'";

            ResultSet rs = query(q);

            while (rs.next()) {
                //get time and remove seconds
                String time = rs.getTime("starttime").toString();
                String[] clock = time.split(":");
                time = clock[0] + ":" + clock[1];

                list.add(time + " " + rs.getString("name"));
            }

            return list;
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
            return list;
        }
    }

    public Vector<Account> getAccounts(){
        try {
            ResultSet rs = query("SELECT * FROM calendars");
            int count = count(rs);
            
            Vector<Account> vec = new Vector<Account>();
                        
            for(int i = 0; i < count; i++){
                String s = rs.getString("id");
                vec.add(new Account(this,s));
                System.out.println((i)+" Records Exist: "+ s); 
            }
            
            numberOfAccounts = vec.size();
            
            return vec;
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public int getAccountCount(){
        return numberOfAccounts;
    }
    
    public ResultSet query(String query) {
        try {
            Statement stmt;

            stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            return stmt.executeQuery(query);

        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public void insert(String query) {
        try {
            Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate(query);

        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    

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
                + "','" + app.getFromDate().toString() + "','"
                + app.getFromTime().toString() + "','"
                + toDateStr + "','"
                + toTimeStr + "','"
                + guests + "'"
                + ") ";
        insert(q);
    }

    public int count(ResultSet rs) {
        try {
            return rs.last() ? rs.getRow() : 0;
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public Account getStandardAccount(){
        try {
            ResultSet rs = query("SELECT * FROM calendars WHERE default_account=true");
            rs.next();
            return new Account(this,rs.getString("id"));
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public void setStandardAccount(){
        
    }
    
    public boolean isIdAvailable(String comparedID){
        try {
            ResultSet rs = query("SELECT * FROM calendars WHERE id='"+comparedID+"'");
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Maybe Record available, but ERROR thrown [CalendarDB.isIdAvailable("+comparedID+")");
            return false;
        }
    }
}
