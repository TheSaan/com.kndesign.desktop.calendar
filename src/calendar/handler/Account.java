/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendar.handler;

import calendar.database.CalendarDB;
import com.kndesign.common.ColorHandler;
import com.kndesign.common.RandomHandler;
import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Respresents a calendar account
 *
 * @author mknoefler
 */
public class Account {

    private String name, id, url;
    private boolean default_account = false;

    private Color color;
    private CalendarDB database;

    public Account(CalendarDB db, String id) {
        database = db;
        init(id);
    }

    /**
     *
     * @param id
     */
    private void init(String id) {
        try {
            ResultSet rs = database.query("SELECT * FROM " + CalendarDB.TABLE_ACCOUNTS + " WHERE id='" + id + "'");
            rs.next();
//            color = Color.decode(rs.getString("color")); TODO
            color = Color.BLUE;

            this.id = rs.getString("id");
            name = rs.getString("name");
            url = rs.getString("url");
            default_account = rs.getBoolean("default_account");

            //set as default
            if (default_account && database.getAccountsCount(CalendarDB.TABLE_ACCOUNTS) > 1) {
                //first make the last default account false
                database.update("UPDATE " + CalendarDB.TABLE_ACCOUNTS + " SET default_account='" + !default_account + "' WHERE default_account='" + default_account + "'");
            }
            //now set the new one to true
            database.update("UPDATE " + CalendarDB.TABLE_ACCOUNTS + " SET default_account='" + default_account + "' WHERE id='" + id+"'");

        } catch (SQLException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param db
     * @return
     */
    public static String createId(CalendarDB db) {

        //contains the chars which are not allowed to be used
        char[] skipChars = {
            34, 39, 64, 91, 92, 93, 123, 125
        };

        int len = 10;
        char[] cArray = new char[len];

        for (int i = 0; i < len; i++) {

            boolean isAllowed = false;

            Random rand = new Random();
            char r = '0';
//            char test = 53;
//            System.out.println("Test char("+test+")");

            //[0] 48-57 0-9
            //[1] 65-90 A-Z
            //[2] 97- 122 a-z
            char[] valids = new char[3];

            //checkif the char is valid
            while (!isAllowed) {
                /*
                only takes valid chars. The fourth random just chooses one of the
                three in valids[].            
                 */
                valids[0] = (char) RandomHandler.createIntegerFromRange(48, 57, rand);
                valids[1] = (char) RandomHandler.createIntegerFromRange(65, 90, rand);
                valids[2] = (char) RandomHandler.createIntegerFromRange(97, 122, rand);
                int start = 0;

                //the first char must not be a number
                if (i == 0) {
                    start += 1;
                }

                r = valids[RandomHandler.createIntegerFromRange(start, 2, rand)];

                for (char ch : skipChars) {
                    if (r != ch) {
                        isAllowed = true;
                    } else {
                        isAllowed = false;
                    }
                }
            }

            cArray[i] = r;
        }
        String s = new String(cArray);

        //retry creating id when the same is already used in database
        if (db.isIdAvailable(s)) {
            createId(db);
        }

        System.out.println("ID created: " + s);
        return s;
    }

    /**
     *
     * @return
     */
    public Color getColor() {
        return color;
    }

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @return
     */
    public boolean isDefault() {
        return default_account;
    }

}
