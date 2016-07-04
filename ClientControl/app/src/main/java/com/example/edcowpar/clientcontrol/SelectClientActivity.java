package com.example.edcowpar.clientcontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SelectClientActivity extends AppCompatActivity {
    Button mButton, btnBack;
    EditText mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_client);
        mButton = (Button) findViewById(R.id.btnModify);
        mEdit = (EditText) findViewById(R.id.etSerialNo);
        btnBack = (Button) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String strClientNo = mEdit.getText().toString();
                        Intent i = new Intent(SelectClientActivity.this, ModifyClientActivity.class);
                        i.putExtra("ClientNo", strClientNo);
                        startActivity(i);
                    }
                });
    }

}
