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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import ar.edu.unlam.mobile.scaffolding.ui.screens.follows.Followed
import ar.edu.unlam.mobile.scaffolding.ui.screens.post.controller.ControllerPostScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.post.detail.DetailPostScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.post.draft.DraftContainerScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite.FavoriteScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.splash.SplashScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.user.UserScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.user.config.Edit
import ar.edu.unlam.mobile.scaffolding.ui.screens.user.userprofile.UserProfileScreen
import ar.edu.unlam.mobile.scaffolding.ui.theme.Green
import ar.edu.unlam.mobile.scaffolding.ui.theme.ScaffoldingV2Theme
import ar.edu.unlam.mobile.scaffolding.utils.UserStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
    val context = LocalContext.current
    val userStore = remember { UserStore(context) }
    val coroutineScope = rememberCoroutineScope()

    val showBottomBar = currentRoute != "splash"

    val handleLogout: () -> Unit = {
        coroutineScope.launch {
            userStore.escribirDatosUsuario("")
            userStore.escribirEstadoLogin(false)
            userStore.escribirTokenUsuario("")
            kotlinx.coroutines.delay(200)
            controller.navigate("login") {
                popUpTo(0)
            }
        }
    }

    Scaffold(
        topBar = {
            if (showBottomBar && currentRoute != "login" && currentRoute != "register") {
                TopBar(
                    title = "UNLAM",
                    onActionClick = {},
                    onLogout = handleLogout,
                )
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

            // user
            composable(
                route = "user/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
            ) { navBackStackEntry ->
                navBackStackEntry.arguments?.getString("id") ?: "1"
                UserScreen(controller)
            }

            composable(
                route = "userProfile/{id}/{name}/{avatarUrl}",
                arguments =
                    listOf(
                        navArgument("id") { type = NavType.StringType },
                        navArgument("name") { type = NavType.StringType },
                        navArgument("avatarUrl") { type = NavType.StringType },
                    ),
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                val name = backStackEntry.arguments?.getString("name") ?: ""
                val avatarUrl = backStackEntry.arguments?.getString("avatarUrl") ?: ""

                UserProfileScreen(userId = id, userName = name, avatarUrl = avatarUrl, controller = controller)
            }

            composable(
                "newPost",
            ) { backStackEntry ->
                ControllerPostScreen(navigateToHome = { controller.navigate("home") })
            }

            composable("drafts") {
                DraftContainerScreen(
                    onNavigateBack = { controller.navigate("home") },
                    onEditDraft = { draft ->
                        // TODO: Navegar a la pantalla de crear post con el texto del borrador
                        controller.navigate("newPost")
                    },
                )
            }

            composable("followed") {
                val context = LocalContext.current
                val userStore = remember { UserStore(context) }
                val currentUserId by userStore.leerDatosUsuario.collectAsState(initial = "")
                Followed(currentUserEmail = currentUserId)
            }
        }
    }
}
