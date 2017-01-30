package com.example.edcowpar.sbclub;

import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Locale;

public class SbError extends AppCompatActivity {

    TextToSpeech t1;
    CharSequence toSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sberror);
        //get parameter in extra
        Bundle b = getIntent().getExtras();
        TextView eMes = (TextView) findViewById(R.id.textView);
        eMes.setText(b.getString("eMes"));

        // Make sure we're running on LOLLIPOP or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //add listener
            t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        t1.setLanguage(Locale.UK);
                    }
                }
            });
            toSpeak = b.getString("eMes");
            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    public void onPause() {
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }
}
