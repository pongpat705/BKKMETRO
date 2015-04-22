package com.example.maoz.hellowworld;

/**
 * Created by pongpat705 on 4/11/2015.
 */
public class Path_objects {
    private String station_a;
    private String station_b;
    private double distance;
    private String type;

    public Path_objects(String station_a, String station_b, double distance, String type) {
        this.station_a = station_a;
        this.station_b = station_b;
        this.distance = distance;
        this.type = type;
    }

    public String getStation_a() {
        return station_a;
    }
    public String getStation_b() {
        return station_b;
    }
    public double getDistance() {
        return distance;
    }
    public String getType() {
        return type;
    }

}
