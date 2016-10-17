/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendar.database;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author mknoefler
 */
public class DBConnect {
    
    private boolean connected = false;
    Connection con;
    
    public DBConnect(String host, String username, String password){
        connect(host,username,password);
    }
    
    private void connect(String host, String username, String password){
        try {
            con = DriverManager.getConnection(host,username,password);
            connected = true;
        }
        catch ( SQLException err ) {
        System.out.println( err.getMessage( ) );
        }
    }
    
    protected boolean isConnected(){
        return connected;
    }

    public Connection getConnection() {
        return con;
    }

    
    
}
