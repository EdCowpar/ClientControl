package com.example.edcowpar.clientcontrol;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    Switch swAutoLogin, swSaveUser;
    AppSettings a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swAutoLogin = (Switch) findViewById(R.id.swAutoLogin);
        swSaveUser = (Switch) findViewById(R.id.swSaveUser);

        a = GetData.Read(this.getApplicationContext());
        if (a.AutoLoad.equals("Yes")) {
            swAutoLogin.setChecked(true);
        }
        if (a.SaveUser.equals("Yes")) {
            swSaveUser.setChecked(true);
        }

        swAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    a.AutoLoad = "Yes";
                } else {
                    a.AutoLoad = "No";
                }
            }
        });
        swSaveUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    a.SaveUser = "Yes";
                } else {
                    a.SaveUser = "No";
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Saved", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                GetData.Write(view.getContext(), a);
                finish();
            }
        });
    }

}
