package com.example.news.ui

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.news.ui.components.bottom_bar.MainBottomNavigation
import com.example.news.ui.components.bottom_bar.MainNavigation

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsApp(
    viewModel: NewsViewModel
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { MainBottomNavigation(navController = navController)},
    ) {
        MainNavigation(navHostController = navController, viewModel = viewModel )
    }
}
