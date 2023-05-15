package com.example.news.ui.components.bottom_bar

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.news.ui.DetailsScreen
import com.example.news.ui.NewsHomeScreen
import com.example.news.ui.NewsViewModel
import com.example.news.ui.SavedScreen
import com.example.news.ui.SearchScreen

@Composable
fun MainNavigation(navHostController: NavHostController, viewModel: NewsViewModel) {
    NavHost(
        navController = navHostController,
        startDestination = BottomNavItem.Home.route,
    ) {
        composable(BottomNavItem.Home.route) {
            NewsHomeScreen(viewModel = viewModel, navController = navHostController)
        }
        composable(BottomNavItem.Search.route) {
            SearchScreen(viewModel = viewModel,navHostController)
        }
        composable(BottomNavItem.Saved.route) {
            SavedScreen(viewModel = viewModel,navHostController)
        }
        composable("detailScreen"){
            DetailsScreen(article = viewModel.currentArticle.value,navHostController)
        }
    }
}

@Composable
fun MainBottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Saved
    )
    BottomNavigation (backgroundColor = Color.Black){
        val navStack by navController.currentBackStackEntryAsState()
        val currentRoute = navStack?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route){
                        navController.graph.startDestinationRoute?.let{
                            popUpTo(item.route)
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label
                    )
                },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Gray
            )
        }
    }
}
