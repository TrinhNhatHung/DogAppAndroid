package com.example.dogapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogapp.ListFragmentDirections;
import com.example.dogapp.R;
import com.example.dogapp.databinding.ItemDogBinding;
import com.example.dogapp.databinding.ItemMenuBinding;
import com.example.dogapp.model.DogBreed;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private static ArrayList<DogBreed> mDogBreed;
    private ArrayList<DogBreed> mDogBreedAll;
    ItemDogBinding itemDogBinding;
    ItemMenuBinding itemMenuBinding;
    private Context context;
    private final int SHOW_MENU = 1;
    private final int HIDE_MENU = 2;

    public MyAdapter(ArrayList<DogBreed> mDogBreed, Context context) {
        this.mDogBreed = mDogBreed;
        this.mDogBreedAll = new ArrayList<DogBreed>(mDogBreed);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == HIDE_MENU){
            itemDogBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_dog,parent,false);
            v = itemDogBinding.getRoot();
            return new  MyViewHolder(v);
        } else {
            itemMenuBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_menu,parent,false);
            v = itemMenuBinding.getRoot();
            return new MenuViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
         if (holder instanceof MyViewHolder){
             itemDogBinding.setDogBreed(mDogBreed.get(position));
             String url = mDogBreed.get(position).url;
             Picasso.with(context).load(url).into(((MyViewHolder) holder).imDog, new Callback() {
                 @Override
                 public void onSuccess() {
                     ((MyViewHolder) holder).progressBar.setVisibility(View.GONE);
                 }

                 @Override
                 public void onError() {

                 }
             });
         }

         if (holder instanceof MenuViewHolder){
             itemMenuBinding.setDogBreed(mDogBreed.get(position));
         }
    }


    @Override
    public int getItemViewType(int position) {
        if (mDogBreed.get(position).isShowMenu()){
            return SHOW_MENU;
        } else {
            return HIDE_MENU;
        }
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
                    if (dogBreed.name.toLowerCase().contains(charSequence.toString().toLowerCase())){
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private ImageView imDog;
        private ProgressBar progressBar;
        private TextView tvName;
        private TextView tvTem;
        public MyViewHolder(View v) {
            super(v);
            view = v;
            imDog = view.findViewById(R.id.img_dog);
            tvName = view.findViewById(R.id.name);
            tvTem = view.findViewById(R.id.temperament);
            progressBar = view.findViewById(R.id.pb_loading);
            view.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  NavDirections action = ListFragmentDirections.actionListFragmentToDetailFragment(mDogBreed.get(getAdapterPosition()));
                  Navigation.findNavController(view).navigate(action);
              }
            });
        }
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder{
        View view;
        public MenuViewHolder(View view){
            super(view);
            this.view = view;
            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDogBreed.get(getAdapterPosition()).setShowMenu(false);
                    notifyDataSetChanged();
                }
            });
        }
    }

    public void showMenu (int position){
        for (int i =0 ; i < mDogBreed.size();i++){
            mDogBreed.get(i).setShowMenu(false);
        }
        mDogBreed.get(position).setShowMenu(true);
        notifyDataSetChanged();
    }

    public boolean isMenuShown (){
        for (int i = 0; i < mDogBreed.size();i++){
            if (mDogBreed.get(i).isShowMenu()){
                return true;
            }
        }
        return false;
    }

    public void closeMenu(){
        for (int i = 0; i< mDogBreed.size();i++){
            mDogBreed.get(i).setShowMenu(false);
        }
        notifyDataSetChanged();
    }

//    public class  AsyncDownLoadImage extends AsyncTask<Void,Void, Bitmap> {
//        private MyViewHolder holder;
//        private String url;
//
//        public AsyncDownLoadImage(MyViewHolder holder,String url) {
//            this.holder = holder;
//            this.url = url;
//        }
//
//        @Override
//        protected Bitmap doInBackground(Void... voids) {
//            Bitmap bitmap = null;
//            try {
//                InputStream inputStream = new URL(url).openStream();
//                bitmap = BitmapFactory.decodeStream(inputStream);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return bitmap;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//            if (url.equals(holder.imDog.getTag())){
//                holder.imDog.setImageBitmap(bitmap);
//                holder.imDog.setTag(null);
//            }
//
//        }
//    }
}
