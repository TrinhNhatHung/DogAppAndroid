package com.example.dogapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dogapp.model.DogBreed;

import java.util.ArrayList;
import java.util.List;

public class MyViewModel extends ViewModel {
    private MutableLiveData<List<DogBreed>> liveData;
    public LiveData<List<DogBreed>> getArrayLiveData() {
        if (liveData == null) {
            liveData = new MutableLiveData<List<DogBreed>>();
        }
        return liveData;
    }

    public void loadDogBreeds(ArrayList<DogBreed> dogBreeds) {
        liveData.setValue(dogBreeds);
    }
}
