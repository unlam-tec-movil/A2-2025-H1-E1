package ar.edu.unlam.mobile.scaffolding.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(controller: NavHostController) {
    val navBackStackEntry by controller.currentBackStackEntryAsState()

    Column {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = Color.LightGray
        )

        NavigationBar(
            modifier = Modifier
                .height(60.dp)
                .padding(0.dp)
                .fillMaxWidth(),
            containerColor = Color.White
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
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.LightGray,
                )
            )

            NavigationBarItem(
                selected = navBackStackEntry?.destination?.hierarchy?.any { it.route == "favorite" } == true,
                onClick = { controller.navigate("favorite") },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "Favorites",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
                modifier = Modifier.clip(CircleShape),
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.LightGray,
                )
            )

            NavigationBarItem(
                selected = navBackStackEntry?.destination?.hierarchy?.any { it.route == "drafts" } == true,
                onClick = { controller.navigate("drafts") },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Drafts,
                        contentDescription = "Drafts",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
                modifier = Modifier.clip(CircleShape),
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.LightGray,
                )
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
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.LightGray,
                )
            )
        }
    }
}