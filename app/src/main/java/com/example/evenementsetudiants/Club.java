package com.example.evenementsetudiants;

public class Club {

    private int id;
    private String name;
    private String image;
    public Club() {
    }


    public Club(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }
    public Club(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name =name;
    }

    public void setId(int id) {
        this.id=id;
    }

    public void setImage(String  image) {
        this.image=image;
    }
}
