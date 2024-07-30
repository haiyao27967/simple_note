package com.example.simplenote;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.snackbar.Snackbar;

import java.util.Map;

public class NoteListViewModel extends AndroidViewModel {

    private final MutableLiveData<Map<String, Note>> notes = new MutableLiveData<>(); // (id, note)
    private String currentId = "";
    private final Context context = getApplication().getApplicationContext();

    public NoteListViewModel(@NonNull Application application) {
        super(application);
        notes.setValue(FileHandler.loadNotes(context));
    }

    // Get note by ID
    public Note getNoteById(String id) {
        if (notes.getValue().containsKey(id)) {
            Note note = notes.getValue().get(id);
            if (FileHandler.loadNote(note, context)){
                return note;
            }
        }
        return null;
    }

    public MutableLiveData<Map<String, Note>> getNotes() {
        return notes;
    }

    public void setCurrentId(String id) {
        currentId = id;
    }

    // Get the current note ID, if return null means is currently creating a new note
    public String getCurrentId() {
        return currentId;
    }

    public boolean addNote(Note note) {
        // Add a new note to the notes LiveData
        if (FileHandler.createNote(note, context)){
            note.lastModified = System.currentTimeMillis();
            notes.getValue().put(note.id, note);
            return true;
        }
        return false;
    }

    public boolean deleteNote(Note note) {
        // Delete a note from the notes LiveData
        if (FileHandler.deleteNote(note, context)){
            notes.getValue().remove(note.id);
            return true;
        }
        return false;
    }

    public boolean updateNote(Note note) {
        // Update an existing note in the notes LiveData
        if (FileHandler.modifyNote(note, context)){
            note.lastModified = System.currentTimeMillis();
            notes.getValue().replace(note.id, note);
            return true;
        }
        return false;
    }

    public boolean renameNote(Note note, String newTitle) {
        if (FileHandler.renameNote(note, newTitle, context)){
            note.title = newTitle;
            note.lastModified = System.currentTimeMillis();
            notes.getValue().replace(note.id, note);
            return true;
        }
        return false;
    }

}
