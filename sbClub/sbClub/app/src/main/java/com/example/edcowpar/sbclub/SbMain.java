package com.example.edcowpar.sbclub;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class SbMain extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private ProgressBar progressBar;
    private RelativeLayout rl;
    private SqlGet sq;
    private Context ctx;
    private String Pky, SFP, TabNo, eMes, PkyName, DbTable;
    private List<ScrTabs> Tabs;
    private List<ScrFields> Flds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.sbmain);
        //get Parameters
        Bundle b = getIntent().getExtras();
        Pky = b.getString("Pky");  //Pky
        SFP = b.getString("SFP");  //Screen Design
        DbTable = b.getString("DbTable");  //Database
        PkyName = b.getString("PkyName");  //PkyName
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Gilbert and Sullivan Society");
        actionBar.setSubtitle(Pky);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        //Load Client
        getClient g = new getClient();
        g.execute("");


    }

    public class getClient extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //set progress
            progressBar.setVisibility(View.VISIBLE);
            //Open Sql
            sq = new SqlGet();
            eMes = sq.OpenConnection();
            if (!eMes.equals("ok")) {
                Intent i = new Intent(SbMain.this, SbError.class);
                i.putExtra("eMes", eMes);
                startActivity(i);
                finish();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // Get Tabs
            Tabs = sq.getScrTabs(SFP);                          //Read Record
            Flds = sq.getScrFields(SFP);                        //Load Screen Design
            Flds=sq.getDataRecord(DbTable,PkyName,Pky,Flds);   //Load Fields
            return "ok";
        }

        @Override
        protected void onPostExecute(String r) {
            //set Screen tabs
            for (int i = 0; i < Tabs.size(); i++) {
                String t = Tabs.get(i).getDescription();
                tabLayout.addTab(tabLayout.newTab().setText(t));
            }
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            //Load Tab 0
            TabNo = "0";
            rl = (RelativeLayout) findViewById((R.id.pnlMain));
            LinearLayout lm = new LinearLayout(ctx);
            rl.addView(lm);
            Load_Flds(lm,TabNo);

            //Load Tab 1
            TabNo = "1";
            rl = (RelativeLayout) findViewById((R.id.pnlTabs));
            ScrollView sv = new ScrollView(ctx);
            sv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
            LinearLayout   lt = new LinearLayout(ctx);
            sv.addView(lt);
            Load_Flds(lt,TabNo);
            rl.addView(sv);
        }
        void Load_Flds(LinearLayout ll, String TabNo) {
            ll.setOrientation(LinearLayout.VERTICAL);
            for(int i = 0; i < Flds.size(); i++)
            {
                String t=Flds.get(i).getTabBox();
                if (t.equals(TabNo)) {
                    String d = Flds.get(i).getDescription();
                    Button b = new Button(ctx);
                    b.setText(d);
                    ll.addView(b);
                    TextView tv=new TextView(ctx);
                    d=Flds.get(i).getValue();
                    tv.setText(d);
                    ll.addView(tv);
                }
            }

        }
    }

}
