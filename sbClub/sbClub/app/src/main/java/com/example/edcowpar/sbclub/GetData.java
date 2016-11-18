package com.example.edcowpar.sbclub;

import android.app.Activity;
import android.content.Context;
import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Created by EdCowpar on 2016/07/17.
 */
public class GetData {
    static String eMes = "";

    public static void Write(Context ctx, AppSettings a) {
        String filename = "settings.txt";
        try {
            FileOutputStream fileOut = ctx.openFileOutput(filename, Activity.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(a);
            out.close();
            fileOut.close();
        } catch (Exception ex) {
            eMes = ex.getMessage();
        }
    }

    public static AppSettings Read(Context ctx) {
        AppSettings a = new AppSettings();
        String filename = "settings.txt";
        File f = new File(ctx.getFilesDir(), filename);
        a.RecNo = 0;
        a.SaveUser = "No";
        a.AutoLoad = "No";
        a.UserLevel = 0;
        a.Speech = "No";
        a.ClientSeq = 0;
        a.Supervisor="No";

        if (f.isFile()) {
            try {
                FileInputStream fileIn = ctx.openFileInput(filename);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                a = (AppSettings) in.readObject();
                in.close();
                fileIn.close();
            } catch (Exception ex) {
                eMes = ex.getMessage();
                a = new AppSettings();
                a.RecNo = 0;
            }
        }
        if (a.AutoLoad == null) a.AutoLoad = "No";
        if (a.SaveUser == null) a.SaveUser = "No";
        if (a.Speech == null) a.Speech = "No";
        if (a.ClientSeq == null) a.ClientSeq = 0;
        if (a.Supervisor == null) a.Supervisor = "No";
        return a;
    }

    public static void Write_ReportHeadings(Context ctx, String filename, ReportHeadings r) {
        try {
            FileOutputStream fileOut = ctx.openFileOutput(filename, Activity.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            r.RecNo = 1;
            out.writeObject(r);
            out.close();
            fileOut.close();
        } catch (Exception ex) {
            eMes = ex.getMessage();
        }
    }

    public static void Write_AuditHeadings(Context ctx, String filename,  AuditHeadings r) {
        try {
            FileOutputStream fileOut = ctx.openFileOutput(filename, Activity.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            r.RecNo = 1;
            out.writeObject(r);
            out.close();
            fileOut.close();
        } catch (Exception ex) {
            eMes = ex.getMessage();
        }
    }

    public static ReportHeadings Read_ReportHeadings(Context ctx, String filename) {
        ReportHeadings r = new ReportHeadings();
        File f = new File(ctx.getFilesDir(), filename);
        r.RecNo = 0;
        r.ClientNo = true;
        r.ClientName = true;
        r.ContactName = false;
        r.EmailAddress = false;
        r.PayeNo = false;
        r.Telephone = false;
        r.ExpiryDate = false;
        r.Volumn = false;
        r.UIFNo = false;
        r.SDLNo = false;
        r.System = false;
        r.AnnualLicence = false;
        r.Paid = false;
        r.Postal_01 = false;
        r.Postal_02 = false;
        r.Postal_03 = false;
        r.PostCode = false;
        r.InstallPin = false;
        r.PDFModule = false;
        r.Consultant = false;
        r.InCloud = false;
        if (f.isFile()) {
            try {
                FileInputStream fileIn = ctx.openFileInput(filename);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                r = (ReportHeadings) in.readObject();
                in.close();
                fileIn.close();
            } catch (Exception ex) {
                eMes = ex.getMessage();
            }
        }
        return r;
    }

    public static  AuditHeadings Read_AuditHeadings(Context ctx, String filename) {
        AuditHeadings r = new AuditHeadings();
        File f = new File(ctx.getFilesDir(), filename);
        r.RecNo = 0;
        r.ClientNo = true;
        r.ClientName = true;
        r.UserName = false;
        r.Remarks = false;
        r.Action = false;
        r.runDate = false;
        r.runTime = false;
        if (f.isFile()) {
            try {
                FileInputStream fileIn = ctx.openFileInput(filename);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                r = (AuditHeadings) in.readObject();
                in.close();
                fileIn.close();
            } catch (Exception ex) {
                eMes = ex.getMessage();
            }
        }
        return r;
    }

    public static void Write_ReportTotals(Context ctx, String filename, ReportTotals r) {
        try {
            FileOutputStream fileOut = ctx.openFileOutput(filename, Activity.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            r.RecNo = 1;
            out.writeObject(r);
            out.close();
            fileOut.close();
        } catch (Exception ex) {
            eMes = ex.getMessage();
        }
    }

    public static ReportTotals Read_ReportTotals(Context ctx, String filename) {
        ReportTotals r = new ReportTotals();
        File f = new File(ctx.getFilesDir(), filename);
        r.RecNo = 0;
        r.TotalsOnly = false;
        if (f.isFile()) {
            try {
                FileInputStream fileIn = ctx.openFileInput(filename);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                r = (ReportTotals) in.readObject();
                in.close();
                fileIn.close();
            } catch (Exception ex) {
                eMes = ex.getMessage();
            }
        }
        return r;
    }

    public static MemberRecord Read_MemberRecord(Context ctx) {
        MemberRecord c = new MemberRecord();
        String filename = "clientno.txt";
        File f = new File(ctx.getFilesDir(), filename);
        c.RecNo = 0;
        if (f.isFile()) {
            try {
                FileInputStream fileIn = ctx.openFileInput(filename);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                c = (MemberRecord) in.readObject();
                in.close();
                fileIn.close();
            } catch (Exception ex) {
                c.ClientName = ex.getMessage();
            }
        }
        return c;
    }

    public static void Write_MemberRecord(Context ctx, MemberRecord c) {
        String filename = "clientno.txt";
        try {
            FileOutputStream fileOut = ctx.openFileOutput(filename, Activity.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(c);
            out.close();
            fileOut.close();
        } catch (Exception ex) {
            eMes = ex.getMessage();
        }
    }

}
