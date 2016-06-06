package com.test;

import com.vo.EstructuraTabla;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.Map;

public class Test {

    String nameDB;
    Map<String, ArrayList<EstructuraTabla>> tablaEstrctura;
    Map<String, ArrayList<String[]>> tablaDatos;
    ArrayList<String> tableNames;
    MySqlConn mySqlConn;

    public Test(String nameDB, Map<String, ArrayList<EstructuraTabla>> tablaEstrctura, Map<String, ArrayList<String[]>> tablaDatos, ArrayList<String> tableNames, MySqlConn mySqlConn) {
        this.mySqlConn = mySqlConn;
        this.nameDB = nameDB;
        this.tablaEstrctura = tablaEstrctura;
        this.tablaDatos = tablaDatos;
        this.tableNames = tableNames;
    }

    public void test(){
        ArrayList<String> querys = new ArrayList<>();
        String query = "DROP database IF EXISTS " + nameDB + ";";
        querys.add(query);
        String temp3 = "CREATE DATABASE " + nameDB;
        querys.add(temp3);
        String temp2 = "USE " + nameDB;
        querys.add(temp2);

        for (String nameTable : tableNames){
            ArrayList<EstructuraTabla> tablas =  tablaEstrctura.get(nameTable);
            String query1 = "CREATE TABLE "+ nameTable + " (";
            String temp = "";
            String aux = "";
            for (EstructuraTabla estructuraTabla : tablas){
                if (!estructuraTabla.getDefaultinfo().equals("PRI")){
                    temp += "" + estructuraTabla.getField() + " " + estructuraTabla.getType() + " ";
                    if (estructuraTabla.getNullinfo().equals("NO")){
                        temp += "NOT NULL ";
                    }else {
                        temp += "DEFAULT NULL ";
                    }
                    temp += estructuraTabla.getExtra() + ",";
                }else {
                    temp += "" + estructuraTabla.getField() + " " + estructuraTabla.getType() + " ";
                    if (estructuraTabla.getNullinfo().equals("NO")){
                        temp += "NOT NULL ";
                    }else {
                        temp += "DEFAULT NULL ";
                    }
                    temp += estructuraTabla.getExtra() + ",";
                    aux += "PRIMARY KEY ("+estructuraTabla.getField() +") );";
                }
            }

            if (!aux.equals("")){
                query1 += temp;
                query1 += aux;
            }else {
                temp = temp.substring(0,temp.length()-1);
                query1 +=  temp;
                query1 += ");";
            }
            querys.add(query1);
        }

        for (String nameTable : tableNames){
            ArrayList<EstructuraTabla> tablas =  tablaEstrctura.get(nameTable);
            String query1 = "";
            String temp = "";
            String aux = "";
            ArrayList<String[]> datos = tablaDatos.get(nameTable);
            for (String[] strings : datos){
                query1 = "INSERT INTO " + nameTable +" VALUES (";
                temp = "";
                aux = "";
                for (int i = 0; i < strings.length; i++){
                    if (strings[i].indexOf('\\') != -1) {}
                    temp += "'" + strings[i] + "',";
                }
                if (datos.size() != 0) {
                    temp = temp.substring(0,temp.length()-1);
                    query1 += temp;
                    query1 += ");";
                    querys.add(query1);
                }
            }

        }
        int contador = 0;
        for (String query2 : querys){
            int res = query2.indexOf("INTO");
            if (res != -1){
                contador++;
            }
            mySqlConn.update(query2);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacion");
        alert.setHeaderText(null);
        alert.setContentText("Se han ingresado "+ contador +" registros correctamente");
        alert.showAndWait();
    }
}
