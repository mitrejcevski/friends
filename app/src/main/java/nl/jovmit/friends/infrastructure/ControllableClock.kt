package nl.jovmit.friends.infrastructure

class ControllableClock(
  private val timestamp: Long
) {

  fun now(): Long {
    return timestamp
  }
}