package nl.jovmit.friends.domain.post

class InMemoryPostCatalog {

  fun postsFor(userIds: List<String>): List<Post> {
    val availablePosts = listOf(
      Post("postId", "timId", "post text", 1L),
      Post("post2", "lucyId", "post 2", 2L),
      Post("post1", "lucyId", "post 1", 1L),
      Post("post4", "saraId", "post 4", 4L),
      Post("post3", "saraId", "post 3", 3L)
    )
    return availablePosts.filter { userIds.contains(it.userId) }
  }
}