package com.example.news.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.news.model.Article
import com.example.news.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
): ViewModel() {


    val pagingArticle = newsRepository.getBreakingNews().cachedIn(viewModelScope)

    val searchedArticle:MutableStateFlow<PagingData<Article>> = MutableStateFlow(PagingData.empty())

    val savedNews = newsRepository.getAllSavedArticle()

    private val _currentArticle =  mutableStateOf(Article())
    val currentArticle = _currentArticle

    fun getSearchedArticles(query: String){
        val searchedFlow = newsRepository.getSearchedNews(query).cachedIn(viewModelScope)
        viewModelScope.launch {
            searchedFlow.collectLatest { pagingData ->
                searchedArticle.value = pagingData
            }
        }
    }
    fun saveArticle(article: Article){
        viewModelScope.launch {
            newsRepository.saveArticle(article)
        }
    }
    fun removeArticle(article: Article){
        viewModelScope.launch {
            newsRepository.removeArticle(article)
        }
    }
    fun updateCurrentArticle(article: Article){
        _currentArticle.value = article
    }
}