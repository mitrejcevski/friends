package nl.jovmit.friends.postcomposer

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.domain.user.InMemoryUserData
import nl.jovmit.friends.infrastructure.ControllableClock
import nl.jovmit.friends.infrastructure.ControllableIdGenerator
import nl.jovmit.friends.postcomposer.state.CreatePostState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class FailedPostCreationTest {

  @Test
  fun backedError() {
    val viewModel = CreatePostViewModel(
      InMemoryUserData("userId"),
      ControllableClock(1L),
      ControllableIdGenerator("postId1")
    )

    viewModel.createPost(":backend:")

    assertEquals(CreatePostState.BackendError, viewModel.postState.value)
  }

  @Test
  fun offlineError() {
    val viewModel = CreatePostViewModel(
      InMemoryUserData("userId"),
      ControllableClock(1L),
      ControllableIdGenerator("postId2")
    )

    viewModel.createPost(":offline:")

    assertEquals(CreatePostState.Offline, viewModel.postState.value)
  }
}