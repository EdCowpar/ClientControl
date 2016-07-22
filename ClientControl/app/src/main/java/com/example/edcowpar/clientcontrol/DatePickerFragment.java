package com.example.edcowpar.clientcontrol;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by EdCowpar on 2016/07/03.
 */
public class DatePickerFragment extends DialogFragment {
    public static String date;
    StringBuilder sb = new StringBuilder();
    private EditText etExpiryDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), (ClientDetailActivity.setDateActivity) getActivity(), year, month, day);
    }

}
