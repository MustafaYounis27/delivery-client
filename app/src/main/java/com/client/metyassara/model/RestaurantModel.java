package com.client.metyassara.model;

public class RestaurantModel {
    private double lat,longt;
    private float rate;
    private String restaurant_name,sub_title,image_url;

    public RestaurantModel(double lat, double longt, int rate, String restaurant_name, String sub_title, String image_url) {
        this.lat = lat;
        this.longt = longt;
        this.rate = rate;
        this.restaurant_name = restaurant_name;
        this.sub_title = sub_title;
        this.image_url = image_url;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongt() {
        return longt;
    }

    public void setLongt(double longt) {
        this.longt = longt;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public RestaurantModel() {
    }
}
