package com.example.edcowpar.clientcontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void SelectClient(View view) {
        Intent intent = new Intent(this,SelectClientActivity.class);
        startActivity(intent);
    }

    public void SelectConsultants(View view) {
        Intent i = new Intent(this, ConsultantListActivity.class);
        i.putExtra("SearchText", "");
        i.putExtra("Sequence", "");
        i.putExtra("Table", "Consultants");
        startActivity(i);
    }

    public void SelectReports(View view) {
        Intent intent = new Intent(this, SelectClientActivity.class);
        startActivity(intent);
    }

    public void SelectSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}
