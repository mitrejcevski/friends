package nl.jovmit.friends.timeline

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import nl.jovmit.friends.R

@Composable
fun Timeline() {
  Text(text = stringResource(id = R.string.timeline))
}