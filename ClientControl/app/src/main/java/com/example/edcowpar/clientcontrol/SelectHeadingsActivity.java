package com.example.edcowpar.clientcontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectHeadingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_headings);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, getResources().getStringArray(R.array.client_headings));

        ListView listView = (ListView) findViewById(R.id.lvHeadings);
        listView.setAdapter(adapter);

    }
}
