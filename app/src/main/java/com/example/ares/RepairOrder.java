package com.example.ares;

import com.google.firebase.Timestamp;

import java.lang.reflect.Array;
import java.util.Map;

public class RepairOrder {
    public int id;
    public int vehicleId;
    public int employeeId;
    public Timestamp date;
    public int hours;
    public String status;
    public String paperOrder;
    public String carPicture;
    public Map<String, Integer> repairs;
    public int number;

    public int getVehicleId() {
        return vehicleId;
    }

    public int getNumber() {
        return number;
    }

    public Map<String, Integer> getRepairs() {
        return repairs;
    }
}
