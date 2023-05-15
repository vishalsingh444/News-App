package com.example.news.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "articles"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String? = "unknown",
    val content: String? = "unknown",
    val description: String? = "unknown",
    val publishedAt: String? = "unknown",
    val source: Source? = Source(id = "unknown",name = "unknown"),
    val title: String  = "unknown",
    val url: String? = "unknown",
    val urlToImage: String? = "unknown"
)