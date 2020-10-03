package com.example.dailydiary;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String date;
    private String startTime;
    private String endTime;
    private String description;
    private Boolean isCompleted;

    public Note(String date, String startTime, String endTime, String description, Boolean isCompleted) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.isCompleted = isCompleted;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIsCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }
}
