package nl.jovmit.friends.ui.extensions

import java.text.SimpleDateFormat
import java.util.*

private val DATE_TIME_FORMAT = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US)

fun Long.toDateTime(): String {
  return DATE_TIME_FORMAT.format(this)
}