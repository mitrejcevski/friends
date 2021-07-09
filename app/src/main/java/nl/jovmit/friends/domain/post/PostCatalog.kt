package nl.jovmit.friends.domain.post

interface PostCatalog {

  suspend fun postsFor(userIds: List<String>): List<Post>
}