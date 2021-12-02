package nl.jovmit.friends.people

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import nl.jovmit.friends.R

@Composable
fun PeopleScreen() {
  Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Text(text = stringResource(id = R.string.people))
  }
}