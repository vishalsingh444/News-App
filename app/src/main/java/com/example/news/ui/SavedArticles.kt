@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.news.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SavedScreen(
    viewModel: NewsViewModel,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Saved", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            )
        }
    ) {
        val savedArticleState by viewModel.savedNews.collectAsState(initial = emptyList())
        LazyColumn(
            modifier = Modifier.padding(top = 60.dp, bottom = 55.dp),
        ) {
            items(savedArticleState) { article ->
                NewsCardItem(
                    article = article,
                    onIconClick = { viewModel.removeArticle(article) },
                    isBookmarked = false,
                    onArticle = {
                        viewModel.updateCurrentArticle(article)
                        navController.navigate("detailScreen")
                    }
                )
            }
        }
    }

}