package com.example.edcowpar.clientcontrol;

import android.app.Activity;
import android.app.DialogFragment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;


public class ItemDetailFragment extends Fragment {
    private String strClientNo;
    private EditText etSerialNo, etClientName, etAddress, etContactNo, etContactName, etEmail;
    private EditText etPayeNo, etExpirydate, etVolume, etUifNo, etSdlNo, etInstallPin, etAnnualLicence;
    private CheckBox ckPaid, ckPdfModule, ckInCloud;
    private Spinner spSystemType, spConsultant;
    private Integer i;
    private Button btnDate;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_client_detail, container, false);


        //get parameters
        Bundle b = getArguments();
        strClientNo = b.getString("ClientNo");
        // Setup Fields
        etSerialNo = (EditText) rootView.findViewById(R.id.etSerialNo);
        etClientName = (EditText) rootView.findViewById(R.id.etClientName);
        etAddress = (EditText) rootView.findViewById(R.id.etAddress);
        etContactNo = (EditText) rootView.findViewById(R.id.etContactNo);
        etContactName = (EditText) rootView.findViewById(R.id.etContactName);
        etEmail = (EditText) rootView.findViewById(R.id.etEmail);
        etPayeNo = (EditText) rootView.findViewById(R.id.etPayeNo);
        etExpirydate = (EditText) rootView.findViewById(R.id.etExpiryDate);
        etVolume = (EditText) rootView.findViewById(R.id.etVolume);
        etUifNo = (EditText) rootView.findViewById(R.id.etUifNo);
        etSdlNo = (EditText) rootView.findViewById(R.id.etSdlNo);
        etInstallPin = (EditText) rootView.findViewById(R.id.etInstallPin);
        etAnnualLicence = (EditText) rootView.findViewById(R.id.etAnnualLicence);
        spSystemType = (Spinner) rootView.findViewById(R.id.spSystemType);
        spConsultant = (Spinner) rootView.findViewById(R.id.spConsultant);
        ckPaid = (CheckBox) rootView.findViewById(R.id.ckPaid);
        ckPdfModule = (CheckBox) rootView.findViewById(R.id.ckPdfModule);
        ckInCloud = (CheckBox) rootView.findViewById(R.id.ckInCloud);
        btnDate = (Button) rootView.findViewById(R.id.btnBack);
        //set Filters
        etClientName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        etContactName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        // read Sql
        SqlGet sq = new SqlGet();
        ClientRecord c = sq.getClient(strClientNo);   //Read Record
        // Populate Consultants
        ComboItems ci = sq.getAllConsultants("Not Set");
        Spinner spConsultant = (Spinner) rootView.findViewById(R.id.spConsultant);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, ci.Description);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        if (!c.System.equals(" ")) {
            i = Integer.parseInt(c.System);
        } else {
            i = 10;
        }
        spSystemType.setSelection(i);
        //Set Consultant
        short s = SubRoutines.getListIndex(ci.Count, ci.Code, c.Consultant);
        spConsultant.setSelection(s);
        //Set CheckBoxes
        ckInCloud.setChecked(SubRoutines.setCheckBox(c.InCloud));
        ckPaid.setChecked(SubRoutines.setCheckBox(c.Paid));
        ckPdfModule.setChecked(SubRoutines.setCheckBox(c.PDFModule));

        return rootView;
    }


}
