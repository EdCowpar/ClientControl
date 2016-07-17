package com.example.edcowpar.clientcontrol;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class ModifyClientActivity extends AppCompatActivity {
    private String strClientNo;
    private EditText etSerialNo, etClientName, etAddress, etContactNo, etContactName, etEmail;
    private EditText etPayeNo, etExpirydate,etVolume,etUifNo,etSdlNo,etInstallPin, etAnnualLicence;
    private CheckBox ckPaid,ckPdfModule,ckInCloud;
    private Spinner spSystemType, spConsultant;
    private Integer i;
    private Button btnBack, btnDate;
    private ComboItems ci;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_client);
        //get parameter in extra
        Bundle b = getIntent().getExtras();
        strClientNo = b.getString("ClientNo");  //get SerialNo

        // Setup Fields
        etSerialNo = (EditText) findViewById(R.id.etSerialNo);
        etClientName = (EditText) findViewById(R.id.etClientName);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etContactNo = (EditText) findViewById(R.id.etContactNo);
        etContactName = (EditText) findViewById(R.id.etContactName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPayeNo = (EditText) findViewById(R.id.etPayeNo);
        etExpirydate = (EditText) findViewById(R.id.etExpiryDate);
        etVolume = (EditText) findViewById(R.id.etVolume);
        etUifNo = (EditText) findViewById(R.id.etUifNo);
        etSdlNo = (EditText) findViewById(R.id.etSdlNo);
        etInstallPin = (EditText) findViewById(R.id.etInstallPin);
        etAnnualLicence = (EditText) findViewById(R.id.etAnnualLicence);
        spSystemType = (Spinner) findViewById(R.id.spSystemType);
        spConsultant = (Spinner) findViewById(R.id.spConsultant);
        ckPaid = (CheckBox) findViewById(R.id.ckPaid);
        ckPdfModule = (CheckBox) findViewById(R.id.ckPdfModule);
        ckInCloud = (CheckBox) findViewById(R.id.ckInCloud);
        btnBack=(Button) findViewById(R.id.btnBack);
        //set Filters
        etClientName.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        etContactName.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        // read Sql
        SqlGet sq = new SqlGet();
        ClientRecord c = sq.getClient(strClientNo);   //Read Record
        // Populate Consultants
        // Spinner Drop down elements
        ci = sq.getAllConsultants("Not Set");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, ci.Description);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spConsultant.setAdapter(dataAdapter);

        //Move In Values
        etSerialNo.setText(c.ClientNo);
        etClientName.setText(c.ClientName);
        etAddress.setText(c.Postal_01);
        etContactNo.setText(c.Telephone);
        etContactName.setText(c.ContactName);
        etEmail.setText(c.EmailAddress);
        etPayeNo.setText(c.PayeNo);
        etExpirydate.setText(c.ExpiryDate);
        etVolume.setText(c.Volumn);
        etUifNo.setText(c.UIFNo);
        etSdlNo.setText(c.SDLNo);
        etInstallPin.setText(c.InstallPin);
        etAnnualLicence.setText(c.Annual_Licence);
        //Set System Type
        i = Integer.parseInt(c.System);
        spSystemType.setSelection(i);
        //Set Consultant
        ConsultantRecord con = sq.getConsultant(c.Consultant);
        spConsultant.setSelection(getIndex(spConsultant, con.UserName));
        //Set CheckBoxes
        ckInCloud.setChecked(setCheckBox(c.InCloud));
        ckPaid.setChecked(setCheckBox(c.Paid));
        ckPdfModule.setChecked(setCheckBox(c.PDFModule));

         btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
         });


    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }
    private Boolean setCheckBox(String myString) {
        return myString.equals("Y");
    }



}
