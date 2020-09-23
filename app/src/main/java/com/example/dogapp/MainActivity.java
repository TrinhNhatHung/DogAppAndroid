package com.example.dogapp;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends FragmentActivity {
    private SearchView svDog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        svDog = findViewById(R.id.sv_dog);
        svDog.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ListFragment listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.listFragment);
//              listFragment.getFilter(newText);
                return false;
            }
        });
    }
}