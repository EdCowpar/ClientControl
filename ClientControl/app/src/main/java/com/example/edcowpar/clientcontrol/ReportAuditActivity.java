package com.example.edcowpar.clientcontrol;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportAuditActivity extends AppCompatActivity {

    private String eMes, RepName, sql, curKey, saveKey;
    private TableLayout table_layout;
    private Integer cols, col_qty;
    private double tot9_rows;
    private double tot1_rows;
    private TableRow row;
    private TextView tv;
    private SqlGet sq;
    private Connection cn;
    private Intent i;
    private AuditHeadings r;
    private ReportTotals t;
    private AuditRecord c;
    private ActionBar actionBar;

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
        getHeadings(RepName + ".txt");   //Get Headings
        eMes = Build_Rep(RepName);
        if (!eMes.equals("ok")) {
            Intent i = new Intent(this, ErrorActivity.class);
            i.putExtra("eMes", eMes);
            startActivity(i);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // show menu when menu button is pressed
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.audit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.Search:
                i = new Intent(this, SelectClientActivity.class);
                startActivity(i);
                return true;

            case R.id.Headings:
                i = new Intent(this, Dialog_SelectAuditHeadings.class);
                i.putExtra("FileName", RepName + ".txt");
                startActivity(i);
                return true;

            case R.id.Totals:
                i = new Intent(this, Dialog_SelectTotals_Activity.class);
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
        r = GetData.Read_AuditHeadings(this.getApplicationContext(), filename);
        t = GetData.Read_ReportTotals(this.getApplicationContext(), filename);
        actionBar = getSupportActionBar();
        actionBar.setTitle(RepName);
        switch (RepName) {
            case "Aud_001":  //Client Renewals by Month
                if (r.RecNo.equals(0)) {
                    r.runDate = true;
                }
                actionBar.setSubtitle("Client Renewals by Month");
                sql = "select * from sbAudit order by substring(runDate,1,6) DESC, ClientNo, runDate DESC";
                break;
            case "Aud_002":  //Client Renewals by Client
                if (r.RecNo.equals(0)) {
                    r.runDate = true;
                }
                actionBar.setSubtitle("Client Renewals by Client");
                sql = "select * from sbAudit order by ClientNo, runDate DESC";
                break;
            case "Aud_003":  //Client Renewals by Consultant
                if (r.RecNo.equals(0)) {
                    r.runDate = true;
                }
                actionBar.setSubtitle("Client Renewals by Consultant");
                sql = "select * from sbAudit order by UserName, ClientNo, runDate DESC";
                break;
            case "Aud_004":  //Client Renewals by Action
                if (r.RecNo.equals(0)) {
                    r.runDate = true;
                }
                actionBar.setSubtitle("Client Renewals by Action");
                sql = "select * from sbAudit order by Action, ClientNo, runDate DESC";
                break;
            default:
                sql = "select * from sbAudit";

        }
    }

    private String Build_Rep(String RepName) {
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
            tot9_rows = 0;
            tot1_rows = 0;

            while (rs.next()) {
                c = sq.populateAuditRecord(rs);
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
                tot9_rows++;
                tot1_rows++;
            }
            doTotals();
            doFinalTotals();

        } catch (SQLException e) {
            eMes = e.getMessage();
        }
        return eMes;
    }

    private String Build_Key() {
        String key;
        switch (RepName) {
            case "Aud_001":  //Client Renewals by Month
                key = SubRoutines.FmtString(c.runDate, "m");  //ccyymmdd to Month ccyy
                break;
            case "Aud_002":  //Client Renewals by ClientNo
                key = "Client " + c.ClientNo;
                break;
            case "Aud_003":  //Client Renewals by Consultant
                key = "Consultant " + c.UserName;
                break;
            case "Aud_004":  //Client Renewals by Action
                key = c.Action;
                break;
            default:
                key = "";
        }
        return key;
    }

    private void doTotals() {
        row = newTableRow();
        row = addTotalCell(row, "Total", "");
        String s = saveKey;
        if (col_qty.equals(0)) {
            s = s + " [" + String.format("%.0f", tot9_rows) + "]";
            row = addTotalCell(row, s, "");
            cols = 3;
        } else {
            row = addTotalCell(row, s, "");
            row = addTotalCell(row, String.format("%.0f", tot9_rows), "RIGHT");
            cols = 4;
        }
        table_layout.addView(row);
        //add space
        tv = new TextView(this);
        tv.setPadding(15, 15, 15, 15);
        tv.setBackgroundResource(R.drawable.cell_shape);
        tv.setText("");
        table_layout.addView(tv);
        //accumulate final totals
        tot1_rows = tot1_rows + tot9_rows;
        //clear totals
        tot9_rows = 0;
    }

    private void doFinalTotals() {
        row = newTableRow();
        row = addTotalCell(row, "Total", "");
        String s = actionBar.getSubtitle().toString();
        if (col_qty.equals(0)) {
            s = s + " [" + String.format("%.0f", tot1_rows) + "]";
            row = addTotalCell(row, s, "");
            cols = 3;
        } else {
            row = addTotalCell(row, s, "");
            row = addTotalCell(row, String.format("%.0f", tot1_rows), "RIGHT");
            cols = 4;
        }
        table_layout.addView(row);
        //add space
        tv = new TextView(this);
        tv.setPadding(15, 15, 15, 15);
        tv.setBackgroundResource(R.drawable.cell_shape);
        tv.setText("");
        table_layout.addView(tv);
    }

    private void doNewKey() {
        if (t.TotalsOnly.equals(false)) {
            tv = new TextView(this);
            tv.setPadding(15, 15, 15, 15);
            tv.setBackgroundResource(R.drawable.cell_heading);
            tv.setText(curKey);
            table_layout.addView(tv);
        }
    }

    private void Add_Heading() {
        row = newTableRow();
        cols = 0;
        col_qty = 0;
        if (r.ClientNo.equals(true)) {
            cols++;
            row = addTableCell(row, "ClientNo");
        }
        if (r.ClientName.equals(true)) {
            cols++;
            row = addTableCell(row, "ClientName");
        }
        if (r.runDate.equals(true)) {
            cols++;
            row = addTableCell(row, "runDate");
            if (col_qty.equals(0)) {
                col_qty = cols;
            }
        }
        if (r.UserName.equals(true)) {
            cols++;
            row = addTableCell(row, "UserName");
            if (col_qty.equals(0)) {
                col_qty = cols;
            }
        }
        if (r.Action.equals(true)) {
            cols++;
            row = addTableCell(row, "Action");
            if (col_qty.equals(0)) {
                col_qty = cols;
            }
        }
        if (r.runTime.equals(true)) {
            cols++;
            row = addTableCell(row, "runTime");
            if (col_qty.equals(0)) {
                col_qty = cols;
            }
        }
        if (r.Remarks.equals(true)) {
            cols++;
            row = addTableCell(row, "Remarks");
            if (col_qty.equals(0)) {
                col_qty = cols;
            }
        }
        table_layout.addView(row);
    }

    private void Add_Data() {
        if (t.TotalsOnly.equals(false)) {

            row = newTableRow();
            if (r.ClientNo.equals(true)) {
                row = addTableCell(row, c.ClientNo);
            }
            if (r.ClientName.equals(true)) {
                row = addTableCell(row, c.ClientName);
            }
            if (r.runDate.equals(true)) {
                String strTxt = c.runDate;
                strTxt = SubRoutines.FmtString(strTxt, "a");
                row = addTableCell(row, strTxt);
            }
            if (r.UserName.equals(true)) {
                row = addTableCell(row, c.UserName);
            }
            if (r.Action.equals(true)) {
                row = addTableCell(row, c.Action);
            }
            if (r.runTime.equals(true)) {
                row = addTableCell(row, c.runTime);
            }
            if (r.Remarks.equals(true)) {
                row = addTableCell(row, c.Remarks);
            }
            table_layout.addView(row);
        }
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

    private TableRow addNumberTableCell(TableRow row, String cellText) {
        tv = new TextView(this);
        tv.setPadding(15, 15, 15, 15);
        tv.setBackgroundResource(R.drawable.cell_shape);
        tv.setText(cellText);
        tv.setGravity(Gravity.RIGHT);
        row.addView(tv);
        return row;
    }

    private TableRow addTotalCell(TableRow row, String cellText, String align) {
        tv = new TextView(this);
        tv.setPadding(15, 15, 15, 15);
        tv.setBackgroundResource(R.drawable.cell_total);
        tv.setText(cellText);
        if (align.equals("RIGHT")) {
            tv.setGravity(Gravity.RIGHT);
        }
        row.addView(tv);
        return row;
    }
}

