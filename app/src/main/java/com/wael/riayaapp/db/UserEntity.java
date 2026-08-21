package com.wael.riayaApp.DB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String emailOrPhone;
    private String passwordHash; // يتم حفظها مشفرة فقط
    private String token;        // توكن الجلسة (Auth Token)
    private boolean isRememberMe; // لحفظ حالة خيار "تذكرني"

    // Constructor
    public UserEntity(String emailOrPhone, String passwordHash, String token, boolean isRememberMe) {
        this.emailOrPhone = emailOrPhone;
        this.passwordHash = passwordHash;
        this.token = token;
        this.isRememberMe = isRememberMe;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmailOrPhone() { return emailOrPhone; }
    public void setEmailOrPhone(String emailOrPhone) { this.emailOrPhone = emailOrPhone; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public boolean isRememberMe() { return isRememberMe; }
    public void setRememberMe(boolean rememberMe) { isRememberMe = rememberMe; }
}