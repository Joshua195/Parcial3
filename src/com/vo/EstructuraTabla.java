package com.vo;

public class EstructuraTabla {

    private String field;
    private String type;
    private String nullinfo;
    private String key;
    private String defaultinfo;
    private String extra;

    public EstructuraTabla(String field, String type, String nullinfo, String key, String defaultinfo, String extra) {
        this.field = field;
        this.type = type;
        this.nullinfo = nullinfo;
        this.key = key;
        this.defaultinfo = defaultinfo;
        this.extra = extra;
    }

    public String getField() {
        return field;
    }

    public String getType() {
        return type;
    }

    public String getNullinfo() {
        return nullinfo;
    }

    public String getKey() {
        return key;
    }

    public String getDefaultinfo() {
        return defaultinfo;
    }

    public String getExtra() {
        return extra;
    }

    }
