package com.example.news.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.news.R
import com.example.news.model.Article

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewsHomeScreen(
    viewModel: NewsViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = {
                    Text(text = "Trending", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            )
        }
    ) {
        SuccessScreen(viewModel = viewModel, navController = navController )
    }
}

@Composable
fun NewsCardItem(
    article: Article,
    onIconClick: () -> Unit,
    onArticle: () -> Unit,
    isBookmarked: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp),
        verticalArrangement = Arrangement.Bottom,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(article.urlToImage)
                .crossfade(true)
                .build(),
            contentDescription = null,
            Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(8.dp))
                .clickable { onArticle() },
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.loading_img)
        )
        if (article.urlToImage != null) {
            Spacer(Modifier.height(8.dp))
        }
        Text(
            text = article.title,
            fontSize = 20.sp,
            modifier = Modifier.clickable { onArticle() }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            var source = "Unknown"

            if(article.source!=null){
                source = article.source!!.name
            }
            Text(text = source, fontSize = 12.sp,)
            IconButton(onClick = onIconClick) {
                Icon(
                    painter = if (isBookmarked) painterResource(id = R.drawable.baseline_bookmark_24) else painterResource(
                        id = R.drawable.outline_bookmark_border_24
                    ),
                    contentDescription = if (isBookmarked) "Bookmarked" else "not Bookmarked",
                    tint = Color.Gray
                )
            }
        }
        
    }

}

@Composable
fun SuccessScreen(
    viewModel: NewsViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val pagingData = viewModel.pagingArticle.collectAsLazyPagingItems()
    LazyColumn(
        modifier = modifier
            .padding(top = 60.dp, bottom = 55.dp)
            .background(Color.Black),
    ) {

        when (pagingData.loadState.refresh) {
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
                items(pagingData) {
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
                                .clickable { pagingData.refresh() }
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
        when (pagingData.loadState.append) {
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
                    AppendError(retry = { pagingData.retry() })
                }
            }

            else -> Unit
        }
        when (pagingData.loadState.prepend) {
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
                        pagingData.retry()
                    }
                }
            }

            else -> Unit
        }
    }
}


@Composable
fun AppendError(retry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Warning, contentDescription = null)
            Text(
                text = "Loading Error!",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Box(Modifier.fillMaxWidth()) {
                Text(
                    text = "Retry",
                    modifier = Modifier
                        .clickable { retry.invoke() }
                        .align(Alignment.CenterEnd),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
