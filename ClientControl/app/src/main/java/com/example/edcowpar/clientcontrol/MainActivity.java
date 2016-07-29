package com.example.edcowpar.clientcontrol;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private final int REQ_CODE_SPEECH_INPUT = 100;
    TextToSpeech t1;
    CharSequence toSpeak;
    String txtSpeechInput;
    private boolean initialized;
    private String queuedText;
    private SpeechRecognizer sr;
    private Bundle params;
    private UtteranceProgressListener mProgressListener = new UtteranceProgressListener() {
        @Override
        public void onStart(String utteranceId) {
        } // Do nothing

        @Override
        public void onError(String utteranceId) {
        } // Do nothing.

        @Override
        public void onDone(String utteranceId) {
            promptSpeechInput();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        t1 = new TextToSpeech(getApplicationContext(), this);
        t1.setOnUtteranceProgressListener(mProgressListener);
        sr = SpeechRecognizer.createSpeechRecognizer(this); // added
        params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.speak:
                promptSpeechInput();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput = result.get(0).toUpperCase();
                    Intent i = null;

                    if (txtSpeechInput.contains("SHOW")) {
                        if (txtSpeechInput.contains("CONSULTANT")) {
                            i = new Intent(this, ReportViewActivity.class);
                            i.putExtra("RepName", "Rep_001");
                        } else if (txtSpeechInput.contains("DATE")) {
                            i = new Intent(this, ReportViewActivity.class);
                            i.putExtra("RepName", "Rep_002");
                        } else if (txtSpeechInput.contains("TYPE")) {
                            i = new Intent(this, ReportViewActivity.class);
                            i.putExtra("RepName", "Rep_003");
                        } else if (txtSpeechInput.contains("VALUE")) {
                            i = new Intent(this, ReportViewActivity.class);
                            i.putExtra("RepName", "Rep_004");
                        } else if (txtSpeechInput.contains("VOLUMN")) {
                            i = new Intent(this, ReportViewActivity.class);
                            i.putExtra("RepName", "Rep_005");
                        }
                    } else {
                        if (txtSpeechInput.contains("NEW CLIENT") | txtSpeechInput.contains("ADD CLIENT")) {
                            i = new Intent(this, AddClientActivity.class);
                        } else if (txtSpeechInput.contains("NEW CONSULTANT") | txtSpeechInput.contains("ADD CONSULTANT")) {
                            i = new Intent(this, AddConsultantActivity.class);
                        } else if (txtSpeechInput.contains("CLIENTS")) {
                            i = new Intent(this, ClientListActivity.class);
                        } else if (txtSpeechInput.contains("CONSULTANTS")) {
                            i = new Intent(this, ConsultantListActivity.class);
                        } else if (txtSpeechInput.contains("REPORTS")) {
                            i = new Intent(this, ReportMenuActivity.class);
                        } else if (txtSpeechInput.contains("SETTINGS")) {
                            i = new Intent(this, SettingsActivity.class);
                        } else if (txtSpeechInput.contains("HELP")) {
                            i = new Intent(this, HelpActivity.class);
                        }
                    }

                    if (i != null) {
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), txtSpeechInput,
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            initialized = true;
            int result = t1.setLanguage(Locale.UK);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(getApplicationContext(), "This Language is not supported", Toast.LENGTH_SHORT).show();
            } else {
                // Welcome speech
                Bundle b = getIntent().getExtras();
                if (b != null) {
                    String welcome = b.getString("Speech");
                    if (welcome != null && !welcome.equals("")) speak(welcome);
                    getIntent().putExtra("Speech", "");   //one time only
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Speech is not supported", Toast.LENGTH_SHORT).show();
        }
    }

    public void speak(String text) {
        // If not yet initialized, queue up the text.
        if (!initialized) {
            queuedText = text;
            return;
        }
        queuedText = null;
        setTtsListener(); // no longer creates a new UtteranceProgressListener each time
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            t1.speak(text, TextToSpeech.QUEUE_FLUSH, params, "UniqueID");
        }
    }

    private void setTtsListener() {
        // Method radically simplified; callWithResult is retained but not used here
        final SpeechRecognizer callWithResult = sr; // was `this`
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        // Associate searchable configuration with the SearchView
        /*
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        */
        return true;
    }

    public void SelectClient(View view) {
        Intent intent = new Intent(this, SelectClientActivity.class);
        startActivity(intent);
    }

    public void SelectConsultants(View view) {
        Intent i = new Intent(this, ConsultantListActivity.class);
        startActivity(i);
    }

    public void SelectReports(View view) {
        Intent intent = new Intent(this, ReportMenuActivity.class);
        startActivity(intent);
    }

    public void SelectSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


    public void onDestroy() {
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onDestroy();
    }
}
