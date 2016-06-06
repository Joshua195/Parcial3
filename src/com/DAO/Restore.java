package com.DAO;

import com.XML.ReadXML;
import com.vo.DatosConex;

import java.sql.SQLException;

public class Restore {
    DatosConex datosConex;
    String urlArchivo;

    public Restore(String urlArchivo, DatosConex datosConex){
        this.urlArchivo = urlArchivo;
        this.datosConex = datosConex;
    }

    public void restaurar() throws SQLException, ClassNotFoundException {
        ReadXML readXML = new ReadXML(urlArchivo, datosConex);
        readXML.leerXML();
    }
}
