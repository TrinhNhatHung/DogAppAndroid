package com.example.dogapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dogapp.model.DogBreed;
import com.example.dogapp.view.MyAdapter;
import com.example.dogapp.viewmodel.DogBreedDao;
import com.example.dogapp.viewmodel.DogBreedDatabase;
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
    private MyViewModel model;
    private Toolbar toolbar;
    private SearchView svDog;
    private DogBreedDao dogBreedDao;
    private DogBreedDatabase dogBreedDatabase;
    private DogsApiService dogsApiService;
    private SwipeRefreshLayout srlLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvDogs = view.findViewById(R.id.rv_dogs);
        toolbar = view.findViewById(R.id.toolbar);
        svDog = toolbar.findViewById(R.id.sv_dog);
        srlLayout = view.findViewById(R.id.swipe_refresh);
        rvDogs.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mDogBreeds = new ArrayList<DogBreed>();

        model = new ViewModelProvider(this).get(MyViewModel.class);
        model.getArrayLiveData().observe(getViewLifecycleOwner(), new Observer<List<DogBreed>>() {
            @Override
            public void onChanged(List<DogBreed> dogBreeds) {
                myAdapter = new MyAdapter((ArrayList<DogBreed>) dogBreeds, getContext());
                rvDogs.setAdapter(myAdapter);
            }
        });

        dogsApiService = new DogsApiService();
        loadDataToUI();

        rvDogs.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                myAdapter.closeMenu();
            }
        });

        ItemTouchHelper.SimpleCallback touchHelperCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            private final ColorDrawable background = new ColorDrawable();

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                myAdapter.showMenu(viewHolder.getAdapterPosition());
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;

                if (dX > 0) {
                    background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX), itemView.getBottom());
                } else if (dX < 0) {
                    background.setBounds(itemView.getRight() + ((int) dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                } else {
                    background.setBounds(0, 0, 0, 0);
                }

                background.draw(c);
            }

        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallBack);
        itemTouchHelper.attachToRecyclerView(rvDogs);
        svDog.setQueryHint("Search");
        EditText edText = svDog.findViewById(androidx.appcompat.R.id.search_src_text);
        edText.setTextColor(Color.WHITE);
        edText.setHintTextColor(Color.WHITE);
        svDog.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.getFilter().filter(newText);
                return false;
            }
        });
        srlLayout.setColorSchemeColors(Color.GREEN,Color.BLUE,Color.RED);
        srlLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout();
            }
        });
    }

    public class AsyncLoadDataOffline extends AsyncTask<MyViewModel, MyViewModel, Void> {

        private List<DogBreed> list;

        @Override
        protected Void doInBackground(MyViewModel... myViewModels) {
            dogBreedDatabase = DogBreedDatabase.getInstance(getContext());
            dogBreedDao = dogBreedDatabase.dogBreedDao();
            list = dogBreedDao.getAllDogBreed();
            publishProgress(myViewModels);
            return null;
        }

        @Override
        protected void onProgressUpdate(MyViewModel... values) {
            super.onProgressUpdate(values);
            values[0].loadDogBreeds((ArrayList<DogBreed>) list);
        }
    }

    public class AsyncLoadData extends AsyncTask<MyViewModel, MyViewModel, Void> {
        private List<DogBreed> list;

        public AsyncLoadData(List<DogBreed> list) {
            this.list = list;
        }

        @Override
        protected Void doInBackground(MyViewModel... myViewModels) {
            dogBreedDatabase = DogBreedDatabase.getInstance(getContext());
            dogBreedDao = dogBreedDatabase.dogBreedDao();
            dogBreedDao.deleteAll();
            dogBreedDao.insertDogBreed((ArrayList<DogBreed>) this.list);
            publishProgress(myViewModels);
            return null;
        }

        @Override
        protected void onProgressUpdate(MyViewModel... values) {
            super.onProgressUpdate(values);
            values[0].loadDogBreeds((ArrayList<DogBreed>) list);
        }
    }

    public class AsyncRefreshData extends AsyncTask<MyViewModel,MyViewModel,Void>{
        private List<DogBreed> list;

        public AsyncRefreshData(List<DogBreed> list) {
            this.list = list;
        }

        @Override
        protected Void doInBackground(MyViewModel... myViewModels) {
            dogBreedDatabase = DogBreedDatabase.getInstance(getContext());
            dogBreedDao = dogBreedDatabase.dogBreedDao();
            dogBreedDao.deleteAll();
            dogBreedDao.insertDogBreed((ArrayList<DogBreed>) this.list);
            publishProgress(myViewModels);
            return null;
        }

        @Override
        protected void onProgressUpdate(MyViewModel... values) {
            super.onProgressUpdate(values);
            values[0].loadDogBreeds((ArrayList<DogBreed>) list);
            srlLayout.setRefreshing(false);
            Toast.makeText(getContext(),"Data updated",Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshLayout (){
        dogsApiService.getAllDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                    @Override
                    public void onSuccess(@NonNull List<DogBreed> list) {
                        new AsyncRefreshData(list).execute(model);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        srlLayout.setRefreshing(false);
                        Toast.makeText(getContext(),"No internet",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void loadDataToUI (){
        dogsApiService.getAllDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                    @Override
                    public void onSuccess(@NonNull List<DogBreed> list) {
                        new AsyncLoadData(list).execute(model);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        new AsyncLoadDataOffline().execute(model);
                    }
                });
    }
}