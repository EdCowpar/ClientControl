package com.example.edcowpar.clientcontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddClientActivity extends AppCompatActivity {
    private EditText etSerialNo, etClientName, etAddress, etContactNo, etContactName, etEmail;
    private String strSerialNo, strClientName, strAddress, strContactNo, strContactName, strEmail;

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

        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {

               strSerialNo = etSerialNo.getText().toString();
               strClientName = etClientName.getText().toString();
               strAddress = etAddress.getText().toString();
               strContactNo = etContactNo.getText().toString();
               strContactName = etContactName.getText().toString();
               strEmail = etEmail.getText().toString();

               if (!isValidData()) return;

               SqlGet sq = new SqlGet();
               String ser=sq.getSerialNo(strSerialNo);
               if (ser != null && ser.equals(strSerialNo)) {
                   String name=sq.getClientName(strSerialNo);
                   etSerialNo.requestFocus();
                   etSerialNo.setError("This SerialNo belongs to "+name.trim());
                   return;
               }
               //Add Record
               String  msg=sq.AddClient(strSerialNo,strClientName,strAddress,strContactNo,strContactName,strEmail);
               Toast.makeText(AddClientActivity.this, msg,Toast.LENGTH_LONG).show();
               finish();
            }

        });
    }

    private boolean isValidData() {

        if(TextUtils.isEmpty(strSerialNo)) {
            etSerialNo.requestFocus();
            etSerialNo.setError("Please enter SerialNo");
            return false;
        }
        if (!SubRoutines.isValidSerialNo(strSerialNo)) {
            etSerialNo.requestFocus();
            etSerialNo.setError("Invalid SerialNo");
            return false;
        }
        if(TextUtils.isEmpty(strClientName)) {
            etClientName.requestFocus();
            etClientName.setError("Please enter Client Name");
            return false;
        }
        if(TextUtils.isEmpty(strAddress)) {
            etAddress.requestFocus();
            etAddress.setError("Please enter Address");
            return false;
        }
        if(TextUtils.isEmpty(strContactNo)) {
            etContactNo.requestFocus();
            etContactNo.setError("Please enter Contact No");
            return false;
        }
        if(TextUtils.isEmpty(strContactName)) {
            etContactName.requestFocus();
            etContactName.setError("Please enter Contact Name");
            return false;
        }
        if(TextUtils.isEmpty(strEmail)) {
            etEmail.requestFocus();
            etEmail.setError("Please enter Email Address");
            return false;
        }
        if (!SubRoutines.isValidEmail(strEmail)) {
            etEmail.requestFocus();
            etEmail.setError("Invalid Email Address");
            return false;
        }

        return true;
    }
}
