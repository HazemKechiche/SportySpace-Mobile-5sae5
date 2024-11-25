package com.example.userinterfaceproject.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

// User.java
@Entity(tableName = "User")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String email;
    private String password;
    private String role;


    public User(int id,String username, String email, String role) {
        this.username = username;
        this.email = email;
        this.id = id;
        this.role = role;
    }
    @Ignore
    public User(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }
}
