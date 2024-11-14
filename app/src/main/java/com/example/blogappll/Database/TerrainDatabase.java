package com.example.blogappll.Database;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.blogappll.Dao.TerrainDao;
import com.example.blogappll.Entity.Terrain;

@Database(entities = {Terrain.class}, version = 1)
public  abstract class TerrainDatabase extends  RoomDatabase  {
    public abstract TerrainDao TerrainDao();
}
