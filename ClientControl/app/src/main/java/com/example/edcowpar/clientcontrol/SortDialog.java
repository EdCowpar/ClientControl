package com.example.edcowpar.clientcontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class SortDialog extends AppCompatActivity {
    AppSettings a;
    RadioButton rb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_dialog);
        setTitle("Sort by:");
        a = GetData.Read(this.getApplicationContext());
        // set radio button
        switch (a.ClientSeq) {
            case 0: //rbClientNo
                rb = (RadioButton) findViewById(R.id.rbClientNo);
                rb.setChecked(true);
                break;
            case 1: //rbClientNameAsc:
                rb = (RadioButton) findViewById(R.id.rbClientNameAsc);
                rb.setChecked(true);
                break;
            case 2: //R.id.rbClientNameDsc:
                rb = (RadioButton) findViewById(R.id.rbClientNameDsc);
                rb.setChecked(true);
                break;
            case 3: //R.id.rbExpiryDateAsc:
                rb = (RadioButton) findViewById(R.id.rbExpiryDateAsc);
                rb.setChecked(true);
                break;
            case 4: //R.id.rbExpiryDateDsc:
                rb = (RadioButton) findViewById(R.id.rbExpiryDateDsc);
                rb.setChecked(true);
                break;
            case 5: //R.id.rbConsultant:
                rb = (RadioButton) findViewById(R.id.rbConsultant);
                rb.setChecked(true);
                break;
            case 6: //R.id.rbSystemType:
                rb = (RadioButton) findViewById(R.id.rbSystemType);
                rb.setChecked(true);
                break;

        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.rbClientNo:
                if (checked)
                    a.setClientSeq(0);
                break;
            case R.id.rbClientNameAsc:
                if (checked)
                    a.setClientSeq(1);
                break;
            case R.id.rbClientNameDsc:
                if (checked)
                    a.setClientSeq(2);
                break;
            case R.id.rbExpiryDateAsc:
                if (checked)
                    a.setClientSeq(3);
                break;
            case R.id.rbExpiryDateDsc:
                if (checked)
                    a.setClientSeq(4);
                break;
            case R.id.rbConsultant:
                if (checked)
                    a.setClientSeq(5);
                break;
            case R.id.rbSystemType:
                if (checked)
                    a.setClientSeq(6);
                break;

        }
        GetData.Write(this.getApplicationContext(), a); //Save File
        finish();
    }

}
