package com.example.edcowpar.clientcontrol;

import java.io.Serializable;

/**
 * Created by EdCowpar on 2016/07/17.
 */
public class AppSettings implements Serializable {
    String AutoLoad;
    String SaveUser;
    Integer UserLevel;
    Integer RecNo;
    String Password;
    String UserCode;
    String UserName;
    String Email;
    String Supervisor;
    String Controller;
    String Telephone;

    public String getSaveUser() {
        return SaveUser;
    }

    public void setSaveUser(String saveUser) {
        SaveUser = saveUser;
    }

    public Integer getRecNo() {
        return RecNo;
    }

    public void setRecNo(Integer recNo) {
        RecNo = recNo;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getSupervisor() {
        return Supervisor;
    }

    public void setSupervisor(String supervisor) {
        Supervisor = supervisor;
    }

    public String getController() {
        return Controller;
    }

    public void setController(String controller) {
        Controller = controller;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getAutoLoad() {
        return AutoLoad;
    }

    public void setAutoLoad(String autoLoad) {
        AutoLoad = autoLoad;
    }

    public Integer getUserLevel() {
        return UserLevel;
    }

    public void setUserLevel(Integer userLevel) {
        UserLevel = userLevel;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String userCode) {
        UserCode = userCode;
    }
}
