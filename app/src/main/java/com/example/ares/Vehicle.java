package com.example.ares;

public class Vehicle {
    private int id;
    private String make;
    private String model;
    private int year;
    private String licensePlate;
    private int[] pastRepairs;

    public int getId(){return id;}
    public void setId(int newVal){
        id = newVal;
    }

    public int[] getPastRepairs(){return pastRepairs;}
    public void setPastRepairs(int[] newVal){
        pastRepairs = newVal;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int newVal){
        year = newVal;
    }

    public String getMake() {
        return make;
    }
    public void setMake(String newVal) {
        make = newVal;
    }

    public String getModel() {
        return model;
    }
    public void setModel(String newVal){
        model = newVal;
    }

    public String getLP(){return licensePlate;}
    public void setLP(String newVal){
        licensePlate = newVal;
    }
}
