/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendar.ui;

import calendar.database.CalendarDB;
import calendar.handler.Account;
import com.kndesign.common.ColorHandler;
import com.kndesign.common.RandomHandler;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Random;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author mknoefler
 */
public class NewAccountWindow extends JFrame {

    CalendarDB database;
    JComboBox<JLabel> colorList;
    Vector<Color> colors;
    Calendar calendar;
    int numColors = 20;

    JTextField jtfName, jtfUrl;
    JCheckBox cbIsDefault;
    JLabel name, color;
    JButton abort, save;

    public NewAccountWindow(CalendarDB db, Calendar cal) {
        super("Neuen Kalender erstellen");
        database = db;
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        calendar = cal;
        
        jtfName = new JTextField();
        jtfUrl = new JTextField();

        cbIsDefault = new JCheckBox("Als Standard verwenden ");

        name = new JLabel("Bezeichnung");
        color = new JLabel("Farbe waehlen");

        abort = new JButton("Abbrechen");
        save = new JButton("Speichern");

//        Adding colors to list
        colors = ColorHandler.getUniqueColors(numColors);
        
        //@see http://stackoverflow.com/questions/13671666/changing-the-colour-of-jcombobox-selected-item-permanantly
//        Vector<JLabel> labelTmp = new Vector<JLabel>();
//        
//        for (Color c : colors) {
//            JLabel label = new JLabel();
//            label.setOpaque(true);
//            label.setBackground(c);  
//            label.setText(" ");
//            labelTmp.add(label);
//        }
//        colorList = new JComboBox<JLabel>(labelTmp);
        
        
        setListeners();
        
        add(name);
        add(jtfName);
//        add(color);
//        add(colorList);
        add(cbIsDefault);
        add(save);
        add(abort);
        
        setVisible(true);
        setSize(300,150);
        setLocation(500, 350);
        
    }

    private void setListeners(){
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        
        abort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
    }
    private void newAccount(String name, Color c, String url, boolean isdefault) {

        String id = Account.createId(database);
        String color;

        if (c == null) {
            //if no hex set, get a random color
            color = ColorHandler.getUniqueColors(1).elementAt(0).toString();
        } else {
            color = c.toString();
        }
        System.out.print(color);
        String q
                = "INSERT INTO calendars ("
                + "id, "
                + "name, "
                + "color, "
                + "url, "
                + "default_account"
                + ") VALUES ("
                + "'" + id
                + "','" + name
                + "','" + color
                + "','" + url
                + "'," + isdefault 
                + ") ";

        database.insert(q);
        
        String newTable = 
                "CREATE TABLE "+id
                +" ("
                +"name VARCHAR(150),"
                +"description VARCHAR(400),"
                +"startdate DATE,"
                +"enddate DATE,"
                +"starttime TIME,"
                +"endtime TIME,"
                +"fullday BOOLEAN,"
                +"guests VARCHAR(5000)"
                +")";
        
        database.insert(newTable);
        
        System.out.println("");
    }

    private void save() {
        if (name.getText() != "") {

//            int colorIndex = colorList.getSelectedIndex();
//            Color c = colors.elementAt(colorIndex);
            int max = colors.size()-1;
            //get random color
            Color c = colors.elementAt(RandomHandler.createIntegerFromRange(0, max, new Random()));
            newAccount(jtfName.getText(),c, jtfUrl.getText(), cbIsDefault.isSelected());
            calendar.calendarsAvailable(true);
            calendar.refresh(false);
            close();
            
        }
    }

    private void close(){
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
