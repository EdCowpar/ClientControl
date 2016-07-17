package com.example.edcowpar.clientcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText etUserCode, etPassword;
    String strUserCode, strPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserCode = (EditText) findViewById(R.id.etUserCode);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        //  GetData.Write(this.getApplicationContext()); //Create File
        //  AppSettings a = GetData.Read(this.getApplicationContext());
    }

    public void Login(View view) {
        strUserCode = etUserCode.getText().toString();
        strPassword = etPassword.getText().toString();

        if (!isValidData()) return;

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private boolean isValidData() {

        if (TextUtils.isEmpty(strUserCode)) {
            etUserCode.requestFocus();
            etUserCode.setError("Please enter User Code");
            return false;
        }

        if (TextUtils.isEmpty(strPassword)) {
            etPassword.requestFocus();
            etPassword.setError("Please enter Password");
            return false;
        }
        SqlGet sq = new SqlGet();
        ConsultantRecord con = sq.getConsultant(strUserCode);
        if (con != null && !con.UserCode.equals(strUserCode)) {
            etUserCode.requestFocus();
            etUserCode.setError("Invalid User Code");
            return false;
        }
        if (con != null && !con.Password.equals(strPassword)) {
            etPassword.requestFocus();
            etPassword.setError("Invalid Password");
            return false;
        }

        return true;
    }
}














