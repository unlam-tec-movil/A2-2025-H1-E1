package ar.edu.unlam.mobile.scaffolding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ar.edu.unlam.mobile.scaffolding.ui.components.BottomBar
import ar.edu.unlam.mobile.scaffolding.ui.components.TopBar
import ar.edu.unlam.mobile.scaffolding.ui.screens.auth.LoginScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.auth.RegisterScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.feed.FeedScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.post.controller.ControllerPostScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.post.detail.DetailPostScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite.FavoriteScreen

import ar.edu.unlam.mobile.scaffolding.ui.screens.user.UserScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.user.config.Edit
import ar.edu.unlam.mobile.scaffolding.ui.theme.Green
import ar.edu.unlam.mobile.scaffolding.ui.theme.ScaffoldingV2Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            ScaffoldingV2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    // Controller es el elemento que nos permite navegar entre pantallas. Tiene las acciones
    // para navegar como naviegate y también la información de en dónde se "encuentra" el usuario
    // a través del back stack
    val controller = rememberNavController()
    val currentBackStackEntry = controller.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    val showBottomBar = currentRoute != "splash"

    Scaffold(
        topBar = {
            if (showBottomBar && currentRoute != "login" && currentRoute != "register") {
                TopBar("UNLAM", {})
            }
        },
        bottomBar = {
            if (showBottomBar && currentRoute != "login" && currentRoute != "register") {
                BottomBar(
                    controller = controller,
                )
            }
        },
        floatingActionButton = {
            if (showBottomBar && currentRoute != "comments/{idPost}" && currentRoute != "login" && currentRoute != "register") {
                FloatingActionButton(
                    onClick = { controller.navigate("newPost") },
                    containerColor = Green,
                    shape = CircleShape,
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar",
                        tint = Color.White,
                    )
                }
            }
        },

    ) { paddingValue ->
        // NavHost es el componente que funciona como contenedor de los otros componentes que
        // podrán ser destinos de navegación.
        NavHost(
            // composable es el componente que se usa para definir un destino de navegación.
            // Por parámetro recibe la ruta que se utilizará para navegar a dicho destino.
            navController = controller,
            startDestination = "splash",
            modifier = Modifier.padding(paddingValue),
        ) {
            composable("splash") {
                SplashScreen(navController = controller)
            }
            composable("home") {
                // Home es el componente en sí que es el destino de navegación.
                FeedScreen(modifier = Modifier, controller)
            }
            composable("favorite") {
                FavoriteScreen(navController = controller)
            }
            composable("login") {
                LoginScreen(navController = controller)
            }

            composable("register") {
                RegisterScreen(navController = controller)
            }
            composable("edit profile") {
                Edit(controller)
            }

            composable(
                route = "comments/{idPost}",
                arguments = listOf(navArgument("idPost") { type = NavType.IntType }),
            ) { backStackEntry ->
                val idPost = backStackEntry.arguments?.getInt("idPost") ?: 0
                DetailPostScreen(controller, idPost)
            }

            composable(
                route = "user/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
            ) { navBackStackEntry ->
                val id = navBackStackEntry.arguments?.getString("id") ?: "1"
                UserScreen(userId = id, controller)
            }

            composable(
                "newPost",
            ) { backStackEntry ->
                ControllerPostScreen(navigateToHome = { controller.navigate("home") })
            }
        }
    }
}
