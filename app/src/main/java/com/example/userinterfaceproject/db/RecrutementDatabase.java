package com.example.userinterfaceproject.db;import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.userinterfaceproject.entities.Recrutement;


@Database(entities = {Recrutement.class}, version = 1)
public abstract class RecrutementDatabase extends  RoomDatabase  {
    private static volatile RecrutementDatabase INSTANCE;

    public abstract RecrutementDao recrutementDao();
}