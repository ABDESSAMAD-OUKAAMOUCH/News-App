package com.example.appnews.Models

data class MainNews(
    var status:String,
    var totalResults:String,
    var articles:ArrayList<ModelClass>
)