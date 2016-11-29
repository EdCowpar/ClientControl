package com.example.edcowpar.sbclub;

import android.content.Context;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EdCowpar on 2016/06/21.
 */
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EdCowpar on 2016/07/06.
 */

/**
 * Remember to add
 * <uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * in AndroidManifest.xml
 */
public class SqlGet {
    String eMes;
    Connection cn;

    public static String getField(ResultSet rs, String Name) {
        try {
            String myString = rs.getString(Name);
            if (myString != null) {
                return myString;
            } else {
                return "Null";
            }
        } catch (SQLException e) {
            return "";
        }
    }

    public static String getDateField(ResultSet rs, String Name) {
        try {
            String myString = rs.getString(Name);
            if (myString != null) {
                return myString;
            } else {
                return "        ";
            }
        } catch (SQLException e) {
            return "";
        }
    }

    public static String getNumberField(ResultSet rs, String Name) {
        try {
            String myString = rs.getString(Name);
            if (rs.wasNull()) {
                return "0";
            } else {
                return myString;
            }
        } catch (SQLException e) {
            return "";
        }
    }

    public String OpenConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        eMes = "ok";
        cn = null;
        String ConnURL;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnURL = "jdbc:jtds:sqlserver://winsqls02.cpt.wa.co.za;databaseName=sb1610271;user=RoseCowpar;Password=Rose01;";
            cn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            eMes = se.getMessage();
        } catch (ClassNotFoundException e) {
            eMes = e.getMessage();
        } catch (Exception e) {
            eMes = e.getMessage();
        }
        return eMes;
    }

    public Connection getConnection() {
        return cn;
    }


    public UserRecord getUser(String strUser) {
        String sql = "select * from sbUsers WHERE Usr = '" + strUser + "'";
        ResultSet rs;
        UserRecord c = new UserRecord();
        c.RecNo = 0;

        try {

            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                c.RecNo = Integer.parseInt(rs.getString("RecNo"));
                c.Pas = rs.getString("Pas").trim();
                c.Usr = rs.getString("Usr").trim();
                c.Usn = rs.getString("Usn").trim();
            }
        } catch (SQLException e) {
            c.RecNo = 0;
            eMes = e.getMessage();
        }
        return c;
    }
    public String get_eMes() {
        return eMes;
    }

    public String getUserName(String strUser) {
        String sql = "select * from sbUsers WHERE Usr = '" + strUser + "'";
        ResultSet rs;
        String c = "Not Set";
        try {

            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                c = rs.getString("Usn").trim();
            }
        } catch (SQLException e) {
            eMes = e.getMessage();
        }
        return c;
    }

    public List<UserRecord> getListConsultants() {
        List<UserRecord> Users = new ArrayList<UserRecord>();
        String sql, order, where;


        sql = "select * from sbUsers ";
        ResultSet rs;

        try {

            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                UserRecord c = new UserRecord();
                c.RecNo = Integer.parseInt(rs.getString("RecNo"));
                c.Pas = rs.getString("Pas");
                c.Usr = rs.getString("Usr");
                c.Usn = rs.getString("Usn");

                Users.add(c);

            }
        } catch (SQLException e) {
            eMes = e.getMessage();
        }

        return Users;
    }

     public List<DataRecord> getDataRecord(String SFP) {
        List<DataRecord> Fields = new ArrayList<DataRecord>();
        String sql;

        sql = "select * from sbScreens WHERE ScrSFP = '" + SFP + "' AND Prnt = 'YY'";
        ResultSet rs;

        try {
            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                DataRecord d = new DataRecord();
                d.dbTable = rs.getString("dbTable");
                d.dbField = rs.getString("dbName");
                d.Description = rs.getString("Desc");
                Fields.add(d);
            }
        } catch (SQLException e) {
            eMes = e.getMessage();
        }
        return Fields;
    }

}
