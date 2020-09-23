package com.example.dogapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dogapp.model.DogBreed;
import com.example.dogapp.view.MyAdapter;
import com.example.dogapp.viewmodel.DogsApiService;
import com.example.dogapp.viewmodel.MyViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ListFragment extends Fragment {
    private RecyclerView rvDogs;
    private ArrayList<DogBreed> mDogBreeds;
    private MyAdapter myAdapter;
    private DogsApiService apiService;
    private MyViewModel model;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvDogs = view.findViewById(R.id.rv_dogs);
        rvDogs.setLayoutManager(new GridLayoutManager(getContext(),2));
        mDogBreeds = new ArrayList<DogBreed>();
        apiService = new DogsApiService();
        apiService.getAllDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                    @Override
                    public void onSuccess(@NonNull List<DogBreed> list) {
                        mDogBreeds = (ArrayList<DogBreed>) list;
                        model = new ViewModelProvider(ListFragment.this).get(MyViewModel.class);
                        model.loadDogBreeds(mDogBreeds);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
        model = new ViewModelProvider(this).get(MyViewModel.class);
        model.getArrayLiveData().observe(getViewLifecycleOwner(), new Observer<List<DogBreed>>() {
            @Override
            public void onChanged(List<DogBreed> dogBreeds) {
                myAdapter = new MyAdapter((ArrayList<DogBreed>) dogBreeds);
                rvDogs.setAdapter(myAdapter);
            }
        });

    }

       public void getFilter (String s){
        myAdapter.getFilter().filter(s);
    }


}