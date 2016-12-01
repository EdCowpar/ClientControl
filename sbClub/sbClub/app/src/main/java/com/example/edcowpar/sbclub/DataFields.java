package com.example.edcowpar.sbclub;

import java.io.Serializable;

/**
 * Created by EdCowpar on 2016/11/30.
 */
public class DataFields implements Serializable {
    String dbTable;
    String dbField;
    String Description;

    public String getDbTable() {
        return dbTable;
    }

    public String getDbField() {
        return dbField;
    }

    public String getDescription() {
        return Description;
    }
}
