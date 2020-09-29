package com.example.dogapp.viewmodel;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.dogapp.model.DogBreed;

@Database(entities = DogBreed.class, version = 1)
public abstract class DogBreedDatabase extends RoomDatabase {
    public abstract DogBreedDao dogBreedDao();

    public static DogBreedDatabase instance;

    public static DogBreedDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, DogBreedDatabase.class,"dogbreed").build();
        }
        return instance;
    }
}
