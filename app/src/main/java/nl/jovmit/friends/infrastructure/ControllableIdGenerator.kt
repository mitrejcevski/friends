package nl.jovmit.friends.infrastructure

class ControllableIdGenerator(
  private val id: String
) : IdGenerator {

  override fun next(): String {
    return id
  }
}