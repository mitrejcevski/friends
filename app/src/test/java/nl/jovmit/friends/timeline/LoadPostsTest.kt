package nl.jovmit.friends.timeline

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.timeline.state.TimelineState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class LoadPostsTest {

  @Test
  fun noPostsAvailable() {
    val viewModel = TimelineViewModel()

    viewModel.timelineFor("annaId")

    assertEquals(
      TimelineState.Posts(emptyList()),
      viewModel.timelineState.value
    )
  }
}