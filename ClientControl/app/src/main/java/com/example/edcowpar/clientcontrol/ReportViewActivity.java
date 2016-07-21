package com.example.edcowpar.clientcontrol;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReportViewActivity extends AppCompatActivity {
    String strRepName, strRepTitle;
    TableLayout table_layout;
    Integer rows, cols;
    TableRow row;
    TextView tv, qty;
    CheckBox checkBox;
    ImageButton addBtn, minusBtn;
    SqlGet sq;
    Connection cn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_view);
        table_layout = (TableLayout) findViewById(R.id.tableLayout1);

        Bundle b = getIntent().getExtras();
        strRepName = b.getString("RepName");
        strRepTitle = b.getString("RepTitle");
        getSupportActionBar().setTitle(strRepName + " " + strRepTitle);
        Build_Rep_001();

    }

    private void Build_Rep_001() {
        String eMes;
        sq = new SqlGet(); //Open sql
        cn = sq.getConnection();
        String sql = "select * from sbClients ";
        ResultSet rs;
        TableRow row;
        TextView tv;

        try {

            Statement statement = cn.createStatement();
            rs = statement.executeQuery(sql);
            //Headings
            row = newTableRow();
            row = addTableCell(row, "ClientNo");
            row = addTableCell(row, "ClientName");
            row = addTableCell(row, "ContactName");
            row = addTableCell(row, "EmailAddress");
            row = addTableCell(row, "ExpiryDate");
            row = addTableCell(row, "Volume");
            table_layout.addView(row);

            while (rs.next()) {
                ClientRecord c = sq.populateClientRecord(rs);
                row = newTableRow();
                row = addTableCell(row, c.ClientNo);
                row = addTableCell(row, c.ClientName);
                row = addTableCell(row, c.ContactName);
                row = addTableCell(row, c.EmailAddress);
                row = addTableCell(row, c.ExpiryDate);
                row = addTableCell(row, c.Volumn);
                table_layout.addView(row);

            }
        } catch (SQLException e) {
            eMes = e.getMessage();
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

}

