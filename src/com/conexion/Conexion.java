package com.conexion;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private String baseDatos;
    private String login;
    private String password;
    private String url;
    Connection conexion = null;

    public Conexion(String baseDatos, String login, String password, String ip) {
        this.baseDatos = baseDatos;
        this.login = login;
        this.password = password;
        url = "jdbc:mysql://"+ ip +"/" + baseDatos;
    }

    public String init() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        conexion = DriverManager.getConnection(url, login, password);
        if (conexion != null) {
             return "Conexión a base de datos " + baseDatos + " OK\n";
        }else return "";
    }
    public Connection getConnection() {
        return conexion;
    }

    public void desconectar() throws SQLException {
        conexion.close();
        conexion = null;
    }

    public String getBaseDatos() {
        return baseDatos;
    }
}

