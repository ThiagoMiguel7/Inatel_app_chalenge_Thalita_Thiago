package com.example.inatel_app_chalenge;

import com.google.android.gms.maps.model.Marker;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Installer implements Serializable {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("rating")
    int rating;
    @SerializedName("price_per_km")
    int pricePerKm;
    @SerializedName("lat")
    double latitude;
    @SerializedName("lng")
    double longitude;
    transient Marker marker;

    public Installer(int id, String name, int rating, int pricePerKm, long latitude, long longitude) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.pricePerKm = pricePerKm;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
