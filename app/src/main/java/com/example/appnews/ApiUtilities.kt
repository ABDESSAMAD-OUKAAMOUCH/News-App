package com.example.appnews

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiUtilities {
    companion object{
        var retrofit:Retrofit? =null
        fun getApiInterface(): ApiInterface {
            if(retrofit==null){
                retrofit=Retrofit.Builder()
                    .baseUrl("https://newsapi.org/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(ApiInterface::class.java)
        }
    }
}