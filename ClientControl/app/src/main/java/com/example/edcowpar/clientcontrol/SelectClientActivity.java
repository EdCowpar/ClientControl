package com.example.edcowpar.clientcontrol;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;


public class SelectClientActivity extends AppCompatActivity {
    EditText etSearchText, etFromDate, etToDate;
    Spinner spSystemType, spConsultant;
    SqlGet sq;
    ComboItems ci;
    String strTitle, strSequence, strSearchText, strWhere, strFromDate, strToDate;
    FloatingActionButton fab;

    private int year, month, day;
    private DatePickerDialog.OnDateSetListener FromDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            String date = SubRoutines.formatDate(year, month, day, "yyyyMMdd");
            etFromDate.setText(date);
        }
    };
    private DatePickerDialog.OnDateSetListener ToDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            String date = SubRoutines.formatDate(year, month, day, "yyyyMMdd");
            etToDate.setText(date);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_client);

        //clear fields
        strTitle = "Current";
        strSequence = "ORDER BY ClientNo";
        strSearchText = "";
        strWhere = "";
        strFromDate = "";
        strToDate = "";

        etSearchText = (EditText) findViewById(R.id.etSearchText);
        etFromDate = (EditText) findViewById(R.id.etFromDate);
        etToDate = (EditText) findViewById(R.id.etToDate);
        spSystemType = (Spinner) findViewById(R.id.spSystemType);
        spConsultant = (Spinner) findViewById(R.id.spConsultant);
        //Set System Type to All
        spSystemType.setSelection(10);
        // Load Consultants
        populateConsultants();

        fab = (FloatingActionButton) findViewById(R.id.fabNew);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSearch();
            }
        });

    }

    private void doSearch() {
        strSearchText = etSearchText.getText().toString();
        if (isClientNo(strSearchText)) {
            //Use ClientNo and byPass any other selection
            strWhere = "WHERE ClientNo = '" + strSearchText + "'";
            strSearchText = "";
        } else {
            strFromDate = etFromDate.getText().toString();
            strToDate = etToDate.getText().toString();

            //Test System Types
            Integer sTyp = spSystemType.getSelectedItemPosition();
            if (sTyp < 10) {
                strTitle = spSystemType.getSelectedItem().toString().trim() + " ";
                if (strWhere.equals("")) {
                    strWhere = "WHERE System = '" + Integer.toString(sTyp) + "' ";
                } else {
                    strWhere = strWhere + "AND System = '" + Integer.toString(sTyp) + "' ";
                }
            }
            // Test Consultants
            String strConsultant = ci.Code.get(spConsultant.getSelectedItemPosition());
            if (!strConsultant.equals("All")) {
                strWhere = "WHERE Consultant = '" + strConsultant + "' ";
                strTitle = strTitle + " " + spConsultant.getSelectedItem().toString().trim();
            }
            //test From and To Dates - FromDate Only
            if (!strFromDate.equals("") & strToDate.equals("")) {
                if (strWhere.equals("")) {
                    strWhere = "WHERE ExpiryDate >= '" + strFromDate + "' ";
                } else {
                    strWhere = strWhere + "AND ExpiryDate >= '" + strFromDate + "' ";
                }
            }
            //test From and To Dates - ToDate Only
            if (!strToDate.equals("") & strFromDate.equals("")) {
                if (strWhere.equals("")) {
                    strWhere = "WHERE ExpiryDate <= '" + strToDate + "' ";
                } else {
                    strWhere = strWhere + "AND ExpiryDate <= '" + strToDate + "' ";
                }
            }
            //test From and To Dates - Both
            if (!strToDate.equals("") & !strFromDate.equals("")) {
                if (strWhere.equals("")) {
                    strWhere = "WHERE ExpiryDate >= '" + strFromDate + "' AND ExpiryDate <= '" + strToDate + "' ";
                } else {
                    strWhere = strWhere + "AND ExpiryDate >= '" + strFromDate + "' AND ExpiryDate <= '" + strToDate + "' ";
                }
            }
        }
        Intent i = new Intent(SelectClientActivity.this, ClientListActivity.class);
        i.putExtra("Where", strWhere);
        i.putExtra("SearchText", strSearchText);
        i.putExtra("Sequence", strSequence);
        i.putExtra("Table", "Clients");
        i.putExtra("Title", strTitle + " Clients");
        startActivity(i);
        finish();
    }

    public void setFromDate(View view) {
        createdFromDialog(0).show();
    }

    public void setToDate(View view) {
        createdToDialog(0).show();
    }

    protected Dialog createdFromDialog(int id) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(this, FromDateListener, year, month, day);
    }

    protected Dialog createdToDialog(int id) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(this, ToDateListener, year, month, day);
    }

    private boolean isClientNo(String txt) {
        String regex = "\\d+";
        if (txt.matches(regex)) {
            return txt.length() == 7;
        } else {
            return false;
        }
    }

    private void populateConsultants() {
        sq = new SqlGet();
        String eMes = sq.OpenConnection();
        ci = sq.getAllConsultants("All Consultants");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, ci.Description);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spConsultant.setAdapter(dataAdapter);
    }




}
