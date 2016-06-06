package com.vista;

import com.DAO.Backup;
import com.DAO.Restore;
import com.conexion.Conexion;
import com.vo.DatosConex;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;


import java.io.*;
import java.sql.SQLException;

public class Controller {
    @FXML
    TextField textFieldIP;

    @FXML
    TextField textFieldUsuario;

    @FXML
    PasswordField passwordField;

    @FXML
    TextField textFieldBD;

    @FXML
    Label labelEstado;

    @FXML
    private Button buttonConectar;

    @FXML
    private ProgressIndicator progressIndicator;

    public Conexion conexion;

    private Main main;
    public DatosConex datosConex;
    private boolean backup = false, restore = false;
    private boolean error = false;

    public void setMain(Main main){
        this.main = main;
    }

    @FXML
    void handleConectar() throws SQLException, IOException {
        if (backup){
            labelEstado.setText("En espera...");
            datosConex = new DatosConex(textFieldIP.getText(),textFieldUsuario.getText(),passwordField.getText(),textFieldBD.getText());
            this.conexion = new Conexion(datosConex.getBaseDatos(),datosConex.getUser(),datosConex.getPassword(),datosConex.getIp());
            try {
                labelEstado.setText(conexion.init());
            }catch (SQLException | ClassNotFoundException e){
                error();
            }
            if (!error) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Guardar Archivo XML");
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
                File file = fileChooser.showSaveDialog(main.getStagePrincipal());
                if (file != null) {
                    String url = file.getAbsolutePath();
                    Backup backup = new Backup(conexion, conexion.getBaseDatos(), url);
                    backup.crear();
                    progressIndicator.setProgress(1);
                    labelEstado.setText("listo");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Informacion");
                    alert.setHeaderText(null);
                    progressIndicator.setProgress(1);
                    alert.setContentText("Se ha creado el Archivo de Respaldo de la base de datos " + datosConex.getBaseDatos() + "!!");
                    alert.showAndWait();
                    try {
                        Process process = Runtime.getRuntime().exec("explorer " + url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                conexion.desconectar();
                backup = false;
                backup(true);
                limpiar();
            }
        }

        if (restore){
            labelEstado.setText("En espera...");
            datosConex = new DatosConex(textFieldIP.getText(),textFieldUsuario.getText(),passwordField.getText(),textFieldBD.getText());
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Abrir abrir XML");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
            File file = fileChooser.showOpenDialog(main.getStagePrincipal());
            if (file != null){
                String urlArchivo = file.getAbsolutePath();
                Restore restore = new Restore(urlArchivo, datosConex);
                try {
                    restore.restaurar();
                } catch (SQLException | ClassNotFoundException e) {
                   error();
                }
                if (!error) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Informacion");
                    alert.setHeaderText(null);
                    progressIndicator.setProgress(1);
                    alert.setContentText("Se ha restaurado la base de datos del archivo " + file.getName() + " correctamente");
                    alert.showAndWait();
                }
            }
            restore = false;
            restaurar(true);
            textFieldBD.setDisable(true);
            labelEstado.setText("Listo");
            progressIndicator.setProgress(1);
            limpiar();
        }

    }

    @FXML
    void handleBackup(){
        progressIndicator.setProgress(-1);
        backup(false);
        backup = true;
        labelEstado.setText("En espera...");
    }

    @FXML
    public void handleRestore(){
        progressIndicator.setProgress(-1);
        restaurar(false);
        restore = true;
        labelEstado.setText("En espera...");
    }

    public void editable(boolean estado){
        textFieldIP.setEditable(estado);
        textFieldUsuario.setEditable(estado);
        passwordField.setEditable(estado);
        textFieldBD.setEditable(estado);
    }

    public void backup(boolean estado){
        textFieldIP.setDisable(estado);
        textFieldUsuario.setDisable(estado);
        passwordField.setDisable(estado);
        textFieldBD.setDisable(estado);
        buttonConectar.setDisable(estado);
    }

    public void restaurar(boolean estado){
        textFieldIP.setDisable(estado);
        textFieldUsuario.setDisable(estado);
        passwordField.setDisable(estado);
        textFieldBD.setDisable(!estado);
        buttonConectar.setDisable(estado);
    }

    public void limpiar(){
        textFieldIP.setText("127.0.0.1");
        textFieldUsuario.setText("");
        passwordField.setText("");
        textFieldBD.setText("");
    }

    public void error(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        progressIndicator.setProgress(1);
        alert.setContentText("Error en el login verifique sus datos!!    Cerrando Aplicacion...");
        alert.showAndWait();
        main.getStagePrincipal().close();
        error = true;
    }
}

