package com.example.memorite.model;

public class Entry {
    private String title;
    private String date;
    private byte[] image;
    private String memo;
    private Integer mood;
    private Double latitude, longitude, altitude;

    public Entry(String title, String date, byte[] image, String memo, Integer mood, Double latitude, Double longitude, Double altitude) {
        this.title = title;
        this.date = date;
        this.image = image;
        this.memo = memo;
        this.mood = mood;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public Entry(String title, String date, String memo, Integer mood, Double latitude, Double longitude, Double altitude) {
        this.title = title;
        this.date = date;
        this.memo = memo;
        this.mood = mood;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getMood() {
        return mood;
    }

    public void setMood(Integer mood) {
        this.mood = mood;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }
}
