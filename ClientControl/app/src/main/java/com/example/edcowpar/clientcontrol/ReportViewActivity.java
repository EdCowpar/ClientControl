package com.example.edcowpar.clientcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReportViewActivity extends AppCompatActivity {
    private String RepName, sql, curKey, saveKey;
    private TableLayout table_layout;
    private Integer rows, cols;
    private TableRow row;
    private TextView tv, qty;
    private CheckBox checkBox;
    private ImageButton addBtn, minusBtn;
    private SqlGet sq;
    private Connection cn;
    private Intent i;
    private ReportHeadings r;
    private ClientRecord c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_view);
        table_layout = (TableLayout) findViewById(R.id.tableLayout1);
        Bundle b = getIntent().getExtras();
        RepName = b.getString("RepName");


    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        table_layout.removeAllViews();   //delete old data
        getHeadings(RepName + ".txt");     //Get Headings
        Build_Rep(RepName);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // show menu when menu button is pressed
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.report_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Sort:
                i = new Intent(this, SortDialog.class);
                startActivity(i);
                return true;

            case R.id.Search:
                i = new Intent(this, SelectClientActivity.class);
                startActivity(i);
                return true;

            case R.id.Headings:
                i = new Intent(this, Dialog_SelectHeadings_Activity.class);
                i.putExtra("FileName", RepName + ".txt");
                startActivity(i);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void getHeadings(String filename) {
        r = GetData.Read_ReportHeadings(this.getApplicationContext(), filename);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(RepName);
        switch (RepName) {
            case "Rep_001":  //Clients by Consultant
                actionBar.setSubtitle(getResources().getString(R.string.Rep_001));
                sql = "select * from sbClients order by Consultant,ClientName";
                break;
            case "Rep_002": //Clients by Expiry Date
                actionBar.setSubtitle(getResources().getString(R.string.Rep_002));
                sql = "select * from sbClients order by ExpiryDate,ClientName";
                break;
            case "Rep_003":   //Clients by System Type
                actionBar.setSubtitle(getResources().getString(R.string.Rep_003));
                sql = "select * from sbClients order by System,ClientName";
                break;
            case "Rep_004":   //Clients by Value
                actionBar.setSubtitle(getResources().getString(R.string.Rep_004));
                sql = "select * from sbClients order by Value,ClientName";
                break;
            case "Rep_005":   //Clients by Volume
                actionBar.setSubtitle(getResources().getString(R.string.Rep_005));
                sql = "select * from sbClients order by Volumn,ClientName";
                break;
            default:
                sql = "select * from sbClients";

        }
    }

    private void Build_Rep(String RepName) {
        String eMes;
        sq = new SqlGet(); //Open sql
        eMes = sq.OpenConnection();
        cn = sq.getConnection();
        ResultSet rs;

        try {

            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);
            Add_Heading();
            saveKey = "";

            while (rs.next()) {
                c = sq.populateClientRecord(rs);
                curKey = Build_Key();
                if (saveKey.equals("")) {
                    saveKey = curKey;
                    doNewKey();
                }
                if (!saveKey.equals(curKey)) {
                    doTotals();
                    doNewKey();
                    saveKey = curKey;
                }
                Add_Data();
            }
        } catch (SQLException e) {
            eMes = e.getMessage();
        }

    }

    private String Build_Key() {
        String key;
        switch (RepName) {
            case "Rep_001":  //Clients by Consultant
                key = "Consultant " + c.Consultant;
                break;
            case "Rep_002": //Clients by Expiry Date
                key = c.ExpiryDate;
                break;
            case "Rep_003":   //Clients by System Type
                key = c.System;
                break;
            case "Rep_004":   //Clients by Value
                key = c.Annual_Licence;
                break;
            case "Rep_005":   //Clients by Volume
                key = c.Volumn;
                break;
            default:
                key = "";
        }
        return key;
    }

    private void doTotals() {
        row = newTableRow();
        row = addTotalCell(row, "Total");
        row = addTotalCell(row, saveKey);
        table_layout.addView(row);
        //add space
        tv = new TextView(this);
        tv.setPadding(15, 15, 15, 15);
        tv.setBackgroundResource(R.drawable.cell_shape);
        tv.setText("");
        table_layout.addView(tv);
    }

    private void doNewKey() {
        tv = new TextView(this);
        tv.setPadding(15, 15, 15, 15);
        tv.setBackgroundResource(R.drawable.cell_heading);
        tv.setText(curKey);
        table_layout.addView(tv);
    }

    private void Add_Heading() {
        row = newTableRow();
        if (r.ClientNo.equals(true)) {
            row = addTableCell(row, "ClientNo");
        }
        if (r.ClientName.equals(true)) {
            row = addTableCell(row, "ClientName");
        }
        if (r.ContactName.equals(true)) {
            row = addTableCell(row, "ContactName");
        }
        if (r.EmailAddress.equals(true)) {
            row = addTableCell(row, "EmailAddress");
        }
        if (r.ExpiryDate.equals(true)) {
            row = addTableCell(row, "ExpiryDate");
        }
        if (r.Volumn.equals(true)) {
            row = addTableCell(row, "Volume");
        }
        table_layout.addView(row);
    }

    private void Add_Data() {
        row = newTableRow();
        row = addTableCell(row, c.ClientNo);
        row = addTableCell(row, c.ClientName);
        if (r.ContactName.equals(true)) {
            row = addTableCell(row, c.ContactName);
        }
        if (r.EmailAddress.equals(true)) {
            row = addTableCell(row, c.EmailAddress);
        }
        if (r.ExpiryDate.equals(true)) {
            row = addTableCell(row, c.ExpiryDate);
        }
        if (r.Volumn.equals(true)) {
            row = addTableCell(row, c.Volumn);
        }
        table_layout.addView(row);

    }

    private TableRow newTableRow() {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        return row;
    }

    private TableRow addTableCell(TableRow row, String cellText) {
        tv = new TextView(this);
        tv.setPadding(15, 15, 15, 15);
        tv.setBackgroundResource(R.drawable.cell_shape);
        tv.setText(cellText);
        row.addView(tv);
        return row;
    }

    private TableRow addTotalCell(TableRow row, String cellText) {
        tv = new TextView(this);
        tv.setPadding(15, 15, 15, 15);
        tv.setBackgroundResource(R.drawable.cell_total);
        tv.setText(cellText);
        row.addView(tv);
        return row;
    }
}

