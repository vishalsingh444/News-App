package com.example.news.ui

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.news.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: NewsViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }

    val searchArticles = viewModel.searchedArticle.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = query,
            onValueChange = { query = it },
            placeholder = {  Text(text = "Search") },
            keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    viewModel.getSearchedArticles(query)
                }
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(50.dp)),
            trailingIcon = {
                IconButton(onClick = { viewModel.getSearchedArticles(query) }) {
                    Icon(painter = painterResource(id = R.drawable.baseline_search_24), contentDescription = "search" )
                }
            },

        )
        LazyColumn(
            modifier = Modifier
                .padding(bottom = 55.dp)
                .weight(1f),
        ) {
            when (searchArticles.loadState.refresh) {
                is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize()
                        ) {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }
                    }
                }

                is LoadState.NotLoading -> {
                    items(searchArticles) {
                        it?.let { article ->
                            NewsCardItem(
                                article = article,
                                onIconClick = { viewModel.saveArticle(article) },
                                isBookmarked = true,
                                onArticle = {
                                    viewModel.updateCurrentArticle(article)
                                    navController.navigate("detailScreen")
                                }
                            )
                        }
                        Divider(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                        )
                    }
                }

                is LoadState.Error -> {
                    item {
                        Box(modifier = Modifier.fillParentMaxSize()) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh",
                                modifier = Modifier
                                    .clickable { searchArticles.refresh() }
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            }
            when (searchArticles.loadState.append) {
                LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }
                    }
                }

                is LoadState.Error -> {
                    item {
                        AppendError(retry = { searchArticles.retry() })
                    }
                }

                else -> Unit
            }
            when (searchArticles.loadState.prepend) {
                LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize()
                        ) {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }
                    }
                }

                is LoadState.Error -> {
                    item {
                        AppendError {
                            searchArticles.retry()
                        }
                    }
                }

                else -> Unit
            }
        }


    }
}
