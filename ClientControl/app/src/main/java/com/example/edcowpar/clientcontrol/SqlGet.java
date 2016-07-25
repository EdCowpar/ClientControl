package com.example.edcowpar.clientcontrol;

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

    public String get_eMes() {
        return eMes;
    }

    /**
     * Getting all labels
     * returns list of labels
     */
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

    public ConsultantRecord getConsultant(String strConsultant) {
        String sql = "select * from sbUsers WHERE UserCode = '" + strConsultant + "'";
        ResultSet rs;
        ConsultantRecord c = new ConsultantRecord();
        try {

            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                c.RecNo = Integer.parseInt(rs.getString("RecNo"));
                c.Password = rs.getString("Password");
                c.UserCode = rs.getString("UserCode");
                c.UserName = rs.getString("UserName");
                c.Email = rs.getString("Email");
                c.Supervisor = rs.getString("Supervisor");
                c.Controller = rs.getString("Controller");
                c.Telephone = rs.getString("Telephone");
            }
        } catch (SQLException e) {
            eMes = e.getMessage();
        }
        return c;
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

    public String AddClient(String strSerialNo, String strClientName, String strAddress,
                            String strContactNo, String strContactName, String strEmail) {
        //Split multiline Address
        String post1 = SubRoutines.splitAddress(strAddress, 1);
        String post2 = SubRoutines.splitAddress(strAddress, 2);
        String post3 = SubRoutines.splitAddress(strAddress, 3);
        String PostCode = SubRoutines.splitAddress(strAddress, 4);

        //Check Last Line for postcode
        if (PostCode.equals("")) {
            PostCode = post3;
            post3 = "";
        }
        String sql = "INSERT INTO sbClients (ClientNo,ClientName,Telephone,ContactName," +
                "Postal_01,Postal_02,Postal_03,PostCode,EmailAddress)" +
                "VALUES (" +
                "'" + strSerialNo.trim() + "'," +
                "'" + strClientName.trim().toUpperCase() + "'," +
                "'" + strContactNo.trim() + "'," +
                "'" + strContactName.trim().toUpperCase() + "'," +
                "'" + post1.toUpperCase() + "'," +
                "'" + post2.toUpperCase() + "'," +
                "'" + post3.toUpperCase() + "'," +
                "'" + PostCode.toUpperCase() + "'," +
                "'" + strEmail.trim() + "')";
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

    public ClientRecord getClient(String strSerialNo) {
        String sql = "select * from sbClients WHERE ClientNo = '" + strSerialNo + "'";
        ResultSet rs;
        ClientRecord c = new ClientRecord();

        try {

            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                c.RecNo = Integer.parseInt(rs.getString("RecNo"));
                c.ClientNo = rs.getString("ClientNo");
                c.ClientName = rs.getString("ClientName");
                c.ContactName = rs.getString("ContactName");
                c.Telephone = rs.getString("Telephone");
                c.EmailAddress = rs.getString("EmailAddress");
                c.PayeNo = rs.getString("PayeNo");
                c.ExpiryDate = rs.getString("ExpiryDate");
                c.Volumn = rs.getString("Volumn");
                c.UIFNo = rs.getString("UIFNo");
                c.SDLNo = rs.getString("SDLNo");
                c.System = rs.getString("System");
                c.Annual_Licence = rs.getString("Annual_Licence");
                c.Paid = rs.getString("Paid");
                c.Postal_01 = rs.getString("Postal_01");
                c.Postal_02 = rs.getString("Postal_02");
                c.Postal_03 = rs.getString("Postal_03");
                c.PostCode = rs.getString("PostCode");
                c.InstallPin = rs.getString("InstallPin");
                c.PDFModule = rs.getString("PDFModule");
                c.Consultant = rs.getString("Consultant");
                c.InCloud = rs.getString("InCloud");
            }
        } catch (SQLException e) {
            eMes = e.getMessage();
        }
        return c;

    }

    public List<ClientRecord> getAllClients(String Where, String Search, String Sequence) {
        List<ClientRecord> Clients = new ArrayList<ClientRecord>();
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
                ClientRecord c = new ClientRecord();
                c.RecNo = Integer.parseInt(rs.getString("RecNo"));
                c.ClientNo = rs.getString("ClientNo");
                c.ClientName = rs.getString("ClientName");
                c.ContactName = rs.getString("ContactName");
                c.Telephone = rs.getString("Telephone");
                c.EmailAddress = rs.getString("EmailAddress");
                c.PayeNo = rs.getString("PayeNo");
                c.ExpiryDate = rs.getString("ExpiryDate");
                c.Volumn = rs.getString("Volumn");
                c.UIFNo = rs.getString("UIFNo");
                c.SDLNo = rs.getString("SDLNo");
                c.System = rs.getString("System");
                c.Annual_Licence = rs.getString("Annual_Licence");
                c.Paid = rs.getString("Paid");
                c.Postal_01 = rs.getString("Postal_01");
                c.Postal_02 = rs.getString("Postal_02");
                c.Postal_03 = rs.getString("Postal_03");
                c.PostCode = rs.getString("PostCode");
                c.InstallPin = rs.getString("InstallPin");
                c.PDFModule = rs.getString("PDFModule");
                c.Consultant = rs.getString("Consultant");
                c.InCloud = rs.getString("InCloud");

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

    public String UpdClient(ClientRecord c) {

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
            Statement statement = cn.createStatement();
            statement.execute(sql);
            sql = "Record Updated";

        } catch (SQLException e) {
            eMes = e.getMessage();
            sql = "ERROR " + e.getMessage();
        }
        return sql;
    }

    public List<ConsultantRecord> getListConsultants() {
        List<ConsultantRecord> Consultants = new ArrayList<ConsultantRecord>();
        String sql, order, where;


        sql = "select * from sbUsers ";
        ResultSet rs;

        try {

            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                ConsultantRecord c = new ConsultantRecord();
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

    public ClientRecord populateClientRecord(ResultSet rs) {
        ClientRecord c = new ClientRecord();

        try {
            c.RecNo = Integer.parseInt(rs.getString("RecNo"));
            c.ClientNo = rs.getString("ClientNo");
            c.ClientName = rs.getString("ClientName");
            c.ContactName = rs.getString("ContactName");
            c.Telephone = rs.getString("Telephone");
            c.EmailAddress = rs.getString("EmailAddress");
            c.PayeNo = rs.getString("PayeNo");
            c.ExpiryDate = rs.getString("ExpiryDate");
            c.Volumn = rs.getString("Volumn");
            c.UIFNo = rs.getString("UIFNo");
            c.SDLNo = rs.getString("SDLNo");
            c.System = rs.getString("System");
            c.Annual_Licence = rs.getString("Annual_Licence");
            c.Paid = rs.getString("Paid");
            c.Postal_01 = rs.getString("Postal_01");
            c.Postal_02 = rs.getString("Postal_02");
            c.Postal_03 = rs.getString("Postal_03");
            c.PostCode = rs.getString("PostCode");
            c.InstallPin = rs.getString("InstallPin");
            c.PDFModule = rs.getString("PDFModule");
            c.Consultant = rs.getString("Consultant");
            c.InCloud = rs.getString("InCloud");
        } catch (SQLException e) {
            eMes = e.getMessage();
        }
        return c;
    }
}
