package com.example.news.ui.components.bottom_bar

import androidx.annotation.DrawableRes
import com.example.news.R

sealed class BottomNavItem(@DrawableRes val icon: Int,val label: String, val route: String){
    object Home: BottomNavItem(icon = R.drawable.home_48px,label = "Trending",route = "home")
    object Search: BottomNavItem(icon = R.drawable.baseline_search_24,label = "Search", route = "search")
    object Saved: BottomNavItem(icon = R.drawable.baseline_bookmark_24, label = "Saved",route = "saved")
}
