package com.example.ares;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Vehicle {
    private int id;
    private String make;
    private String model;
    private int year;
    private String licensePlate;
    private List<Integer> pastRepairs;

    public Vehicle(){
        id = 0;
        make = "";
        model = "";
        year = 1000;
        licensePlate = "";
        pastRepairs = new ArrayList<Integer>();
    }

    public int getId(){return id;}
    public void setId(int newVal){
        id = newVal;
    }

    public List<Integer> getPastRepairs() {return pastRepairs;}
    public void setPastRepairs(List<Integer> newVal){
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

    public void setLP(String newVal) {
        licensePlate = newVal;
    }


}
