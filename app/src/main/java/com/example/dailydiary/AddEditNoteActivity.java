package com.example.dailydiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class AddEditNoteActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    public static final String TAG = "addActivity";
    public static final String EXTRA_ID = "com.example.dailydiaryId";
    public static final String EXTRA_START = "com.example.dailydiaryStart";
    public static final String EXTRA_END = "com.example.dailydiaryEnd";
    public static final String EXTRA_DESCRIPTION = "com.example.dailydiaryStartDescription";
    private static final int SPEECH_REQUEST_CODE = 10;
    private static final int TIME_SPEECH_REQUEST_CODE = 11;


    private ImageButton startTimerButton;
    private ImageButton endTimerButton;
    private TextView startTextView;
    private TextView endTextView;
    private TextView timeDuration;
    private TextView descriptionTextView;
    private EditText decriptionEditText;
    private ImageButton micButton;
    private ImageButton micTimeButton;
    Boolean isStartTimePicker = false;
    Boolean isEndTimePicker = false;
    public ArrayList<String> result;
    String listString = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //check addActivity or EditActivity
        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Task");

            if(intent.getStringExtra(EXTRA_END).length() > 0){
                startTextView.setText(intent.getStringExtra(EXTRA_START));
                endTextView.setText(intent.getStringExtra(EXTRA_END));
                decriptionEditText.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            }

        }else {
            setTitle("Add Task");
        }
        // for close button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        //initialize ui component
        initializeUI();

        startTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open Time picker
                isStartTimePicker = true;
                isEndTimePicker = false;
                openTimePicker();
            }
        });

        endTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open Time picker
                isStartTimePicker = false;
                isEndTimePicker = true;
                openTimePicker();
            }
        });

        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechToText();
            }
        });

        micTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSpeechToText();
            }
        });

    }

    private void timeSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hi Speak Something");

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent,TIME_SPEECH_REQUEST_CODE);
        }else {
            //Log.d(TAG, "speechToText: "+e.getMessage());
            Toast.makeText(this,"error",Toast.LENGTH_SHORT).show();
        }
    }

    private void speechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hi Speak Something");

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent,SPEECH_REQUEST_CODE);
        }else {
            //Log.d(TAG, "speechToText: "+e.getMessage());
            Toast.makeText(this,"error",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case SPEECH_REQUEST_CODE:
                if(resultCode == RESULT_OK && data != null){
                    //String str = data.getStringExtra(RecognizerIntent.EXTRA_PROMPT);
                    result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    descriptionTextView.setText(result.get(0));

                }else{
                    Toast.makeText(this,"No data find",Toast.LENGTH_SHORT).show();
                }
                break;
            case TIME_SPEECH_REQUEST_CODE:
                if(resultCode == RESULT_OK && data != null){
                    //String str = data.getStringExtra(RecognizerIntent.EXTRA_PROMPT);
                    result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    timeDuration.setText(result.get(0));

                }else{
                    Toast.makeText(this,"No data find",Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String amOrPm = "AM";
        if(hourOfDay / 12 == 1){
            hourOfDay = hourOfDay % 12;
            amOrPm = "PM";
        }
        if(hourOfDay == 0){
            hourOfDay = 12;
        }
        String str = String.valueOf(hourOfDay) +" : " + String.valueOf(minute) +" " +amOrPm;
        Log.d(TAG, "onTimeSet: " + str);
        Toast.makeText(this,str,Toast.LENGTH_LONG).show();

        if(isStartTimePicker){
            startTextView.setText(str);
        }else if(isEndTimePicker){
            endTextView.setText(str);
        }
    }

    public void openTimePicker(){
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(),"time piker");
    }

    private void initializeUI() {
        startTimerButton = (ImageButton) findViewById(R.id.start_timer_button);
        endTimerButton = (ImageButton) findViewById(R.id.end_timer_button);

        startTextView = (TextView) findViewById(R.id.start_time);
        timeDuration = (TextView) findViewById(R.id.time_duration);
        descriptionTextView = (TextView) findViewById(R.id.mic_description);
        endTextView = (TextView) findViewById(R.id.end_time);
        decriptionEditText = (EditText)findViewById(R.id.description);
        micButton = (ImageButton)findViewById(R.id.mic_button);
        micTimeButton = (ImageButton) findViewById(R.id.time_mic_button);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveNote() {
        String start = startTextView.getText().toString();
        String end = endTextView.getText().toString();
        String description = decriptionEditText.getText().toString();
        String micDescription = descriptionTextView.getText().toString();
        String timeDurationString =  timeDuration.getText().toString();


        Intent intent = new Intent();

        if(start.trim().isEmpty() || end.trim().isEmpty() || description.trim().isEmpty()){

            if(timeDurationString.trim().isEmpty() || micDescription.trim().isEmpty()){

                Toast.makeText(this,"Details Missing",Toast.LENGTH_SHORT).show();
                return;
            }



            intent.putExtra(EXTRA_START,timeDurationString);
            intent.putExtra(EXTRA_END,"");
            intent.putExtra(EXTRA_DESCRIPTION,micDescription);

            int id = getIntent().getIntExtra(EXTRA_ID,-1);
            if(id != -1){
                intent.putExtra(EXTRA_ID,id);
            }

            setResult(RESULT_OK,intent);
            finish();

        }




        intent.putExtra(EXTRA_START,start);
        intent.putExtra(EXTRA_END,end);
        intent.putExtra(EXTRA_DESCRIPTION,description);

        int id = getIntent().getIntExtra(EXTRA_ID,-1);
        if(id != -1){
           intent.putExtra(EXTRA_ID,id);
        }

        setResult(RESULT_OK,intent);
        finish();
    }

}
