package com.example.edcowpar.clientcontrol;

import android.app.Application;
import android.content.Context;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by EdCowpar on 2016/06/21.
 */
public class SubRoutines {

    public static boolean isValidEmail(String email) {
        // validating email id
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidSerialNo(String SerialNo) {
        // validating password with retype password
        return SerialNo != null && SerialNo.length() > 6;
    }

    public static String FmtString(String strText, String Fmt) {
        String txt;

        if (strText != null && !strText.equals("        ")) {
            switch (Fmt) {
                case "[a]":
                case "a":
                    // ccyymmdd to dd-mm-ccyy  (start,End) from 0
                    txt = strText.substring(6, 8) + "-" + strText.substring(4, 6) + "-" + strText.substring(0, 4);
                    return txt;
                case "m":
                case "[m]":
                    // ccyymmdd to Month ccyy
                    txt = getMonth(strText.substring(4, 6)) + " " + strText.substring(0, 4);
                    return txt;
                default:
                    return strText;
            }
        } else {
            txt = "        ";
            return txt;
        }
    }

    public static String getMonth(String mth) {
        switch (mth) {
            case "01":
                return "January";
            case "02":
                return "February";
            case "03":
                return "March";
            case "04":
                return "April";
            case "05":
                return "May";
            case "06":
                return "June";
            case "07":
                return "July";
            case "08":
                return "August";
            case "09":
                return "September";
            case "10":
                return "October";
            case "11":
                return "November";
            case "12":
                return "December";
            default:
                return mth;
        }
    }
    public static String formatDate(int year, int month, int day, String fmt) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);

        return sdf.format(date);
    }

    public static String splitAddress(String strAddress, Integer LineNo) {
        // split multiline string and return lineno
        BufferedReader rdr = new BufferedReader(new StringReader(strAddress));
        List<String> lines = new ArrayList<String>();
        String str = "";
        Integer count = 0;
        try {
            for (String line = rdr.readLine(); line != null; line = rdr.readLine()) {
                lines.add(line);
                count++;
                if (count.equals(LineNo)) str = line;
            }
            rdr.close();
            // lines now contains all the strings between line breaks
        } catch (IOException e) {
            str = "ERROR " + e.getMessage();
        }
        return str;
    }

    public static String leftpad(String text, int length) {
        return String.format("%" + length + "." + length + "s", text);
    }

    public static String rightpad(String text, int length) {
        return String.format("%-" + length + "." + length + "s", text);
    }

    public static Boolean setCheckBox(String myString) {
        if (myString != null) {
            return myString.equals("Y");
        } else {
            return false;
        }
    }

    public static String getCheckBox(Boolean checked) {
        if (!checked) {
            return "N";
        } else {
            return "Y";
        }
    }

    public static int getSpinnerIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static short getListIndex(short Count, List lst, String myString) {
        short index = 0;

        for (short i = 0; i < Count; i++) {
            if (lst.get(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static List<ReportNames> getReportNames() {
        List<ReportNames> ReportNames = new ArrayList<ReportNames>();

        ReportNames c = new ReportNames();
        c.Name = "Rep_001";
        c.Description = "Clients by Consultant";
        ReportNames.add(c);

        c = new ReportNames();
        c.Name = "Rep_002";
        c.Description = "Clients by Expiry Date";
        ReportNames.add(c);

        c = new ReportNames();
        c.Name = "Rep_003";
        c.Description = "Clients by System Type";
        ReportNames.add(c);

        c = new ReportNames();
        c.Name = "Rep_004";
        c.Description = "Clients by Value";
        ReportNames.add(c);

        c = new ReportNames();
        c.Name = "Rep_005";
        c.Description = "Clients by Volume";
        ReportNames.add(c);


        return ReportNames;
    }

    public static List<ReportNames> getAuditReportNames() {
        List<ReportNames> ReportNames = new ArrayList<ReportNames>();

        ReportNames c = new ReportNames();
        c.Name = "Aud_001";
        c.Description = "Client Renewals by Month";
        ReportNames.add(c);

        c = new ReportNames();
        c.Name = "Aud_002";
        c.Description = "Client Renewals by Client";
        ReportNames.add(c);

        c = new ReportNames();
        c.Name = "Aud_003";
        c.Description = "Client Renewals by Consultant";
        ReportNames.add(c);

        c = new ReportNames();
        c.Name = "Aud_004";
        c.Description = "Client Renewals by Action";
        ReportNames.add(c);

        return ReportNames;
    }

    public static int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }


}
