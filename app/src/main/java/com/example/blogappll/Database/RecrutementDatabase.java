package com.example.blogappll.Database;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.blogappll.Dao.RecrutementDao;
import com.example.blogappll.Entity.Recrutement;


@Database(entities = {Recrutement.class}, version = 1)
public abstract class RecrutementDatabase extends  RoomDatabase  {
    private static volatile RecrutementDatabase INSTANCE;

    public abstract RecrutementDao recrutementDao();
}