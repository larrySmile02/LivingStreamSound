package com.md.basedpc.persistence.db;

public class NameValuePairDB {
    public NameValuePairDB(String key, String value) {
        this.name = key;
        this.value = value;
    }

    String name;
    String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
