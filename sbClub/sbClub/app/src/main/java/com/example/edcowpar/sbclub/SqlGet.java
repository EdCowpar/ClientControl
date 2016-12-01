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

    public List<DataKeys> getDataKeys(String SFP) {
        List<DataKeys> Keys = new ArrayList<>();
        String sql;

        sql = "select * from sbDatabases WHERE Prg = '" + SFP + "'";
        ResultSet rs;

        try {
            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                DataKeys k = new DataKeys();
                k.Prg = rs.getString("Prg").trim();
                k.dbTable = rs.getString("Description").trim();
                k.Pky = rs.getString("PkydbName").trim();
                k.Sky = rs.getString("SkydbName").trim();
                k.Scl = rs.getString("ScldbName").trim();
                Keys.add(k);
            }
        } catch (SQLException e) {
            eMes = e.getMessage();
        }
        return Keys;
    }

    public List<DataFields> getDataFields(List<DataKeys> Keys) {
        List<DataFields> Fields = new ArrayList<>();
        String dbName, sql, pky, sky, scl;
        DataFields d;
        //extract keys
        pky = Keys.get(0).getPky();
        sky = Keys.get(0).getSky();
        scl = Keys.get(0).getScl();
        //add key fields
        d = new DataFields();
        d.dbTable=Keys.get(0).getDbTable();
        d.dbField = pky;
        Fields.add(d);
        d = new DataFields();
        d.dbField = sky;
        Fields.add(d);
        d = new DataFields();
        d.dbField = scl;
        Fields.add(d);

        //get additional display fields'
        String SFP = Keys.get(0).getPrg();

        sql = "select * from sbScreens WHERE ScrSFP = '" + SFP + "' AND Prnt = 'YY'";
        ResultSet rs;

        try {
            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                dbName = rs.getString("dbName").trim();
                if (!dbName.equals(pky) && !dbName.equals(sky) && !dbName.equals(scl)) {
                    d = new DataFields();
                    d.dbField = dbName;
                    d.Description = rs.getString("Desc").trim();
                    Fields.add(d);
                }
            }
        } catch (SQLException e) {
            eMes = e.getMessage();
        }
        return Fields;
    }

    public List<DataRecord> getDataRecords(List<DataFields> Fields) {
        List<DataRecord> Record = new ArrayList<>();
        String sql;
        String dbTable;
        String heading;

        dbTable = Fields.get(0).getDbTable();
        sql = "select * from " + dbTable;
        ResultSet rs;

        try {
            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                DataRecord d = new DataRecord();
                for (int i = 0; i < Fields.size(); i++) {
                    switch (i) {
                        case 0:  //pky
                            d._0 = rs.getString(Fields.get(i).getDbField()).trim();
                            break;
                        case 1:  //sky
                            d._1 = rs.getString(Fields.get(i).getDbField()).trim();
                            break;
                        case 2:  //scl
                            d._2 = rs.getString(Fields.get(i).getDbField()).trim();
                            break;
                        case 4:   //additional fields with heading
                            heading=Fields.get(i).getDescription().trim() + ": ";
                            d._4 =heading + rs.getString(Fields.get(i).getDbField()).trim();
                            break;
                        case 5:   //additional fields with heading
                            heading=Fields.get(i).getDescription().trim() + ": ";
                            d._5 =heading + rs.getString(Fields.get(i).getDbField()).trim();
                            break;
                        case 6:   //additional fields with heading
                            heading=Fields.get(i).getDescription().trim() + ": ";
                            d._6 =heading + rs.getString(Fields.get(i).getDbField()).trim();
                            break;
                        case 7:   //additional fields with heading
                            heading=Fields.get(i).getDescription().trim() + ": ";
                            d._7 =heading + rs.getString(Fields.get(i).getDbField()).trim();
                            break;
                        case 8:   //additional fields with heading
                            heading=Fields.get(i).getDescription().trim() + ": ";
                            d._8 =heading + rs.getString(Fields.get(i).getDbField()).trim();
                            break;
                        case 9:   //additional fields with heading
                            heading=Fields.get(i).getDescription().trim() + ": ";
                            d._9 =heading + rs.getString(Fields.get(i).getDbField()).trim();
                            break;
                        case 10:   //additional fields with heading
                            heading=Fields.get(i).getDescription().trim() + ": ";
                            d._10 =heading + rs.getString(Fields.get(i).getDbField()).trim();
                            break;
                        case 11:   //additional fields with heading
                            heading=Fields.get(i).getDescription().trim() + ": ";
                            d._11 =heading + rs.getString(Fields.get(i).getDbField()).trim();
                            break;
                        case 12:   //additional fields with heading
                            heading=Fields.get(i).getDescription().trim() + ": ";
                            d._12 =heading + rs.getString(Fields.get(i).getDbField()).trim();
                            break;
                        default:
                            //ignore
                    }
                }
                Record.add(d);
            }
        } catch (SQLException e) {
            eMes = e.getMessage();
        }
        return Record;
    }

}
