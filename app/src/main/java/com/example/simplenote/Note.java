package com.example.simplenote;

import java.util.Date;

/*
    Data class for a note.
    Each note has its own unique ID, title, and content.
* */
public class Note {
    public String id;
    public String title;
    public String content;
    public long lastModified;

    // New note
    public Note(String title, String content) {
        this.id = java.util.UUID.randomUUID().toString();
        this.title = title;
        this.content = content;
        this.lastModified = System.currentTimeMillis();
    }

    // Existed note
    public Note(String id, String title, String content, long lastModified) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.lastModified = lastModified;
    }
}