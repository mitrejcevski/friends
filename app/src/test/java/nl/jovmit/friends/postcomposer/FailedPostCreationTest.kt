package nl.jovmit.friends.postcomposer

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.domain.exceptions.BackendException
import nl.jovmit.friends.domain.exceptions.ConnectionUnavailableException
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.domain.post.PostCatalog
import nl.jovmit.friends.domain.post.PostRepository
import nl.jovmit.friends.domain.user.InMemoryUserData
import nl.jovmit.friends.postcomposer.state.CreatePostState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class FailedPostCreationTest {

  @Test
  fun backedError() {
    val viewModel = CreatePostViewModel(
      PostRepository(
        InMemoryUserData("userId"),
        UnavailablePostCatalog()
      )
    )

    viewModel.createPost(":backend:")

    assertEquals(CreatePostState.BackendError, viewModel.postState.value)
  }

  @Test
  fun offlineError() {
    val viewModel = CreatePostViewModel(
      PostRepository(
        InMemoryUserData("userId"),
        OfflinePostCatalog()
      )
    )

    viewModel.createPost(":offline:")

    assertEquals(CreatePostState.Offline, viewModel.postState.value)
  }

  private class OfflinePostCatalog : PostCatalog {
    override fun addPost(userId: String, postText: String): Post {
      throw ConnectionUnavailableException()
    }

    override suspend fun postsFor(userIds: List<String>): List<Post> {
      TODO("Not yet implemented")
    }
  }

  private class UnavailablePostCatalog : PostCatalog {
    override fun addPost(userId: String, postText: String): Post {
      throw BackendException()
    }

    override suspend fun postsFor(userIds: List<String>): List<Post> {
      TODO("Not yet implemented")
    }
  }
}