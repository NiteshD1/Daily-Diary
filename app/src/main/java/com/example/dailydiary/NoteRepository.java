package com.example.dailydiary;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.lifecycle.LiveData;

public class NoteRepository {



    public static String currentDate;
    private NoteDao noteDao;
    public LiveData<List<Note>> allNotes;

    public NoteRepository(Application application){
        if(currentDate == null){
            currentDate = getCurrentDate();
        }

        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getSelectedDateNotes(currentDate);
    }



    public String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public void insert(Note note){
         new InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void update(Note note){

        new UpdateNoteAsyncTask(noteDao).execute(note);
    }
    public void delete(Note note){

        new DeleteNoteAsyncTask(noteDao).execute(note);

    }

    public void deleteAllNotes(){
        new DeleteAllNoteAsyncTask(noteDao).execute();
    }

    public LiveData<List<Note>> getSelectedDateNotes(){
        return allNotes;
    }

     private static class InsertNoteAsyncTask extends AsyncTask< Note,Void,Void> {
        private NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

         @Override
         protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
         }
     }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note,Void,Void> {
        private NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {

            noteDao.delete(notes[0]);
            return null ;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note,Void,Void> {
        private NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {

            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNoteAsyncTask extends AsyncTask<Void,Void,Void> {
        private NoteDao noteDao;

        private DeleteAllNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }

//    private static class SetSelectedDateAsynTask extends AsyncTask<String,Void,LiveData<List<Note>>> {
//        private NoteDao noteDao;
//        public LiveData<List<Note>> notes;
//
//        private SetSelectedDateAsynTask(NoteDao noteDao){
//            this.noteDao = noteDao;
//
//        }
//
//
//        @Override
//        protected LiveData<List<Note>> doInBackground(String... strings) {
//
//            return noteDao.getSelectedDateNotes(strings[0]);
//        }
//
//        @Override
//        protected void onPostExecute(LiveData<List<Note>> listLiveData) {
//            super.onPostExecute(listLiveData);
//
//            notes = listLiveData;
//            //Toast.makeText(MainActivity.this,"dd" ;)
//        }
//    }
}
