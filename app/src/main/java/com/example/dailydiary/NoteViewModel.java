package com.example.dailydiary;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getSelectedDateNotes();
    }



    public Void insert(Note note){
        repository.insert(note);
        return null;
    }

    public Void update(Note note){
        repository.update(note);
        return null;
    }

    public Void delete(Note note){
        repository.delete(note);
        return null;
    }

    public Void deleteAllNotes(){
        repository.deleteAllNotes();
        return null;
    }

    public LiveData<List<Note>> getSelectedDateNotes() {
        return repository.getSelectedDateNotes();
    }
}
