package com.example.edcowpar.clientcontrol;

import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
}
