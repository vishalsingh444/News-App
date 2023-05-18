package com.example.news.ui.components.browser_button

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun OpenInAppBrowserButton(url: String) {
    val context = LocalContext.current

    Text(text = "Read more...", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.clickable { openInAppBrowser(context,url) })

}

private fun openInAppBrowser(context: Context, url: String) {
    val customTabsIntent = CustomTabsIntent.Builder().build()
    customTabsIntent.launchUrl(context, Uri.parse(url))
}
