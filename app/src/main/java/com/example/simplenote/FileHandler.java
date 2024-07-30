package com.example.simplenote;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileHandler {
    private static final String FILE_EXTENSION = ".txt";

    // Load the content of note from the storage directory
    public static boolean loadNote(Note note, Context context) {

        File file = new File(context.getFilesDir(), note.id + FILE_EXTENSION);
        if (file.exists()){
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                reader.readLine(); // skip the title
                while ((line = reader.readLine()) != null) {
                    content.append(line).append('\n');
                }
                reader.close();
                note.content = content.toString();
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    // Load all notes from the storage directory
    public static Map<String, Note> loadNotes(Context context) {
        Map<String, Note> notes = new HashMap<>();

        // Retrieve notes from the storage directory
        File directory = context.getFilesDir();

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));
            if (files != null) {
                for (File file : files) {

                    // retrieve title
                    String title = "";
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        if ((line = reader.readLine()) != null) {
                            title = line;
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // lazy load: only load the content of the note when it is needed
                    Note note = new Note(file.getName().replace(".txt", ""), title, "", file.lastModified());
                    notes.put(note.id, note);
                }
            }
        }
        return notes;
    }

    public static boolean deleteNote(Note note, Context context) {
        File file = new File(context.getFilesDir(), note.id + FILE_EXTENSION);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static boolean createNote(Note note, Context context) {
        File file = new File(context.getFilesDir(), note.id + FILE_EXTENSION);
        try {
            if (file.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(note.title.getBytes());
                fos.write('\n');
                fos.write(note.content.getBytes());
                fos.close();
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean modifyNote(Note note, Context context) {
        File file = new File(context.getFilesDir(), note.id + FILE_EXTENSION);
        try {
            if (file.exists()) {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(note.title.getBytes());
                fos.write('\n');
                fos.write(note.content.getBytes());
                fos.close();
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean renameNote(Note note, String newTitle, Context context) {
        File file = new File(context.getFilesDir(), note.id + FILE_EXTENSION);
        try {
            if (file.exists()) {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(newTitle.getBytes());
                fos.write('\n');
                fos.write(note.content.getBytes());
                fos.close();
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
