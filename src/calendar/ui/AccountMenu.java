/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendar.ui;

import calendar.database.CalendarDB;
import calendar.database.CalendarSqLiteDB;
import calendar.handler.Account;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;

/**
 *
 * @author mknoefler
 */
public class AccountMenu extends JMenu {

    Vector<JCheckBoxMenuItem> accountLabels;
    Vector<Account> accounts;
    CalendarDB database;

    public AccountMenu(String title,Vector<Account> accounts, CalendarSqLiteDB db) {
        super(title);
        database = db;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        update(accounts);
    }

    public void update(Vector<Account> accounts) {
        this.accounts = accounts;
        this.removeAll();
        addCalendars();
    }

    /**
     * Adds all calendars to make them selectable
     *
     * @param layout
     */
    private void addCalendars() {
        accountLabels = new Vector<JCheckBoxMenuItem>();

        int len = accounts.size();

        for (int i = 0; i < len; i++) {
            Account a = accounts.elementAt(i);
            accountLabels.add(new JCheckBoxMenuItem(a.getName(), a.isDefault()));
            
            JCheckBoxMenuItem check = accountLabels.elementAt(i);
            
            accountLabels.elementAt(i).addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    boolean is = check.isSelected();
                    if (is) {
                        //first make the last default account false
                        database.update("UPDATE "+CalendarDB.TABLE_ACCOUNTS+" SET default_account='" + !is + "' WHERE default_account='" + is+"'");

                        //now set the new one to true
                        database.update("UPDATE "+CalendarDB.TABLE_ACCOUNTS+" SET default_account='" + is + "' WHERE id='" + a.getId()+"'");
                        
                        update(accounts);
                        
                        check.setSelected(is);
                    }else{
                        check.setSelected(!is);
                    }
                }
            });

            add(accountLabels.elementAt(i));
        }
    }

}
