package com.example.maoz.hellowworld;

/**
 * Created by ZEPTIMUS on 11/24/2014.
 */
public class Station_objects {
    private String stations;
    private Double lat;
    private Double lng;
    private String type;

    public Station_objects(String stations, Double lat, Double lng,String type){
        this.stations = stations;
        this.lat = lat;
        this.lng = lng;
        this.type = type;
    }

    public String getStations(){
        return stations;
    }
    public Double getLat(){
        return lat;
    }
    public Double getLng(){
        return lng;
    }
    public String getType() {
        return type;
    }

}
