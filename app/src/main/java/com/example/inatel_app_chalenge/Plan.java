package com.example.inatel_app_chalenge;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Plan implements Serializable {

    @SerializedName("id")
    int id;
    @SerializedName("isp")
    String isp;
    @SerializedName("data_capacity")
    double data_capacity;
    @SerializedName("download_speed")
    double download_speed;
    @SerializedName("upload_speed")
    double upload_speed;
    @SerializedName("description")
    String description;
    @SerializedName("price_per_month")
    double price_per_month;
    @SerializedName("type_of_internet")
    String type_of_internet;

    public Plan(int id, String isp, int data_capacity, int download_speed, int upload_speed, String description, double price_per_month, String type_of_internet){
        this.id = id;
        this.isp = isp;
        this.data_capacity = data_capacity;
        this.download_speed = download_speed;
        this.upload_speed = upload_speed;
        this.description = description;
        this.price_per_month = price_per_month;
        this.type_of_internet = type_of_internet;
    }
}
