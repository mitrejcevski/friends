package nl.jovmit.friends.timeline

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.infrastructure.builder.UserBuilder.Companion.aUser
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

    assertEquals(TimelineState.Posts(emptyList()), viewModel.timelineState.value)
  }

  @Test
  fun postsAvailable() {
    val tim = aUser().withId("timId").build()
    val timPosts = listOf(Post("postId", tim.id, "post text", 1L))
    val viewModel = TimelineViewModel()

    viewModel.timelineFor(tim.id)

    assertEquals(TimelineState.Posts(timPosts), viewModel.timelineState.value)
  }
}