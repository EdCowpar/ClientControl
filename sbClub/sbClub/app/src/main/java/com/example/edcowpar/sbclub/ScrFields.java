package com.example.edcowpar.sbclub;

import java.io.Serializable;

/**
 * Created by EdCowpar on 2017/01/23.
 */
public class ScrFields implements Serializable {
    public Integer RecNo;
    public String FldNo;
    public String Description;
    public String Fmt;
    public String Value;
    public String oValue;  //Original Value
    public String dbName;
    public String TabBox;

    public String getFldNo() {
        return FldNo;
    }

    public String getDescription() {
        return Description;
    }

    public String getFmt() {
        return Fmt;
    }

    public String getValue() {
        return Value;
    }

    public String getoValue() {
        return oValue;
    }

    public void setValue(String value) {
        Value = value;
    }

    public void setoValue(String oValue) {
        this.oValue = oValue;
    }

    public String getDbName() {
        return dbName;
    }

    public String getTabBox() {
        return TabBox;
    }

    public Integer getRecNo() {
        return RecNo;
    }

    public void setRecNo(Integer recNo) {
        RecNo = recNo;
    }
}
