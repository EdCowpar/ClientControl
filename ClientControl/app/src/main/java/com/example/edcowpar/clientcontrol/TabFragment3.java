package com.example.edcowpar.clientcontrol;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class TabFragment3 extends Fragment {
    private Context ctx;
    private EditText etPayeNo, etUifNo, etSdlNo;
    private ClientRecord c;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ctx = this.getContext();
        return inflater.inflate(R.layout.tab_fragment3, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Setup Fields
        etPayeNo = (EditText) getView().findViewById(R.id.etPayeNo);
        etUifNo = (EditText) getView().findViewById(R.id.etUifNo);
        etSdlNo = (EditText) getView().findViewById(R.id.etSdlNo);
        //Read clientno
        GetData gd = new GetData();
        c = GetData.Read_ClientRecord(ctx);
        //Move In Values
        etPayeNo.setText(c.PayeNo);
        etUifNo.setText(c.UIFNo);
        etSdlNo.setText(c.SDLNo);
    }

}