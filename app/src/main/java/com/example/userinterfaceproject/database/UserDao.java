package com.example.userinterfaceproject.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.userinterfaceproject.models.User;

@Dao
public interface UserDao {

    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM user WHERE username = :username LIMIT 1")
    User getUserByUsername(String username);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM user WHERE idUser = :id")
    void deleteUserById(int id);
}
