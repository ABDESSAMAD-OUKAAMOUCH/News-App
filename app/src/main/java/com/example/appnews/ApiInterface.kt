package com.example.appnews

import com.example.appnews.Models.MainNews
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("top-headlines")
    fun getNews(
        @Query("country") country:String,
        @Query("pageSize") pageSize:Int,
        @Query("apiKey") apiKey:String,
    ):Call<MainNews>

    @GET("top-headlines")
    fun getCategoryNews(
        @Query("country") country:String,
        @Query("category") category:String,
        @Query("pageSize") pageSize:Int,
        @Query("apiKey") apiKey:String,
    ):Call<MainNews>
}