package com.xbb.la.modellibrary.bean;

import java.io.Serializable;

public class LocationBean implements Serializable{
    private String lon;
    private String lat;
    private String city;
    private String addr;
    private double targetLat;
    private double targetLon;
    private String targetCity;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getTargetLat() {
        return targetLat;
    }

    public void setTargetLat(double targetLat) {
        this.targetLat = targetLat;
    }

    public double getTargetLon() {
        return targetLon;
    }

    public void setTargetLon(double targetLon) {
        this.targetLon = targetLon;
    }

    public String getTargetCity() {
        return targetCity;
    }

    public void setTargetCity(String targetCity) {
        this.targetCity = targetCity;
    }

}
