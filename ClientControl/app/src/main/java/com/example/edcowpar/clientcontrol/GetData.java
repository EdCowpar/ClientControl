package com.example.edcowpar.clientcontrol;

import android.app.Activity;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by EdCowpar on 2016/07/17.
 */
public class GetData {
    static String eMes = "";

    public static void Write(Context ctx) {
        AppSettings a = new AppSettings();
        a.setAutoLoad("Joe");
        a.setUserCode("x");
        a.setUserLevel("a");
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

        if (f.isFile()) {
            try {
                FileInputStream fileIn = ctx.openFileInput(filename);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                a = (AppSettings) in.readObject();
                in.close();
                fileIn.close();
            } catch (Exception ex) {
                eMes = ex.getMessage();
            }
        }
        return a;
    }
}
