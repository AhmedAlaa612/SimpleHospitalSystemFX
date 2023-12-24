package com.example.dbfinalproject;

import java.sql.Connection;
import java.sql.DriverManager;
public class DatabaseConnection {
    public Connection dblink;

    public Connection getConnection() {
        String dbName = "";
        String user = "";
        String password = "";
        String url = "jdbc:mysql://localhost/" + dbName;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            dblink = DriverManager.getConnection(url, user, password);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return dblink;
    }
}
