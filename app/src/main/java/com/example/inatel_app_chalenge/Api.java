package com.example.inatel_app_chalenge;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    String BASE_URL = "https://app-challenge-api.herokuapp.com/";

    @GET("installers")
    Call<List<Installer>> getInstallers(@Query("plan") int planId);

    @GET("plans")
    Call<List<Plan>> getPlans(@Query("state") String state);
}