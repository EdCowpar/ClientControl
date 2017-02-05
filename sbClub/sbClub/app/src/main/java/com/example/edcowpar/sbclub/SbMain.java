package com.example.edcowpar.sbclub;

import android.app.TabActivity;
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

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

public class SbMain extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private ProgressBar progressBar;
    private RelativeLayout rl;
    private SqlGet sq;
    private ResultSet rs;
    private Context ctx;
    private String Pky, SFP, TabNo, eMes, PkyName, DbTable, Scrl;
    private List<ScrTabs> Tabs;
    private List<DataKeys> Keys;
    private List<ScrFields> Flds;
    private LinearLayout layoutTabs;
    private RelativeLayout rlmain;
    private RelativeLayout rlTabs;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.sbmain);
        rlmain = (RelativeLayout) findViewById(R.id.pnlMain);
        rlTabs = (RelativeLayout) findViewById(R.id.pnlTabs);
        //get Parameters
        Bundle b = getIntent().getExtras();
        Pky = b.getString("Pky");  //Pky
        SFP = b.getString("SFP");  //Screen Design
        DbTable = b.getString("DbTable");  //Database
        PkyName = b.getString("PkyName");  //PkyName
        actionBar = getSupportActionBar();
        actionBar.setTitle("Gilbert and Sullivan Society");

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
            Keys=sq.getDataKeys(SFP,"");                        //Load Pky,Sky etc
            Scrl=sq.getScrl(DbTable, PkyName, Pky,Keys);       //Load Scrl
            Flds=sq.Load_Form(DbTable, PkyName, Pky,Flds);     //Load Form
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
            actionBar.setSubtitle(Scrl);
            //Load Tab 0
            //LinearLayout lm = new LinearLayout(ctx);
            //rlmain.addView(lm);
            //Load_Flds(lm, "0");
            progressBar.setVisibility(View.GONE);
            Load_Tabs("1");     //Load Tab 1

            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    TabNo=String.valueOf(tab.getPosition()+1);
                Load_Tabs(TabNo);                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        }
        void Load_Tabs(String TabNo) {
            rlTabs.removeAllViews();
            ScrollView sv = new ScrollView(ctx);
            sv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            layoutTabs = new LinearLayout(ctx);
            sv.addView(layoutTabs);
            Load_Flds(layoutTabs, TabNo);
            rlTabs.addView(sv);
        }

        void Load_Flds(LinearLayout ll, String TabNo) {
            String t, Fmt, Desc, value;
            EditText et;
            Button btn;
            TextView tv;

            ll.setOrientation(LinearLayout.VERTICAL);
            for (int i = 0; i < Flds.size(); i++) {
                t = Flds.get(i).getTabBox();
                if (t.equals(TabNo)) {
                    Fmt = Flds.get(i).getFmt();
                    Desc = Flds.get(i).getDescription();
                    value = Flds.get(i).getValue();
                    switch (Fmt) {
                        case "M":  //menu
                            tv = new TextView(ctx);
                            tv.setText(Desc);
                            ll.addView(tv);
                            break;
                        case "N":  //Normal
                            tv = new TextView(ctx);
                            tv.setText(Desc);
                            ll.addView(tv);
                            et = new EditText(ctx);
                            et.setText(value);
                            ll.addView(et);
                            break;
                        case "B":   //Button
                            tv = new TextView(ctx);
                            tv.setText(Desc);
                            ll.addView(tv);
                            tv = new TextView(ctx);
                            tv.setText(value);
                            ll.addView(tv);
                            btn=new Button(ctx);
                            btn.setText("...");
                            ll.addView(btn);
                            break;
                        case "R":   //Tabstrip
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

}
