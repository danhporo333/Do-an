package com.example.do_an_qlnv_hutech.database;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    String classes = "net.sourceforge.jtds.jdbc.Driver";
    protected static String ip = "10.0.2.2";
    protected static String port = "1433";
    protected static String dbname = "QLNHANSU";
    protected static String username = "danh";
    protected static String password = "123";
    public Connection Conn() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        try {
            Class.forName(classes);
            String conUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbname;
            conn =  DriverManager.getConnection(conUrl, username, password);

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }
}
