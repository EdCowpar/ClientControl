package com.example.edcowpar.clientcontrol;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class TabFragment1 extends Fragment {
    private EditText etSerialNo, etClientName, etExpirydate, etVolume, etInstallPin, etAnnualLicence;
    private Spinner spSystemType, spConsultant;
    private CheckBox ckPaid, ckPdfModule, ckInCloud;
    private ClientRecord c;
    private Context ctx;
    private ComboItems ci;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ctx = this.getContext();
        return inflater.inflate(R.layout.tab_fragment1, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Setup Fields
        etSerialNo = (EditText) getView().findViewById(R.id.etSerialNo);
        etClientName = (EditText) getView().findViewById(R.id.etClientName);
        etExpirydate = (EditText) getView().findViewById(R.id.etExpiryDate);
        etVolume = (EditText) getView().findViewById(R.id.etVolume);
        etInstallPin = (EditText) getView().findViewById(R.id.etInstallPin);
        etAnnualLicence = (EditText) getView().findViewById(R.id.etAnnualLicence);
        spSystemType = (Spinner) getView().findViewById(R.id.spSystemType);
        spConsultant = (Spinner) getView().findViewById(R.id.spConsultant);
        ckPaid = (CheckBox) getView().findViewById(R.id.ckPaid);
        ckPdfModule = (CheckBox) getView().findViewById(R.id.ckPdfModule);
        ckInCloud = (CheckBox) getView().findViewById(R.id.ckInCloud);
        //set Filters
        etClientName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        //Read clientno
        GetData gd = new GetData();
        c = GetData.Read_ClientRecord(ctx);
        // Populate Consultants
        // Spinner Drop down elements
        //Open sql
        SqlGet sq = new SqlGet();
        String eMes = sq.OpenConnection();
        ci = sq.getAllConsultants("Not Set");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(ctx,
                android.R.layout.simple_spinner_item, ci.Description);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spConsultant.setAdapter(dataAdapter);
        //Move In Values
        etSerialNo.setText(c.ClientNo);
        etClientName.setText(c.ClientName);
        //Move In Values
        etSerialNo.setText(c.ClientNo);
        etClientName.setText(c.ClientName);
        etExpirydate.setText(c.ExpiryDate);
        etVolume.setText(c.Volumn);
        etInstallPin.setText(c.InstallPin);
        etAnnualLicence.setText(c.Annual_Licence);
        //Set System Type
        if (!c.System.equals(" ")) {
            Integer i = Integer.parseInt(c.System);
            spSystemType.setSelection(i);
        }
        //Set Consultant
        ConsultantRecord con = sq.getConsultant(c.Consultant);
        spConsultant.setSelection(getIndex(spConsultant, con.UserName));
        //Set CheckBoxes
        ckInCloud.setChecked(setCheckBox(c.InCloud));
        ckPaid.setChecked(setCheckBox(c.Paid));
        ckPdfModule.setChecked(setCheckBox(c.PDFModule));

    }

    private Boolean setCheckBox(String myString) {
        return myString.equals("Y");
    }

    private int getIndex(Spinner spinner, String myString) {
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