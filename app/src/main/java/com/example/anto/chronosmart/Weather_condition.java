package com.example.anto.chronosmart;

public class Weather_condition {
    private String description = new String();
    private int picture = -1;

    public Weather_condition(String description, int picture) {
        this.description = description;
        this.picture = picture;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
