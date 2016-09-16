package com.example.edcowpar.clientcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportViewActivity extends AppCompatActivity {
    private String RepName, sql, curKey, saveKey, today;
    private TableLayout table_layout;
    private Integer cols, col_qty, col_Lic, col_Vol;
    private double tot9_rows, tot9_Lic, tot9_Vol;
    private double tot1_rows, tot1_Lic, tot1_Vol;
    private TableRow row;
    private TextView tv;
    private SqlGet sq;
    private Connection cn;
    private Intent i;
    private ReportHeadings r;
    private ReportTotals t;
    private ClientRecord c;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_view);
        table_layout = (TableLayout) findViewById(R.id.tableLayout1);
        Bundle b = getIntent().getExtras();
        RepName = b.getString("RepName");
        today = new SimpleDateFormat("yyyyMMdd").format(new Date());


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
        r = GetData.Read_ReportHeadings(this.getApplicationContext(), filename);
        t = GetData.Read_ReportTotals(this.getApplicationContext(), filename);
        actionBar = getSupportActionBar();
        actionBar.setTitle(RepName);
        switch (RepName) {
            case "Rep_001":  //Clients by Consultant
                actionBar.setSubtitle(getResources().getString(R.string.Rep_001));
                sql = "select * from sbClients order by Consultant,ClientName";
                break;
            case "Rep_002": //Clients by Expiry Date
                if (r.RecNo.equals(0)) {
                    r.ExpiryDate = true;
                }
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
            tot9_rows = 0;
            tot1_rows = 0;

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
                tot9_rows++;
                tot1_rows++;
            }
            doTotals();
            doFinalTotals();

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
                int result = c.ExpiryDate.compareTo(today);
                if (result == 0)
                    key = "Expires Today";
                else if (result < 0)
                    key = "Already Expired";
                else
                    key = "Expires " + SubRoutines.FmtString(c.ExpiryDate, "a");
                break;
            case "Rep_003":   //Clients by System Type
                key = "System Type " + sq.getSystemType(this, c.System);
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
        if (col_Vol > 0) {
            while (cols < col_Vol) {
                cols++;
                row = addTableCell(row, " ");
            }
            row = addTotalCell(row, String.format("%.0f", tot9_Vol), "RIGHT");
            cols++;
        }
        if (col_Lic > 0) {
            while (cols < col_Lic) {
                cols++;
                row = addTableCell(row, " ");
            }
            row = addTotalCell(row, String.format("%.2f", tot9_Lic), "RIGHT");
            cols++;
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
        tot1_Lic = tot1_Lic + tot9_Lic;
        tot1_Vol = tot1_Vol + tot9_Vol;
        //clear totals
        tot9_rows = 0;
        tot9_Lic = 0;
        tot9_Vol = 0;

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
        if (col_Vol > 0) {
            while (cols < col_Vol) {
                cols++;
                row = addTableCell(row, " ");
            }
            row = addTotalCell(row, String.format("%.0f", tot1_Vol), "RIGHT");
            cols++;
        }
        if (col_Vol > 0) {
            while (cols < col_Vol) {
                cols++;
                row = addTableCell(row, " ");
            }
            row = addTotalCell(row, String.format("%.0f", tot1_Vol), "RIGHT");
            cols++;
        }
        if (col_Lic > 0) {
            while (cols < col_Lic) {
                cols++;
                row = addTableCell(row, " ");
            }
            row = addTotalCell(row, String.format("%.2f", tot1_Lic), "RIGHT");
            cols++;
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
        col_Vol = 0;
        col_Lic = 0;
        col_qty = 0;
        tot9_Vol = 0;
        tot9_Lic = 0;
        tot1_Vol = 0;
        tot1_Lic = 0;
        if (r.ClientNo.equals(true)) {
            cols++;
            row = addTableCell(row, "ClientNo");
        }
        if (r.ClientName.equals(true)) {
            cols++;
            row = addTableCell(row, "ClientName");
        }
        if (r.ContactName.equals(true)) {
            cols++;
            row = addTableCell(row, "ContactName");
            if (col_qty.equals(0)) {
                col_qty = cols;
            }
        }
        if (r.EmailAddress.equals(true)) {
            cols++;
            row = addTableCell(row, "EmailAddress");
            if (col_qty.equals(0)) {
                col_qty = cols;
            }
        }
        if (r.PayeNo.equals(true)) {
            cols++;
            row = addTableCell(row, "PayeNo");
            if (col_qty.equals(0)) {
                col_qty = cols;
            }
        }
        if (r.Telephone.equals(true)) {
            cols++;
            row = addTableCell(row, "Telephone");
            if (col_qty.equals(0)) {
                col_qty = cols;
            }
        }
        if (r.ExpiryDate.equals(true)) {
            cols++;
            row = addTableCell(row, "ExpiryDate");
            if (col_qty.equals(0)) {
                col_qty = cols;
            }
        }
        if (r.Volumn.equals(true)) {
            cols++;
            col_Vol = cols;
            row = addTableCell(row, "Volume");
        }
        if (r.UIFNo.equals(true)) {
            cols++;
            row = addTableCell(row, "UIFNo");
            if (col_qty.equals(0)) {
                col_qty = cols;
            }
        }
        if (r.SDLNo.equals(true)) {
            cols++;
            row = addTableCell(row, "SDLNo");
            if (col_qty.equals(0)) {
                col_qty = cols;
            }
        }
        if (r.System.equals(true)) {
            cols++;
            row = addTableCell(row, "System");
            if (col_qty.equals(0)) {
                col_qty = cols;
            }
        }
        if (r.AnnualLicence.equals(true)) {
            cols++;
            col_Lic = cols;
            row = addTableCell(row, "AnnualLicence");
        }
        if (r.Paid.equals(true)) {
            cols++;
            row = addTableCell(row, "Paid");
            if (col_qty.equals(0)) {
                col_qty = cols;
            }
        }
        if (r.Postal_01.equals(true)) {
            cols++;
            row = addTableCell(row, "Postal_01");
            if (col_qty.equals(0)) {
                col_qty = cols;
            }
        }
        if (r.Postal_02.equals(true)) {
            cols++;
            row = addTableCell(row, "Postal_02");
            if (col_qty.equals(0)) {
                col_qty = cols;
            }
        }
        if (r.Postal_03.equals(true)) {
            cols++;
            row = addTableCell(row, "Postal_03");
            if (col_qty.equals(0)) {
                col_qty = cols;
            }
        }
        if (r.PostCode.equals(true)) {
            cols++;
            row = addTableCell(row, "PostCode");
            if (col_qty.equals(0)) {
                col_qty = cols;
            }
        }
        if (r.InstallPin.equals(true)) {
            cols++;
            row = addTableCell(row, "InstallPin");
            if (col_qty.equals(0)) {
                col_qty = cols;
            }
        }
        if (r.PDFModule.equals(true)) {
            cols++;
            row = addTableCell(row, "PDFModule");
            if (col_qty.equals(0)) {
                col_qty = cols;
            }
        }
        if (r.Consultant.equals(true)) {
            cols++;
            row = addTableCell(row, "Consultant");
            if (col_qty.equals(0)) {
                col_qty = cols;
            }
        }
        if (r.InCloud.equals(true)) {
            cols++;
            row = addTableCell(row, "InCloud");
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
            if (r.ContactName.equals(true)) {
                row = addTableCell(row, c.ContactName);
            }
            if (r.EmailAddress.equals(true)) {
                row = addTableCell(row, c.EmailAddress);
            }
            if (r.PayeNo.equals(true)) {
                row = addTableCell(row, c.PayeNo);
            }
            if (r.Telephone.equals(true)) {
                row = addTableCell(row, c.Telephone);
            }
            if (r.ExpiryDate.equals(true)) {
                String strTxt = c.ExpiryDate;
                strTxt = SubRoutines.FmtString(strTxt, "a");
                row = addTableCell(row, strTxt);
            }
            if (r.Volumn.equals(true)) {
                tot9_Vol = tot9_Vol + Double.parseDouble(c.Volumn);
                row = addNumberTableCell(row, c.Volumn);
            }
            if (r.UIFNo.equals(true)) {
                row = addTableCell(row, c.UIFNo);
            }
            if (r.SDLNo.equals(true)) {
                row = addTableCell(row, c.SDLNo);
            }
            if (r.System.equals(true)) {
                String sType = c.System;
                String v = sq.getSystemType(this, sType);
                row = addTableCell(row, v);
            }
            if (r.AnnualLicence.equals(true)) {
                tot9_Lic = tot9_Lic + Double.parseDouble(c.Annual_Licence);
                row = addNumberTableCell(row, c.Annual_Licence);
            }
            if (r.Paid.equals(true)) {
                row = addTableCell(row, c.Paid);
            }
            if (r.Postal_01.equals(true)) {
                row = addTableCell(row, c.Postal_01);
            }
            if (r.Postal_02.equals(true)) {
                row = addTableCell(row, c.Postal_02);
            }
            if (r.Postal_03.equals(true)) {
                row = addTableCell(row, c.Postal_03);
            }
            if (r.PostCode.equals(true)) {
                row = addTableCell(row, c.PostCode);
            }
            if (r.InstallPin.equals(true)) {
                row = addTableCell(row, c.InstallPin);
            }
            if (r.PDFModule.equals(true)) {
                row = addTableCell(row, c.PDFModule);
            }
            if (r.Consultant.equals(true)) {
                String cc = sq.getConsultantName(c.Consultant);
                row = addTableCell(row, cc);
            }
            if (r.InCloud.equals(true)) {
                row = addTableCell(row, c.InCloud);
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

