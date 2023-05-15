package com.example.news.repository

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.news.api.NewsAPI
import com.example.news.db.ArticleDao
import com.example.news.model.Article
import com.example.news.ui.NewsPagingSource
import com.example.news.ui.SearchPagingSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class NewsRepository(
    private val newsAPI: NewsAPI,
    private val dao: ArticleDao
) {
    fun getBreakingNews(): Flow<PagingData<Article>>{
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 10,
            ),
            pagingSourceFactory = {
                NewsPagingSource(newsAPI)
            }
        ).flow
    }
    fun getSearchedNews(query: String): Flow<PagingData<Article>>{
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 10,
            ),
            pagingSourceFactory = {
                SearchPagingSource(newsAPI = newsAPI,query)
            }
        ).flow
    }
    suspend fun saveArticle(article: Article){
        return dao.upsertArticle(article)
    }
    suspend fun removeArticle(article: Article){
        return dao.deleteArticle(article)
    }
    fun getAllSavedArticle():Flow<List<Article>>{
        return dao.getAllArticle()
    }

}