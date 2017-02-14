package com.example.edcowpar.sbclub;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Calendar;
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
    private ScrollView sv;
    private RelativeLayout rlmain;
    private RelativeLayout rlTabs;
    private ActionBar actionBar;
    private FloatingActionButton fabSave;
    private int DateId;

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
        fabSave = (FloatingActionButton) findViewById(R.id.fabSave);

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Save_Tabs(TabNo);
                String msg = Save_Record();
                if (!msg.equals("")) {
                    Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });

        //Load Client
        getClient g = new getClient();
        g.execute("");
    }

    void Save_Tabs(String TabNo) {
        Save_Flds(layoutTabs, TabNo);
    }

    void Save_Flds(LinearLayout ll, String TabNo) {
        String t, Fmt, Desc,txt, value;
        EditText et;
        Button btn;
        TextView tv;

        for (int i = 0; i < Flds.size(); i++) {
            t = Flds.get(i).getTabBox();
            if (t.equals(TabNo)) {
                Fmt = Flds.get(i).getFmt();
                Desc = Flds.get(i).getDescription();
                value = Flds.get(i).getValue();
                switch (Fmt) {
                    case "M":  //menu
                        break;
                    case "N":  //Normal
                        et = (EditText) ll.findViewById(i);
                        txt = et.getText().toString();
                        Flds.get(i).setValue(txt);
                        break;
                    case "B":   //Button
                        et = (EditText) ll.findViewById(i);
                        txt = et.getText().toString();
                        Flds.get(i).setValue(txt);
                        break;
                    case "R":   //Tabstrip
                        break;
                    default:
                        break;
                }
            }
        }
    }

    String Save_Record() {
        String Fmt, value, oValue, dbName;
        String sql = "Update " + DbTable + " SET ";
        String where = " WHERE RecNo = '"+Flds.get(0).RecNo.toString()+"'";
        String eMes = "";
        int x = 0;

        for (int i = 0; i < Flds.size(); i++) {
            value = Flds.get(i).getValue();
            oValue = Flds.get(i).getoValue();
            if (!value.equals(oValue)) {
                x = x + 1;
                if (x > 1) {
                    sql = sql + "," + Flds.get(i).getDbName() + " = '" + value + "'";
                } else {
                    sql = sql + Flds.get(i).getDbName() + " = '" + value + "'";
                }
            }
        }
        if (x > 0) {
            eMes = sq.updSql(sql+where);
        }
        return eMes;
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
            Keys = sq.getDataKeys(SFP, "");                        //Load Pky,Sky etc
            Scrl = sq.getScrl(DbTable, PkyName, Pky, Keys);       //Load Scrl
            Flds = sq.Load_Form(DbTable, PkyName, Pky, Flds);     //Load Form
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
            progressBar.setVisibility(View.GONE);
            TabNo = "1";
            Load_Tabs(TabNo);     //Load Tab 1

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    TabNo = String.valueOf(tab.getPosition() + 1);
                    Load_Tabs(TabNo);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    TabNo = String.valueOf(tab.getPosition() + 1);
                    Save_Tabs(TabNo);

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }


        void Load_Tabs(String TabNo) {
            rlTabs.removeAllViews();
            sv = new ScrollView(ctx);
            sv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            layoutTabs = new LinearLayout(ctx);
            sv.addView(layoutTabs);
            Load_Flds(layoutTabs, TabNo);
            rlTabs.addView(sv);
        }

        class mDateSetListener implements DatePickerDialog.OnDateSetListener {

            @Override
            public void onDateSet(DatePicker view, int year, int month,int day) {
                EditText et=(EditText)findViewById(DateId);
                String date = SubRoutines.formatDate(year, month, day, "ddMMyy");
                et.setText(date);
            }
        }
        void Load_Flds(LinearLayout ll, String TabNo) {
            String t, Fmt, Desc, value;
            EditText et;
            Button btn;
            TextView tv;
            DatePicker dp;
            RelativeLayout rl;
            RelativeLayout.LayoutParams lp;

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
                            et.setId(i);
                            et.setSelectAllOnFocus(true);
                            et.setText(value);
                            ll.addView(et);
                            break;
                        case "B":   //Button
                            //Description
                            tv = new TextView(ctx);
                            tv.setText(Desc);
                            ll.addView(tv);
                            // Layout for text and button
                            rl = new RelativeLayout(ctx);
                            et = new EditText(ctx);
                            et.setText(value);
                            et.setInputType(InputType.TYPE_CLASS_NUMBER);
                            et.setId(i);
                            et.setSelectAllOnFocus(true);
                            lp = new RelativeLayout.LayoutParams
                                    (LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );
                            lp.addRule(RelativeLayout.CENTER_VERTICAL);
                            rl.addView(et, lp);

                            btn = new Button(ctx);
                            btn.setText("Change Date");
                            btn.setTag(i);
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Button tmpBtn=(Button)v;
                                    DateId=(int)tmpBtn.getTag();
                                    Calendar c = Calendar.getInstance();
                                    int mYear = c.get(Calendar.YEAR);
                                    int mMonth = c.get(Calendar.MONTH);
                                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                                    DatePickerDialog dialog = new DatePickerDialog(ctx,
                                            new mDateSetListener(), mYear, mMonth, mDay);
                                    dialog.show();
                                }
                            });
                            lp = new RelativeLayout.LayoutParams
                                    (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            rl.addView(btn, lp);
                            ll.addView(rl);
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
