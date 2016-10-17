/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendar.ui;

import calendar.database.CalendarSqLiteDB;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;

/**
 *
 * @author mknoefler
 */
public class EntryLabel extends JLabel implements MouseListener{
    private JToolBar labelToolBar;
    private String tooltiptext;
    private String text;
    private String id;
    JButton delete, save, close;
    CalendarSqLiteDB database;
    public EntryLabel(String text,String accountId, CalendarSqLiteDB db){
        super(text);
        id = accountId;
        database = db;
        this.text = text;
        addMouseListener(this);
    }
    
    private void animateFont(boolean mouse){
        int size = 10;
        
        if(mouse)
            size = 17;
        setFont(new Font(getFont().getName(), Font.PLAIN, size));
    }
    
    private String getDescription(){
        ResultSet rs = database.query("SELECT description FROM")
    }
    
    private void createToolBar(){
        
    }
    
    public JToolBar getToolBar(){
        return labelToolBar;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
