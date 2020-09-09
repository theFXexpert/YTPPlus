package com.ytp.ytpplus;

public class TimeStamp {

    private final int HOURS;
    private final int MINUTES;
    private final double SECONDS;
    
    public TimeStamp(String time) {
        String[] parts = time.split(":");
        this.HOURS = Integer.parseInt(parts[0]);
        this.MINUTES = Integer.parseInt(parts[1]);
        this.SECONDS = Double.parseDouble(parts[2]);
    }
    
    public TimeStamp(double l) {
        double seconds = l % 60;
        l /= 60;
        double minutes = l % 60;
        l /= 60;
        double hours = l % 24;
        
        this.HOURS = (int)hours;
        this.MINUTES = (int)minutes;
        this.SECONDS = seconds;
    }
    
    public double getLengthSec() {
        return SECONDS + (MINUTES*60) + (HOURS*60*60);
    }
    
    public int getHours() {
        return HOURS;
    }
    public int getMinutes() {
        return MINUTES;
    }
    public double getSeconds() {
        return SECONDS;
    }
    
    public void getDeets() {
        System.out.println("HOURS: " + this.HOURS);
        System.out.println("MIN: " + this.MINUTES);
        System.out.println("SEC: " + this.SECONDS);
    }
    
    public String getTimeStamp() {
        return this.HOURS + ":" + this.MINUTES + ":" + this.SECONDS;
    }
}
