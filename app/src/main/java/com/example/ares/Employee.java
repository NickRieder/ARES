package com.example.ares;

public class Employee {
    //Functional Requirements:
    private int id;
    private String username;
    private String password;

    //Non-Functional Requirements:
    private int employerId;

    public Employee(){
        id = 0;
        username = "";
        password = "";
        employerId = 0;
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
}
