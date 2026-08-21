package com.wael.riayaapp.data.local.entity;
import androidx.room.*;
@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private boolean isVerified;


    //constructor
    public User(String fullName,String email,String phone,String password){
        this.fullName=fullName;
        this.email=email;
        this.phone=phone;
        this.password=password;
    }
   //Getters and Setters
    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
