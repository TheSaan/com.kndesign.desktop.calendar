/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendar.database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
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

    
}
