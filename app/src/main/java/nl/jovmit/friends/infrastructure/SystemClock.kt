package nl.jovmit.friends.infrastructure

class SystemClock : Clock {

  override fun now(): Long {
    return System.currentTimeMillis()
  }
}