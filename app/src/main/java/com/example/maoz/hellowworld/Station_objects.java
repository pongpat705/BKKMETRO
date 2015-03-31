package com.example.maoz.hellowworld;

/**
 * Created by ZEPTIMUS on 11/24/2014.
 */
public class Station_objects {
    private String stations;
    private Double lat;
    private Double lng;

    public Station_objects(String stations, Double lat, Double lng){
        this.stations = stations;
        this.lat = lat;
        this.lng = lng;
    }


    public String getStations(){
        return this.stations;
    }

    public Double getLat(){
        return this.lat;
    }

    public Double getLng(){
        return this.lng;
    }

}
