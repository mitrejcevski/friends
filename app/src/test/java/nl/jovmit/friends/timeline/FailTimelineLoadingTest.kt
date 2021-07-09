package nl.jovmit.friends.timeline

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.app.TestDispatchers
import nl.jovmit.friends.domain.exceptions.BackendException
import nl.jovmit.friends.domain.exceptions.ConnectionUnavailableException
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.domain.post.PostCatalog
import nl.jovmit.friends.domain.timeline.TimelineRepository
import nl.jovmit.friends.domain.user.InMemoryUserCatalog
import nl.jovmit.friends.timeline.state.TimelineState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class FailTimelineLoadingTest {

  @Test
  fun backendError() {
    val userCatalog = InMemoryUserCatalog()
    val postCatalog = UnavailablePostCatalog()
    val viewModel = TimelineViewModel(
      TimelineRepository(userCatalog, postCatalog),
      TestDispatchers()
    )

    viewModel.timelineFor(":irrelevant:")

    assertEquals(TimelineState.BackendError, viewModel.timelineState.value)
  }

  @Test
  fun offlineError() {
    val userCatalog = InMemoryUserCatalog()
    val postCatalog = OfflinePostCatalog()
    val viewModel = TimelineViewModel(
      TimelineRepository(userCatalog, postCatalog),
      TestDispatchers()
    )

    viewModel.timelineFor(":irrelevant:")

    assertEquals(TimelineState.OfflineError, viewModel.timelineState.value)
  }

  private class UnavailablePostCatalog : PostCatalog {
    override suspend fun postsFor(userIds: List<String>): List<Post> {
      throw BackendException()
    }
  }

  private class OfflinePostCatalog : PostCatalog {
    override suspend fun postsFor(userIds: List<String>): List<Post> {
      throw ConnectionUnavailableException()
    }
  }
}