package com.delevery.metyassara.model;

public class UserModel {
    private String ImgeUrl,UserName,uid,phone;

    public UserModel(String imgeUrl, String userName, String uid, String phone) {
        ImgeUrl = imgeUrl;
        UserName = userName;
        this.uid = uid;
        this.phone = phone;
    }

    public String getImgeUrl() {
        return ImgeUrl;
    }

    public void setImgeUrl(String imgeUrl) {
        ImgeUrl = imgeUrl;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
