package nl.jovmit.friends.infrastructure

interface Clock {
  fun now(): Long
}