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
    String strUserCode, strPassword, eMes;
    AppSettings a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Client Control - Login");

        etUserCode = (EditText) findViewById(R.id.etUserCode);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        a = GetData.Read(this.getApplicationContext());
        if (a.getRecNo() > 0) {
            if (a.getSaveUser().equals("Yes")) {
                etUserCode.setText(a.UserCode);
                etPassword.setText(a.Password);
            }
            if (a.AutoLoad.equals("Yes")) {
                btnLogin.performClick();
            }
        }

    }

    public void Login(View view) {
        strUserCode = etUserCode.getText().toString();
        strPassword = etPassword.getText().toString();

        if (!isValidData()) return;

        Intent intent = new Intent(this, MainActivity.class);
        if (a.getSpeech().equals("Yes")) {
            intent.putExtra("Speech", a.getWelcome());
        }

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
        eMes = sq.OpenConnection();
        if (!eMes.equals("ok")) {
            etUserCode.requestFocus();
            etUserCode.setError(eMes);
            return false;
        }
        ConsultantRecord c = sq.getConsultant(strUserCode);
        if (!eMes.equals("ok")) {
            etUserCode.requestFocus();
            etUserCode.setError(eMes);
            return false;
        }
        if (c != null && !c.UserCode.trim().equals(strUserCode.trim())) {
            etUserCode.requestFocus();
            etUserCode.setError("Invalid User Code");
            return false;
        }
        if (c != null && !c.Password.trim().equals(strPassword.trim())) {
            etPassword.requestFocus();
            etPassword.setError("Invalid Password");
            return false;
        }
        //Save Settings
        a.setUserCode(c.UserCode.trim());
        a.setPassword(c.Password.trim());
        a.setUserName(c.UserName.trim());
        a.setRecNo(c.RecNo);
        GetData.Write(this.getApplicationContext(), a); //Create File

        return true;
    }
}














