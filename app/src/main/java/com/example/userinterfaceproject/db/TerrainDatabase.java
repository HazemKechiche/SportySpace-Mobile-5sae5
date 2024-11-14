package com.example.userinterfaceproject.db;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.userinterfaceproject.entities.Terrain;

@Database(entities = {Terrain.class}, version = 1)
public  abstract class TerrainDatabase extends  RoomDatabase  {
    public abstract TerrainDao TerrainDao();
}
