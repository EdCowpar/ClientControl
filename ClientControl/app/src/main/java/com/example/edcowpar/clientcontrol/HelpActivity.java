package com.example.edcowpar.clientcontrol;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        String versionName = "";
        try {
            PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        TextView aboutTextView = (TextView) findViewById(R.id.about_text_view);

        Spanned aboutText = Html.fromHtml("<h1>Menu Commands</h1>"
                + "Clients<br/>Consultants<br/>Reports<br/>Settings<br/>Help<br/>"
                + "<h1>New Input Commands</h1>"
                + "<b>Keyword Add or Add New</b><br/>"
                + "Add New Client<br/>Add New Consultant<br/>"
                + "<h1>Report Commands</h1>"
                + "<b>Keyword Show</b><br/>"
                + "Show All Clients<br/>Show Clients Expiring<br/>Show Clients by Consultant<br/>"
                + "Show Clients by Volume<br/>Show Clients by System Type<br/>"
                + "<h1>Report Selection Commands</h1>"
                + "<b>Keyword Select</b><br/>");
        aboutTextView.setText(aboutText);
    }
}
