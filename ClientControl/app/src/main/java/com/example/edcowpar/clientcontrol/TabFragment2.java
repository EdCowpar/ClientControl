package com.example.edcowpar.clientcontrol;

import android.content.Context;
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

public class TabFragment2 extends Fragment {
    private EditText etAddress1, etAddress2, etAddress3, etAddress4, etContactName, etContactNo, etEmail;
    private ClientRecord c;
    private Context ctx;
    private FloatingActionButton fabSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ctx = this.getContext();
        return inflater.inflate(R.layout.tab_fragment2, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Setup Fields
        fabSave = (FloatingActionButton) getView().findViewById(R.id.fabSave);
        etAddress1 = (EditText) getView().findViewById(R.id.etAddress1);
        etAddress2 = (EditText) getView().findViewById(R.id.etAddress2);
        etAddress3 = (EditText) getView().findViewById(R.id.etAddress3);
        etAddress4 = (EditText) getView().findViewById(R.id.etAddress4);
        etContactName = (EditText) getView().findViewById(R.id.etContactName);
        etContactNo = (EditText) getView().findViewById(R.id.etContactNo);
        etEmail = (EditText) getView().findViewById(R.id.etEmail);
        //set Filters
        etContactName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        //Read clientno
        GetData gd = new GetData();
        c = GetData.Read_ClientRecord(ctx);
        //Move In Values
        etAddress1.setText(c.Postal_01);
        etAddress2.setText(c.Postal_02);
        etAddress3.setText(c.Postal_03);
        etAddress4.setText(c.PostCode);
        etContactName.setText(c.ContactName);
        etContactNo.setText(c.Telephone);

    }

}