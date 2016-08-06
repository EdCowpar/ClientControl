package com.example.edcowpar.clientcontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SortDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_dialog);
        setTitle("Sort by:");
    }
}
