package com.example.news.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.news.model.Article
import kotlinx.coroutines.flow.Flow


@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertArticle(article: Article)

    @Query("SELECT * FROM articles")
    fun getAllArticle(): Flow<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}