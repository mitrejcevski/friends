package nl.jovmit.friends.domain.post

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class InMemoryPostCatalogTest {

  @Test
  fun postFound() = runBlocking {
    val tomId = "tomId"
    val tomPost = Post("tomPostId", tomId, "Post by Tom", 1L)
    val annaPost = Post("annaPostId", "annaId", "Post by Anna", 2L)
    val lucyPost = Post("lucyPostId", "lucyId", "Post by Lucy", 3L)
    val postCatalog = InMemoryPostCatalog(
      availablePosts = listOf(annaPost, tomPost, lucyPost)
    )

    val result = postCatalog.postsFor(listOf(tomId))

    assertEquals(listOf(tomPost), result)
  }
}