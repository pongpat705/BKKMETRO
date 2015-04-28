package com.example.maoz.hellowworld;

/**
 * อ็อบเจ็ค สถานี
 */
public class Station_objects {
    private String stations;
    private Double lat;
    private Double lng;
    private String type;
    private int price;
    private int extd;



    public Station_objects(String stations, Double lat, Double lng,String type,int price,int extd){
        this.stations = stations;
        this.lat = lat;
        this.lng = lng;
        this.type = type;
        this.price = price;
        this.extd = extd;
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
    public int getPrice() {
        return price;
    }
    public int getExtd() {
        return extd;
    }

}
