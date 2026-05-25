package com.example.evenementsetudiants;

public class Event {

    private int id;
    private String title;
    private String date;
    private String location;
    private String description;
    private String image;;
    private String time;
    // Required for Firebase Firestore
    public Event() {
    }

    // Used for manual creation (e.g., in HomeFragment)
    public Event(int id, String title, String date,
                 String time,
                 String location,
                 String description,
                 String image) {

        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.location = location;
        this.description = description;
        this.image = image;
    }

    // Used for SQLite retrieval
    public Event(String title,
                 String date,
                 String time,
                 String location,
                 String description,
                 String image) {

        this.title = title;
        this.date = date;
        this.time = time;
        this.location = location;
        this.description = description;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

