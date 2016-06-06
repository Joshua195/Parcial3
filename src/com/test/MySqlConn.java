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

        //try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://"+ ip +"/";
            conn = DriverManager.getConnection(connectionUrl,user,password);
        //} catch (SQLException e) {
          //  System.out.println("SQL Exception: " + e.toString());
            //JOptionPane.showMessageDialog(null, "Login not successful ", "Try again", JOptionPane.ERROR_MESSAGE);

        //} catch (ClassNotFoundException cE) {
            //System.out.println("Class Not Found Exception: " + cE.toString());
            //JOptionPane.showMessageDialog(null, "Login not successful ", "Try again", JOptionPane.ERROR_MESSAGE);

        //}
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
