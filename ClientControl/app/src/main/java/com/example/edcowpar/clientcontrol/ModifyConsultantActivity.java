package com.example.edcowpar.clientcontrol;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class ModifyConsultantActivity extends AppCompatActivity {
    private String strUserCode, eMes;
    private EditText etUserCode, etUserName, etPassword, etTelephone, etEmail;
    private CheckBox ckSupervisor, ckController;
    private FloatingActionButton fabSave;
    private ConsultantRecord c;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultant_detail);
        //get parameter in extra
        Bundle b = getIntent().getExtras();
        strUserCode = b.getString("Consultant");  //get SerialNo

        // Setup Fields
        etUserCode = (EditText) findViewById(R.id.etUserCode);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etTelephone = (EditText) findViewById(R.id.etTelephone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        ckSupervisor = (CheckBox) findViewById(R.id.ckSupervisor);
        ckController = (CheckBox) findViewById(R.id.ckController);

        // read Sql
        SqlGet sq = new SqlGet();
        eMes = sq.OpenConnection();
        c = sq.getConsultant(strUserCode);   //Read Record

        //Move In Values
        etUserCode.setText(c.UserCode);
        etUserName.setText(c.UserName);
        etTelephone.setText(c.Telephone);
        etPassword.setText(c.Password);
        etEmail.setText(c.Email);
        //Set CheckBoxes
        ckSupervisor.setChecked(setCheckBox(c.Supervisor));
        ckController.setChecked(setCheckBox(c.Controller));
        fabSave = (FloatingActionButton) findViewById(R.id.fabAddNew);

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.UserCode = etUserCode.getText().toString();
                c.UserName = etUserName.getText().toString();
                c.Telephone = etTelephone.getText().toString();
                c.Password = etPassword.getText().toString();
                c.Email = etEmail.getText().toString();
                c.Supervisor = setChecked(ckSupervisor.isChecked());
                c.Controller = setChecked(ckController.isChecked());
                if (!isValidData(c)) return;

                SqlGet sq = new SqlGet();
                eMes = sq.OpenConnection();
                if (!eMes.equals("ok")) {
                    Intent i = new Intent(ModifyConsultantActivity.this, ErrorActivity.class);
                    i.putExtra("eMes", eMes);
                    startActivity(i);
                    finish();
                }
                //Update Record
                String msg = sq.UpdConsultant(c);
                Toast.makeText(ModifyConsultantActivity.this, msg, Toast.LENGTH_LONG).show();
                finish();
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.modify_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Delete:
                AlertDialog.Builder alert = new AlertDialog.Builder(ModifyConsultantActivity.this);
                alert.setTitle("Confirm Delete");
                alert.setMessage("Are you sure you want to delete this consultant");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //do your work here
                        SqlGet sq = new SqlGet();
                        eMes = sq.OpenConnection();
                        if (!eMes.equals("ok")) {
                            Intent i = new Intent(ModifyConsultantActivity.this, ErrorActivity.class);
                            i.putExtra("eMes", eMes);
                            startActivity(i);
                            finish();
                        }
                        //Delete Record
                        String msg = sq.DeleteConsultant(c.RecNo);
                        Toast.makeText(ModifyConsultantActivity.this, msg, Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

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

    private Boolean setCheckBox(String myString) {
        return myString.equals("Y");
    }

    private String setChecked(boolean checked) {
        if (checked) {
            return "Y";
        } else {
            return "N";
        }
    }
}
