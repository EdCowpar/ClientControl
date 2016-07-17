package com.example.edcowpar.clientcontrol;

import java.io.Serializable;

/**
 * Created by EdCowpar on 2016/07/17.
 */
public class AppSettings implements Serializable {
    String UserCode;
    String AutoLoad;
    String UserLevel;

    public String getAutoLoad() {
        return AutoLoad;
    }

    public void setAutoLoad(String autoLoad) {
        AutoLoad = autoLoad;
    }

    public String getUserLevel() {
        return UserLevel;
    }

    public void setUserLevel(String userLevel) {
        UserLevel = userLevel;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String userCode) {
        UserCode = userCode;
    }
}
