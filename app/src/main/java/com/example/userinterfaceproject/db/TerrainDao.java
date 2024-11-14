package com.example.userinterfaceproject.db;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.userinterfaceproject.entities.Terrain;

import java.util.List;
@Dao
public interface TerrainDao {

    @Insert
    void insert(Terrain Terrain);

    @Update
    void update(Terrain Terrain);

    @Delete
    void delete(Terrain Terrain);

    @Query("SELECT * FROM Terrain")
    List<Terrain> getAllTerrains();

    @Query("SELECT * FROM Terrain WHERE id = :id LIMIT 1")
    Terrain getTerrainById(int id);

}
