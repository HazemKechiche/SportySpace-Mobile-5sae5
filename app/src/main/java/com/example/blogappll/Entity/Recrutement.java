package com.example.blogappll.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity
public class Recrutement implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nomposte;
    public String localisation;
    public String salaire;
    public String description;

}
