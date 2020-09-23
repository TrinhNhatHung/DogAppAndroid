package com.example.dogapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class DogBreed implements Parcelable {

    @SerializedName("id")
     private int id;
    @SerializedName("name")
     private String name;
    @SerializedName("life_span")
     private String lifeSpan;
    @SerializedName("origin")
     private String origin;
     @SerializedName("url")
     private String url;
     @SerializedName("temperament")
     private String temperament;
    public DogBreed() {
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTemperament() {
        return temperament;
    }

    public void setTemperament(String temperament) {
        this.temperament = temperament;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLifeSpan() {
        return lifeSpan;
    }

    public void setLifeSpan(String lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

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
