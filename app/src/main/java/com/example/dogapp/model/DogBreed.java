package com.example.dogapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class DogBreed implements Parcelable, Serializable {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo
     public final int dogId;

    @ColumnInfo
    @SerializedName("name")
     public final String name;

    @ColumnInfo
    @SerializedName("life_span")
     public final String lifeSpan;

    @ColumnInfo
    @SerializedName("origin")
     public final String origin;

    @ColumnInfo
    @SerializedName("url")
     public final String url;

    @ColumnInfo
    @SerializedName("temperament")
     public final String temperament;

    private boolean showMenu = false;

    public DogBreed(int dogId, String name, String lifeSpan, String origin, String url, String temperament) {
        this.dogId = dogId;
        this.name = name;
        this.lifeSpan = lifeSpan;
        this.origin = origin;
        this.url = url;
        this.temperament = temperament;
    }

    protected DogBreed(Parcel in) {
        dogId = in.readInt();
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
        parcel.writeInt(dogId);
        parcel.writeString(name);
        parcel.writeString(lifeSpan);
        parcel.writeString(origin);
        parcel.writeString(url);
        parcel.writeString(temperament);
    }
}
