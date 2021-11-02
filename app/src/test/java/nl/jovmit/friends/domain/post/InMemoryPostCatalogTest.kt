package nl.jovmit.friends.domain.post

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InMemoryPostCatalogTest {

  private val tomId = "tomId"
  private val tomPost = Post("tomPostId", tomId, "Post by Tom", 1L)
  private val annaPost = Post("annaPostId", "annaId", "Post by Anna", 2L)
  private val lucyPost = Post("lucyPostId", "lucyId", "Post by Lucy", 3L)

  @Test
  fun postFound() = runBlocking {
    val postCatalog = InMemoryPostCatalog(
      availablePosts = listOf(annaPost, tomPost, lucyPost)
    )

    val result = postCatalog.postsFor(listOf(tomId))

    assertEquals(listOf(tomPost), result)
  }
}