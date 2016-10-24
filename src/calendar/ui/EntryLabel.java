/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendar.ui;

import calendar.database.CalendarSqLiteDB;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author mknoefler
 */
public class EntryLabel extends JLabel implements MouseListener {

    private String tooltiptext;
    private String text;
    private String id;
    private int dbIndex;
    Calendar cal;
    CalendarSqLiteDB database;
    int fontSize;

    public EntryLabel(String text, String accountId, CalendarSqLiteDB db) {
        super(text);
        id = accountId;
        database = db;
        this.text = text;
        fontSize = 12;
        addMouseListener(this);
        setAccountForeground();
    }

    /**
     *
     * @param mouse
     */
    private void animateFont(boolean mouse) {
        int size;

        if (mouse) {
            size = 17;
        } else {
            size = fontSize;
        }
        setFont(new Font(getFont().getName(), Font.BOLD, size));
    }

    /**
     *
     * @param id
     */
    public void setIndex(int id) {
        if (id > 0) {
            dbIndex = id;
        }
    }

    /**
     *
     */
    private void setAccountForeground() {
        setForeground(database.getAccountForegroundColor(id));
    }

    /**
     *
     * @return
     */
    private String getDescription() {
//        ResultSet rs = database.query("SELECT description FROM");
        return null;
    }

    /**
     *
     * @throws SQLException
     */
    private void createDetailsFrame() throws SQLException {
        boolean inEditMode = false;

        JButton delete, save, edit;
        JLabel fT, fD, tT, tD, n, d, g;
        JLabel accountName = new JLabel("Kalender: " + database.getAccountName(id));
        ResultSet rs = database.getAppointment(id, dbIndex);

        if (rs.next()) {

            final String fTs = rs.getString("starttime");
            fT = new JLabel("Von " + fTs);
            String fDs = rs.getString("startdate");
            fD = new JLabel(fDs);

            String tTs = rs.getString("endtime");
            tT = new JLabel("Bis " + tTs);
            String tDs = rs.getString("enddate");
            tD = new JLabel(tDs);

            String ns = rs.getString("name");
            n = new JLabel("Termin: " + ns);

            String ds = rs.getString("description");
            if (ds.equals("")) {
                ds = "nicht vorhanden";
            }
            d = new JLabel("Beschreibung:\n\n" + ds);

            String gs = rs.getString("guests");
            String[] gs2 = gs.split(";");
            gs = "";
            for (String s : gs2) {
                gs += s + "\n";
            }

            if (gs.equals("")) {
                gs = "Keine Teilnehmer";
            }
            g = new JLabel("Teilnehmer:\n\n" + gs);

            JLabel[] labels = {fT, fD, tT, tD, n, d, g};

            JFrame frame = new JFrame("Optionen");
            frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
            frame.setVisible(true);

            delete = new JButton("Loeschen");
            delete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    database.deleteAppointment(id, dbIndex);
                    database.getCalendar().update();
                    frame.dispose();
                }
            });

            edit = new JButton("Bearbeiten");
            save = new JButton("Speichern");

            JPanel fromPanel = new JPanel();
            fromPanel.setLayout(new FlowLayout());
            JPanel toPanel = new JPanel();
            toPanel.setLayout(new FlowLayout());
            frame.add(accountName);
            frame.add(n);
            frame.add(d);

            fromPanel.add(fT);
            fromPanel.add(fD);
            frame.add(fromPanel);

            toPanel.add(tT);
            toPanel.add(tD);
            frame.add(toPanel);

            frame.add(g);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());
            buttonPanel.add(edit);
            buttonPanel.add(save);
            buttonPanel.add(delete);
            buttonPanel.setVisible(true);

            frame.add(buttonPanel);
            frame.setSize(300, 300);
            frame.setLocation(300, 300);
            frame.setVisible(true);
            System.out.println("Frame created");

        }

    }

    @Override
    public String getText() {
        return super.getText(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        try {
            createDetailsFrame();
        } catch (SQLException ex) {
            Logger.getLogger(EntryLabel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        animateFont(true);
//        System.out.println(text+"\tmouse in");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        animateFont(false);
//        System.out.println(text+"\tmouse in");
    }

}
