package com.example.ares;

public class Employer {
    //functional requirements
    private int id;
    private String username;
    private String password;
    private String status;

    //non-functional requirements:
    private int[] employeeList;

    public Employer(){
        id = 0;
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
}
