package com.example.maoz.hellowworld;

/**
 * Created by pongpat705 on 4/11/2015.
 */
public class Path_objects {
    private String station_a;
    private String station_b;
    private double distance;
    private double price;
    private String type;
    private String detail;

    public Path_objects(String station_a, String station_b, double distance, double price, String type, String detail) {
        this.station_a = station_a;
        this.station_b = station_b;
        this.distance = distance;
        this.price = price;
        this.type = type;
        this.detail = detail;
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

    public double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }
}
