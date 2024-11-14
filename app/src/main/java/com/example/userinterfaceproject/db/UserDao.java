package com.example.userinterfaceproject.db;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.userinterfaceproject.entities.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    long addUser(User user);

    @Query("SELECT * FROM User WHERE id = :userId")
    User getUserById(int userId);

    @Update
    int updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM User")
    List<User> getAllUsers();

    @Query("SELECT * FROM User WHERE username = :username AND password = :password")
    User checkUserCredentials(String username, String password);

    @Query("SELECT role FROM User WHERE username = :username AND password = :password")
    String getUserRole(String username, String password);

    @Query("SELECT role FROM User WHERE username = :username")
    String getUserRoleByUsername(String username);

    // Get user by email
    @Query("SELECT * FROM User WHERE email = :email")
    User getUserByEmail(String email);
    @Query("SELECT * FROM User WHERE username = :username")
    User getUserByUsername(String username);
}
