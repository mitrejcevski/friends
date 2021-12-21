package nl.jovmit.friends.domain.people

import nl.jovmit.friends.domain.user.Friend

interface PeopleCatalog {

  suspend fun loadPeopleFor(userId: String): List<Friend>
}