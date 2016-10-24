/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendar.database;

import calendar.ui.EntryLabel;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Equal to {@link CalendarDB}. Except for the connection to a Sqlite Database.
 * 
 * @author mknoefler
 */
public class CalendarSqLiteDB extends CalendarDB {

    public CalendarSqLiteDB(DBConnect dbc) {
        super(dbc);
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:/home/mknoefler/NetBeansProjects/Calendar/src/calendar/database/calendar.sqlite ");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }
/**
     *
     * @param date
     * @param account
     * @return
     */
    public Vector<EntryLabel> getMeetingsList(Date date, String account,int methodOverload) {

        Vector<EntryLabel> list = new Vector<EntryLabel>();

        try {
            String d = date.toString();
            String q = "SELECT * FROM " + account + " WHERE startdate ='" + d + "'";

            ResultSet rs = query(q);

            while (rs.next()) {
                //get time and remove seconds
//                Time time = rs.getDate("starttime");
                int index = rs.getInt(1);
                String time = rs.getString("starttime");
                String[] times = time.split(":");
                time = times[0]+":"+times[1];
                
                String s = time+ " " + rs.getString("name");
                EntryLabel label = new EntryLabel(s, account, this);
                label.setIndex(index);
//                System.out.println(s);
                list.add(label);
            }

            return list;
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDB.class.getName()).log(Level.SEVERE, null, ex);
            return list;
        }
    }
    
}
