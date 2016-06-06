package com.XML;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;

import com.test.MySqlConn;
import com.test.Test;
import com.vo.DatosConex;
import com.vo.EstructuraTabla;
import org.jespxml.JespXML;
import org.jespxml.excepciones.AtributoNotFoundException;
import org.jespxml.modelo.Tag;
import org.xml.sax.SAXException;

public class ReadXML {

    String nameDB;
    String urlArchivo;
    DatosConex datosConex;

    public ReadXML(String urlArchivo, DatosConex datosConex){
        this.urlArchivo = urlArchivo;
        this.datosConex = datosConex;
    }

    public void leerXML() throws SQLException, ClassNotFoundException {
        Map<String,ArrayList<EstructuraTabla>> tablaEstructura = new HashMap<>();
        ArrayList<String> tablas = new ArrayList<>();
        Map<String, ArrayList<String[]>> tablaDatos = new HashMap<>();
        try {
            JespXML archivo = new JespXML(urlArchivo);
            Tag database = archivo.leerXML();
            nameDB = database.getValorDeAtributo("name");
            for (Tag table_structure: database.getTagHijoByName("table_structure",null)){
                ArrayList<EstructuraTabla> listaEstructuras = new ArrayList<>();
                for (Tag field : table_structure.getTagHijoByName("field",null)) {
                    String fieldinfo = field.getValorDeAtributo("Field");
                    String type = field.getValorDeAtributo("Type");
                    String nullinfo = field.getValorDeAtributo("Null");
                    String key = field.getValorDeAtributo("Key");
                    String defaultinfo = field.getValorDeAtributo("Default");
                    String extra = field.getValorDeAtributo("Extra");
                    EstructuraTabla estructuraTabla = new EstructuraTabla(fieldinfo,type,nullinfo,key,defaultinfo,extra);
                    listaEstructuras.add(estructuraTabla);
                }
                tablaEstructura.put(table_structure.getValorDeAtributo("name"),listaEstructuras);
                tablas.add(table_structure.getValorDeAtributo("name"));
            }

            for (Tag table_data : database.getTagHijoByName("table_data",null)){
                ArrayList<String[]> datos = new ArrayList<>();
                ArrayList<EstructuraTabla> temporal = tablaEstructura.get(table_data.getValorDeAtributo("name"));
                for (Tag row : table_data.getTagHijoByName("row",null)){
                    String[] filas = new String[temporal.size()];
                    int i = 0;
                    for (Tag field : row.getTagHijoByName("field",null)){
                        filas[i] = field.getContenido();
                        i++;
                    }
                    datos.add(filas);
                }
                tablaDatos.put(table_data.getValorDeAtributo("name"),datos);
            }
        } catch (SAXException | IOException | ParserConfigurationException | AtributoNotFoundException e) {
            e.printStackTrace();
        }
        MySqlConn mysql = new MySqlConn(datosConex.getIp(),datosConex.getUser(),datosConex.getPassword(),datosConex.getBaseDatos());
        Test test = new Test(nameDB,tablaEstructura,tablaDatos,tablas,mysql);
        test.test();
    }
}

