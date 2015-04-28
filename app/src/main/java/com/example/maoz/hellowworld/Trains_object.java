package com.example.maoz.hellowworld;

/**
 * อ็อบเจ็คข้อมูลรถไฟฟ้า
 */
public class Trains_object {
    private int id;
    private String name;
    private String coupon;
    private String min;
    private String max;
    private String service;
    private String frequency;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCoupon() {
        return coupon;
    }

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }

    public String getService() {
        return service;
    }

    public String getFrequency() {
        return frequency;
    }

    public Trains_object(int id, String name, String coupon, String min, String max, String service, String frequency) {

        this.id = id;
        this.name = name;
        this.coupon = coupon;
        this.min = min;
        this.max = max;
        this.service = service;
        this.frequency = frequency;
    }
}
