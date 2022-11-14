package com.example.ares;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class Vehicle {
    private int id;
    private String make;
    private String model;
    private int year;
    private String licensePlate;
    private Map<String, Integer> pastRepairs;

    public Vehicle(){
        id = 0;
        make = "";
        model = "";
        year = 1000;
        licensePlate = "";
        pastRepairs = new HashMap<>();
    }

    public int getId(){return id;}
    public void setId(int newVal){
        id = newVal;
    }

    public Map<String, Integer> getPastRepairs() {return pastRepairs;}
    public void setPastRepairs(Map<String, Integer> newVal){
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

    public String getLicensePlate(){return licensePlate;}

    public void setLicensePlate(String newVal) {
        licensePlate = newVal;
    }


}
