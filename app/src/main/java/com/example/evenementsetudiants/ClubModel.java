package com.example.evenementsetudiants;


public class ClubModel {

    String name;
    private String image;

    public ClubModel(String name ) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public String getImage() {
        return image;
    }
}
