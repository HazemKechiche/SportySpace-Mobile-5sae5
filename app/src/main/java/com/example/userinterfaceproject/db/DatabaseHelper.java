package com.example.userinterfaceproject.db;

// DatabaseHelper.java

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.userinterfaceproject.entities.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Créer la table User
        String createTable = "CREATE TABLE User (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "email TEXT," +
                "password TEXT," +
                "role TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS User");
        onCreate(db);
    }

    // Méthode pour ajouter un utilisateur
    public boolean addUser(String username, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);
        values.put("password", password);  // Ensure that the password is hashed before inserting
        values.put("role", role);

        long result = db.insert("User", null, values);
        db.close();

        // Return true if insertion is successful, otherwise false
        return result != -1;
    }

    // Méthode pour obtenir un utilisateur par son identifiant
    public Cursor getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("User", null, "id = ?", new String[]{String.valueOf(userId)}, null, null, null);
    }

    // Méthode pour mettre à jour un utilisateur
    public int updateUser(int userId, String username, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);
        values.put("password", password);  // Assurez-vous de hasher le mot de passe
        values.put("role", role);

        return db.update("User", values, "id = ?", new String[]{String.valueOf(userId)});
    }

    // Méthode pour supprimer un utilisateur
    public void deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("User", "id = ?", new String[]{String.valueOf(userId)});
        db.close();
    }
    // Add this method in DatabaseHelper.java

    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to check if the username and password match any user in the database
        Cursor cursor = db.query("User", new String[]{"id", "username", "password", "role"},
                "username = ? AND password = ?", new String[]{username, password},
                null, null, null);

        // Check if any result is returned
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true; // User found
        }

        cursor.close();
        return false; // User not found
    }
    public void updateUserPassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("password", newPassword); // Assuming "password" is the column name

        int result = db.update("User", contentValues, "email = ?", new String[]{email}); // Assuming "users" is the table name
        if (result == -1) {
            // Handle failure
            Log.e("Database", "Password update failed");
        } else {
            // Handle success
            Log.d("Database", "Password updated successfully");
        }

        db.close();
    }
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("User", null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));


                users.add(new User(id, username, email, role));
            }
            cursor.close();
        }
        db.close();
        return users;
    }

    // Delete user by ID
    public void deleteUserById(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("User", "id = ?", new String[]{String.valueOf(userId)});
        db.close();
    }
    public String getUserRole(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("User", new String[]{"role"},
                "username = ? AND password = ?", new String[]{username, password},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
            cursor.close();
            return role; // Return the user's role
        }

        if (cursor != null) {
            cursor.close();
        }
        return null; // Return null if no user found
    }
    public String getUserRoleByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("User", new String[]{"role"},
                "username = ?", new String[]{username}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
            cursor.close();
            return role;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null; // Return null if no role found
    }



}
