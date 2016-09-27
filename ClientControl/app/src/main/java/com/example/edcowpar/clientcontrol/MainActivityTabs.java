package com.example.edcowpar.clientcontrol;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;


public class MainActivityTabs extends AppCompatActivity {
    private ProgressBar progressBar;
    private String strClientNo, eMes;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private ClientRecord c;
    private SqlGet sq;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_main_tabs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        //Load Client
        getClient g = new getClient();
        g.execute("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //    if (id == R.id.action_settings) {
        //       return true;
        //   }
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public class getClient extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //get parameter in extra
            Bundle b = getIntent().getExtras();
            strClientNo = b.getString("ClientNo");  //get SerialNo
            //set progress
            progressBar.setVisibility(View.VISIBLE);
            //Open Sql
            sq = new SqlGet();
            eMes = sq.OpenConnection();
            if (eMes.equals("ok")) {
                //set tabs
                tabLayout.addTab(tabLayout.newTab().setText("GENERAL"));
                tabLayout.addTab(tabLayout.newTab().setText("ADDRESS"));
                tabLayout.addTab(tabLayout.newTab().setText("SARS"));
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // read Sql
            c = sq.getClient(strClientNo);   //Read Record
            return c.ClientNo;
        }

        @Override
        protected void onPostExecute(String r) {
            //Save Client to Local Storage
            GetData gd = new GetData();
            GetData.Write_ClientRecord(ctx, c);
            progressBar.setVisibility(View.GONE);
            adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    }
}