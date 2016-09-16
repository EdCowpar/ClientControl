package com.example.edcowpar.clientcontrol;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Dialog_SelectTotals_Activity extends AppCompatActivity {
    Switch swTotals;
    FloatingActionButton fab;
    private String filename;
    private ReportTotals t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_totals_layout);
        //get parameter in extra
        Bundle b = getIntent().getExtras();
        filename = b.getString("FileName");
        t = GetData.Read_ReportTotals(this.getApplicationContext(), filename);

        swTotals = (Switch) findViewById(R.id.swTotals);
        swTotals.setChecked(t.TotalsOnly);

        swTotals.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                t.TotalsOnly = isChecked;
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Saved", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                GetData.Write_ReportTotals(getApplicationContext(), filename, t);
                finish();
            }
        });
    }

}
