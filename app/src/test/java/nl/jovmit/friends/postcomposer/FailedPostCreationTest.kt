package nl.jovmit.friends.postcomposer

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.app.TestDispatchers
import nl.jovmit.friends.domain.post.OfflinePostCatalog
import nl.jovmit.friends.domain.post.PostRepository
import nl.jovmit.friends.domain.post.UnavailablePostCatalog
import nl.jovmit.friends.domain.user.InMemoryUserDataStore
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
        InMemoryUserDataStore("userId"),
        UnavailablePostCatalog()
      ),
      TestDispatchers()
    )

    viewModel.createPost(":backend:")

    assertEquals(CreatePostState.BackendError, viewModel.postState.value)
  }

  @Test
  fun offlineError() {
    val viewModel = CreatePostViewModel(
      PostRepository(
        InMemoryUserDataStore("userId"),
        OfflinePostCatalog()
      ),
      TestDispatchers()
    )

    viewModel.createPost(":offline:")

    assertEquals(CreatePostState.Offline, viewModel.postState.value)
  }
}