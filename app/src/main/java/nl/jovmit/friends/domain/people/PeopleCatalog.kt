package nl.jovmit.friends.domain.people

import nl.jovmit.friends.domain.user.Friend

interface PeopleCatalog {
  fun loadPeopleFor(userId: String): List<Friend>
}