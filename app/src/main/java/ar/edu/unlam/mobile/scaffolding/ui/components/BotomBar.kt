package ar.edu.unlam.mobile.scaffolding.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(controller: NavHostController) {
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    NavigationBar(
        modifier =
            Modifier
                .height(60.dp)
                .padding(0.dp)
                .fillMaxWidth(),
    ) {
        NavigationBarItem(
            selected = navBackStackEntry?.destination?.hierarchy?.any { it.route == "home" } == true,
            onClick = { controller.navigate("home") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
            modifier = Modifier.clip(CircleShape),
        )
        NavigationBarItem(
            selected = navBackStackEntry?.destination?.hierarchy?.any { it.route == "nofitication" } == true,
            onClick = { controller.navigate("nofitication") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "nofitications",
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
            modifier = Modifier.clip(CircleShape),
        )
        NavigationBarItem(
            selected = navBackStackEntry?.destination?.hierarchy?.any { it.route == "favorite" } == true,
            onClick = { controller.navigate("favorite") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = "favorites",
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
            modifier = Modifier.clip(CircleShape),
        )
        NavigationBarItem(
            selected = navBackStackEntry?.destination?.hierarchy?.any { it.route == "user/{id}" } == true,
            onClick = { controller.navigate("user/test") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User",
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
            modifier = Modifier.clip(CircleShape),
        )
    }
}
