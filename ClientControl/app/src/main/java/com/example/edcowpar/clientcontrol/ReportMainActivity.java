package com.example.edcowpar.clientcontrol;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ReportMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Client Control");
        actionBar.setSubtitle("Reports");
    }

    public void SelectReports(View view) {
        Intent intent = new Intent(this, ReportMenuActivity.class);
        startActivity(intent);
    }

    public void SelectAudits(View view) {
        Intent intent = new Intent(this, ReportAuditMenuActivity.class);
        startActivity(intent);
    }

}
