package com.client.metyassara.model;

public class RequestOderModel {
    private String restaurant_name,addreasr,phone,describe_order;

    public RequestOderModel(String restaurant_name, String addreasr, String phone, String describe_order) {
        this.restaurant_name = restaurant_name;
        this.addreasr = addreasr;
        this.phone = phone;
        this.describe_order = describe_order;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getAddreasr() {
        return addreasr;
    }

    public void setAddreasr(String addreasr) {
        this.addreasr = addreasr;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescribe_order() {
        return describe_order;
    }

    public void setDescribe_order(String describe_order) {
        this.describe_order = describe_order;
    }
}
