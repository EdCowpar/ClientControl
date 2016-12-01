package com.example.edcowpar.sbclub;

import java.io.Serializable;

/**
 * Created by EdCowpar on 2016/11/30.
 */
public class DataFields implements Serializable {
    String dbTable;
    String dbField;
    String Fmt;
    String Description;

    public String getFmt() {
        return Fmt;
    }

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
