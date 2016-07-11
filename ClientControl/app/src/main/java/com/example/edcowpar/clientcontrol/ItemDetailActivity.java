package com.example.edcowpar.clientcontrol;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class ItemDetailActivity extends AppCompatActivity {
    private String strClientNo;
    private EditText etSerialNo, etClientName, etAddress, etContactNo, etContactName, etEmail;
    private EditText etPayeNo, etExpirydate, etVolume, etUifNo, etSdlNo, etInstallPin, etAnnualLicence;
    private CheckBox ckPaid, ckPdfModule, ckInCloud;
    private Spinner spSystemType, spConsultant;
    private Integer i;
    private Button btnBack, btnDate, btnModify;
    private SqlGet sq;
    private ClientRecord c;
    private ComboItems ci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //get parameter in extra
        Bundle b = getIntent().getExtras();
        strClientNo = b.getString("ClientNo");
        toolbar.setTitle("ClientNo " + strClientNo);
        setSupportActionBar(toolbar);
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
        btnBack = (Button) findViewById(R.id.btnBack);
        btnDate = (Button) findViewById(R.id.btnBack);
        btnModify = (Button) findViewById(R.id.btnModify);
        //set Filters
        etClientName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        etContactName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        // read Sql
        sq = new SqlGet();
        c = sq.getClient(strClientNo);   //Read Record
        // Populate Consultants
        // Spinner Drop down elements
        ci = sq.getAllConsultants();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ci.Description);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spConsultant.setAdapter(dataAdapter);

        //Move In Values
        etSerialNo.setText(c.ClientNo);
        etClientName.setText(c.ClientName);
        String address = c.Postal_01;
        c.Postal_02 = c.Postal_02.trim();
        if (!c.Postal_02.equals("")) {
            address = address + "\n" + c.Postal_02;
        }
        c.Postal_03 = c.Postal_03.trim();
        if (!c.Postal_03.equals("")) {
            address = address + "\n" + c.Postal_03;
        }
        c.PostCode = c.PostCode.trim();
        if (!c.PostCode.equals("")) {
            address = address + "\n" + c.PostCode;
        }
        etAddress.setText(address);
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
        if (!c.System.equals(" ")) {
            i = Integer.parseInt(c.System);
            spSystemType.setSelection(i);
        }
        //Set Consultant
        short s = SubRoutines.getListIndex(ci.Count, ci.Code, c.Consultant);
        spConsultant.setSelection(s);
        //Set CheckBoxes
        ckInCloud.setChecked(SubRoutines.setCheckBox(c.InCloud));
        ckPaid.setChecked(SubRoutines.setCheckBox(c.Paid));
        ckPdfModule.setChecked(SubRoutines.setCheckBox(c.PDFModule));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emes = UpdateDatabase(c.RecNo);
                Toast.makeText(getApplicationContext(), emes, Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private String UpdateDatabase(String RecNo) {
        String emes;
        ClientRecord c = new ClientRecord();

        //Split multiline Address
        String strAddress = etAddress.getText().toString();
        String post1 = SubRoutines.splitAddress(strAddress, 1);
        String post2 = SubRoutines.splitAddress(strAddress, 2);
        String post3 = SubRoutines.splitAddress(strAddress, 3);
        String PostCode = SubRoutines.splitAddress(strAddress, 4);

        //Check Last Line for postcode
        if (PostCode.equals("")) {
            PostCode = post3;
            post3 = "";
        }

        //Move In Values
        c.RecNo = RecNo;
        c.ClientNo = etSerialNo.getText().toString();
        c.ClientName = etClientName.getText().toString();
        c.Postal_01 = post1;
        c.Postal_02 = post2;
        c.Postal_03 = post3;
        c.PostCode = PostCode;
        c.Telephone = etContactNo.getText().toString();
        c.ContactName = etContactName.getText().toString();
        c.EmailAddress = etEmail.getText().toString();
        c.PayeNo = etPayeNo.getText().toString();
        c.ExpiryDate = etExpirydate.getText().toString();
        c.Volumn = etVolume.getText().toString();
        c.UIFNo = etUifNo.getText().toString();
        c.SDLNo = etSdlNo.getText().toString();
        c.InstallPin = etInstallPin.getText().toString();
        c.Annual_Licence = etAnnualLicence.getText().toString();
        c.System = spSystemType.getSelectedItem().toString();
        c.Consultant = ci.Code.get(spConsultant.getSelectedItemPosition());
        c.InCloud = SubRoutines.getCheckBox(ckInCloud.isChecked());
        c.Paid = SubRoutines.getCheckBox(ckPaid.isChecked());
        c.PDFModule = SubRoutines.getCheckBox(ckPdfModule.isChecked());
        //  Update Database
        emes = sq.UpdClient(c);
        return emes;
    }
}
