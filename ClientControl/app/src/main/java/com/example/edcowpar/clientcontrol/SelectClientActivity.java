package com.example.edcowpar.clientcontrol;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SelectClientActivity extends AppCompatActivity {
    Button btnModify, btnBack, btnSearchName, btnSearchNo;
    EditText etSearchText;
    String strSearchtext;
    Spinner spSystemType, spConsultant;
    SqlGet sq;
    ComboItems ci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_client);
        btnModify = (Button) findViewById(R.id.btnModify);
        etSearchText = (EditText) findViewById(R.id.etSearchText);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnSearchName = (Button) findViewById(R.id.btnSearchName);
        btnSearchNo = (Button) findViewById(R.id.btnSearchNo);
        spSystemType = (Spinner) findViewById(R.id.spSystemType);
        spConsultant = (Spinner) findViewById(R.id.spConsultant);
        //Set System Type to All
        spSystemType.setSelection(10);
        // Load Consultants
        sq = new SqlGet();
        // Populate Consultants
        // Spinner Drop down elements
        ci = sq.getAllConsultants("All Consultants");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ci.Description);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spConsultant.setAdapter(dataAdapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnModify.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        strSearchtext = etSearchText.getText().toString();
                        Intent i = new Intent(SelectClientActivity.this, ModifyClientActivity.class);
                        i.putExtra("ClientNo", strSearchtext);
                        startActivity(i);
                    }
                });

        btnSearchName.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        strSearchtext = etSearchText.getText().toString();
                        Intent i = new Intent(SelectClientActivity.this, ItemListActivity.class);
                        i.putExtra("SearchText", strSearchtext);
                        i.putExtra("Sequence", "ClientName");
                        startActivity(i);
                    }
                });

        btnSearchNo.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        strSearchtext = etSearchText.getText().toString();
                        Intent i = new Intent(SelectClientActivity.this, ItemListActivity.class);
                        i.putExtra("SearchText", strSearchtext);
                        i.putExtra("Sequence", "ClientNo");
                        startActivity(i);
                    }
                });

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

}
