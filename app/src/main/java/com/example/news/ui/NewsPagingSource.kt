package com.example.news.ui

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.news.api.NewsAPI
import com.example.news.model.Article
import com.example.news.repository.NewsRepository
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.lang.Exception

class NewsPagingSource(
    private val newsAPI: NewsAPI
): PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        // We will refresh data when there's no more data to load from the previous page
        // To do this, we get the last loaded page, and increment it to get the next page to load
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)?.prevKey ?: 1
            anchorPage + 1
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try{
            delay(2000)
            val nextPage = params.key?: 1

            val response = newsAPI.getBreaking(country = "in", pageNumber = nextPage)

            val articles = response.body()!!.articles

            return LoadResult.Page(
                data = articles,
                prevKey = if(nextPage == 1) null else nextPage-1,
                nextKey = if(articles.isEmpty()) null else nextPage +1
            )
        }
        catch (e: Exception){
            return LoadResult.Error(e)
        }
        catch (e:HttpException){
            return LoadResult.Error(e)
        }
    }
}