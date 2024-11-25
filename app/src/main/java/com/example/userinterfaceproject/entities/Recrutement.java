package com.example.userinterfaceproject.entities;

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

    // Getters and Setters for each field
    public String getNomposte() {
        return nomposte;
    }

    public void setNomposte(String nomposte) {
        this.nomposte = nomposte;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSalaire() {
        return salaire;
    }

    public void setSalaire(String salaire) {
        this.salaire = salaire;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

}
