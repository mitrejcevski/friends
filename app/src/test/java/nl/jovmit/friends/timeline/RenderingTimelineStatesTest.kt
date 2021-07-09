package nl.jovmit.friends.timeline

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.app.TestDispatchers
import nl.jovmit.friends.domain.post.InMemoryPostCatalog
import nl.jovmit.friends.domain.timeline.TimelineRepository
import nl.jovmit.friends.domain.user.InMemoryUserCatalog
import nl.jovmit.friends.timeline.state.TimelineState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class RenderingTimelineStatesTest {

  private val timelineRepository = TimelineRepository(
    InMemoryUserCatalog(),
    InMemoryPostCatalog()
  )
  private val viewModel = TimelineViewModel(
    timelineRepository,
    TestDispatchers()
  )

  @Test
  fun timelineStatesExposedToAnObserver() {
    val renderedStates = mutableListOf<TimelineState>()
    viewModel.timelineState.observeForever { renderedStates.add(it) }

    viewModel.timelineFor(":irrelevant:")

    assertEquals(
      listOf(TimelineState.Loading, TimelineState.Posts(emptyList())),
      renderedStates
    )
  }
}