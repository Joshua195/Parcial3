package com.test;

import java.sql.*;
import javax.swing.JOptionPane;

public class MySqlConn {
    Statement statement = null;
    Connection conn = null;
    boolean conectado = false;

    String ip = "", databaseName = "", user = "", password = "";

    public MySqlConn(String ip, String user, String password, String databaseName) throws ClassNotFoundException, SQLException {
        conectado = true;
        this.ip = ip;
        this.user = user;
        this.password = password;
        this.databaseName = databaseName;
        Class.forName("com.mysql.jdbc.Driver");
        String connectionUrl = "jdbc:mysql://"+ ip +"/";
        conn = DriverManager.getConnection(connectionUrl,user,password);
    }

    public int update(String query) {
        int rModif = 0;
        try {
            statement = conn.createStatement();
            rModif = statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return rModif;
    }
}
