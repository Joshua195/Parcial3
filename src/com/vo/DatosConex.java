package com.vo;


public class DatosConex {
    private String ip;
    private String user;
    private String password;
    private String baseDatos;

    public DatosConex(String ip, String user, String password, String baseDatos) {
        this.ip = ip;
        this.user = user;
        this.password = password;
        this.baseDatos = baseDatos;
    }

    public String getIp() {
        return ip;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getBaseDatos() {
        return baseDatos;
    }
}
