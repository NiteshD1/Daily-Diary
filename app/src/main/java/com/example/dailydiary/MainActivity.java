package com.example.dailydiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private String TAG = "MainActivity";
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    public static Calendar changedCalender;

    public static MainActivity instance;
    public NoteViewModel noteViewModel;


    public NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: in MainActivity");

        if(NoteRepository.currentDate == null){
            setTitle(getCurrentDate());
        }else{
            setTitle(NoteRepository.currentDate);
        }

        instance = this;

        // recyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // adapter

        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        // viemodel

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        noteViewModel.getSelectedDateNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //update on recyclerview

                adapter.setNotes(notes);
                Log.d(TAG, "onChanged: ");
                if(notes.size() ==0){
                    Toast.makeText(MainActivity.this,"No Task Added",Toast.LENGTH_SHORT).show();
                }

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                //Toast.makeText(this, "Task Deleted",Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        // floating action button

        FloatingActionButton button = findViewById(R.id.floating_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to addNote activity

                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK && data != null) {

            // get data from intent and save it to database
            String start = data.getStringExtra(AddEditNoteActivity.EXTRA_START);
            String end = data.getStringExtra(AddEditNoteActivity.EXTRA_END);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);

            String todaysDate = getCurrentDate();
            Note note = new Note(todaysDate, start, end, description, false);

            noteViewModel.insert(note);

            Toast.makeText(this, "Task Saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK && data != null){
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID,-1);

            Toast.makeText(this,"Task clicked",Toast.LENGTH_SHORT).show();
            if(id == -1){
                Toast.makeText(this,"Task Not Updated",Toast.LENGTH_SHORT).show();
                return;
            }
            String start = data.getStringExtra(AddEditNoteActivity.EXTRA_START);
            String end = data.getStringExtra(AddEditNoteActivity.EXTRA_END);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);

            String todaysDate = getCurrentDate();
            Note note = new Note(todaysDate, start, end, description, false);

            note.setId(id);

            noteViewModel.update(note);

            Toast.makeText(this, "Task Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Not Created", Toast.LENGTH_SHORT).show();
        }
    }

    public String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All Task Deleted", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.date_picker:
                openDatePicker();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void openDatePicker() {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(),"date picker");
    }

    public static MainActivity getInstance(){
        return instance;
    }

    public void onRecyclerViewItemClick(Note currentNote) {
        Toast.makeText(this,"onItemClick",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);

        intent.putExtra(AddEditNoteActivity.EXTRA_ID,currentNote.getId());
        intent.putExtra(AddEditNoteActivity.EXTRA_START,currentNote.getStartTime());
        intent.putExtra(AddEditNoteActivity.EXTRA_END,currentNote.getEndTime());
        intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION,currentNote.getDescription());

        startActivityForResult(intent,EDIT_NOTE_REQUEST);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        changedCalender = Calendar.getInstance();

        changedCalender.set(Calendar.YEAR,year);
        changedCalender.set(Calendar.MONTH,month);
        changedCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth);


        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(changedCalender.getTime());//df.format(c);

        //use this date to find task of particular date
        loadSelectedDayTask(formattedDate);
    }

    private void loadSelectedDayTask(String formattedDate) {
        setTitle(formattedDate);
        NoteRepository.currentDate = formattedDate;

        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
//        noteViewModel.setSelectedDateNotes(formattedDate);
//        noteViewModel.getAllNotes(formattedDate).observe(this, new Observer<List<Note>>() {
//            @Override
//            public void onChanged(List<Note> notes) {
//                adapter.setNotes(notes);
//                Toast.makeText(MainActivity.this,"onChanged 2",Toast.LENGTH_SHORT).show();
//            }
//        });

    }
}
