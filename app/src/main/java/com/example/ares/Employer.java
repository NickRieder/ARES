package com.example.ares;

import java.util.ArrayList;

public class Employer {
    //functional requirements
    private int id;
    private String name;
    private String username;
    private String password;
    private String status;

    //non-functional requirements:
    private ArrayList<Integer> employeeList;

    public Employer(){
        id = 0;
        name = "";
        username = "";
        password = "";
        status = "N/A";
        employeeList = null;
    }

    public int getId(){
        return id;
    }
    public void setId(int newId){
        id = newId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String newUsername){
        username = newUsername;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String newPassword){
        password = newPassword;
    }
    public String getStatus(){
        return status;
    }
    public void setStatus(String newStatus){
        status = newStatus;
    }
    public String getCompanyName(){ return name; }
    public void setCompanyName(String newName){ name = newName;}

    public ArrayList<Integer> getEmployeeList(){
        return employeeList;
    }
    public void setEmployeeList(ArrayList<Integer> newList){
        employeeList = newList;
    }
}
