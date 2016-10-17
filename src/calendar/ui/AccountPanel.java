/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendar.ui;

import calendar.database.CalendarDB;
import calendar.handler.Account;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 *
 * @author mknoefler
 */
public class AccountPanel extends JPanel {

    Vector<JCheckBox> accountLabels;
    Vector<Account> accounts;
    CalendarDB database;

    public AccountPanel(Vector<Account> accounts, CalendarDB db) {
        database = db;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setVisible(true);
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
        accountLabels = new Vector<JCheckBox>();

        int len = accounts.size();

        for (int i = 0; i < len; i++) {
            Account a = accounts.elementAt(i);
            accountLabels.add(new JCheckBox(a.getName(), a.isDefault()));
            JCheckBox check = accountLabels.elementAt(i);
            accountLabels.elementAt(i).addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    boolean is = check.isSelected();
                    if (is) {
                        //first make the last default account false
                        database.insert("UPDATE calendars SET default_account=" + !is + " WHERE default_account=" + is);

                        //now set the new one to true
                        database.insert("UPDATE calendars SET default_account=" + is + " WHERE id='" + a.getId()+"'");
                        
                        update(accounts);
                    }
                }
            });

            add(accountLabels.elementAt(i));
        }
    }

}
