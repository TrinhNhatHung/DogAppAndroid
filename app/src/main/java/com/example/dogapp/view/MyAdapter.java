package com.example.dogapp.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogapp.ListFragmentDirections;
import com.example.dogapp.R;
import com.example.dogapp.model.DogBreed;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {
    private static ArrayList<DogBreed> mDogBreed;
    private ArrayList<DogBreed> mDogBreedAll;

    public MyAdapter(ArrayList<DogBreed> mDogBreed) {
        this.mDogBreed = mDogBreed;
        this.mDogBreedAll = new ArrayList<DogBreed>(mDogBreed);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dog, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
         holder.tvName.setText(mDogBreed.get(position).getName());
         holder.tvTemperament.setText(mDogBreed.get(position).getTemperament());
         String url = mDogBreed.get(position).getUrl();
         holder.imDog.setTag(url);
         new AsyncDownLoadImage(holder,url).execute(mDogBreed.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
        return mDogBreed.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<DogBreed> filtered = new ArrayList<DogBreed>();
            if (charSequence.toString().isEmpty()){
                filtered.addAll(mDogBreedAll);
            } else {
                for (DogBreed dogBreed : mDogBreedAll ){
                    if (dogBreed.getName().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filtered.add(dogBreed);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filtered;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mDogBreed.clear();
            mDogBreed.addAll((Collection<? extends DogBreed>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView tvName;
        private TextView tvTemperament;
        private ImageView imDog;
        public MyViewHolder(View v) {
            super(v);
            view = v;
            tvName = view.findViewById(R.id.name);
            tvTemperament = view.findViewById(R.id.tv_temperament);
            imDog = view.findViewById(R.id.img_dog);
            view.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  NavDirections action = ListFragmentDirections.actionListFragmentToDetailFragment(mDogBreed.get(getAdapterPosition()));
                  Navigation.findNavController(view).navigate(action);
              }
            });
        }
    }

    public class  AsyncDownLoadImage extends AsyncTask<String,Void,Bitmap>{
        private MyViewHolder holder;
        private String url;

        public AsyncDownLoadImage(MyViewHolder holder,String url) {
            this.holder = holder;
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (url.equals(holder.imDog.getTag())){
                holder.imDog.setImageBitmap(bitmap);
                holder.imDog.setTag(null);
            }

        }
    }
}
