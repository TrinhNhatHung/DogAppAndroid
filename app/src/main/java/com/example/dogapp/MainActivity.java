package com.example.dogapp;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.example.dogapp.viewmodel.DogBreedDao;
import com.example.dogapp.viewmodel.DogBreedDatabase;


public class MainActivity extends FragmentActivity {
    private DogBreedDatabase dogBreedDatabase;
    private DogBreedDao dogBreedDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}