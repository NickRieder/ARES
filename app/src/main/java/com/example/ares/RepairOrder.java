package com.example.ares;

import com.google.firebase.Timestamp;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.Map;

public class RepairOrder {
    public int id;
    public int vehicleId;
    public int employeeId;
    public Date date;
    public int hours;
    public String status;
    public String paperOrder;
    public String carPicture;
    public Map<String, Integer> repairs;
    public int number;

    public RepairOrder(){
        id = 0;
        vehicleId = 0;
        employeeId = 0;
        date = new Date();
        hours = 0;
        status = "new";
        paperOrder= " ";
        carPicture= " ";
        number = 0;
        repairs.clear();
    }

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
