package com.example.edcowpar.clientcontrol;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class AddConsultantActivity extends AppCompatActivity {
    private EditText etPassword, etUserCode, etUserName, etEmail, etTelephone;
    private CheckBox ckSupervisor, ckController;
    private ConsultantRecord c;
    private String eMes;
    private FloatingActionButton fabSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultant_detail);

        etPassword = (EditText) findViewById(R.id.etPassword);
        etUserCode = (EditText) findViewById(R.id.etUserCode);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        ckSupervisor = (CheckBox) findViewById(R.id.ckSupervisor);
        ckController = (CheckBox) findViewById(R.id.ckController);
        etTelephone = (EditText) findViewById(R.id.etTelephone);
        fabSave = (FloatingActionButton) findViewById(R.id.fabAddNew);

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = new ConsultantRecord();
                c.Password = etPassword.getText().toString();
                c.UserCode = etUserCode.getText().toString();
                c.UserName = etUserName.getText().toString();
                c.Email = etEmail.getText().toString();
                c.Supervisor = setChecked(ckSupervisor.isChecked());
                c.Controller = setChecked(ckController.isChecked());
                c.Telephone = etTelephone.getText().toString();

                if (!isValidData(c)) return;

                SqlGet sq = new SqlGet();
                eMes = sq.OpenConnection();
                if (!eMes.equals("ok")) {
                    Intent i = new Intent(AddConsultantActivity.this, ErrorActivity.class);
                    i.putExtra("eMes", eMes);
                    startActivity(i);
                    finish();
                }
                ConsultantRecord ec = sq.getConsultant(c.UserCode);

                if (ec.RecNo > 0) {
                    etUserCode.requestFocus();
                    etUserCode.setError("Duplicate User Code");
                    return;
                }
                //Add Record
                String msg = sq.AddConsultant(c);
                Toast.makeText(AddConsultantActivity.this, msg, Toast.LENGTH_LONG).show();
                finish();
            }

        });

    }

    private String setChecked(boolean checked) {
        if (checked) {
            return "Y";
        } else {
            return "N";
        }
    }

    private boolean isValidData(ConsultantRecord c) {

        if (TextUtils.isEmpty(c.Password)) {
            etPassword.requestFocus();
            etPassword.setError("Please enter Password");
            return false;
        }
        if (TextUtils.isEmpty(c.UserCode)) {
            etUserCode.requestFocus();
            etUserCode.setError("Please enter User Code");
            return false;
        }
        if (TextUtils.isEmpty(c.UserName)) {
            etUserName.requestFocus();
            etUserName.setError("Please enter User Name");
            return false;
        }
        if (TextUtils.isEmpty(c.Email)) {
            etEmail.requestFocus();
            etEmail.setError("Please enter Email Address");
            return false;
        }
        if (!SubRoutines.isValidEmail(c.Email)) {
            etEmail.requestFocus();
            etEmail.setError("Invalid Email Address");
            return false;
        }
        if (TextUtils.isEmpty(c.Telephone)) {
            etTelephone.requestFocus();
            etTelephone.setError("Please enter TelephoneNo");
            return false;
        }

        return true;
    }
}
