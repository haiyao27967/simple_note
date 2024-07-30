package com.example.simplenote;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Map<String, Note> notes;
    private NavController navController;
    private NoteListViewModel viewModel;

    public NoteAdapter(Map<String, Note> notes, NavController navController, NoteListViewModel viewModel) {
        this.notes = notes;
        this.navController = navController;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false); // Use your note_item layout
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        // sort by last modified time
        List<Note> noteList = new ArrayList<>(notes.values());
        noteList.sort((n1, n2) -> Long.compare(n2.lastModified, n1.lastModified));

        Note note = noteList.get(position);
        holder.titleTextView.setText(note.title);

        holder.itemView.setOnClickListener(view -> {
            // Navigate to NoteDetailFragment, passing the note ID
            viewModel.setCurrentId(note.id);
            navController.navigate(R.id.action_noteListFragment_to_noteDetailFragment);
        });

        holder.likeButton.setOnClickListener(view -> {
            // Handle like button click
        });

        holder.deleteButton.setOnClickListener(view -> {
            // Handle delete button click
            viewModel.deleteNote(note);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public Button likeButton, deleteButton;

        public NoteViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.note_title); // Adjust IDs as needed
            likeButton = itemView.findViewById(R.id.like_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}