package com.example.ares;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RepairOrder {
    private int id;
    private int vehicleId;
    private int employeeId;
    private Date date;
    private int hours;
    private String status;
    private String paperOrder;
    private String carPicture;
    private Map<String, Integer> repairs;
    private int number;

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
        repairs = new HashMap<String, Integer>();
    }


    public int getId(){return id;}
    public void setId(int newVal){
        id = newVal;
    }

    public Date getDate(){return date;}
    public void setDate(Date newVal){
        date = newVal;
    }

    public int getHours(){return hours;}
    public void setHours(int newVal){
        hours = newVal;
    }

    public String getStatus(){return status;}
    public void setStatus(String newVal){
        status = newVal;
    }


    public void setNumber(int newVal){
        number = newVal;
    }

    public String getCarPicture(){return carPicture;}
    public void setCarPicture(String newVal){
        carPicture = newVal;
    }

    public String getPaperOrder(){return paperOrder;}
    public void setPaperOrder(String newVal){
        paperOrder = newVal;
    }

    public void setVehicleId(int newVal){vehicleId = newVal;}
    public int getVehicleId() {
        return vehicleId;
    }

    public int getNumber() {
        return number;
    }

    public int getEmployeeId(){
        return employeeId;
    }

    public void setEmployeeId(int newVal){employeeId = newVal;}

    public Map<String, Integer> getRepairs() {
        return repairs;
    }
    public void setRepairs(Map<String, Integer> newVal) {
        repairs = newVal;
    }
}
