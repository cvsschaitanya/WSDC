package com.example.homeactivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    String BASE_URL = "https://api.myjson.com/bins/";

    @GET("nd11z")
    Call<List<Student>> getStudents();



}
//https://api.myjson.com/bins/nd11z