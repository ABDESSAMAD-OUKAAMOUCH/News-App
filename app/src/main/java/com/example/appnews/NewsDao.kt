package com.example.appnews

import androidx.room.*
import com.example.appnews.Models.ModelClass

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ModelClass>)

    @Query("SELECT * FROM articles")
    suspend fun getAllArticles(): List<ModelClass>

    @Query("DELETE FROM articles")
    suspend fun clearArticles()
    @Query("SELECT * FROM articles WHERE category = :category")
    suspend fun getArticlesByCategory(category: String): List<ModelClass>

    @Query("DELETE FROM articles WHERE category = :category")
    suspend fun deleteByCategory(category: String)

}
