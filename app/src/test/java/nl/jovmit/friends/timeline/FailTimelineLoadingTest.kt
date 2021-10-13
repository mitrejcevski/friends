package nl.jovmit.friends.timeline

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.app.TestDispatchers
import nl.jovmit.friends.domain.post.OfflinePostCatalog
import nl.jovmit.friends.domain.post.UnavailablePostCatalog
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
}