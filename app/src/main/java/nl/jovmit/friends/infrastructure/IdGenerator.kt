package nl.jovmit.friends.infrastructure

interface IdGenerator {
  fun next(): String
}