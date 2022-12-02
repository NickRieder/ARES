package com.example.ares;

public class Employee {
    //Functional Requirements:
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private int employerId;

    public Employee(){
        id = 0;
        employerId = 0;
        firstName = "";
        lastName = "";
        username = "";
        password = "";
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
    public String getFirstName(){  return firstName;  }
    public void setFirstName(String name){ firstName = name; }
    public String getLastName(){  return lastName;  }
    public void setLastName(String name){ lastName = name; }
    public int getEmployerId() {return employerId;}
    public void setEmployerId(int var){ employerId = var;}
}
