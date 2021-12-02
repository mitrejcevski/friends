package nl.jovmit.friends

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import nl.jovmit.friends.postcomposer.CreateNewPostScreen
import nl.jovmit.friends.signup.SignUpScreen
import nl.jovmit.friends.timeline.TimelineScreen
import nl.jovmit.friends.ui.theme.FriendsTheme

class MainActivity : ComponentActivity() {

  sealed class Screen(val route: String) {
    object SignUp : Screen("signUp")

    object Home : Screen("home/{userId}") {
      fun createRoute(userId: String) = "home/$userId"
    }

    sealed class Main(
      route: String,
      @StringRes val title: Int,
      @DrawableRes val icon: Int
    ) : Screen(route) {

      object Timeline : Main("timeline", R.string.timeline, R.drawable.ic_timeline)

      object People : Main("people", R.string.people, R.drawable.ic_people)
    }

    object PostComposer : Screen("createNewPost")
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      FriendsTheme {
        Surface(color = MaterialTheme.colors.background) {
          val navController = rememberNavController()
          NavHost(navController = navController, startDestination = Screen.SignUp.route) {
            composable(Screen.SignUp.route) {
              SignUpScreen { signedUpUserId ->
                navController.navigate(Screen.Home.createRoute(signedUpUserId)) {
                  popUpTo(Screen.SignUp.route) { inclusive = true }
                }
              }
            }
            composable(route = Screen.Home.route) { backStackEntry ->
              HomeScreen(userId = backStackEntry.arguments?.getString("userId") ?: "")
            }
          }
        }
      }
    }
  }

  @Composable
  fun HomeScreen(userId: String) {
    val navigationController = rememberNavController()
    val homeNavigationScreens = listOf(Screen.Main.Timeline, Screen.Main.People)
    val currentDestination = currentDestination(navigationController)
    val isMainDestination = homeNavigationScreens.any { it.route == currentDestination }
    Scaffold(bottomBar = {
      if (isMainDestination) {
        HomeScreenBottomNavigation(navigationController, homeNavigationScreens)
      }
    }) {
      NavHost(
        navController = navigationController,
        startDestination = homeNavigationScreens.first().route,
        modifier = Modifier.padding(bottom = if (isMainDestination) 50.dp else 0.dp)
      ) {
        composable(route = Screen.Main.Timeline.route) {
          TimelineScreen(
            userId = userId
          ) { navigationController.navigate(Screen.PostComposer.route) }
        }
        composable(route = Screen.PostComposer.route) {
          CreateNewPostScreen {
            navigationController.navigateUp()
          }
        }
        composable(route = Screen.Main.People.route) {
          People()
        }
      }
    }
  }

  @Composable
  private fun HomeScreenBottomNavigation(
    navigationController: NavHostController,
    homeNavigationScreens: List<Screen.Main>
  ) {
    val currentDestination = currentDestination(navigationController)
    BottomNavigation {
      homeNavigationScreens.forEach { screen ->
        val title = stringResource(id = screen.title)
        BottomNavigationItem(
          icon = { Icon(painter = painterResource(id = screen.icon), contentDescription = title) },
          label = { Text(text = title) },
          selected = currentDestination == screen.route,
          onClick = {
            navigationController.navigate(screen.route) {
              popUpTo(navigationController.graph.findStartDestination().id) {
                saveState = true
              }
              launchSingleTop = true
              restoreState = true
            }
          }
        )
      }
    }
  }

  @Composable
  private fun currentDestination(navigationController: NavHostController): String? {
    val navBackStackEntry by navigationController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
  }

  @Composable
  fun People() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Text(text = stringResource(id = R.string.people))
    }
  }
}