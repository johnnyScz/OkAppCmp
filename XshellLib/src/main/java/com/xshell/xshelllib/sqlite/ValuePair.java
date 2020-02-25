package com.xshell.xshelllib.sqlite;

/**
 * Created by Administrator on 2017/12/12.
 */

public class ValuePair {

    public String id;
    public String key;
    public String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
