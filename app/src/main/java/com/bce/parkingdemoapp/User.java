package com.bce.parkingdemoapp;

public class User {

    String username;
    String vehicle;
    String phone;

    public User(){
    }

    public User(String username, String vehicle, String phone) {
        this.username = username;
        this.vehicle = vehicle;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
