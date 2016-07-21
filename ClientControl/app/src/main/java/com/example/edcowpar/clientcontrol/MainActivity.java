package com.example.edcowpar.clientcontrol;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        return true;

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
        Intent intent = new Intent(this, ReportMenuActivity.class);
        startActivity(intent);
    }

    public void SelectSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}
