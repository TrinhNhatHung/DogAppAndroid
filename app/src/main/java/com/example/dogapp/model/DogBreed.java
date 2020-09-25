package com.example.dogapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class DogBreed implements Parcelable {

    @SerializedName("id")
     public final int id;
    @SerializedName("name")
     public final String name;
    @SerializedName("life_span")
     public final String lifeSpan;
    @SerializedName("origin")
     public final String origin;
    @SerializedName("url")
     public final String url;
    @SerializedName("temperament")
     public final String temperament;

    private boolean showMenu = false;

    public DogBreed(int id, String name, String lifeSpan, String origin, String url, String temperament) {
        this.id = id;
        this.name = name;
        this.lifeSpan = lifeSpan;
        this.origin = origin;
        this.url = url;
        this.temperament = temperament;
    }

    protected DogBreed(Parcel in) {
        id = in.readInt();
        name = in.readString();
        lifeSpan = in.readString();
        origin = in.readString();
        url = in.readString();
        temperament = in.readString();
    }

    public boolean isShowMenu() {
        return showMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    public static final Creator<DogBreed> CREATOR = new Creator<DogBreed>() {
        @Override
        public DogBreed createFromParcel(Parcel in) {
            return new DogBreed(in);
        }

        @Override
        public DogBreed[] newArray(int size) {
            return new DogBreed[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(lifeSpan);
        parcel.writeString(origin);
        parcel.writeString(url);
        parcel.writeString(temperament);
    }
}
