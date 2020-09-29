package com.example.dogapp.viewmodel;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.dogapp.model.DogBreed;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DogBreedDao {
        @Query("SELECT * FROM DogBreed")
        public List<DogBreed> getAllDogBreed();

        @Insert
        public void insertDogBreed(ArrayList<DogBreed> dogBreeds);

        @Query("DELETE FROM DogBreed")
        public void deleteAll();
}
