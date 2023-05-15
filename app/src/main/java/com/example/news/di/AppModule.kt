package com.example.news.di

import android.app.Application
import androidx.room.Room
import com.example.news.api.NewsAPI
import com.example.news.db.ArticleDatabase
import com.example.news.repository.NewsRepository
import com.example.news.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun providesArticlesDatabase(app: Application): ArticleDatabase{
        return Room.databaseBuilder(
            app,
            ArticleDatabase::class.java,
            ArticleDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideAPIService(): NewsAPI{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(NewsAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(newsAPI: NewsAPI,articleDatabase: ArticleDatabase): NewsRepository{
        return NewsRepository(dao = articleDatabase.articleDao(), newsAPI = newsAPI);
    }
}