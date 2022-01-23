package nl.jovmit.friends.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import nl.jovmit.friends.navigation.Screen
import nl.jovmit.friends.friends.FriendsScreen
import nl.jovmit.friends.postcomposer.CreateNewPostScreen
import nl.jovmit.friends.timeline.TimelineScreen

@Composable
fun HomeScreen(userId: String) {
  val navigationController = rememberNavController()
  val homeNavigationScreens = listOf(Screen.Main.Timeline, Screen.Main.Friends)
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
      composable(route = Screen.Main.Friends.route) {
        FriendsScreen(
          userId = userId
        )
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