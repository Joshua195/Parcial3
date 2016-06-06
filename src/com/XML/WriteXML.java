package com.XML;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.vo.EstructuraTabla;
import org.jespxml.JespXML;
import org.jespxml.modelo.Atributo;
import org.jespxml.modelo.Encoding;
import org.jespxml.modelo.Tag;

public class WriteXML {

    String nameDB;
    Map<String, ArrayList<EstructuraTabla>> tablaEstrctura;
    Map<String, ArrayList<String[]>> tablaDatos;
    ArrayList<String> tableNames;
    String rutaArchivo;

    public WriteXML(String nameDB, Map<String, ArrayList<EstructuraTabla>> tablaEstrctura, Map<String, ArrayList<String[]>> tablaDatos, ArrayList<String> tableNames, String rutaArchivo) {
        this.nameDB = nameDB;
        this.tablaEstrctura = tablaEstrctura;
        this.tablaDatos = tablaDatos;
        this.tableNames = tableNames;
        this.rutaArchivo = rutaArchivo;
    }

    public void generaXML() {
        Tag raiz = new Tag("database");
        raiz.addAtributo(new Atributo("name", nameDB));
        if (tableNames != null) {
            for (String nameTabla : tableNames) {
                ArrayList<String[]> datos = tablaDatos.get(nameTabla);
                ArrayList<EstructuraTabla> estructura = tablaEstrctura.get(nameTabla);

                Tag table_structure = new Tag("table_structure");
                table_structure.addAtributo(new Atributo("name", nameTabla));
                raiz.addTagHijo(table_structure);

                for (EstructuraTabla estructuraTabla : estructura) {
                    Tag field = new Tag("field");
                    table_structure.addTagHijo(field);
                    field.addAtributo(new Atributo("Field", estructuraTabla.getField()));
                    field.addAtributo(new Atributo("Type", estructuraTabla.getType()));
                    field.addAtributo(new Atributo("Null", estructuraTabla.getNullinfo()));
                    field.addAtributo(new Atributo("Key", estructuraTabla.getKey()));
                    field.addAtributo(new Atributo("Default", estructuraTabla.getDefaultinfo()));
                    field.addAtributo(new Atributo("Extra", estructuraTabla.getExtra()));
                }

                Tag table_data = new Tag("table_data");
                table_data.addAtributo(new Atributo("name", nameTabla));
                raiz.addTagHijo(table_data);

                if (datos != null) {
                    for (String[] strings : datos) {
                        Tag row = new Tag("row");
                        table_data.addTagHijo(row);
                        String[] datas = strings;
                        for (int j = 0; j < datas.length; j++) {
                            Tag field = new Tag("field");
                            row.addTagHijo(field);
                            field.addAtributo(new Atributo("name", estructura.get(j).getField()));
                            field.addContenido(datas[j]);
                        }
                    }
                }
            }
        }
        JespXML xml = new JespXML(new File(rutaArchivo), Encoding.UTF_8);
        try {
            xml.escribirXML(raiz);
        } catch (ParserConfigurationException | FileNotFoundException | TransformerException ex) {
            Logger.getLogger(WriteXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
