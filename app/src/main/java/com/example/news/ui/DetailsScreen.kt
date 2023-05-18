package com.example.news.ui

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.news.R
import com.example.news.model.Article
import com.example.news.ui.components.browser_button.OpenInAppBrowserButton

@SuppressLint("SuspiciousIndentation")
@Composable
fun DetailsScreen(
    article: Article,
    navController: NavController
) {
    BackHandler {
        navController.popBackStack()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 55.dp)
            .verticalScroll(rememberScrollState(0))
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current).data(article.urlToImage)
                .crossfade(true).build(),
            contentDescription = null,
            Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.loading_img)
        )
        Spacer(Modifier.height(16.dp))
        Text(text = article.title, fontSize = 20.sp,fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        if(article.content!=null)
        Text(text = article.content, style = MaterialTheme.typography.bodyMedium)
        OpenInAppBrowserButton(url = article.url?: "")
    }
}