package com.example.edcowpar.clientcontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        //get parameter in extra
        Bundle b = getIntent().getExtras();
        TextView errMessage = (TextView) findViewById(R.id.textView);
        assert errMessage != null;
        errMessage.setText(b.getString("errMessage"));

    }
}
