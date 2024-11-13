package com.example.blogappll.Dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.blogappll.Entity.Recrutement;

import java.util.List;
@Dao
public interface RecrutementDao {
    @Insert
    void insert(Recrutement recrutement);

    @Update
    void update(Recrutement recrutement);

    @Delete
    void delete(Recrutement recrutement);

    @Query("SELECT * FROM Recrutement")
    List<Recrutement> getAllRecrutements();

    @Query("SELECT * FROM Recrutement WHERE id = :id LIMIT 1")
    Recrutement getRecrutementById(int id);

}
