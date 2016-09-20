package com.example.edcowpar.clientcontrol;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddClientActivity extends AppCompatActivity {
    private EditText etSerialNo, etClientName, etAddress, etContactNo, etContactName, etEmail;
    private ClientRecord c;
    private String strAddress, eMes;
    private FloatingActionButton fabSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        etSerialNo = (EditText) findViewById(R.id.SerialNo);
        etClientName = (EditText) findViewById(R.id.ClientName);
        etAddress = (EditText) findViewById(R.id.Address);
        etContactNo = (EditText) findViewById(R.id.ContactNo);
        etContactName = (EditText) findViewById(R.id.ContactName);
        etEmail = (EditText) findViewById(R.id.Email);
        fabSave = (FloatingActionButton) findViewById(R.id.fabSave);

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = new ClientRecord();
                c.ClientNo = etSerialNo.getText().toString();
                c.ClientName = etClientName.getText().toString();
                strAddress = etAddress.getText().toString();
                c.Telephone = etContactNo.getText().toString();
                c.ContactName = etContactName.getText().toString();
                c.EmailAddress = etEmail.getText().toString();

                if (!isValidData()) return;
                //split address
                c = splitAddress(c, strAddress);

                SqlGet sq = new SqlGet();
                eMes = sq.OpenConnection();
                if (!eMes.equals("ok")) {
                    Intent i = new Intent(AddClientActivity.this, ErrorActivity.class);
                    i.putExtra("eMes", eMes);
                    startActivity(i);
                    finish();
                }
                String ser = sq.getSerialNo(c.ClientNo);
                if (ser != null && ser.equals(c.ClientNo)) {
                    String name = sq.getClientName(c.ClientNo);
                    etSerialNo.requestFocus();
                    etSerialNo.setError("This SerialNo belongs to " + name.trim());
                    return;
                }
                //Add Record
                String msg = sq.AddClient(c);
                Toast.makeText(AddClientActivity.this, msg, Toast.LENGTH_LONG).show();
                finish();
            }

        });
    }

    private boolean isValidData() {

        if (TextUtils.isEmpty(c.ClientNo)) {
            etSerialNo.requestFocus();
            etSerialNo.setError("Please enter SerialNo");
            return false;
        }
        if (!SubRoutines.isValidSerialNo(c.ClientNo)) {
            etSerialNo.requestFocus();
            etSerialNo.setError("Invalid SerialNo");
            return false;
        }
        if (TextUtils.isEmpty(c.ClientName)) {
            etClientName.requestFocus();
            etClientName.setError("Please enter Client Name");
            return false;
        }
        if (TextUtils.isEmpty(strAddress)) {
            etAddress.requestFocus();
            etAddress.setError("Please enter Address");
            return false;
        }
        if (TextUtils.isEmpty(c.Telephone)) {
            etContactNo.requestFocus();
            etContactNo.setError("Please enter Contact No");
            return false;
        }
        if (TextUtils.isEmpty(c.ContactName)) {
            etContactName.requestFocus();
            etContactName.setError("Please enter Contact Name");
            return false;
        }
        if (TextUtils.isEmpty(c.EmailAddress)) {
            etEmail.requestFocus();
            etEmail.setError("Please enter Email Address");
            return false;
        }
        if (!SubRoutines.isValidEmail(c.EmailAddress)) {
            etEmail.requestFocus();
            etEmail.setError("Invalid Email Address");
            return false;
        }

        return true;
    }

    private ClientRecord splitAddress(ClientRecord c, String strAddress) {
        //Split multiline Address
        c.Postal_01 = SubRoutines.splitAddress(strAddress, 1);
        c.Postal_02 = SubRoutines.splitAddress(strAddress, 2);
        c.Postal_03 = SubRoutines.splitAddress(strAddress, 3);
        c.PostCode = SubRoutines.splitAddress(strAddress, 4);

        //Check Last Line for postcode
        if (c.PostCode.equals("")) {
            c.PostCode = c.Postal_03;
            c.Postal_03 = "";
        }
        return c;
    }
}
