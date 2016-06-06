package com.DAO;

import com.conexion.Conexion;
import com.vo.EstructuraTabla;
import com.XML.WriteXML;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Backup {

    Conexion conexion;
    String nombreBD;
    String rutaArchivo;

    public Backup(Conexion conexion, String nombreBD, String rutaArchivo){
        this.conexion = conexion;
        this.nombreBD = nombreBD;
        this.rutaArchivo = rutaArchivo;
    }

    private ArrayList<EstructuraTabla> getEstructuraTabla(String tabla){
        boolean existe = false;
        ArrayList<EstructuraTabla> tablas = new ArrayList<>();
        try {
            PreparedStatement consulta = conexion.getConnection().prepareStatement("DESC " + tabla);
            ResultSet res = consulta.executeQuery();
            while(res.next()){
                existe = true;
                String field = res.getString("Field");
                String type = res.getString("Type");
                String nullinfo = res.getString("Null");
                String defaultinfo = res.getString("Default");
                String key = res.getString("Key");
                String extra = res.getString("Extra");
                EstructuraTabla estructuraTabla = new EstructuraTabla(field,type,nullinfo,defaultinfo,key,extra);
                tablas.add(estructuraTabla);
            }
            res.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        if (existe) {
            return tablas;
        }
        else return null;
    }

    private ArrayList<String> getTablas() throws SQLException {
        boolean existe = false;
        ArrayList<String> tablas = new ArrayList<>();
        Connection connection = conexion.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getTables(null, null, "%", null);
        while (rs.next()) {
            existe = true;
            String tabla = rs.getString(3);
            tablas.add(tabla);
        }
        rs.close();
        if (existe) {
            return tablas;
        } else return null;
    }

    private ArrayList<String[]> getDatos(String tabla, int filas){
        Connection connection = conexion.getConnection();
        boolean existe = false;
        ArrayList<String[]> datos = new ArrayList<>();
        try {
            Statement estatuto = connection.createStatement();
            ResultSet rs = estatuto.executeQuery("SELECT * FROM " + tabla);

            while(rs.next()){
                existe = true;
                String vector[] = new String[filas];
                for (int i = 0; i < filas; i++){
                    vector[i] = rs.getString(i + 1);
                }
                datos.add(vector);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        if (existe) {
            return datos;
        }
        else return null;
    }

    public void crear() throws SQLException {
        Map<String,ArrayList<EstructuraTabla>> tablaEstructura = new HashMap<>();
        Map<String, ArrayList<String[]>> tablaDatos = new HashMap<>();
        ArrayList<String> tablas = getTablas();
        if (tablas != null) {
            for (String string : tablas) {
                ArrayList<EstructuraTabla> estructuraTabla = getEstructuraTabla(string);
                tablaEstructura.put(string, estructuraTabla);
            }

            for (String string : tablas) {
                ArrayList<EstructuraTabla> estructuraTablas = tablaEstructura.get(string);
                ArrayList<String[]> datos = getDatos(string, estructuraTablas.size());
                tablaDatos.put(string, datos);
            }
        }
        WriteXML writeXML = new WriteXML(nombreBD,tablaEstructura,tablaDatos,tablas,rutaArchivo);
        writeXML.generaXML();
    }

}
