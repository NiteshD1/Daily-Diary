package com.example.dailydiary;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    public static final String TAG ="NoteAdapter";

    private List<Note> notes = new ArrayList<>();
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item,parent,false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {

        Note currentNote = notes.get(position);

        if(currentNote.getEndTime().length() == 0){
            holder.startTime.setText(currentNote.getStartTime());
        }else {
            String s = currentNote.getStartTime() + " To " + currentNote.getEndTime();
            holder.startTime.setText(s);
        }

        //holder.endTime.setText(currentNote.getEndTime());
        holder.description.setText(currentNote.getDescription());
        if(currentNote.getIsCompleted()){
            holder.checkButton.setImageResource(R.drawable.ic_check_circle_dark_24dp);
        }else {
            holder.checkButton.setImageResource(R.drawable.ic_check_circle_grey_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public Void setNotes(List<Note> notes){
        this.notes = notes;
        notifyDataSetChanged();
        return null;
    }

    public Note getNoteAt(int position){
        return notes.get(position);
    }
    class NoteHolder extends RecyclerView.ViewHolder{

        private TextView startTime;
        private TextView endTime;
        private TextView description;
        private ImageButton checkButton;

        public NoteHolder(View itemView){
            super(itemView);

            startTime = itemView.findViewById(R.id.start_time);
            //endTime = itemView.findViewById(R.id.end_time);
            description = itemView.findViewById(R.id.description);
            checkButton = itemView.findViewById(R.id.check_button);

            checkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity mainActivity = MainActivity.getInstance();
                    Note note = notes.get(getAdapterPosition());
                    if(note.getIsCompleted()){
                        checkButton.setImageResource(R.drawable.ic_check_circle_grey_24dp);

                        note.setIsCompleted(false);
                    }else {
                        checkButton.setImageResource(R.drawable.ic_check_circle_dark_24dp);

                        note.setIsCompleted(true);
                    }

                    mainActivity.noteViewModel.update(note);
                }
            });

//            description.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d(TAG, "onClick:ccc ");
//                    MainActivity mainActivityInstance = MainActivity.getInstance();
//                    Note currentNote = notes.get(getAdapterPosition());
//
////                    Log.d(TAG, "onitemClick: Outside");
//                    mainActivityInstance.onRecyclerViewItemClick(currentNote);
//                    //Toast.makeText(this,"on click description",Toast.LENGTH_SHORT).show();
//                }
//            });
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    MainActivity mainActivityInstance = MainActivity.getInstance();
//                    Note currentNote = notes.get(getAdapterPosition());
//                    Log.d(TAG, "onitemClick: Outside");
//                    //mainActivityInstance.onRecyclerViewItemClick(currentNote);
////                    if(currentNote.getDate() == mainActivityInstance.getCurrentDate()){
////                        Log.d(TAG, "onitemClick: ");
////                        mainActivityInstance.onRecyclerViewItemClick(currentNote);
////                    }else{
////                        //Toast.makeText(MainActivity.this,"Past Days Can't Update",Toast.LENGTH_LONG).show();
////                    }
//                }
//            });

        }
    }
}
