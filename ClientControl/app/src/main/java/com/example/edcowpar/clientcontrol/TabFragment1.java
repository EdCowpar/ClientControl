package com.example.edcowpar.clientcontrol;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class TabFragment1 extends Fragment {
    private EditText etSerialNo, etClientName, etExpirydate, etVolume, etInstallPin, etAnnualLicence;
    private Spinner spSystemType, spConsultant;
    private CheckBox ckPaid, ckPdfModule, ckInCloud;
    private ClientRecord c;
    private Context ctx;
    private ComboItems ci;
    private FloatingActionButton fabSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ctx = this.getContext();
        return inflater.inflate(R.layout.tab_fragment1, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        //Read from Local Storage
        GetData gd = new GetData();
        c = GetData.Read_ClientRecord(ctx);
        //Move in Changed Values
        c.ClientNo = etSerialNo.getText().toString();
        c.ExpiryDate = etExpirydate.getText().toString();
        c.Volumn = etVolume.getText().toString();
        c.Consultant = ci.Code.get(spConsultant.getSelectedItemPosition());
        c.InstallPin = etInstallPin.getText().toString();
        c.Annual_Licence = etAnnualLicence.getText().toString();
        c.System = spSystemType.getSelectedItem().toString();
        c.Paid = etClientName.getText().toString();
        c.PDFModule = etClientName.getText().toString();
        c.InCloud = etClientName.getText().toString();
        c.InCloud = SubRoutines.getCheckBox(ckInCloud.isChecked());
        c.Paid = SubRoutines.getCheckBox(ckPaid.isChecked());
        c.PDFModule = SubRoutines.getCheckBox(ckPdfModule.isChecked());
        //Save Client to Local Storage
        GetData.Write_ClientRecord(ctx, c);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Setup Fields
        fabSave = (FloatingActionButton) getView().findViewById(R.id.fabSave);
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
        if (!c.System.equals("Null") && !c.System.equals(" ")) {
            Integer i = Integer.parseInt(c.System);
            spSystemType.setSelection(i);
        }
        //Set Consultant
        short s = SubRoutines.getListIndex(ci.Count, ci.Code, c.Consultant);
        spConsultant.setSelection(s);

        //Set CheckBoxes
        ckInCloud.setChecked(SubRoutines.setCheckBox(c.InCloud));
        ckPaid.setChecked(SubRoutines.setCheckBox(c.Paid));
        ckPdfModule.setChecked(SubRoutines.setCheckBox(c.PDFModule));
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Read from Local Storage
                GetData gd = new GetData();
                c = GetData.Read_ClientRecord(ctx);
                //Move in Changed Values
                c.ClientNo = etSerialNo.getText().toString();
                c.ExpiryDate = etExpirydate.getText().toString();
                c.Volumn = etVolume.getText().toString();
                c.Consultant = ci.Code.get(spConsultant.getSelectedItemPosition());
                c.InstallPin = etInstallPin.getText().toString();
                c.Annual_Licence = etAnnualLicence.getText().toString();
                Integer s = spSystemType.getSelectedItemPosition();
                c.System = s.toString();
                c.InCloud = SubRoutines.getCheckBox(ckInCloud.isChecked());
                c.Paid = SubRoutines.getCheckBox(ckPaid.isChecked());
                c.PDFModule = SubRoutines.getCheckBox(ckPdfModule.isChecked());
                //Save Client to Local Storage
                GetData.Write_ClientRecord(ctx, c);
                //Save to Database
                SqlGet sq = new SqlGet();
                String eMes = sq.OpenConnection();
                if (eMes.equals("ok")) {
                    eMes = sq.UpdClient(c); //Save Record
                }
                if (!eMes.equals("ok")) {
                    Intent i = new Intent(getActivity(), ErrorActivity.class);
                    i.putExtra("eMes", eMes);
                    startActivity(i);
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
            }
        });

    }



}