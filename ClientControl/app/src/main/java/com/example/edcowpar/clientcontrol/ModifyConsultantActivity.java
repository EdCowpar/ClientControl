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

public class ModifyConsultantActivity extends AppCompatActivity {
    private String strUserCode, eMes;
    private EditText etUserCode, etUsername, etPassword, etTelephone, etEmail;
    private CheckBox ckSupervisor, ckController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultant_detail);
        //get parameter in extra
        Bundle b = getIntent().getExtras();
        strUserCode = b.getString("Consultant");  //get SerialNo

        // Setup Fields
        etUserCode = (EditText) findViewById(R.id.etUserCode);
        etUsername = (EditText) findViewById(R.id.etUserName);
        etTelephone = (EditText) findViewById(R.id.etTelephone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        ckSupervisor = (CheckBox) findViewById(R.id.ckSupervisor);
        ckController = (CheckBox) findViewById(R.id.ckController);

        // read Sql
        SqlGet sq = new SqlGet();
        eMes = sq.OpenConnection();
        ConsultantRecord c = sq.getConsultant(strUserCode);   //Read Record

        //Move In Values
        etUserCode.setText(c.UserCode);
        etUsername.setText(c.UserName);
        etTelephone.setText(c.Telephone);
        etPassword.setText(c.Password);
        etEmail.setText(c.Email);
        //Set CheckBoxes
        ckSupervisor.setChecked(setCheckBox(c.Supervisor));
        ckController.setChecked(setCheckBox(c.Controller));
    }

    private Boolean setCheckBox(String myString) {
        return myString.equals("Y");
    }


}
