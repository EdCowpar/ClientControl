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
public class SqlGet {


    private  Connection cn;

    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnURL = "jdbc:jtds:sqlserver://winsqls01.cpt.wa.co.za;databaseName=sbLic;user=sbUser;password=User2013;";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            //Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            //Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
           // Log.e("ERRO", e.getMessage());
        }
        return conn;
    }

    /**
     * Getting all labels
     * returns list of labels
     * */
    public List<String> getAllConsultants(){
        List<String> labels = new ArrayList<String>();

        if (cn == null) cn=CONN();   //Open Connection

        String sql = "select * from sbUsers ";
        ResultSet rs;
        try {
            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                 labels.add(rs.getString("UserName"));
            }
        } catch (SQLException e) {
            //Toast.makeText(CountryList.this, e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }

        return labels;
    }
    public String getConsultant(String strConsultant) {
        if (cn == null) cn=CONN();   //Open Connection

        String sql = "select UserName from sbUsers WHERE UserCode = '" + strConsultant + "'";
        ResultSet rs;
        String con = null;
        try {

            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                con = rs.getString("UserName");
            }
        } catch (SQLException e) {
            //Toast.makeText(CountryList.this, e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
        return con;
    }

    public String getSerialNo(String strSerialNo) {
        if (cn == null) cn=CONN();   //Open Connection

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
            //Toast.makeText(CountryList.this, e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
        return ser;
    }
    public String getClientName(String strSerialNo) {
        if (cn == null) cn=CONN();   //Open Connection
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
            //Toast.makeText(CountryList.this, e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
        return name;
    }
    public String AddClient(String strSerialNo,String strClientName,String strAddress,
                            String strContactNo,String strContactName,String strEmail){
        //Split multiline Address
        String post1=SubRoutines.splitAddress(strAddress,1);
        String post2=SubRoutines.splitAddress(strAddress,2);
        String post3=SubRoutines.splitAddress(strAddress,3);
        String PostCode=SubRoutines.splitAddress(strAddress,4);

        //Check Last Line for postcode
        if (PostCode.equals("")) {
            PostCode=post3;
            post3="";
        }
        String sql = "INSERT INTO sbClients (ClientNo,ClientName,Telephone,ContactName," +
                     "Postal_01,Postal_02,Postal_03,PostCode,EmailAddress)"+
                     "VALUES ("+
                     "'"+strSerialNo.trim()+"',"+
                     "'"+strClientName.trim().toUpperCase()+"',"+
                     "'"+strContactNo.trim()+"',"+
                     "'"+strContactName.trim().toUpperCase()+"',"+
                     "'"+post1.toUpperCase()+"',"+
                     "'"+post2.toUpperCase()+"',"+
                     "'"+post3.toUpperCase()+"',"+
                     "'"+PostCode.toUpperCase()+"',"+
                     "'"+strEmail.trim()+"')";
        try {
            if (cn == null) cn=CONN();   //Open Connection
            Statement statement = cn.createStatement();
            statement.execute(sql);
            sql="Record Added";

        } catch (SQLException e) {
             sql="ERROR "+ e.getMessage();
        }
        return sql;
    }
    public ClientRecord getClient(String strSerialNo) {
        if (cn == null) cn=CONN();   //Open Connection
        String sql = "select * from sbClients WHERE ClientNo = '" + strSerialNo + "'";
        ResultSet rs;
        ClientRecord c = new ClientRecord();

        try {

            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                c.RecNo = rs.getString("ClientNo").toString();
                c.ClientNo = rs.getString("ClientNo");
                c.ClientName = rs.getString("ClientName");
                c.ContactName = rs.getString("ContactName");
                c.Telephone = rs.getString("Telephone");
                c.EmailAddress = rs.getString("EmailAddress");
                c.PayeNo  = rs.getString("PayeNo");
                c.ExpiryDate  = rs.getString("ExpiryDate");
                c.Volumn  = rs.getString("Volumn");
                c.UIFNo  = rs.getString("UIFNo");
                c.SDLNo  = rs.getString("SDLNo");
                c.System  = rs.getString("System");
                c.Annual_Licence  = rs.getString("Annual_Licence");
                c.Paid  = rs.getString("Paid");
                c.Postal_01  = rs.getString("Postal_01");
                c.Postal_02  = rs.getString("Postal_02");
                c.Postal_03  = rs.getString("Postal_03");
                c.PostCode  = rs.getString("PostCode");
                c.InstallPin  = rs.getString("InstallPin");
                c.PDFModule  = rs.getString("PDFModule");
                c.Consultant  = rs.getString("Consultant");
                c.InCloud  = rs.getString("InCloud");
            }
        } catch (SQLException e) {
            c.errMessage= e.getMessage().toString();
        }
        return c;

    }
}

