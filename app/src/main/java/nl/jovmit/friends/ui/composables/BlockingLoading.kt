package nl.jovmit.friends.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import nl.jovmit.friends.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BlockingLoading(
  isShowing: Boolean
) {
  AnimatedVisibility(
    visible = isShowing,
    enter = fadeIn(
      initialAlpha = 0f,
      animationSpec = tween(durationMillis = 150, easing = FastOutLinearInEasing)
    ),
    exit = fadeOut(
      targetAlpha = 0f,
      animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
    )
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .testTag(stringResource(id = R.string.loading))
        .background(MaterialTheme.colors.surface.copy(alpha = 0.7f)),
      contentAlignment = Alignment.Center
    ) {
      CircularProgressIndicator()
    }
  }
}