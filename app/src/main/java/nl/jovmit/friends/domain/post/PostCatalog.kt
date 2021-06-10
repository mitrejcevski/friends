package nl.jovmit.friends.domain.post

interface PostCatalog {

  fun postsFor(userIds: List<String>): List<Post>
}