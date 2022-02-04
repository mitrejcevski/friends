package nl.jovmit.friends.ui.composables

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun InfoMessage(
  @StringRes stringResource: Int
) {
  if (stringResource == 0) return
  var shouldShow by remember { mutableStateOf(false) }
  LaunchedEffect(stringResource) {
    shouldShow = true
    delay(1500)
    shouldShow = false
  }

  AnimatedVisibility(
    visible = shouldShow,
    enter = slideInVertically(
      initialOffsetY = { fullHeight -> -fullHeight },
      animationSpec = tween(durationMillis = 150, easing = FastOutLinearInEasing)
    ),
    exit = fadeOut(
      targetAlpha = 0f,
      animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
    )
  ) {
    Surface(
      modifier = Modifier.fillMaxWidth(),
      color = MaterialTheme.colors.error,
      elevation = 4.dp
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
      ) {
        Text(
          modifier = Modifier.padding(16.dp),
          text = stringResource(id = stringResource),
          color = MaterialTheme.colors.onError
        )
      }
    }
  }
}