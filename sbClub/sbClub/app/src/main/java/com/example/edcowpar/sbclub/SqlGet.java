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
            ConnURL = "jdbc:jtds:sqlserver://winsqls01.cpt.wa.co.za;databaseName=sbLic;user=sbUser;password=User2013;";
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

    public String AddConsultant(UserRecord c) {

        String sql = "INSERT INTO sbUsers (UserCode,UserName,Password,Email,Supervisor," +
                "Controller,Telephone)" +
                "VALUES (" +
                "'" + c.UserCode + "'," +
                "'" + c.UserName + "'," +
                "'" + c.Password + "'," +
                "'" + c.Email + "'," +
                "'" + c.Supervisor + "'," +
                "'" + c.Controller + "'," +
                "'" + c.Telephone + "')";
        try {
            Statement statement = cn.createStatement();
            statement.execute(sql);
            sql = "Record Added";

        } catch (SQLException e) {
            eMes = e.getMessage();
            sql = "ERROR " + e.getMessage();
        }
        return sql;
    }

    public UserRecord getConsultant(String strConsultant) {
        String sql = "select * from sbUsers WHERE UserCode = '" + strConsultant + "'";
        ResultSet rs;
        UserRecord c = new UserRecord();
        c.RecNo = 0;

        try {

            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                c.RecNo = Integer.parseInt(rs.getString("RecNo"));
                c.Password = rs.getString("Password");
                c.UserCode = rs.getString("UserCode");
                c.UserName = rs.getString("UserName").trim();
                c.Email = rs.getString("Email").trim();
                c.Supervisor = rs.getString("Supervisor");
                c.Controller = rs.getString("Controller");
                c.Telephone = rs.getString("Telephone");
            }
        } catch (SQLException e) {
            c.RecNo = 0;
            eMes = e.getMessage();
        }
        return c;
    }

    public String getConsultantName(String strConsultant) {
        String sql = "select * from sbUsers WHERE UserCode = '" + strConsultant + "'";
        ResultSet rs;
        String c = "Not Set";
        try {

            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                c = rs.getString("UserName").trim();
            }
        } catch (SQLException e) {
            eMes = e.getMessage();
        }
        return c;
    }

    public String UpdConsultant(UserRecord c) {

        String sql = "UPDATE sbUsers SET UserCode = '" + c.UserCode + "'," +
                "UserName  = '" + c.UserName.trim() + "'," +
                "Email  = '" + c.Email.trim() + "'," +
                "Supervisor  = '" + c.Supervisor.trim() + "'," +
                "Controller  = '" + c.Controller.trim() + "'," +
                "Telephone  = '" + c.Telephone.trim() + "' " +
                "WHERE RecNo = '" + c.RecNo + "'";

        try {
            Statement statement = cn.createStatement();
            statement.execute(sql);
            sql = "Record Updated";

        } catch (SQLException e) {
            eMes = e.getMessage();
            sql = "ERROR " + e.getMessage();
        }
        return sql;
    }

    public String DeleteConsultant(Integer RecNo) {

        String sql = "DELETE sbUsers WHERE RecNo = '" + RecNo.toString() + "'";

        try {
            Statement statement = cn.createStatement();
            statement.execute(sql);
            sql = "Record Deleted";

        } catch (SQLException e) {
            eMes = e.getMessage();
            sql = "ERROR " + e.getMessage();
        }
        return sql;
    }

    public String get_eMes() {
        return eMes;
    }

    public ComboItems getAllConsultants(String BlankDescription) {
        ComboItems ci = new ComboItems();
        ci.Code = new ArrayList<String>();
        ci.Description = new ArrayList<String>();

        ci.Code.add("All");
        ci.Description.add(BlankDescription);
        ci.Count = 1;

        String sql = "select * from sbUsers ";
        ResultSet rs;
        try {
            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                ci.Code.add(rs.getString("UserCode"));
                ci.Description.add(rs.getString("UserName"));
                ci.Count++;
            }
        } catch (SQLException e) {
            eMes = e.getMessage();
        }

        return ci;
    }

    public String getSerialNo(String strSerialNo) {
        String sql = "select ClientNo from sbClients WHERE ClientNo = '" + strSerialNo + "'";
        ResultSet rs;
        String ser = null;
        try {

            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                ser = rs.getString("ClientNo");
            }
        } catch (SQLException e) {
            eMes = e.getMessage();
        }
        return ser;
    }

    public String getClientName(String strSerialNo) {
        String sql = "select ClientName from sbClients WHERE ClientNo = '" + strSerialNo + "'";
        ResultSet rs;
        String name = "";
        try {

            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                name = rs.getString("ClientName");
            }
        } catch (SQLException e) {
            eMes = e.getMessage();
        }
        return name;
    }

    public String AddClient(MemberRecord c) {

        String sql = "INSERT INTO sbClients (ClientNo,ClientName,Telephone,ContactName," +
                "Postal_01,Postal_02,Postal_03,PostCode,EmailAddress)" +
                "VALUES (" +
                "'" + c.ClientNo.trim() + "'," +
                "'" + c.ClientName.trim().toUpperCase() + "'," +
                "'" + c.Telephone.trim() + "'," +
                "'" + c.ContactName.trim().toUpperCase() + "'," +
                "'" + c.Postal_01.toUpperCase() + "'," +
                "'" + c.Postal_02.toUpperCase() + "'," +
                "'" + c.Postal_03.toUpperCase() + "'," +
                "'" + c.PostCode.toUpperCase() + "'," +
                "'" + c.EmailAddress.trim() + "')";
        try {
            Statement statement = cn.createStatement();
            statement.execute(sql);
            sql = "Record Added";

        } catch (SQLException e) {
            eMes = e.getMessage();
            sql = "ERROR " + e.getMessage();
        }
        return sql;
    }

    public MemberRecord getClient(String strSerialNo) {
        String sql = "select * from sbClients WHERE ClientNo = '" + strSerialNo + "'";
        ResultSet rs;
        MemberRecord c = new MemberRecord();

        try {

            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                c = populateMemberRecord(rs);
            }
        } catch (SQLException e) {
            eMes = e.getMessage();
        }
        return c;

    }

    public List<MemberRecord> getAllClients(String Where, String Search, String Sequence) {
        List<MemberRecord> Clients = new ArrayList<MemberRecord>();
        String sql, order;
        eMes = "ok";

        if (Search != null) {
            Search = Search.replace("'", "");  //remove quotes
        }

        switch (Sequence) {
            case "ORDER BY ClientName":
                order = "ORDER BY ClientName";
                if (!Search.equals("")) {
                    if (Where.equals("")) {
                        Where = "WHERE ClientName LIKE '" + Search + "%' ";
                    } else {
                        Where = "AND ClientName LIKE '" + Search + "%' ";
                    }
                }
                break;
            case "ORDER BY ClientNo":
                order = "ORDER BY ClientNo";
                if (!Search.equals("")) {
                    if (Where.equals("")) {
                        Where = "WHERE ClientNo LIKE '" + Search + "%' ";
                    } else {
                        Where = "AND ClientNo LIKE '" + Search + "%' ";
                    }
                }
                break;
            default:
                order = Sequence;
        }

        sql = "select * from sbClients " + Where + order; //+ " COLLATE SQL_Latin1_General_CP1_CI_AS";  //Ignore case
        ResultSet rs;
        Integer count = 0;

        try {

            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                count++;
                MemberRecord c = populateMemberRecord(rs);
                Clients.add(c);

            }
        } catch (SQLException e) {
            eMes = sql + "\n" + e.getMessage();
        }
        if (count.equals(0)) {
            eMes = "No records found " + Where;
        }
        return Clients;
    }

    public String UpdClient(MemberRecord c) {

        String sql = "UPDATE sbClients SET ClientNo = '" + c.ClientNo + "'," +
                "ContactName  = '" + c.ContactName.trim() + "'," +
                "EmailAddress  = '" + c.EmailAddress.trim() + "'," +
                "ClientName  = '" + c.ClientName.trim() + "'," +
                "PayeNo  = '" + c.PayeNo.trim() + "'," +
                "Telephone  = '" + c.Telephone.trim() + "'," +
                "ExpiryDate  = '" + c.ExpiryDate.trim() + "'," +
                "Volumn  = '" + c.Volumn.trim() + "'," +
                "UIFNo  = '" + c.UIFNo.trim() + "'," +
                "SDLNo  = '" + c.SDLNo.trim() + "'," +
                "System  = '" + c.System.trim() + "'," +
                "Annual_Licence  = '" + c.Annual_Licence.trim() + "'," +
                "Paid  = '" + c.Paid.trim() + "'," +
                "Postal_01  = '" + c.Postal_01.trim() + "'," +
                "Postal_02  = '" + c.Postal_02.trim() + "'," +
                "Postal_03  = '" + c.Postal_03.trim() + "'," +
                "PostCode  = '" + c.PostCode.trim() + "'," +
                "InstallPin  = '" + c.InstallPin.trim() + "'," +
                "PDFModule  = '" + c.PDFModule.trim() + "'," +
                "Consultant  = '" + c.Consultant.trim() + "'," +
                "InCloud  = '" + c.InCloud.trim() + "' " +
                "WHERE RecNo = '" + c.RecNo + "'";

        try {
            eMes = "ok";
            Statement statement = cn.createStatement();
            statement.execute(sql);

        } catch (SQLException e) {
            eMes = e.getMessage();
        }
        return eMes;
    }

    public List<UserRecord> getListConsultants() {
        List<UserRecord> Consultants = new ArrayList<UserRecord>();
        String sql, order, where;


        sql = "select * from sbUsers ";
        ResultSet rs;

        try {

            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                UserRecord c = new UserRecord();
                c.RecNo = Integer.parseInt(rs.getString("RecNo"));
                c.Password = rs.getString("Password");
                c.UserCode = rs.getString("UserCode");
                c.UserCode = SubRoutines.rightpad(c.UserCode, 4);
                c.UserName = rs.getString("UserName");
                c.Email = rs.getString("Email");
                c.Supervisor = rs.getString("Supervisor");
                c.Controller = rs.getString("Controller");
                c.Telephone = rs.getString("Telephone");

                Consultants.add(c);

            }
        } catch (SQLException e) {
            eMes = e.getMessage();
        }

        return Consultants;
    }

    public MemberRecord populateMemberRecord(ResultSet rs) {
        MemberRecord c = new MemberRecord();

        try {
            c.RecNo = Integer.parseInt(rs.getString("RecNo"));
            c.ClientNo = getField(rs, "ClientNo");
            c.ClientName = getField(rs, "ClientName");
            c.ContactName = getField(rs, "ContactName");
            c.Telephone = getField(rs, "Telephone");
            c.EmailAddress = getField(rs, "EmailAddress");
            c.PayeNo = getField(rs, "PayeNo");
            c.ExpiryDate = getDateField(rs, "ExpiryDate");
            c.Volumn = getNumberField(rs, "Volumn");
            c.UIFNo = getField(rs, "UIFNo");
            c.SDLNo = getField(rs, "SDLNo");
            c.System = getField(rs, "System");
            c.Annual_Licence = getNumberField(rs, "Annual_Licence");
            c.Paid = getField(rs, "Paid");
            c.Postal_01 = getField(rs, "Postal_01");
            c.Postal_02 = getField(rs, "Postal_02");
            c.Postal_03 = getField(rs, "Postal_03");
            c.PostCode = getField(rs, "PostCode");
            c.InstallPin = getField(rs, "InstallPin");
            c.PDFModule = getField(rs, "PDFModule");
            c.Consultant = getField(rs, "Consultant");
            c.InCloud = getField(rs, "InCloud");
        } catch (SQLException e) {
            eMes = e.getMessage();
        }
        return c;
    }

    public AuditRecord populateAuditRecord(ResultSet rs) {
        AuditRecord c = new AuditRecord();

        try {
            c.RecNo = Integer.parseInt(rs.getString("RecNo"));
            c.UserName = getField(rs, "UserName");
            c.Action = getField(rs, "Action");
            c.ClientNo = getField(rs, "ClientNo");
            c.ClientName = getField(rs, "ClientName");
            c.runDate = getField(rs, "runDate");
            c.runTime = getField(rs, "runTime");
            c.Remarks = getField(rs, "Remarks");
        } catch (SQLException e) {
            eMes = e.getMessage();
        }
        return c;
    }

    public String getSystemType(Context context, String sType) {
        int i;
        if (sType != null) {
            if (sType.equals(" ") || sType.equals("Null")) {
                i = 10;
            } else {
                i = Integer.parseInt(sType);
            }
        } else {
            i = 10;   //Not Set'
        }
        String v = context.getResources().getStringArray(R.array.system_types)[i];
        return v;
    }

}
