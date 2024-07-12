package com.example.gostudy;
public class Meeting {
    private String type;
    private String date;
    private String time;
    private String url;
    private String description;

    // Default constructor required for Firebase
    public Meeting() {
    }

    public Meeting(String type, String date, String time, String url, String description) {
        this.type = type;
        this.date = date;
        this.time = time;
        this.url = url;
        this.description = description;
    }

    // Getters and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        // Customize this based on how you want it to appear in the ListView
        return "Date: " + date + "\n" +
                "Time: " + time + "\n" +
                "Description: " + description + "\n" +
                "URL: " + url;
    }

}
